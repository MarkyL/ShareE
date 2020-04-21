package com.mark.sharee.fragments.dailyRoutines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.sharee.R
import com.mark.sharee.core.Action
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.mvvm.State
import com.mark.sharee.mvvm.ViewModelHolder
import com.mark.sharee.navigation.arguments.TransferInfo
import com.mark.sharee.network.model.responses.DailyActivity
import com.mark.sharee.utils.Event
import kotlinx.android.synthetic.main.fragment_daily_routines_tabs.*
import kotlinx.android.synthetic.main.fragment_general_polls.progressBar
import kotlinx.android.synthetic.main.fragment_general_polls.toolbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class DailyRoutinesTabFragment: ShareeFragment() {

    lateinit var transferInfo: TransferInfo

    private lateinit var dailyRoutinesPagerAdapter: DailyRoutinesPagerAdapter
    private val viewModel: DailyRoutinesViewModel by sharedViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_daily_routines_tabs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transferInfo = castArguments(TransferInfo::class.java)
        registerViewModel()
        configureScreen()

        viewModel.dispatchInputEvent(GetDailyRoutines)
    }

    private fun configureScreen() {
        configureToolbar()
//        configureTabsLayout()
    }

    private fun configureToolbar() {
        toolbar.addActions(arrayOf(Action.BackBlack), this)
        toolbar.setTitle(resources.getString(R.string.daily_routine_screen_default_title))
    }

    private fun configureTabsLayout(
        weekdayActivities: MutableList<DailyActivity>, weekendActivities: MutableList<DailyActivity>) {
        tabsLayout.setupWithViewPager(viewPager)

        dailyRoutinesPagerAdapter = DailyRoutinesPagerAdapter(childFragmentManager, resources, weekdayActivities, weekendActivities)
        viewPager.setPageTransformer(true, null)
        viewPager.adapter = dailyRoutinesPagerAdapter
    }

    private fun registerViewModel() {
        viewModel.dataStream.observe(
            viewLifecycleOwner,
            Observer<ViewModelHolder<Event<DailyRoutinesDataState>>> { t ->
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

    private fun showProgressView() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressView() {
        progressBar.visibility = View.GONE
    }

    private fun handleNext(result: Event<DailyRoutinesDataState>?) {
        result?.let { responseEvent ->
            if (!responseEvent.consumed) {
                responseEvent.consume()?.let { response ->
                    when (response) {
                        is GetDailyRoutinesSuccess -> handleGetDailyRoutinesSuccess(response)
                    }
                }
            }
        }
    }

    private fun handleGetDailyRoutinesSuccess(response: GetDailyRoutinesSuccess) {
        configureTabsLayout(response.dailyRoutineResponse.weekdayActivities, response.dailyRoutineResponse.weekendActivities)
        Timber.i("handleGetDailyRoutinesSuccess - $response")
    }

    private fun handleError(throwable: Throwable) {
        hideProgressView()
        errorHandler.handleError(this, throwable)
    }
}