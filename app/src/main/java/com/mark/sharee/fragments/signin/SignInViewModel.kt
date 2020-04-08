package com.mark.sharee.fragments.signin

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sharee.R
import com.mark.sharee.data.ShareeRepository
import com.mark.sharee.model.User
import com.mark.sharee.mvvm.BaseViewModel
import com.mark.sharee.network.model.responses.LoginResponse
import com.mark.sharee.utils.Event
import kotlinx.coroutines.launch
import timber.log.Timber

class SignInViewModel constructor(application: Application, private val shareeRepository: ShareeRepository)
    : BaseViewModel<SignInDataState, SignInDataEvent>(application = application) {

    private val _uiState = MutableLiveData<SignInDataState>()
    val uiState: LiveData<SignInDataState> get() = _uiState

    override fun handleScreenEvents(event: SignInDataEvent) {
        Timber.i("dispatchScreenEvent: ${event.javaClass.simpleName}")
        when (event) {
            is Login -> {
                login(event.phoneNumber, event.uuid)
            }
        }
    }

    private fun login(phoneNumber: String, uuid: String) {
        User.create(phoneNumber, uuid)

        viewModelScope.launch {
            runCatching {
                Timber.i("login - runCatching")
                emitUiState(showProgress = true)
                shareeRepository.login(phoneNumber, uuid)
            }.onSuccess {
                Timber.i("login - onSuccess, loginResponse = $it")
                User.me()?.updateToken(it.verificationToken)
                emitUiState(response = Event(it))
            }.onFailure {
                Timber.e("login - onFailure $it")
                emitUiState(error = Event(it))
            }
        }
    }


    private fun emitUiState(
        showProgress: Boolean = false,
        response: Event<LoginResponse>? = null,
        error: Event<Throwable>? = null
    ) {
        val dataState = SignInDataState(showProgress, response, error)
        _uiState.value = dataState
    }
}

// Events = actions coming from UI
sealed class SignInDataEvent
data class Login(val phoneNumber: String, val uuid: String) : SignInDataEvent()

// State = change of states by the view model
data class SignInDataState(
    val showProgress: Boolean,
    val response: Event<LoginResponse>?,
    val error: Event<Throwable>?
)



