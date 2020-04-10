package com.mark.sharee.activities

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.mark.sharee.data.ShareeRepository
import com.mark.sharee.mvvm.BaseViewModel
import com.mark.sharee.mvvm.State
import com.mark.sharee.utils.Event
import kotlinx.coroutines.launch
import timber.log.Timber

class ActivityViewModel constructor(application: Application, private val shareeRepository: ShareeRepository)
    : BaseViewModel<Event<ActivityDataState>, ActivityDataEvent>(application = application) {

    companion object {
        private const val TAG = "ActivityViewModel"
    }

    override fun handleScreenEvents(event: ActivityDataEvent) {
        Timber.i(TAG,"dispatchScreenEvent: ${event.javaClass.simpleName}")
        when (event) {
            is UpdateFcmToken -> updateFcmToken(event.fcmToken, event.verificationToken)
        }
    }

    private fun updateFcmToken(fcmToken: String, verificationToken: String) {
        Timber.i("mark - test updateFcmToken func")
        viewModelScope.launch {
            runCatching {
                Timber.i(TAG,"updateFcmToken - runCatching")
                publish(state = State.LOADING)
                shareeRepository.updateNotificationMethod(verificationToken, fcmToken)
            }.onSuccess {
                Timber.i(TAG,"updateFcmToken - onSuccess, response = $it")
                publish(state = State.NEXT, items = Event(UpdateFcmTokenSuccess))
            }.onFailure {
                Timber.e(TAG,"updateFcmToken - onFailure $it")
                publish(state = State.ERROR, throwable = it)
            }
        }
    }

}



// Events = actions coming from activity
sealed class ActivityDataEvent
data class UpdateFcmToken(val fcmToken: String, val verificationToken: String) : ActivityDataEvent()

// State = change of states by the view model
sealed class ActivityDataState
object UpdateFcmTokenSuccess : ActivityDataState()