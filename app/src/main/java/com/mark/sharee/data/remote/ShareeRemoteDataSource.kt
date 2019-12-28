package com.mark.sharee.data.remote

import com.mark.sharee.data.interfaces.ShareeDataSource
import com.mark.sharee.model.poll.AnsweredQuestion
import com.mark.sharee.network.endpoint.ShareeEndpoint
import com.mark.sharee.network.model.requests.BasicRequest
import com.mark.sharee.network.model.responses.GeneralResponse
import com.mark.sharee.network.model.responses.LoginResponse
import com.mark.sharee.network.model.responses.MovieResponse
import com.mark.sharee.network.model.responses.PollResponse

class ShareeRemoteDataSource constructor(private val endpoint: ShareeEndpoint) : ShareeDataSource {
    override suspend fun create(name: String): GeneralResponse {
        return endpoint.create(name)
    }

    override suspend fun login(phoneNumber: String, uuid: String): LoginResponse {
        return endpoint.login(phoneNumber, uuid)
    }

    override suspend fun poll(verificationToken: String): PollResponse {
        return endpoint.poll(BasicRequest(verificationToken))
    }

    override suspend fun submitPoll(verificationToken: String, answeredQuestions: List<AnsweredQuestion>) {
        return endpoint.submitPoll(verificationToken, answeredQuestions)
    }

    override suspend fun popularMovies(apiKey: String): MovieResponse {
        return endpoint.popularMovies(apiKey)
    }

}