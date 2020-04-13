package com.mark.sharee.network.endpoint

import com.google.gson.Gson
import com.mark.sharee.model.poll.AnsweredQuestion
import com.mark.sharee.network.model.requests.FcmRequest
import com.mark.sharee.network.model.requests.GeneralRequest
import com.mark.sharee.network.model.requests.LoginRequest
import com.mark.sharee.network.model.requests.SubmitPollRequest
import com.mark.sharee.network.model.responses.DailyRoutineResponse
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

    suspend fun getGeneralPolls(): MutableList<GeneralPollResponse> {
        return shareeService.getGeneralPolls()
    }

    suspend fun getMedicalPolls(): MutableList<GeneralPollResponse> {
        return shareeService.getMedicalPolls()
    }

    suspend fun updateFcmToken(verificationToken: String, fcmToken: String) {
        val fcmRequest = FcmRequest(verificationToken, fcmToken)
        shareeService.updateFcmToken(fcmRequest)
    }

    suspend fun dailyRoutine() : DailyRoutineResponse {
        return shareeService.dailyRoutine()
    }

}