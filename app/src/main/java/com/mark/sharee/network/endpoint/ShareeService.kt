package com.mark.sharee.network.endpoint

import com.mark.sharee.network.model.requests.*
import com.mark.sharee.network.model.responses.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

    @GET(value = PATIENT_BASE + "getNotifications/verificationToken/{verificationToken}")
    suspend fun getMessages(@Path("verificationToken") verificationToken: String) : MutableList<Message>

    @GET(value = PATIENT_BASE + "exercises/verificationToken/{verificationToken}")
    suspend fun getExercises(@Path("verificationToken") verificationToken: String) : MutableList<ExerciseCategory>
}