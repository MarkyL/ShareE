package com.mark.sharee.data.interfaces

import com.mark.sharee.model.poll.AnsweredQuestion
import com.mark.sharee.network.model.responses.GeneralPollsResponse
import com.mark.sharee.network.model.responses.GeneralResponse
import com.mark.sharee.network.model.responses.LoginResponse
import com.mark.sharee.network.model.responses.GeneralPollResponse

// Implement here all methods to be overridden & implemented by the repository
interface ShareeDataSource {
    suspend fun create(name: String): GeneralResponse

    suspend fun login(phoneNumber: String, uuid: String): LoginResponse

    suspend fun poll(): GeneralPollResponse

    suspend fun submitPoll(verificationToken: String, pollId: String, answeredQuestions: List<AnsweredQuestion>)

    suspend fun getGeneralPolls(verificationToken: String): GeneralPollsResponse
}