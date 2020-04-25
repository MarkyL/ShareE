package com.mark.sharee.network.model.responses

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("notificationBody") val body: String = "",
    @SerializedName("notificationTitle") val title: String = "",
    val timeStamp: Long = 0)

