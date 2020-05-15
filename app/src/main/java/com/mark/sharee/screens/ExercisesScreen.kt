package com.mark.sharee.screens

import com.mark.sharee.fragments.exercises.ExercisesFragment
import com.mark.sharee.navigation.Screen
import com.mark.sharee.navigation.arguments.ExercisesInfo

class ExercisesScreen(exercisesInfo: ExercisesInfo) : Screen(exercisesInfo) {

    override fun create(): ExercisesFragment {
        return ExercisesFragment()
    }

}