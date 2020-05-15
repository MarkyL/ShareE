package com.mark.sharee.fragments.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sharee.R
import com.mark.sharee.core.Action
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.navigation.arguments.ExercisesInfo
import com.mark.sharee.network.model.responses.ExerciseCategory
import kotlinx.android.synthetic.main.fragment_department_info.*
import kotlinx.android.synthetic.main.sharee_toolbar.view.*

class ExercisesFragment : ShareeFragment() {


    private lateinit var exercisesInfo: ExercisesInfo
    private lateinit var exerciseCategory: ExerciseCategory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exercisesInfo = castArguments(ExercisesInfo::class.java)
        exerciseCategory = exercisesInfo.exerciseCategory

        configureToolbar()
    }

    private fun configureToolbar() {
        toolbar.titleTextView.text = resources.getString(R.string.about_us)
        toolbar.addActions(arrayOf(Action.BackBlack), this)
    }
}