package com.mark.sharee.data.interfaces

import com.mark.sharee.model.poll.AnsweredQuestion
import com.mark.sharee.network.model.responses.*
import rx.Observable

// Implement here all methods to be overridden & implemented by the repository
interface ShareeDataSource {
    suspend fun login(phoneNumber: String, uuid: String): LoginResponse

    suspend fun poll(): GeneralPollResponse

    suspend fun submitPoll(verificationToken: String, pollId: String, answeredQuestions: List<AnsweredQuestion>)

    suspend fun getGeneralPolls(): MutableList<GeneralPollResponse>

    suspend fun getMedicalPolls(): MutableList<GeneralPollResponse>

    suspend fun updateFcmToken(verificationToken: String, fcmToken: String)

    suspend fun dailyRoutine() : DailyRoutineResponse

    suspend fun scheduledNotifications() : MutableList<ScheduledNotification>

    suspend fun getMessages(verificationToken: String): MutableList<Message>

    suspend fun getExercises(verificationToken: String): MutableList<ExerciseCategory>

}