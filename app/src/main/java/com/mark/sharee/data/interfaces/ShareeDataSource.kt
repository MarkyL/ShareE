package com.mark.sharee.data.interfaces

import com.mark.sharee.model.poll.AnsweredQuestion
import com.mark.sharee.network.model.responses.DailyRoutineResponse
import com.mark.sharee.network.model.responses.GeneralResponse
import com.mark.sharee.network.model.responses.LoginResponse
import com.mark.sharee.network.model.responses.GeneralPollResponse
import rx.Observable

// Implement here all methods to be overridden & implemented by the repository
interface ShareeDataSource {
    suspend fun create(name: String): GeneralResponse

    suspend fun login(phoneNumber: String, uuid: String): LoginResponse

    suspend fun poll(): GeneralPollResponse

    suspend fun submitPoll(verificationToken: String, pollId: String, answeredQuestions: List<AnsweredQuestion>)

    suspend fun getGeneralPolls(): MutableList<GeneralPollResponse>

    suspend fun getMedicalPolls(): MutableList<GeneralPollResponse>

    suspend fun updateFcmToken(verificationToken: String, fcmToken: String)

    suspend fun dailyRoutine() : DailyRoutineResponse
}