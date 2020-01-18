package com.mark.sharee.model.poll

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
abstract class AnsweredQuestion(
    @SerialName(value = "questionId") val questionId: Long,
    @SerialName(value = "type") val type: String) {




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
    var id: Long
) : AnsweredQuestion(questionId = id, type = Question.QuestionType.BOOLEAN.type)

data class NumericalAnswer(
    @SerialName(value = "answer") var answer: Int?,
    var id: Long
) : AnsweredQuestion(questionId = id, type = Question.QuestionType.NUMERICAL.type)


data class TextualAnswer(
    @SerialName(value = "answer") var answer: String?,
    var id: Long
) : AnsweredQuestion(questionId = id, type = Question.QuestionType.TEXTUAL.type)

