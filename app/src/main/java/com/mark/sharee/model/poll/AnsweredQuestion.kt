package com.mark.sharee.model.poll

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
abstract class AnsweredQuestion() {

    @SerialName(value = "id") abstract val id: Long

    companion object {

        fun convertQuestionListToAnsweredQuestionList(questionList: List<Question>): List<AnsweredQuestion> {
            val answeredQuestionsList = mutableListOf<AnsweredQuestion>()
            questionList.forEach { answeredQuestionsList.add(convertQuestionToAnsweredQuestion(it)) }
            return answeredQuestionsList
        }

        private fun convertQuestionToAnsweredQuestion(question: Question): AnsweredQuestion {
            return when (question.type) {
                Question.QuestionType.BOOLEAN -> BooleanAnswer(
                    question.answer as Boolean, question.id
                )
                Question.QuestionType.NUMERICAL -> NumericalAnswer(
                    question.answer as Int, question.id
                )
                Question.QuestionType.TEXTUAL -> TextualAnswer(
                    question.answer as String, question.id
                )
            }
        }
    }
}

data class BooleanAnswer(
    @SerialName(value = "answer") var answer: Boolean?,
    override val id: Long
) : AnsweredQuestion()

data class NumericalAnswer(
    @SerialName(value = "answer") var answer: Int?,
    override val id: Long
) : AnsweredQuestion()


data class TextualAnswer(
    @SerialName(value = "answer") var answer: String?,
    override val id: Long
) : AnsweredQuestion()

