package com.mark.sharee.network.adapter

import kotlinx.serialization.SerialName

data class ServerException(
    @SerialName(value = "errorCode") val errorCode: Int,
    @SerialName(value = "errorReason") val errorReason: String,
    @SerialName(value = "messageToClient") val messageToClient: String) : Exception() {

    companion object {
        const val PERSON_NOT_FOUND_CODE = 111
    }
}