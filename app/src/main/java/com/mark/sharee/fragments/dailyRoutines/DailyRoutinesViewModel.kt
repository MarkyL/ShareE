package com.mark.sharee.fragments.dailyRoutines

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.mark.sharee.data.ShareeRepository
import com.mark.sharee.model.User
import com.mark.sharee.model.poll.AnsweredQuestion
import com.mark.sharee.model.poll.Question
import com.mark.sharee.mvvm.BaseViewModel
import com.mark.sharee.mvvm.State
import com.mark.sharee.network.model.responses.DailyRoutineResponse
import com.mark.sharee.network.model.responses.GeneralPollResponse
import com.mark.sharee.utils.Event
import kotlinx.coroutines.launch
import timber.log.Timber

class DailyRoutinesViewModel constructor(application: Application, private val shareeRepository: ShareeRepository)
    : BaseViewModel<Event<DailyRoutinesDataState>, DailyRoutinesDataEvent>(application = application) {
    
    lateinit var pollId: String

    override fun handleScreenEvents(event: DailyRoutinesDataEvent) {
        Timber.i("dispatchScreenEvent: ${event.javaClass.simpleName}")
        when (event) {
            is GetDailyRoutines -> getDailyRoutines()
        }
    }

    private fun getDailyRoutines() {
        Timber.i("getDailyRoutines")
        viewModelScope.launch {
            runCatching {
                Timber.i("getDailyRoutines - runCatching")
                publish(state = State.LOADING)
                shareeRepository.dailyRoutine()
            }.onSuccess {
                Timber.i("getDailyRoutines - onSuccess")
                publish(state = State.NEXT, items = Event(GetDailyRoutinesSuccess(it)))
            }.onFailure {
                Timber.e("getDailyRoutines - onFailure $it")
                publish(state = State.ERROR, items = Event(GetDailyRoutinesFailure), throwable = it)
            }
        }
    }

}

// Events = actions coming from UI
sealed class DailyRoutinesDataEvent
object GetDailyRoutines : DailyRoutinesDataEvent()

// State = change of states by the view model
sealed class DailyRoutinesDataState
data class GetDailyRoutinesSuccess(val dailyRoutineResponse: DailyRoutineResponse): DailyRoutinesDataState()
object GetDailyRoutinesFailure: DailyRoutinesDataState()