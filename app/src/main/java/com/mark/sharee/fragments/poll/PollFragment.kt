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
import com.mark.sharee.adapters.*
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.model.poll.Question
import com.mark.sharee.mvvm.State
import com.mark.sharee.mvvm.ViewModelHolder
import com.mark.sharee.navigation.arguments.TransferInfo
import com.mark.sharee.network.model.responses.GeneralPollResponse
import com.mark.sharee.network.model.responses.PollSection
import com.mark.sharee.screens.MainScreen
import com.mark.sharee.utils.Event
import com.mark.sharee.utils.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_poll.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.w3c.dom.Text
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
        generatePollDisplayItems(poll)
        pollAdapter.submitList(generatePollDisplayItems(poll))
    }

    private fun generatePollDisplayItems(poll: GeneralPollResponse): MutableList<PollAbstractDisplayItem>  {
        val pollDisplayItems = mutableListOf<PollAbstractDisplayItem>()
        if (poll.pollSections.size == 1) {
            addQuestionsOfSection(poll.pollSections[0], pollDisplayItems)
        } else {
            poll.pollSections.forEach {
                pollDisplayItems.add(PollHeaderItem(it.name))
                addQuestionsOfSection(it, pollDisplayItems)
            }
        }
        return pollDisplayItems
    }

    private fun addQuestionsOfSection(
        it: PollSection,
        pollDisplayItems: MutableList<PollAbstractDisplayItem>
    ) {
        it.questions.forEach { question ->
            when (question.type) {
                Question.QuestionType.BOOLEAN -> pollDisplayItems.add(BooleanQuestionItem(question))
                Question.QuestionType.NUMERICAL -> pollDisplayItems.add(
                    NumericalQuestionItem(
                        question
                    )
                )
                Question.QuestionType.TEXTUAL -> pollDisplayItems.add(TextualQuestionItem(question))
                Question.QuestionType.GENERIC -> pollDisplayItems.add(GenericQuestionItem(question))
            }
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
                        is SubmitPollSuccess -> handleSubmitPollSuccess(response)
                    }
                }
            }
        }
    }

    private fun handleSubmitPollSuccess(response: SubmitPollSuccess) {
        Timber.i("handleSubmitPollSuccess generalPollsResponse = $response")
        Toast.makeText(context, resources.getString(R.string.submit_poll_success), Toast.LENGTH_SHORT).show()
        navigator.goBackTo(MainScreen::class.java)
    }

    private fun handleError(throwable: Throwable?) {
        hideProgressView()
        Toast.makeText(context, throwable?.message, Toast.LENGTH_SHORT).show()
    }

    private fun onSubmitBtnClick() {
        Timber.i("onSubmitBtnClick")
        viewModel.dispatchInputEvent(SubmitPoll(pollId = transferInfo.poll.id, answeredQuestions = generateAnsweredQuestions(pollAdapter.items)))
    }

    private fun generateAnsweredQuestions(items: List<PollAbstractDisplayItem>): List<Question> {
        val answeredQuestions = mutableListOf<Question>()
        items.forEach {
            when (it.type) {
                PollAbstractDisplayItem.PollItemType.BOOLEAN -> answeredQuestions.add((it as BooleanQuestionItem).question)
                PollAbstractDisplayItem.PollItemType.NUMERICAL -> answeredQuestions.add((it as NumericalQuestionItem).question)
                PollAbstractDisplayItem.PollItemType.TEXTUAL -> answeredQuestions.add((it as TextualQuestionItem).question)
                PollAbstractDisplayItem.PollItemType.GENERIC -> answeredQuestions.add((it as GenericQuestionItem).question)
                else -> {}
            }
        }

        return answeredQuestions
    }


}
