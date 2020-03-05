package com.mark.sharee.fragments.poll

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharee.R
import com.mark.sharee.adapters.PollAdapter
import com.mark.sharee.adapters.PollsAdapter
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.mvvm.State
import com.mark.sharee.mvvm.ViewModelHolder
import com.mark.sharee.navigation.arguments.TransferInfo
import com.mark.sharee.utils.Event
import com.mark.sharee.utils.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_poll.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class PollFragment : ShareeFragment() {

    private val viewModel: PollViewModel by sharedViewModel()
    private val pollAdapter: PollAdapter = PollAdapter()

    lateinit var transferInfo: TransferInfo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_poll, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transferInfo = castArguments(TransferInfo::class.java)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            addItemDecoration(GridSpacingItemDecoration(1, 30, true))
            this.adapter = pollAdapter
        }

        registerViewModel()

        configureScreen()

        submitBtn.setOnClickListener { onSubmitBtnClick() }
    }

    private fun configureScreen() {
        val poll = transferInfo.poll
        pollName.text = poll.name
        pollAdapter.submitList(poll.questions)
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
                        is SubmitPollSuccess -> handleSubmitPollSuccess(response)
                    }
                }
            }
        }
    }

    private fun handleSubmitPollSuccess(response: SubmitPollSuccess) {
        Timber.i("handleSubmitPollSuccess generalPollsResponse = $response")
        Toast.makeText(context, resources.getString(R.string.submit_poll_success), Toast.LENGTH_SHORT).show()
    }

    private fun handleError(throwable: Throwable?) {
        hideProgressView()
        Toast.makeText(context, throwable?.message, Toast.LENGTH_SHORT).show()
    }

    private fun onSubmitBtnClick() {
        Timber.i("onSubmitBtnClick")
        viewModel.dispatchInputEvent(SubmitPoll(pollAdapter.items))
    }


}
