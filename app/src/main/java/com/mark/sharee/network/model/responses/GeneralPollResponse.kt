package com.mark.sharee.network.model.responses

import com.mark.sharee.model.poll.Question
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class GeneralPollResponse (
    @SerialName(value = "id") val id: String,
    @SerialName(value = "name") val name: String,
    @SerialName(value = "questions") val questions: MutableList<Question>,
    @SerialName(value = "type") val type: String)

