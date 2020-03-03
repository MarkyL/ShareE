package com.mark.sharee.data

import com.mark.sharee.data.interfaces.ShareeDataSource
import com.mark.sharee.data.mock.ShareeMockDataSource
import com.mark.sharee.data.remote.ShareeRemoteDataSource
import com.mark.sharee.model.poll.AnsweredQuestion
import com.mark.sharee.network.model.responses.GeneralPollsResponse
import com.mark.sharee.network.model.responses.GeneralResponse
import com.mark.sharee.network.model.responses.LoginResponse
import com.mark.sharee.network.model.responses.GeneralPollResponse


class ShareeRepository constructor(private val remoteDataSource: ShareeRemoteDataSource) : ShareeDataSource{
    private val mockDataSource = ShareeMockDataSource()

    private var activeDataSource: ShareeDataSource = remoteDataSource

    override suspend fun create(name: String): GeneralResponse {
        return activeDataSource.create(name)
    }

    override suspend fun login(phoneNumber: String, uuid: String): LoginResponse {
        return activeDataSource.login(phoneNumber, uuid)
    }

    override suspend fun poll(): GeneralPollResponse {
        return activeDataSource.poll()
    }

    override suspend fun submitPoll(verificationToken: String,
                                    pollId: String, answeredQuestions: List<AnsweredQuestion>) {
        activeDataSource.submitPoll(verificationToken, pollId, answeredQuestions)
    }

    override suspend fun getGeneralPolls(verificationToken: String): GeneralPollsResponse {
        return activeDataSource.getGeneralPolls(verificationToken)
    }

}