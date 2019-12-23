package com.mark.sharee.fragments

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharee.R
import com.mark.sharee.core.Constants
import com.mark.sharee.model.mvvmbase.BaseViewModel
import com.mark.sharee.model.mvvmbase.LiveState
import com.mark.sharee.network.endpoint.ShareeService
import com.mark.sharee.network.model.responses.MovieResponse
import com.mark.sharee.utils.Event
import kotlinx.coroutines.launch
import timber.log.Timber

class MovieViewModel constructor(application: Application, private val shareeService: ShareeService) : BaseViewModel<MovieDataState, MovieDataEvent>(application = application) {

    private val _uiState = MutableLiveData<MovieDataState>()
    val uiState: LiveData<MovieDataState> get() = _uiState

    override fun handleScreenEvents(event: MovieDataEvent) {
        Timber.i("dispatchScreenEvent: ${event.javaClass.simpleName}")
        when(event){
            is FetchMovies -> {
                fetchMoviesFromServer()
            }
        }
    }

    private fun fetchMoviesFromServer() {
        viewModelScope.launch {
            runCatching {
                emitUiState(showProgress = true)
                shareeService.popularMovies(apiKey = Constants.API_KEY)
            }.onSuccess {
                emitUiState(movies = Event(it.movies))
            }.onFailure {
                it.printStackTrace()
                emitUiState(error = Event(R.string.internet_failure_error))
            }
        }
    }

    private fun emitUiState(
        showProgress: Boolean = false,
        movies: Event<List<MovieResponse.Movie>>? = null,
        error: Event<Int>? = null
    ) {
        val dataState = MovieDataState(showProgress, movies, error)
        _uiState.value = dataState
    }
}

// Events = actions coming from UI
sealed class MovieDataEvent
object FetchMovies : MovieDataEvent()

// State = change of states by the view model
data class MovieDataState (
    val showProgress: Boolean,
    val movies: Event<List<MovieResponse.Movie>>?,
    val error: Event<Int>?
)


