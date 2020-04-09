package com.mark.sharee.data.remote

import com.mark.sharee.data.interfaces.ShareeDataSource
import com.mark.sharee.model.poll.AnsweredQuestion
import com.mark.sharee.network.endpoint.ShareeEndpoint
import com.mark.sharee.network.model.responses.GeneralResponse
import com.mark.sharee.network.model.responses.LoginResponse
import com.mark.sharee.network.model.responses.GeneralPollResponse
import rx.Observable

class ShareeRemoteDataSource constructor(private val endpoint: ShareeEndpoint) : ShareeDataSource {
    override suspend fun create(name: String): GeneralResponse {
        return endpoint.create(name)
    }

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

    override fun updateNotificationMethod(verificationToken: String, fcmToken: String): Observable<Void> {
        return endpoint.updateNotificationMethod(verificationToken, fcmToken)
    }
}