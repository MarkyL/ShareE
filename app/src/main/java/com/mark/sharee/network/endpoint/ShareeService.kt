package com.mark.sharee.network.endpoint

import com.mark.sharee.network.model.responses.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ShareeService {

    @GET(value = "popular")
    suspend fun popularMovies(
        @Query(value = "api_key", encoded = false)
        apiKey: String, @Query(value = "page") pageNumber: Int = 1): MovieResponse

    @GET(value = "create")
    suspend fun create(name: String)
}