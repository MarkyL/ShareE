package com.mark.sharee.network.model.responses

data class DailyRoutineResponse (
    val weekdayActivities: MutableList<DailyActivity>,
    val weekendActivities: MutableList<DailyActivity>)

