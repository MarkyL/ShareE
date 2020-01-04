package com.mark.sharee.network.endpoint

import com.mark.sharee.model.poll.AnsweredQuestion
import com.mark.sharee.network.model.requests.GeneralRequest
import com.mark.sharee.network.model.requests.LoginRequest
import com.mark.sharee.network.model.requests.SubmitPollRequest
import com.mark.sharee.network.model.responses.GeneralResponse
import com.mark.sharee.network.model.responses.LoginResponse
import com.mark.sharee.network.model.responses.PollResponse

class ShareeEndpoint constructor(private val shareeService: ShareeService) {


    suspend fun create(name: String): GeneralResponse {
        return shareeService.create(GeneralRequest(name))
    }

    suspend fun login(phoneNumber: String, uuid: String): LoginResponse {
        return shareeService.login(LoginRequest(phoneNumber = phoneNumber, uuid = uuid))
    }

    suspend fun poll(): PollResponse {
        return shareeService.poll()
    }

    suspend fun submitPoll(verificationToken: String, pollId: String, answeredQuestions: List<AnsweredQuestion>) {
        shareeService.submitPoll(SubmitPollRequest(verificationToken, pollId, answeredQuestions))
    }


}