package com.mark.sharee.fragments.main

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.sharee.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.mark.sharee.activities.MainActivity
import com.mark.sharee.core.AbstractAction
import com.mark.sharee.core.Action
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.core.SupportsOnBackPressed
import com.mark.sharee.data.ShareeRepository
import com.mark.sharee.model.User
import com.mark.sharee.navigation.arguments.TransferInfo
import com.mark.sharee.screens.GeneralPollsScreen
import com.mark.sharee.utils.Toaster
import com.mark.sharee.widgets.ShareeToolbar
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.sharee_toolbar.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class MainFragment : ShareeFragment(), ShareeToolbar.ActionListener, SupportsOnBackPressed {

    lateinit var transferInfo: TransferInfo

    private val viewModel: MainViewModel by sharedViewModel()

    private val shareeRepository: ShareeRepository by inject()

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
                val msg = getString(R.string.fcm_token)
                Timber.i("$TAG -  FCM token retrieved: [$token]")
                context?.let { Toaster.show(it, msg) }

                User.me()?.let {
                    // if server already has the current token, we shall not bother updating it.
                    //if (it.getFcmToken() == token) {
                    //    Timber.i("$TAG - server already has up to date fcm token")
                    //    return@let
                    //}

                    if (token.isNullOrEmpty()) {
                        Timber.i("$TAG - Empty fcm token")
                    } else {
                        updateFcmToken(token, it.getToken())
                    }
                } ?: Timber.i("$TAG - User is null.")
            })
    }

    //TODO - implement in view model rather than here.
    private fun updateFcmToken(fcmToken: String, verificationToken: String) {
        Timber.i("$TAG - updateFcmToken - start")
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                Timber.i("$TAG - updateFcmToken - runCatching, token - $fcmToken")
                shareeRepository.updateFcmToken(verificationToken, fcmToken)
            }.onSuccess {
                Timber.i("$TAG - updateFcmToken - onSuccess, response = $it")
                User.me()?.setFcmToken(fcmToken)

            }.onFailure {
                Timber.e("$TAG - updateFcmToken - onFailure $it")
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transferInfo = castArguments(TransferInfo::class.java)

        configureToolbar()

        phoneNumberTv.text = "שלום " + transferInfo.phoneNumber

        viewModel.uiState.observe(viewLifecycleOwner, Observer {
            val dataState = it ?: return@Observer
            progressBar.visibility = if (dataState.showProgress) View.VISIBLE else View.GONE
            if (dataState.response != null && !dataState.response.consumed) {
                dataState.response.consume()?.let { response ->
                    Toast.makeText(context, response.name, Toast.LENGTH_LONG).show()
                }
            }
            if (dataState.error != null && !dataState.error.consumed) {
                dataState.error.consume()?.let { errorResource ->
                    Toast.makeText(context, resources.getString(errorResource), Toast.LENGTH_SHORT)
                        .show()
                    // handle error state
                }
            }

        })
    }

    private fun configureToolbar() {
        homeToolbar.titleTextView.text = resources.getString(R.string.app_name)
        homeToolbar.addActions(arrayOf(Action.Drawer, Action.Logo), this)
    }

    override fun onActionSelected(action: AbstractAction): Boolean {
        if (action == Action.Drawer) {
            Timber.i("onActionSelected - Drawer")
            (activity as MainActivity).openDrawer()
            return true
        }


        return false
    }

    override fun onBackPressed(): Boolean {
        return true
    }

    companion object {
        private const val TAG = "MainFragment"
    }

}