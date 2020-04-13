package com.mark.sharee.network.model.responses

import com.mark.sharee.model.poll.Question
import kotlinx.serialization.SerialName
import kotlinx.serialization.Transient

@kotlinx.serialization.Serializable
data class PollSection (
    @SerialName(value = "id") val id: String,
    @SerialName(value = "name") val name: String,
    @SerialName(value = "questions") val questions: MutableList<Question>) {

    @Transient var expanded: Boolean = true
}