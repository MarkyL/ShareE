package com.mark.sharee.screens

import com.mark.sharee.fragments.dailyRoutines.DailyRoutinesTabFragment
import com.mark.sharee.navigation.Screen
import com.mark.sharee.navigation.arguments.TransferInfo

class DailyRoutinesTabScreen(transferInfo : TransferInfo) : Screen(transferInfo) {

    override fun create(): DailyRoutinesTabFragment {
        return DailyRoutinesTabFragment()
    }

}