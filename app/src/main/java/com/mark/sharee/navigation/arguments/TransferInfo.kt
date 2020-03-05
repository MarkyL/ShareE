package com.mark.sharee.navigation.arguments

import com.mark.sharee.navigation.Arguments
import com.mark.sharee.network.model.responses.GeneralPollResponse

class TransferInfo: Arguments() {

    var phoneNumber: String = ""
    lateinit var poll: GeneralPollResponse
}