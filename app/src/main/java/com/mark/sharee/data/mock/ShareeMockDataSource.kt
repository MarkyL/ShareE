package com.mark.sharee.data.mock

import com.mark.sharee.data.interfaces.ShareeDataSource
import com.mark.sharee.model.poll.*
import com.mark.sharee.network.model.responses.*
import rx.Observable

class ShareeMockDataSource : ShareeDataSource {

    override suspend fun login(phoneNumber: String, uuid: String): LoginResponse {
        return LoginResponse(phoneNumber, uuid)
    }

    override suspend fun poll(): GeneralPollResponse {
        return createGeneralPollResponse1()
    }

    override suspend fun submitPoll(verificationToken: String, pollId: String, answeredQuestions: List<AnsweredQuestion>) {

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
            PollSection("1", "", questionsList))

        return GeneralPollResponse("12345", "סקר מוק 1", true, pollSections, "general")
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
            PollSection("1", "", questionsList))

        return GeneralPollResponse("123456", "סקר מוק 2", true, pollSections, "general")
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

        return GeneralPollResponse("1234567", "סקר מוק 3", true, pollSections, "general")
    }

    //endregion

    override suspend fun getMedicalPolls(): MutableList<GeneralPollResponse> {
        return mutableListOf(
            createGeneralPollResponse1(), createGeneralPollResponse2(), createGeneralPollResponse3()
        )
    }

    override suspend fun updateFcmToken(verificationToken: String, fcmToken: String) {
        // STUB!
    }

    override suspend fun dailyRoutine(): DailyRoutineResponse {
        val weekdayActivities = mutableListOf(
            DailyActivity(1,"08:00", "09:00", "ארוחת בוקר"),
            DailyActivity(2,"09:30", "11:00", "ביקור לקראת צהריים"),
            DailyActivity(3,"12:00", "14:00", "ארוחת צהריים"),
            DailyActivity(4,"14:00", "16:00", "זמן מנוחה"),
            DailyActivity(5,"18:00", "20:00", "ארוחת ערב וסוף יום"))

        val weekendActivities = mutableListOf(
            DailyActivity(6,"08:00", "09:00", "ארוחת בוקר"),
            DailyActivity(7,"09:30", "11:00", "ביקור לקראת צהריים"),
            DailyActivity(8,"12:00", "14:00", "ביקורי משפחות"),
            DailyActivity(9,"14:00", "16:00", "ביקורת דגימות"),
            DailyActivity(10,"18:00", "20:00", "פעילות העשרה בחדר אוכל"))

        return DailyRoutineResponse(weekdayActivities, weekendActivities)
    }

    override suspend fun scheduledNotifications(): MutableList<ScheduledNotification> {
        return mutableListOf()
    }

    override suspend fun getMessages(verificationToken: String): MutableList<Message> {
        return mutableListOf(
            Message(body = "הודעה אחת", timeStamp = 1587124800),
            Message(body = "הודעה שתיים", timeStamp = 1589816800),
            Message(body = "מסר שלישי", timeStamp = 1592395200),
            Message(body = "מסר רביעי", timeStamp = 1594987200),
            Message(body =  "מסר חמישי - ארוך מאוד בודק מה קורה כשיש פה טקסט ארוך מאוד בוא נראה אותו", timeStamp = 1596124800),
            Message(body =  "מסר חמישי - ארוך מאוד בודק מה קורה כשיש" +
                    " פה טקסט ארוך מאוד בוא נראה אותו כשיש פה טקסט ארוך מאוד בוא נראה אותו כשיש פה" +
                    " טקסט ארוך מאוד בוא נראה אותו כשיש פה טקסט ארוך מאוד בוא נראה אותו כשיש פה" +
                    " טקסט ארוך מאוד בוא נראה אותו כשיש פה טקסט ארוך מאוד בוא נראה אותו", timeStamp = 1588124800))
    }

    override suspend fun getExercises(verificationToken: String): MutableList<ExerciseCategory> {
        val exercisesSetOne = mutableListOf(
            Exercise("1", "תעשה שכיבות שמיכה", "https://www.youtube.com/watch?v=oHg5SJYRHA0"),
            Exercise("2", "תעשה כפיפות בטן", "https://i.kym-cdn.com/photos/images/original/000/494/606/e6f.jpg"),
            Exercise("3", "תעשה קקי"),
            Exercise("4", "תעשה פיפי"))
        val exercisesSetTwo = mutableListOf(
            Exercise("1", "תחשוב על המחר", "https://en.wikipedia.org/wiki/Owned"),
            Exercise("2", "תדמיין את עצמך יוצא מכאן"),
            Exercise("3", "תדמיין משהו טוב", "https://hospitals.clalit.co.il/meir/he/Pages/default.aspx"),
            Exercise("4", "תחשוב על משהו נחמד"))
        val exercisesSetThree = mutableListOf(
            Exercise("1", "אל תשתולל"),
            Exercise("2", "תיקח את הכדורים"),
            Exercise("3", "תסתכל במראה"),
            Exercise("4", "תתלבש יפה"))
        return mutableListOf(
            ExerciseCategory("1","פיזיוטרפיסט", exercisesSetOne),
            ExerciseCategory("2","פסיכולוג", exercisesSetTwo),
            ExerciseCategory("3","פסיכיאטר", exercisesSetThree))
    }
}
