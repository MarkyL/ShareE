package com.mark.sharee.data.mock

import com.mark.sharee.data.interfaces.ShareeDataSource
import com.mark.sharee.model.poll.*
import com.mark.sharee.network.model.responses.GeneralResponse
import com.mark.sharee.network.model.responses.LoginResponse
import com.mark.sharee.network.model.responses.GeneralPollResponse
import com.mark.sharee.network.model.responses.PollSection

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

    override suspend fun getGeneralPolls(): MutableList<GeneralPollResponse> {
        return mutableListOf(
            createGeneralPollResponse1(), createGeneralPollResponse2(), createGeneralPollResponse3()
        )
    }

    //region polls creation mock

    private fun createGeneralPollResponse1(): GeneralPollResponse {
        val questionsList = mutableListOf(
            Question(1, Question.QuestionType.BOOLEAN, "האם אתה מרגיש טוב?"),
            Question(2, Question.QuestionType.GENERIC, "האם קיבלת טיפול בזמן סביר?",
                mutableListOf("תשובה1", "תשובה2", "תשובה3")),
            Question(3, Question.QuestionType.NUMERICAL, "מה מידת שביעות הרצון שלך מהטיפול?"),
            Question(4, Question.QuestionType.NUMERICAL, "מה מידת היחס שקיבלת מהצוות הרפואי והאם תמליץ על שרי?"),
            Question(5, Question.QuestionType.TEXTUAL, "כיצד היית משפר את השירות במחלקה?"),
            Question(6, Question.QuestionType.TEXTUAL, "האם תרצה להוסיף עוד משהו?")
        )

        val pollSections = mutableListOf(
            PollSection("1", "חלק ראשון", questionsList),
            PollSection("2", "חלק שני", questionsList))

        return GeneralPollResponse("12345", "סקר מוק 1", pollSections, "general")
    }

    private fun createGeneralPollResponse2(): GeneralPollResponse {
        val questionsList = mutableListOf(
            Question(1, Question.QuestionType.BOOLEAN, "האם אתה מרגיש טוב?"),
            Question(2, Question.QuestionType.BOOLEAN, "האם קיבלת טיפול בזמן סביר?"),
            Question(3, Question.QuestionType.NUMERICAL, "מה מידת שביעות הרצון שלך מהטיפול?"),
            Question(4, Question.QuestionType.NUMERICAL, "מה מידת היחס שקיבלת מהצוות הרפואי והאם תמליץ על שרי?"),
            Question(5, Question.QuestionType.TEXTUAL, "כיצד היית משפר את השירות במחלקה?"),
            Question(6, Question.QuestionType.TEXTUAL, "האם תרצה להוסיף עוד משהו?")
        )

        val pollSections = mutableListOf(
            PollSection("1", "סקשן 1", questionsList),
            PollSection("2", "סקשן 2", questionsList))

        return GeneralPollResponse("123456", "סקר מוק 2", pollSections, "general")
    }

    private fun createGeneralPollResponse3(): GeneralPollResponse {
        val questionsList = mutableListOf(
            Question(1, Question.QuestionType.BOOLEAN, "האם אתה מרגיש טוב?"),
            Question(2, Question.QuestionType.BOOLEAN, "האם קיבלת טיפול בזמן סביר?"),
            Question(3, Question.QuestionType.NUMERICAL, "מה מידת שביעות הרצון שלך מהטיפול?"),
            Question(4, Question.QuestionType.NUMERICAL, "מה מידת היחס שקיבלת מהצוות הרפואי והאם תמליץ על שרי?"),
            Question(5, Question.QuestionType.TEXTUAL, "כיצד היית משפר את השירות במחלקה?"),
            Question(6, Question.QuestionType.TEXTUAL, "האם תרצה להוסיף עוד משהו?")
        )

        val pollSections = mutableListOf(
            PollSection("1", "שאלות עישון", questionsList),
            PollSection("2", "שאלון מחלות רקע", questionsList),
            PollSection("3", "שאלות נוספות", questionsList),
            PollSection("4", "חלק אחרון", questionsList))

        return GeneralPollResponse("1234567", "סקר מוק 3", pollSections, "general")
    }

    //endregion
}