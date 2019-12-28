package com.mark.sharee.model.poll

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
abstract class AnsweredQuestion(@SerialName(value = "id") open val id: Long) {

    companion object {

        fun convertQuestionListToAnsweredQuestionList(questionList: List<Question>): List<AnsweredQuestion> {
            val answeredQuestionsList = mutableListOf<AnsweredQuestion>()
            questionList.forEach { answeredQuestionsList.add(convertQuestionToAnsweredQuestion(it)) }
            return answeredQuestionsList
        }

        private fun convertQuestionToAnsweredQuestion(question: Question): AnsweredQuestion {
            return when (question.type) {
                Question.QuestionType.BOOLEAN -> BooleanAnswer(
                    (question as BooleanQuestion).answer,
                    question.id
                )
                Question.QuestionType.NUMERICAL -> NumericalAnswer(
                    (question as NumericalQuestion).answer,
                    question.id
                )
                Question.QuestionType.TEXTUAL -> TextualAnswer(
                    (question as TextualQuestion).answer,
                    question.id
                )
            }
        }
    }
}

data class BooleanAnswer(
    @SerialName(value = "answer") var answer: Boolean?,
    @SerialName(value = "id") override val id: Long
) : AnsweredQuestion(id)

data class NumericalAnswer(
    @SerialName(value = "answer") var answer: Int?,
    @SerialName(value = "id") override val id: Long
) : AnsweredQuestion(id)


data class TextualAnswer(
    @SerialName(value = "answer") var answer: String?,
    @SerialName(value = "id") override val id: Long
) : AnsweredQuestion(id)

