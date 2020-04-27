package com.mark.sharee.screens

import com.mark.sharee.fragments.about.AboutFragment
import com.mark.sharee.navigation.Screen

class AboutScreen : Screen() {

    override fun create(): AboutFragment {
        return AboutFragment()
    }

}