package com.mark.sharee.network.model.responses

import com.google.gson.annotations.SerializedName

data class ScheduledNotification (
    @SerializedName("id") val id : Int,
    @SerializedName("title") val title : String,
    @SerializedName("body") val body : String,
    @SerializedName("time") val time : String,
    @SerializedName("active") val active : Boolean,
    @SerializedName("weekdays") val weekdays : Boolean
)
