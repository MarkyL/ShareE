package com.mark.sharee.network.endpoint

import com.mark.sharee.network.model.requests.GeneralRequest
import com.mark.sharee.network.model.requests.LoginRequest
import com.mark.sharee.network.model.requests.SubmitPollRequest
import com.mark.sharee.network.model.responses.GeneralResponse
import com.mark.sharee.network.model.responses.LoginResponse
import com.mark.sharee.network.model.responses.PollResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ShareeService {

    @POST(value = "create")
    suspend fun create(@Body generalRequest: GeneralRequest): GeneralResponse

    @POST(value = "login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @GET(value = "poll")
    suspend fun poll(): PollResponse

    @POST(value = "submitPoll")
    suspend fun submitPoll(@Body submitPollRequest: SubmitPollRequest)
}