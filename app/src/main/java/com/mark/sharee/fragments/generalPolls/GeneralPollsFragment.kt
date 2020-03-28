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
import com.mark.sharee.adapters.PollsAdapter
import com.mark.sharee.adapters.PollsAdapterListener
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.mvvm.State
import com.mark.sharee.mvvm.ViewModelHolder
import com.mark.sharee.navigation.arguments.TransferInfo
import com.mark.sharee.network.model.responses.GeneralPollResponse
import com.mark.sharee.screens.MainScreen
import com.mark.sharee.screens.PollScreen
import com.mark.sharee.utils.Event
import com.mark.sharee.utils.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_poll.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class GeneralPollsFragment : ShareeFragment(), PollsAdapterListener {
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
            TransferInfo.Flow.GeneralPolls -> viewModel.dispatchInputEvent(GetGeneralPolls)
            TransferInfo.Flow.MedicalPolls -> viewModel.dispatchInputEvent(GetMedicalPolls)
        }
    }

    private fun registerViewModel() {
        viewModel.dataStream.observe(
            viewLifecycleOwner,
            Observer<ViewModelHolder<Event<PollDataState>>> { t ->
                when (t.state) {
                    State.INIT -> {
                    }
                    State.LOADING -> {
                        showProgressView()
                    }
                    State.NEXT -> {
                        hideProgressView()
                        handleNext(t.data)
                    }
                    State.ERROR -> {
                        handleError(t.throwable)
                    }
                    State.COMPLETE -> {
                        hideProgressView()
                    }
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

    private fun handleError(throwable: Throwable?) {
        hideProgressView()
        Toast.makeText(context, throwable?.message, Toast.LENGTH_SHORT).show()
    }

    override fun onPollClick(poll: GeneralPollResponse) {
        transferInfo.poll = poll
        navigator.replace(PollScreen(transferInfo))
    }

}
