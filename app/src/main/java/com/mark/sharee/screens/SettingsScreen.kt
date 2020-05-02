package com.mark.sharee.screens

import com.mark.sharee.fragments.settings.SettingsFragment
import com.mark.sharee.navigation.Screen

class SettingsScreen : Screen() {

    override fun create(): SettingsFragment {
        return SettingsFragment()
    }

}