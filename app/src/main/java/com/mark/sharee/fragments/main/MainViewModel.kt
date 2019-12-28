package com.mark.sharee.fragments.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sharee.R
import com.mark.sharee.data.ShareeRepository
import com.mark.sharee.mvvm.BaseViewModel
import com.mark.sharee.network.model.responses.GeneralResponse
import com.mark.sharee.utils.Event
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel constructor(application: Application, private val shareeRepository: ShareeRepository)
    : BaseViewModel<MainDataState, MainDataEvent>(application = application) {

    private val _uiState = MutableLiveData<MainDataState>()
    val uiState: LiveData<MainDataState> get() = _uiState

    override fun handleScreenEvents(event: MainDataEvent) {
        Timber.i("dispatchScreenEvent: ${event.javaClass.simpleName}")
        when(event){
            is Echo -> {
                Timber.i("mark - Echo")
                echo(event.name)
            }
        }
    }

    private fun echo(name: String) {
        viewModelScope.launch {
            runCatching {
                Timber.i("mark - runCatching")
                emitUiState(showProgress = true)
                shareeRepository.create(name)
            }.onSuccess {
                Timber.i("mark - $it")
                emitUiState(response = Event(it))
            }.onFailure {
                Timber.i("mark - onFailure")
                it.printStackTrace()
                emitUiState(error = Event(R.string.internet_failure_error))
            }
        }
    }

    private fun emitUiState(
        showProgress: Boolean = false,
        response: Event<GeneralResponse>? = null,
        error: Event<Int>? = null
    ) {
        val dataState = MainDataState(showProgress, response, error)
        _uiState.value = dataState
    }
}

// Events = actions coming from UI
sealed class MainDataEvent
data class Echo(val name: String) : MainDataEvent()

// State = change of states by the view model
data class MainDataState (
    val showProgress: Boolean,
    val response: Event<GeneralResponse>?,
    val error: Event<Int>?
)


