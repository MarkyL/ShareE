package com.mark.sharee.network.model.responses

data class ExerciseCategory (
    val id: String,
    val name : String,
    val exercises: MutableList<Exercise>)

data class Exercise (
    val id: String,
    val description: String,
    val url: String? = null)
