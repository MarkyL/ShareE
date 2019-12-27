package com.mark.sharee.network.model.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@kotlinx.serialization.Serializable
data class LoginResponse(@SerialName(value = "verificationToken") val verificationToken: String) {

    @Serializable
    data class LoginResponse(@SerialName(value = "verificationToken") val verificationToken: String)
}


// Patient: loginToken phoneNumber name