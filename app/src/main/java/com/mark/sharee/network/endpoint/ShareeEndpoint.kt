package com.mark.sharee.network.endpoint

import com.google.gson.Gson
import com.mark.sharee.model.poll.AnsweredQuestion
import com.mark.sharee.network.model.requests.*
import com.mark.sharee.network.model.responses.*
import timber.log.Timber

class ShareeEndpoint constructor(private val shareeService: ShareeService) {

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

    suspend fun scheduledNotifications() : MutableList<ScheduledNotification> {
        return shareeService.scheduledNotifications()
    }

    suspend fun getMessages(verificationToken: String): MutableList<Message> {
        return shareeService.getMessages(verificationToken)
    }

    suspend fun getExercises(verificationToken: String): MutableList<ExerciseCategory> {
        return shareeService.getExercises(verificationToken)
    }

}