package com.mark.sharee.network.model.responses

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class GeneralPollsResponse(
    @SerialName(value = "generalPolls") val generalPolls: MutableList<GeneralPollResponse>)