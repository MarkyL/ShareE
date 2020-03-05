package com.mark.sharee.data.mock

import com.mark.sharee.data.interfaces.ShareeDataSource
import com.mark.sharee.model.poll.*
import com.mark.sharee.network.model.responses.GeneralPollsResponse
import com.mark.sharee.network.model.responses.GeneralResponse
import com.mark.sharee.network.model.responses.LoginResponse
import com.mark.sharee.network.model.responses.GeneralPollResponse

class ShareeMockDataSource : ShareeDataSource {

    override suspend fun create(name: String): GeneralResponse {
        return GeneralResponse(name)
    }

    override suspend fun login(phoneNumber: String, uuid: String): LoginResponse {
        return LoginResponse(phoneNumber, uuid)
    }

    override suspend fun poll(): GeneralPollResponse {
        return createGeneralPollResponse1()
    }

    override suspend fun submitPoll(verificationToken: String, pollId: String, answeredQuestions: List<AnsweredQuestion>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getGeneralPolls(verificationToken: String): GeneralPollsResponse {
        val pollsList = mutableListOf(
            createGeneralPollResponse1(), createGeneralPollResponse2(), createGeneralPollResponse3())
        return GeneralPollsResponse(pollsList)
    }

    //region polls creation mock

    private fun createGeneralPollResponse1(): GeneralPollResponse {
        val questionsList = listOf(
            Question(1, Question.QuestionType.BOOLEAN, "האם אתה מרגיש טוב?"),
            Question(2, Question.QuestionType.BOOLEAN, "האם קיבלת טיפול בזמן סביר?"),
            Question(3, Question.QuestionType.NUMERICAL, "מה מידת שביעות הרצון שלך מהטיפול?"),
            Question(
                4,
                Question.QuestionType.NUMERICAL,
                "מה מידת היחס שקיבלת מהצוות הרפואי והאם תמליץ על שרי?"
            ),
            Question(5, Question.QuestionType.TEXTUAL, "כיצד היית משפר את השירות במחלקה?"),
            Question(6, Question.QuestionType.TEXTUAL, "האם תרצה להוסיף עוד משהו?")
        )

        return GeneralPollResponse("12345", "סקר מוק 1", questionsList.toMutableList())
    }

    private fun createGeneralPollResponse2(): GeneralPollResponse {
        val questionsList = listOf(
            Question(1, Question.QuestionType.BOOLEAN, "האם אתה מרגיש טוב?"),
            Question(2, Question.QuestionType.BOOLEAN, "האם קיבלת טיפול בזמן סביר?"),
            Question(3, Question.QuestionType.NUMERICAL, "מה מידת שביעות הרצון שלך מהטיפול?"),
            Question(
                4,
                Question.QuestionType.NUMERICAL,
                "מה מידת היחס שקיבלת מהצוות הרפואי והאם תמליץ על שרי?"
            ),
            Question(5, Question.QuestionType.TEXTUAL, "כיצד היית משפר את השירות במחלקה?"),
            Question(6, Question.QuestionType.TEXTUAL, "האם תרצה להוסיף עוד משהו?")
        )

        return GeneralPollResponse("123456", "סקר מוק 2", questionsList.toMutableList())
    }

    private fun createGeneralPollResponse3(): GeneralPollResponse {
        val questionsList = listOf(
            Question(1, Question.QuestionType.BOOLEAN, "האם אתה מרגיש טוב?"),
            Question(2, Question.QuestionType.BOOLEAN, "האם קיבלת טיפול בזמן סביר?"),
            Question(3, Question.QuestionType.NUMERICAL, "מה מידת שביעות הרצון שלך מהטיפול?"),
            Question(
                4,
                Question.QuestionType.NUMERICAL,
                "מה מידת היחס שקיבלת מהצוות הרפואי והאם תמליץ על שרי?"
            ),
            Question(5, Question.QuestionType.TEXTUAL, "כיצד היית משפר את השירות במחלקה?"),
            Question(6, Question.QuestionType.TEXTUAL, "האם תרצה להוסיף עוד משהו?")
        )

        return GeneralPollResponse("1234567", "סקר מוק 3", questionsList.toMutableList())
    }

    //endregion
}