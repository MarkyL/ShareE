package com.mark.sharee.model.poll

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
abstract class Question(
    @SerialName(value = "id") open val id: Long,
    @SerialName(value = "type") open val type: QuestionType,
    @SerialName(value = "question") open val question: String
) {

    enum class QuestionType(val type: String) {
        BOOLEAN("B"), NUMERICAL("N"), TEXTUAL("T")
    }
}

data class BooleanQuestion(
    @SerialName(value = "answer") val answer: Boolean?,
    @SerialName(value = "id") override val id: Long,
    @SerialName(value = "type") override val type: QuestionType,
    @SerialName(value = "question") override val question: String
) : Question(id, type, question)

data class NumericalQuestion(
    @SerialName(value = "answer") val answer: Int?,
    @SerialName(value = "id") override val id: Long,
    @SerialName(value = "type") override val type: QuestionType,
    @SerialName(value = "question") override val question: String
) : Question(id, type, question)


data class TextualQuestion(
    @SerialName(value = "answer") val answer: String?,
    @SerialName(value = "id") override val id: Long,
    @SerialName(value = "type") override val type: QuestionType,
    @SerialName(value = "question") override val question: String
) : Question(id, type, question)

