package com.mark.sharee.network.model.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@kotlinx.serialization.Serializable
data class GeneralResponse(@SerialName(value = "name") val name: String) {

    @Serializable
    data class GeneralResponse(@SerialName(value = "name") val name: String)
}