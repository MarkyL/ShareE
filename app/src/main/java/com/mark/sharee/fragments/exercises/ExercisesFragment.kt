package com.mark.sharee.fragments.exercises

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharee.R
import com.mark.sharee.adapters.ExercisesAdapter
import com.mark.sharee.adapters.ExercisesAdapterListener
import com.mark.sharee.core.Action
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.model.User
import com.mark.sharee.navigation.arguments.ExercisesInfo
import com.mark.sharee.network.model.responses.Exercise
import com.mark.sharee.network.model.responses.ExerciseCategory
import com.mark.sharee.utils.GridSpacingItemDecoration
import com.mark.sharee.utils.NetworkUtils
import com.mark.sharee.utils.Toaster
import kotlinx.android.synthetic.main.fragment_exercises.*
import kotlinx.android.synthetic.main.sharee_toolbar.view.*
import timber.log.Timber
import java.util.*

class ExercisesFragment : ShareeFragment(), ExercisesAdapterListener {

    private lateinit var exercisesInfo: ExercisesInfo
    private lateinit var exerciseCategory: ExerciseCategory

    private val exercisesAdapter = ExercisesAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_exercises, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exercisesInfo = castArguments(ExercisesInfo::class.java)
        exerciseCategory = exercisesInfo.exerciseCategory
        loadSavedExercisesStates()

        configureToolbar()

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            addItemDecoration(GridSpacingItemDecoration(1, 30, true))
            this.adapter = exercisesAdapter
        }

        this.exercisesAdapter.submitList(exerciseCategory.exercises)
    }

    private fun loadSavedExercisesStates() {
        val user = User.me() ?: return

        if (DateUtils.isToday(user.getLongParam(getCategoryKey()))) {
            exerciseCategory.exercises.forEach {
                it.isChecked = user.getBooleanParam(getExerciseKey(it))
            }
        } else {
            // values by default are false so exercises got back to defaults.
            Toaster.show(this, "יום חדש, הפעילות שלך התאפסה.", false)
            //TODO - consider showing a dialog...
        }
    }

    private fun configureToolbar() {
        toolbar.titleTextView.text = exerciseCategory.name
        toolbar.addActions(arrayOf(Action.BackBlack), this)
    }

    override fun onItemClick(data: Exercise) {
        Toaster.show(this, data.description)
    }

    override fun onOpenUrlClick(url: String) {
        NetworkUtils.openUrl(requireContext(), url)
    }

    override fun onCheckStateChange(exercise: Exercise, isChecked: Boolean) {
        User.me()?.let {
            it.saveBooleanParam(getExerciseKey(exercise), isChecked)

            val now = System.currentTimeMillis()
            it.saveLongParam(getCategoryKey(), now)
        }
    }

    private fun getExerciseKey(exercise: Exercise): String {
        return exerciseCategory.id + KEY_DIVIDER + exercise.id
    }

    private fun getCategoryKey(): String {
        return exerciseCategory.id + KEY_DIVIDER + KEY_DATE
    }

    companion object {
        private const val KEY_DIVIDER = "_"
        private const val KEY_DATE = "date"
    }
}