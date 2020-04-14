package com.mark.sharee.data

import com.mark.sharee.data.interfaces.ShareeDataSource
import com.mark.sharee.data.mock.ShareeMockDataSource
import com.mark.sharee.data.remote.ShareeRemoteDataSource
import com.mark.sharee.model.poll.AnsweredQuestion
import com.mark.sharee.network.model.responses.*


class ShareeRepository constructor(remoteDataSource: ShareeRemoteDataSource) : ShareeDataSource {
    private val mockDataSource = ShareeMockDataSource()

    private var activeDataSource: ShareeDataSource = remoteDataSource
//    private var activeDataSource: ShareeDataSource = mockDataSource

    override suspend fun login(phoneNumber: String, uuid: String): LoginResponse {
        return activeDataSource.login(phoneNumber, uuid)
    }

    override suspend fun poll(): GeneralPollResponse {
        return activeDataSource.poll()
    }

    override suspend fun submitPoll(verificationToken: String, pollId: String, answeredQuestions: List<AnsweredQuestion>) {
        activeDataSource.submitPoll(verificationToken, pollId, answeredQuestions)
    }

    override suspend fun getGeneralPolls(): MutableList<GeneralPollResponse> {
        return activeDataSource.getGeneralPolls()
    }

    override suspend fun getMedicalPolls(): MutableList<GeneralPollResponse> {
        return activeDataSource.getMedicalPolls()
    }

    override suspend fun updateFcmToken(verificationToken: String, fcmToken: String) {
        return activeDataSource.updateFcmToken(verificationToken, fcmToken)
    }

    override suspend fun dailyRoutine(): DailyRoutineResponse {
        return activeDataSource.dailyRoutine()
    }

    override suspend fun scheduledNotifications(): MutableList<ScheduledNotification> {
        return activeDataSource.scheduledNotifications()
    }

}