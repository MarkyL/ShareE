package com.mark.sharee.fragments.generalPolls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharee.R
import com.mark.sharee.adapters.BaseAdapter
import com.mark.sharee.adapters.PollsAdapter
import com.mark.sharee.core.Action
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.mvvm.State
import com.mark.sharee.mvvm.ViewModelHolder
import com.mark.sharee.navigation.arguments.TransferInfo
import com.mark.sharee.network.model.responses.GeneralPollResponse
import com.mark.sharee.screens.MainScreen
import com.mark.sharee.screens.PollScreen
import com.mark.sharee.utils.Event
import com.mark.sharee.utils.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_general_polls.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class GeneralPollsFragment : ShareeFragment(),
    BaseAdapter.AdapterListener<GeneralPollResponse> {

    // This fragment holds all the general polls that are active and available for the
    // patient to view

    lateinit var transferInfo: TransferInfo

    private val viewModel: GeneralPollsViewModel by sharedViewModel()
    private val pollsAdapter: PollsAdapter = PollsAdapter(listener = this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_general_polls, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transferInfo = castArguments(TransferInfo::class.java)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            addItemDecoration(GridSpacingItemDecoration(1, 30, true))
            this.adapter = pollsAdapter
        }

        registerViewModel()

        configureScreen()
    }

    private fun configureScreen() {
        when (transferInfo.flow) {
            TransferInfo.Flow.GeneralPolls -> configureGeneralPollsFlow()
            TransferInfo.Flow.MedicalPolls -> configureMedicalPollsFlow()
        }
        configureToolbar()
    }

    private fun configureToolbar() {
        toolbar.addActions(arrayOf(Action.BackBlack), this)

        when (transferInfo.flow) {
            TransferInfo.Flow.GeneralPolls -> toolbar.setTitle(resources.getString(R.string.polls_screen_default_title))
            TransferInfo.Flow.MedicalPolls -> toolbar.setTitle(resources.getString(R.string.polls_screen_secondary_title))
            TransferInfo.Flow.Default -> TODO()
        }
    }

    private fun configureGeneralPollsFlow() {
        viewModel.dispatchInputEvent(GetGeneralPolls)
    }

    private fun configureMedicalPollsFlow() {
        viewModel.dispatchInputEvent(GetMedicalPolls)
    }

    private fun registerViewModel() {
        viewModel.dataStream.observe(
            viewLifecycleOwner,
            Observer<ViewModelHolder<Event<PollDataState>>> { t ->
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

    private fun handleNext(result: Event<PollDataState>?) {
        result?.let { responseEvent ->
            if (!responseEvent.consumed) {
                responseEvent.consume()?.let { response ->
                    when (response) {
                        is GetGeneralPollsSuccess -> handleGetGeneralPollsSuccess(response)
                    }
                }
            }
        }
    }

    private fun handleGetGeneralPollsSuccess(response: GetGeneralPollsSuccess) {
        Timber.i("handleGetGeneralPollsSuccess generalPollsResponse = $response")
        val generalPolls = response.polls
        //TODO: handle empty generalPolls list (show some view for it)...
        if (generalPolls.isNullOrEmpty()) {
            Toast.makeText(context, "There are no active polls", Toast.LENGTH_LONG).show()
            navigator.goBackTo(MainScreen::class.java)
        } else {
            pollsAdapter.submitList(generalPolls)
        }
    }

    private fun handleError(throwable: Throwable) {
        hideProgressView()
        errorHandler.handleError(this, throwable)
    }

    override fun onItemClick(data: GeneralPollResponse) {
        transferInfo.poll = data
        navigator.replace(PollScreen(transferInfo))
    }

}
