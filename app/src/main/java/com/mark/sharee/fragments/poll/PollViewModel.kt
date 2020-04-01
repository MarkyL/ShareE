package com.mark.sharee.fragments.poll

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.mark.sharee.data.ShareeRepository
import com.mark.sharee.model.User
import com.mark.sharee.model.poll.AnsweredQuestion
import com.mark.sharee.model.poll.Question
import com.mark.sharee.mvvm.BaseViewModel
import com.mark.sharee.mvvm.State
import com.mark.sharee.network.model.responses.GeneralPollResponse
import com.mark.sharee.utils.Event
import kotlinx.coroutines.launch
import timber.log.Timber

class PollViewModel constructor(application: Application, private val shareeRepository: ShareeRepository)
    : BaseViewModel<Event<PollDataState>, PollDataEvent>(application = application) {
    
    lateinit var pollId: String

    override fun handleScreenEvents(event: PollDataEvent) {
        Timber.i("dispatchScreenEvent: ${event.javaClass.simpleName}")
        when (event) {
            is SubmitPoll -> submitPoll(event.pollId, event.answeredQuestions)
        }
    }

    private fun submitPoll(pollId: String, answeredQuestions: List<Question>) {
        val filteredAnswers = answeredQuestions.filter { item -> item.answer != null }
        val verificationToken = User.me()?.getToken()
        if (verificationToken == null) {
            handleNoToken()
            return
        }
        viewModelScope.launch {
            kotlin.runCatching {
                Timber.i("submitPoll - runCatching")
                publish(state = State.LOADING)
                Timber.i("answered questions : ${AnsweredQuestion.convertQuestionListToAnsweredQuestionList(filteredAnswers)}")
                shareeRepository.submitPoll(verificationToken, pollId, AnsweredQuestion.convertQuestionListToAnsweredQuestionList(filteredAnswers))
            }.onSuccess {
                Timber.i("submitPoll - onSuccess")
                publish(state = State.NEXT, items = Event(SubmitPollSuccess))
            }.onFailure {
                Timber.e("submitPoll - onFailure $it")
                publish(state = State.ERROR, items = Event(SubmitPollFailure), throwable = it)
            }
        }
    }

    private fun handleNoToken() {
        Timber.e("getPoll - user has no verificationToken")
        publish(state = State.ERROR, throwable = Throwable("No verification token"))
        return
    }

}

// Events = actions coming from UI
sealed class PollDataEvent
data class SubmitPoll(val pollId: String, val answeredQuestions: List<Question>) : PollDataEvent()

// State = change of states by the view model
sealed class PollDataState
object SubmitPollSuccess : PollDataState()
object SubmitPollFailure : PollDataState()