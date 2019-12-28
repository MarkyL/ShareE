package com.mark.sharee.screens

import com.mark.sharee.fragments.main.MainFragment
import com.mark.sharee.navigation.Screen
import com.mark.sharee.navigation.arguments.TransferInfo

class MainScreen(transferInfo : TransferInfo) : Screen(transferInfo) {

    override fun create(): MainFragment {
        return MainFragment()
    }

}