package com.mark.sharee.model.poll

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Question(
    @SerialName(value = "id") val id: String,
    @SerialName(value = "type") val type: QuestionType,
    @SerialName(value = "question") val question: String) {

    enum class QuestionType(val type: String) {
        BOOLEAN("B") , NUMERICAL("N") , TEXTUAL("T")
    }
}