package com.mark.sharee.network.model.responses

import com.mark.sharee.model.poll.Question
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class GeneralPollResponse (
    @SerialName(value = "id") val id: String,
    @SerialName(value = "name") val name: String,
    @SerialName(value = "isGeneralPoll") val isGeneralPoll: Boolean,
    @SerialName(value = "pollSections") val pollSections: MutableList<PollSection>,
    @SerialName(value = "type") val type: String)

