package com.mark.sharee.fragments.exercises

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.mark.sharee.data.ShareeRepository
import com.mark.sharee.model.User
import com.mark.sharee.mvvm.BaseViewModel
import com.mark.sharee.mvvm.State
import com.mark.sharee.network.model.responses.ExerciseCategory
import com.mark.sharee.utils.Event
import kotlinx.coroutines.launch
import timber.log.Timber

class ExerciseCategoriesViewModel constructor(
    application: Application,
    private val shareeRepository: ShareeRepository
) : BaseViewModel<Event<ExerciseCategoriesDataState>, ExerciseCategoriesDataEvent>(application = application) {

    override fun handleScreenEvents(event: ExerciseCategoriesDataEvent) {
        Timber.i("dispatchScreenEvent: ${event.javaClass.simpleName}")
        when (event) {
            is GetExercises -> getExercises()
        }
    }

    private fun getExercises() {
        User.me()?.let {
            viewModelScope.launch {
                runCatching {
                    Timber.i("getExercises - runCatching")
                    publish(state = State.LOADING)
                    shareeRepository.getExercises(it.getToken())
                }.onSuccess {
                    Timber.i("getExercises - onSuccess, response = $it")
                    publish(state = State.NEXT, items = Event(GetExercisesSuccess(it)))
                }.onFailure {
                    Timber.e("getExercises - onFailure $it")
                    publish(state = State.ERROR, throwable = it)
                }
            }
        }
    }
}

// Events = actions coming from UI
sealed class ExerciseCategoriesDataEvent
object GetExercises : ExerciseCategoriesDataEvent()

// State = change of states by the view model
sealed class ExerciseCategoriesDataState
class GetExercisesSuccess(val exerciseCategories: MutableList<ExerciseCategory>) : ExerciseCategoriesDataState()