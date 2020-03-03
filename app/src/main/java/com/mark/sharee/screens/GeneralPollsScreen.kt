package com.mark.sharee.screens

import com.mark.sharee.fragments.generalPolls.GeneralPollsFragment
import com.mark.sharee.fragments.poll.PollFragment
import com.mark.sharee.navigation.Screen
import com.mark.sharee.navigation.arguments.TransferInfo

class GeneralPollsScreen(transferInfo : TransferInfo) : Screen(transferInfo) {

    override fun create(): GeneralPollsFragment {
        return GeneralPollsFragment()
    }

}