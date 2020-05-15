package com.mark.sharee.data.remote

import com.mark.sharee.data.interfaces.ShareeDataSource
import com.mark.sharee.model.poll.AnsweredQuestion
import com.mark.sharee.network.endpoint.ShareeEndpoint
import com.mark.sharee.network.model.responses.*

class ShareeRemoteDataSource constructor(private val endpoint: ShareeEndpoint) : ShareeDataSource {
    override suspend fun login(phoneNumber: String, uuid: String): LoginResponse {
        return endpoint.login(phoneNumber, uuid)
    }

    override suspend fun poll(): GeneralPollResponse {
        return endpoint.poll()
    }

    override suspend fun submitPoll(verificationToken: String, pollId: String, answeredQuestions: List<AnsweredQuestion>) {
        return endpoint.submitPoll(verificationToken, pollId, answeredQuestions)
    }

    override suspend fun getGeneralPolls(): MutableList<GeneralPollResponse> {
        return endpoint.getGeneralPolls()
    }

    override suspend fun getMedicalPolls(): MutableList<GeneralPollResponse> {
        return endpoint.getMedicalPolls()
    }

    override suspend fun updateFcmToken(verificationToken: String, fcmToken: String) {
        return endpoint.updateFcmToken(verificationToken, fcmToken)
    }

    override suspend fun dailyRoutine(): DailyRoutineResponse {
        return endpoint.dailyRoutine()
    }

    override suspend fun scheduledNotifications(): MutableList<ScheduledNotification> {
        return endpoint.scheduledNotifications()
    }

    override suspend fun getMessages(verificationToken: String): MutableList<Message> {
        return endpoint.getMessages(verificationToken)
    }

    override suspend fun getExercises(verificationToken: String): MutableList<ExerciseCategory> {
        return endpoint.getExercises(verificationToken)
    }
}
