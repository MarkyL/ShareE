package com.mark.sharee.network.endpoint

import com.mark.sharee.network.model.requests.BasicRequest
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

    @POST(value = PATIENT_BASE + "create")
    suspend fun create(@Body generalRequest: GeneralRequest): GeneralResponse

    @POST(value = PATIENT_BASE + "login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @GET(value = PATIENT_BASE + "poll")
    suspend fun poll(): GeneralPollResponse

    @POST(value = PATIENT_BASE + "submitPoll")
    suspend fun submitPoll(@Body submitPollRequest: SubmitPollRequest)

    @POST(value = PATIENT_BASE + "getGeneralPolls")
    suspend fun getGeneralPolls(@Body basicRequest: BasicRequest) : GeneralPollsResponse

    @POST(value = PATIENT_BASE + "getMedicalPolls")
    suspend fun getMedicalPolls(@Body basicRequest: BasicRequest) : MedicalPollsResponse

}
