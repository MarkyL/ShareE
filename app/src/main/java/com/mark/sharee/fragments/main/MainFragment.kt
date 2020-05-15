package com.mark.sharee.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharee.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.mark.sharee.activities.MainActivity
import com.mark.sharee.adapters.MessagesAdapter
import com.mark.sharee.core.AbstractAction
import com.mark.sharee.core.Action
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.core.SupportsOnBackPressed
import com.mark.sharee.model.User
import com.mark.sharee.mvvm.State
import com.mark.sharee.mvvm.ViewModelHolder
import com.mark.sharee.navigation.arguments.TransferInfo
import com.mark.sharee.network.model.responses.Message
import com.mark.sharee.network.model.responses.ScheduledNotification
import com.mark.sharee.screens.DailyRoutinesTabScreen
import com.mark.sharee.screens.GeneralPollsScreen
import com.mark.sharee.screens.MessagesScreen
import com.mark.sharee.utils.AlarmUtils
import com.mark.sharee.utils.Event
import com.mark.sharee.utils.GridSpacingItemDecoration
import com.mark.sharee.utils.Toaster
import com.mark.sharee.widgets.ShareeToolbar
import kotlinx.android.synthetic.main.fragment_general_polls.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.progressBar
import kotlinx.android.synthetic.main.fragment_main.recyclerView
import kotlinx.android.synthetic.main.sharee_toolbar.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.util.*

class MainFragment : ShareeFragment(), ShareeToolbar.ActionListener, SupportsOnBackPressed {

    lateinit var transferInfo: TransferInfo
    private val viewModel: MainViewModel by sharedViewModel()

    private val messagesAdapter = MessagesAdapter()
    private var messages: MutableList<Message> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeFCM()
    }

    private fun initializeFCM() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Timber.i("$TAG - getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                Timber.i("$TAG -  FCM token retrieved: [$token]")

                User.me()?.let {
                    // if server already has the current token, we shall not bother updating it.
                    //if (it.getFcmToken() == token) {
                    //    Timber.i("$TAG - server already has up to date fcm token")
                    //    return@let
                    //}

                    if (token.isNullOrEmpty()) {
                        Timber.i("$TAG - Empty fcm token")
                    } else {
                        viewModel.dispatchInputEvent(UpdateFcmToken(token, it.getToken()))
                    }
                } ?: Timber.i("$TAG - User is null.")
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transferInfo = castArguments(TransferInfo::class.java)

        configureToolbar()

        registerViewModel()

        viewModel.dispatchInputEvent(GetScheduledNotifications)
        User.me()?.let {
            viewModel.dispatchInputEvent(GetMessages(it.getToken()))
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            addItemDecoration(GridSpacingItemDecoration(1, 30, true))
            this.adapter = messagesAdapter
        }

        setNavigationListeners()
    }

    private fun setNavigationListeners() {
        dailyRoutineBtn.setOnClickListener {
            navigator.replace(DailyRoutinesTabScreen(TransferInfo()))
        }

        generalPollsBtn.setOnClickListener {
            navigator.replace(GeneralPollsScreen(TransferInfo(flow = TransferInfo.Flow.GeneralPolls)))
        }

//        medicalPollsBtn.setOnClickListener {
//            navigator.replace(GeneralPollsScreen(TransferInfo(flow = TransferInfo.Flow.MedicalPolls)))
//        }

        exercisesBtn.setOnClickListener {
            Toaster.show(this, "בתהליך פיתוח", true)
        }
    }

    private fun registerViewModel() {
        viewModel.dataStream.observe(
            viewLifecycleOwner,
            Observer<ViewModelHolder<Event<MainDataState>>> { t ->
                when (t.state) {
                    State.INIT -> { }
                    State.LOADING -> { showProgressView() }
                    State.NEXT -> {
                        hideProgressView()
                        handleNext(t.data)
                    }
                    State.ERROR -> { t.throwable?.let { handleError(it) } }
                    State.COMPLETE -> { hideProgressView() }
                }
            })
    }

    private fun handleNext(result: Event<MainDataState>?) {
        result?.let { responseEvent ->
            if (!responseEvent.consumed) {
                responseEvent.consume()?.let { response ->
                    when (response) {
                        is ScheduledNotificationsSuccess -> handleScheduledNotificationsSuccess(response)
                        is GetMessagesSuccess -> handleGetMessagesSuccess(response.messages)
                    }
                }
            }
        }
    }

    private fun handleGetMessagesSuccess(messages: MutableList<Message>) {
        this.messages = messages
        messagesAdapter.submitList(messages.take(MESSAGES_TO_SHOW))
    }

    private fun handleScheduledNotificationsSuccess(response: ScheduledNotificationsSuccess) {
        Timber.i("handleScheduledNotificationsSuccess - $response")

        AlarmUtils.cancelAllNotificationAlarms(requireContext())

        // set the new ones:
        response.scheduledNotifications.forEach { scheduledNotification ->
            // weekday meal at 08:00 morning
            val timeTokens = scheduledNotification.time.split(":")
            val notificationHour = timeTokens[0].toInt()
            val notificationMinute = timeTokens[1].toInt()

            val calendar = Calendar.getInstance()
            calendar.time = Date()
            calendar.set(Calendar.HOUR_OF_DAY, notificationHour)
            calendar.set(Calendar.MINUTE, notificationMinute)

            if (scheduledNotification.weekdays) {
                for (dayOfWeek in 1..5) {
                    scheduleAlarmForDay(calendar, scheduledNotification, dayOfWeek)
                }
            } else {
                // weekend notification
                for (dayOfWeek in 6..7) {
                    scheduleAlarmForDay(calendar, scheduledNotification, dayOfWeek)
                }
            }
        }
    }

    private fun generatePendingIntentRequestCode(id: Int, dayOfWeek: Int): Int {
        return (id.toString() + dayOfWeek.toString()).toInt()
    }

    private fun scheduleAlarmForDay(calendar: Calendar, scheduledNotification: ScheduledNotification, dayOfWeek: Int) {
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek)
        val requestCode = generatePendingIntentRequestCode(scheduledNotification.id, dayOfWeek)
        val pendingIntent =
            AlarmUtils.getAlarmIntent(requireContext(), scheduledNotification.title, scheduledNotification.body, requestCode)

        AlarmUtils.addAlarm(context = requireContext(),
            pendingIntent = pendingIntent,
            notificationId = requestCode,
            scheduledTime = calendar.timeInMillis)
    }

    private fun handleError(throwable: Throwable) {
        hideProgressView()
        errorHandler.handleError(this, throwable)
    }

    private fun configureToolbar() {
        homeToolbar.titleTextView.text = resources.getString(R.string.app_name)
        homeToolbar.addActions(arrayOf(Action.Drawer, Action.Notification), this)
        User.me()?.let {
            (activity as MainActivity).setDrawerTitle(it.getPhone())
        }
    }

    override fun onActionSelected(action: AbstractAction): Boolean {
        if (action == Action.Drawer) {
            Timber.i("onActionSelected - Drawer")
            (activity as MainActivity).openDrawer()
            return true
        } else if (action == Action.Notification) {
            val transferInfo = TransferInfo()
            transferInfo.messages = this.messages
            navigator.replace(MessagesScreen(transferInfo))
        }


        return false
    }

    override fun onBackPressed(): Boolean {
        return true
    }

    companion object {
        private const val TAG = "MainFragment"
        private const val MESSAGES_TO_SHOW = 8
    }

    private fun showProgressView() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressView() {
        progressBar.visibility = View.GONE
    }

}