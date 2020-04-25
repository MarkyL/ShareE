package com.mark.sharee.screens

import com.mark.sharee.fragments.messages.MessagesFragment
import com.mark.sharee.navigation.Screen
import com.mark.sharee.navigation.arguments.TransferInfo

class MessagesScreen(transferInfo : TransferInfo) : Screen(transferInfo) {

    override fun create(): MessagesFragment {
        return MessagesFragment()
    }

}