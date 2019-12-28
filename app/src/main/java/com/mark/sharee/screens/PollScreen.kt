package com.mark.sharee.screens

import com.mark.sharee.fragments.poll.PollFragment
import com.mark.sharee.navigation.Screen
import com.mark.sharee.navigation.arguments.TransferInfo

class PollScreen(transferInfo : TransferInfo) : Screen(transferInfo) {

    override fun create(): PollFragment {
        return PollFragment()
    }

}