package com.mark.sharee.fragments.poll

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharee.R
import com.mark.sharee.adapters.*
import com.mark.sharee.core.Action
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.dialogs.AbstractDialog
import com.mark.sharee.dialogs.ShareeDialog
import com.mark.sharee.model.poll.Question
import com.mark.sharee.mvvm.State
import com.mark.sharee.mvvm.ViewModelHolder
import com.mark.sharee.navigation.arguments.TransferInfo
import com.mark.sharee.network.model.responses.PollSection
import com.mark.sharee.screens.GeneralPollsScreen
import com.mark.sharee.utils.Event
import com.mark.sharee.utils.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_poll.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class PollFragment : ShareeFragment() {

    private val viewModel: PollViewModel by sharedViewModel()
    private val pollSectionsAdapter: PollSectionsAdapter = PollSectionsAdapter()

    companion object {
        private const val TAG = "PollFragment"
    }

    private lateinit var transferInfo: TransferInfo

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_poll, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transferInfo = castArguments(TransferInfo::class.java)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            addItemDecoration(GridSpacingItemDecoration(1, 30, true))
            this.adapter = pollSectionsAdapter
        }

        registerViewModel()

        configureScreen()

        submitBtn.setOnClickListener { onSubmitBtnClick() }
    }

    private fun configureScreen() {
        val poll = transferInfo.poll
        toolbar.setTitle(poll.name)
        toolbar.addActions(arrayOf(Action.BackBlack), this)

        poll.pollSections.forEach {
            it.expanded = true
        }
        pollSectionsAdapter.updateItems(poll.pollSections)
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
                        handleError(t.data, t.throwable)
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
        showSuccessDialog()
    }

    private fun showSuccessDialog() {
        val successDialog = ShareeDialog(
            title = resources.getString(R.string.submit_poll_success),
            subtitle = resources.getString(R.string.thanks_for_cooperation),
            iconDrawable = R.drawable.ic_warning_black,
            positiveButtonText = R.string.dialog_positive_ok,
            callback = object : AbstractDialog.Callback {
                override fun onDialogPositiveAction(requestCode: Int) {
                    navigator.goBackTo(GeneralPollsScreen::class.java)
                }

                override fun onDialogNegativeAction(requestCode: Int) { /* No negative here */ }
            })

        successDialog.show(parentFragmentManager, TAG)
    }

    private fun handleError(result: Event<PollDataState>?, throwable: Throwable?) {
        hideProgressView()
        result?.let { responseEvent ->
            if (!responseEvent.consumed) {
                responseEvent.consume()?.let { response ->
                    when (response) {
                        is SubmitPollFailure -> Toast.makeText(context, throwable?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun onSubmitBtnClick() {
        Timber.i("onSubmitBtnClick")
        viewModel.dispatchInputEvent(SubmitPoll(pollId = transferInfo.poll.id, answeredQuestions = generateAnsweredQuestions()))
    }

    private fun generateAnsweredQuestions(): List<Question> {
        val answeredQuestions = mutableListOf<Question>()
        pollSectionsAdapter.items.forEach { section ->
            answeredQuestions.addAll(section.questions)
        }
        return answeredQuestions
    }

}
