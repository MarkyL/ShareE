package com.mark.sharee.fragments.generalPolls

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.mark.sharee.data.ShareeRepository
import com.mark.sharee.model.User
import com.mark.sharee.mvvm.BaseViewModel
import com.mark.sharee.mvvm.State
import com.mark.sharee.network.model.responses.GeneralPollResponse
import com.mark.sharee.network.model.responses.GeneralPollsResponse
import com.mark.sharee.utils.Event
import kotlinx.coroutines.launch
import timber.log.Timber

class GeneralPollsViewModel constructor(application: Application, private val shareeRepository: ShareeRepository)
    : BaseViewModel<Event<PollDataState>, GeneralPollsDataEvent>(application = application) {
    
    private lateinit var pollId: String

    override fun handleScreenEvents(event: GeneralPollsDataEvent) {
        Timber.i("dispatchScreenEvent: ${event.javaClass.simpleName}")
        when (event) {
            is GetGeneralPolls -> getGeneralPolls()
        }
    }

    private fun getGeneralPolls() {
        val verificationToken = User.me()?.getToken()
        if (verificationToken == null) {
            handleNoToken()
            return
        }

        viewModelScope.launch {
            runCatching {
                Timber.i("getGeneralPolls - runCatching")
                publish(state = State.LOADING)
                shareeRepository.getGeneralPolls(verificationToken)
            }.onSuccess {
                Timber.i("getGeneralPolls - onSuccess, loginResponse = $it")
                publish(state = State.NEXT, items = Event(GetGeneralPollsSuccess(it)))
            }.onFailure {
                Timber.e("getGeneralPolls - onFailure $it")
                publish(state = State.ERROR, throwable = it)
            }
        }
    }

    private fun handleNoToken() {
        Timber.e("getGeneralPolls - user has no verificationToken")
        publish(state = State.ERROR, throwable = Throwable("No verification token"))
        return
    }

}

// Events = actions coming from UI
sealed class GeneralPollsDataEvent
object GetGeneralPolls : GeneralPollsDataEvent()

// State = change of states by the view model
sealed class PollDataState
class GetGeneralPollsSuccess(val generalPollsResponse: GeneralPollsResponse): PollDataState()