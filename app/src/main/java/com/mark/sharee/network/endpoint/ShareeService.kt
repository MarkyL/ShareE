package com.mark.sharee.network.endpoint

import com.mark.sharee.model.poll.AnsweredQuestion
import com.mark.sharee.network.model.requests.BasicRequest
import com.mark.sharee.network.model.requests.GeneralRequest
import com.mark.sharee.network.model.requests.LoginRequest
import com.mark.sharee.network.model.requests.SubmitPollRequest
import com.mark.sharee.network.model.responses.GeneralResponse
import com.mark.sharee.network.model.responses.LoginResponse
import com.mark.sharee.network.model.responses.MovieResponse
import com.mark.sharee.network.model.responses.PollResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ShareeService {

    @GET(value = "popular")
    suspend fun popularMovies(
        @Query(value = "api_key", encoded = false) apiKey: String,
        @Query(value = "page") pageNumber: Int = 1): MovieResponse

    @POST(value = "create")
    suspend fun create(@Body generalRequest: GeneralRequest): GeneralResponse

    @POST(value = "login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @GET(value = "poll")
    suspend fun poll(@Body basicRequest: BasicRequest): PollResponse

    @POST(value = "submitPoll")
    suspend fun submitPoll(@Body submitPollRequest: SubmitPollRequest)
}