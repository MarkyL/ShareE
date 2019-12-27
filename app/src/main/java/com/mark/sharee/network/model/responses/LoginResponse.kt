package com.mark.sharee.network.model.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class LoginResponse(
    @SerialName(value = "verificationToken") val verificationToken: String,
    @SerialName(value = "patientName") val patientName: String)