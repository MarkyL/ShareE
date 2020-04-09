package com.mark.sharee.network.endpoint

import com.mark.sharee.network.model.requests.*
import com.mark.sharee.network.model.responses.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import rx.Observable

interface ShareeService {

    companion object {
        const val PATIENT_BASE = "patient/"
    }

    @POST(value = PATIENT_BASE + "create")
    suspend fun create(@Body generalRequest: GeneralRequest): GeneralResponse

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

    @POST(value = PATIENT_BASE + "updateNotificationMethod")
    fun updateNotificationMethod(@Body fcmRequest: FcmRequest): Observable<Void>
}