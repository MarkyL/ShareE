package com.mark.sharee.network.model.responses

data class DailyActivity (
    val id: Long,
    val startTime: String,
    val endTime: String,
    val activityDescription: String)