package com.mark.sharee.fragments.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharee.R
import com.mark.sharee.adapters.BaseAdapter
import com.mark.sharee.adapters.ExerciseCategoriesAdapter
import com.mark.sharee.core.Action
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.fragments.generalPolls.GetGeneralPollsSuccess
import com.mark.sharee.mvvm.State
import com.mark.sharee.mvvm.ViewModelHolder
import com.mark.sharee.navigation.arguments.ExercisesInfo
import com.mark.sharee.network.model.responses.ExerciseCategory
import com.mark.sharee.screens.ExercisesScreen
import com.mark.sharee.screens.MainScreen
import com.mark.sharee.utils.Event
import com.mark.sharee.utils.GridSpacingItemDecoration
import com.mark.sharee.utils.Toaster
import kotlinx.android.synthetic.main.fragment_general_polls.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class ExerciseCategoriesFragment: ShareeFragment(), BaseAdapter.AdapterListener<ExerciseCategory> {

    private val viewModel: ExerciseCategoriesViewModel by sharedViewModel()
    private val categoriesAdapter = ExerciseCategoriesAdapter(listener = this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_exercise_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            addItemDecoration(GridSpacingItemDecoration(1, 30, true))
            this.adapter = categoriesAdapter
        }

        registerViewModel()

        configureScreen()

        viewModel.dispatchInputEvent(GetExercises)
    }

    private fun configureScreen() {
        configureToolbar()
    }

    private fun configureToolbar() {
        toolbar.addActions(arrayOf(Action.BackBlack), this)
        toolbar.setTitle(resources.getString(R.string.exercise_categories_screen_title))
    }

    private fun registerViewModel() {
        viewModel.dataStream.observe(
            viewLifecycleOwner,
            Observer<ViewModelHolder<Event<ExerciseCategoriesDataState>>> { t ->
                when (t.state) {
                    State.INIT -> { }
                    State.LOADING -> { showProgressView() }
                    State.NEXT -> {
                        hideProgressView()
                        handleNext(t.data)
                    }
                    State.ERROR -> { t.throwable?.let { handleError(it) } }
                    State.COMPLETE -> { hideProgressView() }
                }
            })
    }

    private fun showProgressView() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressView() {
        progressBar.visibility = View.GONE
    }

    private fun handleNext(result: Event<ExerciseCategoriesDataState>?) {
        result?.let { responseEvent ->
            if (!responseEvent.consumed) {
                responseEvent.consume()?.let { response ->
                    when (response) {
                        is GetExercisesSuccess -> handleGetExercisesSuccess(response.exerciseCategories)
                    }
                }
            }
        }
    }

    private fun handleGetExercisesSuccess(exercisesCategories: MutableList<ExerciseCategory>) {
        Timber.i("handleGetExercisesSuccess() exercisesCategories = {$exercisesCategories}")
        categoriesAdapter.submitList(exercisesCategories)
    }

    private fun handleError(throwable: Throwable) {
        hideProgressView()
        errorHandler.handleError(this, throwable)
    }

    override fun onItemClick(data: ExerciseCategory) {
        Toaster.show(this, "on category click")
        navigator.replace(ExercisesScreen(ExercisesInfo(data)))
    }

}
