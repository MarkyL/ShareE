package com.mark.sharee.fragments.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sharee.R
import com.mark.sharee.data.ShareeRepository
import com.mark.sharee.mvvm.BaseViewModel
import com.mark.sharee.mvvm.State
import com.mark.sharee.network.model.responses.GeneralResponse
import com.mark.sharee.network.model.responses.ScheduledNotification
import com.mark.sharee.utils.Event
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel constructor(application: Application, private val shareeRepository: ShareeRepository)
    : BaseViewModel<Event<MainDataState>, MainDataEvent>(application = application) {

    private val _uiState = MutableLiveData<MainDataState>()
    val uiState: LiveData<MainDataState> get() = _uiState

    override fun handleScreenEvents(event: MainDataEvent) {
        Timber.i("dispatchScreenEvent: ${event.javaClass.simpleName}")
        when(event){
            is GetScheduledNotifications -> getScheduledNotifications()
        }
    }

    private fun getScheduledNotifications() {
        viewModelScope.launch {
            runCatching {
                Timber.i("mark - runCatching")
                shareeRepository.scheduledNotifications()
            }.onSuccess {
                Timber.i("mark - $it")
                publish(state = State.NEXT, items = Event(ScheduledNotificationsSuccess(it)))
            }.onFailure {
                Timber.i("mark - onFailure")
                it.printStackTrace()
                publish(state = State.ERROR, items = Event(ScheduledNotificationsFailure), throwable = it)
            }
        }
    }
}

// Events = actions coming from UI
sealed class MainDataEvent
object GetScheduledNotifications: MainDataEvent()

sealed class MainDataState
data class ScheduledNotificationsSuccess(val scheduledNotifications: List<ScheduledNotification>): MainDataState()
object ScheduledNotificationsFailure: MainDataState()


