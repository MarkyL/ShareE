package com.mark.sharee.data.interfaces

import com.mark.sharee.network.model.responses.GeneralResponse
import com.mark.sharee.network.model.responses.LoginResponse
import com.mark.sharee.network.model.responses.MovieResponse

// Implement here all methods to be overridden & implemented by the repository
interface ShareeDataSource {
    suspend fun create(name: String): GeneralResponse

    suspend fun login(phoneNumber: String, uuid: String): LoginResponse

    suspend fun popularMovies(apiKey: String): MovieResponse
}