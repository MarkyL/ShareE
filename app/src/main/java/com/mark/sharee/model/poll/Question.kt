package com.mark.sharee.model.poll

import kotlinx.serialization.SerialName
import kotlinx.serialization.Transient

@kotlinx.serialization.Serializable
data class Question(
    @SerialName(value = "id") val id: Long,
    @SerialName(value = "type") val type: QuestionType,
    @SerialName(value = "question") val question: String,
    @SerialName(value = "answers") var answers: MutableList<String> = mutableListOf(),

    @Transient var answer: Any? = null) {

    enum class QuestionType(val type: String) {
        BOOLEAN("boolean"), NUMERICAL("numerical"), TEXTUAL("textual"), GENERIC("generic")
    }

}