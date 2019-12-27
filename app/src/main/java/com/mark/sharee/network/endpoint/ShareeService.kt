package com.mark.sharee.network.endpoint

import com.mark.sharee.network.model.requests.GeneralRequest
import com.mark.sharee.network.model.responses.GeneralResponse
import com.mark.sharee.network.model.responses.MovieResponse
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
}