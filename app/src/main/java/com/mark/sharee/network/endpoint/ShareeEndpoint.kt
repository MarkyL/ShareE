package com.mark.sharee.network.endpoint

import com.google.gson.Gson
import com.mark.sharee.model.poll.AnsweredQuestion
import com.mark.sharee.network.model.requests.BasicRequest
import com.mark.sharee.network.model.requests.GeneralRequest
import com.mark.sharee.network.model.requests.LoginRequest
import com.mark.sharee.network.model.requests.SubmitPollRequest
import com.mark.sharee.network.model.responses.GeneralPollsResponse
import com.mark.sharee.network.model.responses.GeneralResponse
import com.mark.sharee.network.model.responses.LoginResponse
import com.mark.sharee.network.model.responses.GeneralPollResponse
import timber.log.Timber

class ShareeEndpoint constructor(private val shareeService: ShareeService) {


    suspend fun create(name: String): GeneralResponse {
        return shareeService.create(GeneralRequest(name))
    }

    suspend fun login(phoneNumber: String, uuid: String): LoginResponse {
        return shareeService.login(LoginRequest(phoneNumber = phoneNumber, uuid = uuid))
    }

    suspend fun poll(): GeneralPollResponse {
        return shareeService.poll()
    }

    suspend fun submitPoll(verificationToken: String, pollId: String, answeredQuestions: List<AnsweredQuestion>) {
        val submitPollRequest = SubmitPollRequest(verificationToken, pollId, answeredQuestions)
        var requestJson = Gson().toJson(submitPollRequest)
        Timber.i("submitPoll: request: $requestJson")
        shareeService.submitPoll(submitPollRequest)
    }

    suspend fun getGeneralPolls(verificationToken: String): GeneralPollsResponse {
        val basicRequest = BasicRequest(verificationToken)
        var requestJson = Gson().toJson(basicRequest)
        Timber.i("getGeneralPolls: request: $requestJson")
        return shareeService.getGeneralPolls(basicRequest)
    }


}