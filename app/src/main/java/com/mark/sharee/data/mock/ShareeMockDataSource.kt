package com.mark.sharee.data.mock

import com.mark.sharee.data.interfaces.ShareeDataSource
import com.mark.sharee.model.poll.*
import com.mark.sharee.network.model.responses.GeneralResponse
import com.mark.sharee.network.model.responses.LoginResponse
import com.mark.sharee.network.model.responses.MovieResponse
import com.mark.sharee.network.model.responses.PollResponse

class ShareeMockDataSource : ShareeDataSource {

    override suspend fun create(name: String): GeneralResponse {
        return GeneralResponse(name)
    }

    override suspend fun login(phoneNumber: String, uuid: String): LoginResponse {
        return LoginResponse(phoneNumber, uuid)
    }

    override suspend fun poll(): PollResponse {
        val questionsList = listOf(
            Question( 1, Question.QuestionType.BOOLEAN, "האם אתה מרגיש טוב?"),
            Question(2, Question.QuestionType.BOOLEAN, "האם קיבלת טיפול בזמן סביר?"),
            Question(3, Question.QuestionType.NUMERICAL, "מה מידת שביעות הרצון שלך מהטיפול?"),
            Question(4, Question.QuestionType.NUMERICAL, "מה מידת היחס שקיבלת מהצוות הרפואי והאם תמליץ על שרי?"),
            Question(5, Question.QuestionType.TEXTUAL, "כיצד היית משפר את השירות במחלקה?"),
            Question(6, Question.QuestionType.TEXTUAL, "האם תרצה להוסיף עוד משהו?")
        )

        return PollResponse("12345", "Mark's mock poll", questionsList.toMutableList())
    }

    override suspend fun submitPoll(verificationToken: String, pollId: String, answeredQuestions: List<AnsweredQuestion>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun popularMovies(apiKey: String): MovieResponse {
        return MovieResponse(0, ArrayList())
    }

}