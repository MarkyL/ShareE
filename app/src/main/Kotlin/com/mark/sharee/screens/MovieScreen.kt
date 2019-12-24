package com.mark.sharee.screens

import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.fragments.MovieFragment
import com.mark.sharee.navigation.Screen

class MovieScreen : Screen() {

    override fun create(): ShareeFragment {
        return MovieFragment()
    }

}