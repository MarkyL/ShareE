package com.mark.sharee.model.poll

import kotlinx.serialization.SerialName
import kotlinx.serialization.Transient

@kotlinx.serialization.Serializable
data class Question(
    @SerialName(value = "id") val id: Long,
    @SerialName(value = "type") val type: QuestionType,
    @SerialName(value = "question") val question: String,
    @Transient var answer: Any? = null) {

    enum class QuestionType {
        BOOLEAN, NUMERICAL, TEXTUAL
    }

}

//data class BooleanQuestion(
//    @SerialName(value = "answer") var answer: Boolean?,
//    override val id: Long,
//    override val type: QuestionType,
//    override val question: String
//) : Question()
//
//data class NumericalQuestion(
//    @SerialName(value = "answer") var answer: Int?,
//    override val id: Long,
//    override val type: QuestionType,
//    override val question: String
//) : Question()
//
//
//data class TextualQuestion(
//    @SerialName(value = "answer") var answer: String?,
//    override val id: Long,
//    override val type: QuestionType,
//    override val question: String
//) : Question()

