package com.mark.sharee.navigation.arguments

import com.mark.sharee.navigation.Arguments
import com.mark.sharee.network.model.responses.GeneralPollResponse
import com.mark.sharee.network.model.responses.Message

class TransferInfo(var flow: Flow = Flow.Default): Arguments() {

    var phoneNumber: String = ""
    lateinit var poll: GeneralPollResponse
    var messages: MutableList<Message> = mutableListOf()

    enum class Flow {
        Default,
        GeneralPolls,
        MedicalPolls
    }
}