package com.mark.sharee.fragments.main

import android.app.AlarmManager
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mark.sharee.data.ShareeRepository
import com.mark.sharee.model.User
import com.mark.sharee.mvvm.BaseViewModel
import com.mark.sharee.mvvm.State
import com.mark.sharee.network.model.responses.Message
import com.mark.sharee.network.model.responses.ScheduledNotification
import com.mark.sharee.utils.Event
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel constructor(application: Application, private val shareeRepository: ShareeRepository)
    : BaseViewModel<Event<MainDataState>, MainDataEvent>(application = application) {

    override fun handleScreenEvents(event: MainDataEvent) {
        Timber.i("dispatchScreenEvent: ${event.javaClass.simpleName}")
        when(event){
            is GetScheduledNotifications -> getScheduledNotifications()
            is UpdateFcmToken -> updateFcmToken(event.fcmToken, event.verificationToken)
            is GetMessages -> getMessages(event.verificationToken)
        }
    }

    private fun getScheduledNotifications() {
        viewModelScope.launch {
            runCatching {
                Timber.i("getScheduledNotifications - run")
                shareeRepository.scheduledNotifications()
            }.onSuccess {
                Timber.i("mark - $it")
                publish(state = State.NEXT, items = Event(ScheduledNotificationsSuccess(it)))
//                handleScheduledNotificationData(it)
            }.onFailure {
                Timber.i("mark - onFailure")
                it.printStackTrace()
                publish(state = State.ERROR, items = Event(ScheduledNotificationsFailure), throwable = it)
            }
        }
    }

    private fun updateFcmToken(fcmToken: String, verificationToken: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                Timber.i("updateFcmToken - run")
                shareeRepository.updateFcmToken(verificationToken, fcmToken)
            }.onSuccess {
                Timber.i("updateFcmToken - onSuccess, response = $it")
                User.me()?.setFcmToken(fcmToken)
            }.onFailure {
                Timber.e("updateFcmToken - onFailure $it")
                publish(state = State.ERROR, items = Event(UpdateFcmTokenFailure), throwable = it)
            }
        }
    }

    private fun getMessages(verificationToken: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                Timber.i("updateFcmToken - run")
                shareeRepository.getMessages(verificationToken)
            }.onSuccess {
                Timber.i("updateFcmToken - onSuccess, response = $it")
                publish(state = State.NEXT, items = Event(GetMessagesSuccess(it)))
            }.onFailure {
                Timber.e("updateFcmToken - onFailure $it")
                publish(state = State.ERROR, items = Event(GetMessagesFailure), throwable = it)
            }
        }
    }
}

// Events = actions coming from UI
sealed class MainDataEvent
object GetScheduledNotifications: MainDataEvent()
data class GetMessages(val verificationToken: String): MainDataEvent()
data class UpdateFcmToken(val fcmToken: String, val verificationToken: String): MainDataEvent()

sealed class MainDataState
data class ScheduledNotificationsSuccess(val scheduledNotifications: List<ScheduledNotification>): MainDataState()
object ScheduledNotificationsFailure: MainDataState()
object UpdateFcmTokenFailure: MainDataState()
data class GetMessagesSuccess(val messages: MutableList<Message>): MainDataState()
object GetMessagesFailure: MainDataState()


