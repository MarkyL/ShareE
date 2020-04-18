package com.mark.sharee.network.endpoint

import com.mark.sharee.network.model.requests.FcmRequest
import com.mark.sharee.network.model.requests.GeneralRequest
import com.mark.sharee.network.model.requests.LoginRequest
import com.mark.sharee.network.model.requests.SubmitPollRequest
import com.mark.sharee.network.model.responses.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ShareeService {

    companion object {
        const val PATIENT_BASE = "patient/"
    }

    @POST(value = PATIENT_BASE + "login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @GET(value = PATIENT_BASE + "poll")
    suspend fun poll(): GeneralPollResponse

    @POST(value = PATIENT_BASE + "submitPoll")
    suspend fun submitPoll(@Body submitPollRequest: SubmitPollRequest)

    @GET(value = PATIENT_BASE + "generalPolls")
    suspend fun getGeneralPolls() : MutableList<GeneralPollResponse>

    @GET(value = PATIENT_BASE + "medicalPolls")
    suspend fun getMedicalPolls() : MutableList<GeneralPollResponse>

    @POST(value = PATIENT_BASE + "updateFcmToken")
    suspend fun updateFcmToken(@Body fcmRequest: FcmRequest)

    @GET(value = PATIENT_BASE + "getActiveDailyRoutine")
    suspend fun dailyRoutine() : DailyRoutineResponse

    @GET(value = PATIENT_BASE + "scheduledNotifications")
    suspend fun scheduledNotifications() : MutableList<ScheduledNotification>
}