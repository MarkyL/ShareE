package com.mark.sharee.screens

import com.mark.sharee.fragments.exercises.ExerciseCategoriesFragment
import com.mark.sharee.navigation.Screen

class ExerciseCategoriesScreen : Screen() {

    override fun create(): ExerciseCategoriesFragment {
        return ExerciseCategoriesFragment()
    }

}