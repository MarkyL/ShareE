package com.mark.sharee.fragments.poll

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sharee.R
import com.mark.sharee.data.ShareeRepository
import com.mark.sharee.model.User
import com.mark.sharee.model.poll.AnsweredQuestion
import com.mark.sharee.model.poll.Question
import com.mark.sharee.mvvm.BaseViewModel
import com.mark.sharee.network.model.responses.PollResponse
import com.mark.sharee.utils.Event
import kotlinx.coroutines.launch
import timber.log.Timber

class PollViewModel constructor(application: Application, private val shareeRepository: ShareeRepository)
    : BaseViewModel<PollDataState, PollDataEvent>(application = application) {

    private val _uiState = MutableLiveData<PollDataState>()
    val uiState: LiveData<PollDataState> get() = _uiState

    override fun handleScreenEvents(event: PollDataEvent) {
        Timber.i("dispatchScreenEvent: ${event.javaClass.simpleName}")
        when (event) {
            is GetPoll -> getPoll()
            is SubmitPoll -> submitPoll(event.answeredQuestions)

        }
    }

    private fun getPoll() {
        val verificationToken = User.me()?.getToken()
        if (verificationToken == null) {
            handleNoToken()
            return
        }
        viewModelScope.launch {
            runCatching {
                Timber.i("getPoll - runCatching")
                emitUiState(showProgress = true)
                shareeRepository.poll(verificationToken)
            }.onSuccess {
                Timber.i("login - onSuccess, loginResponse = $it")
                emitUiState(response = Event(it))
            }.onFailure {
                Timber.e("login - onFailure $it")
                emitUiState(error = Event(R.string.error_general))
            }
        }
    }

    private fun submitPoll(answeredQuestions: List<Question>) {
        val verificationToken = User.me()?.getToken()
        if (verificationToken == null) {
            handleNoToken()
            return
        }
        viewModelScope.launch {
            kotlin.runCatching {
                Timber.i("submitPoll - runCatching")
                emitUiState(showProgress = true)
                shareeRepository.submitPoll(verificationToken, AnsweredQuestion.convertQuestionListToAnsweredQuestionList(answeredQuestions))
            }.onSuccess {
                Timber.i("submitPoll - onSuccess")
            }.onFailure {
                Timber.e("submitPoll - onFailure $it")
                emitUiState(error = Event(R.string.error_general))
            }
        }
    }

    private fun emitUiState(
        showProgress: Boolean = false,
        response: Event<PollResponse>? = null,
        error: Event<Int>? = null
    ) {
        val dataState = PollDataState(showProgress, response, error)
        _uiState.value = dataState
    }

    private fun handleNoToken() {
        Timber.e("getPoll - user has no verificationToken")
        emitUiState(error = Event(R.string.no_verification_token))
        return
    }

}

// Events = actions coming from UI
sealed class PollDataEvent
object GetPoll : PollDataEvent()
data class SubmitPoll(val answeredQuestions: List<Question>) : PollDataEvent()

// State = change of states by the view model
data class PollDataState(
    val showProgress: Boolean,
    val response: Event<PollResponse>?,
    val error: Event<Int>?
)


