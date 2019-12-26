package com.mark.sharee.data.interfaces

import com.mark.sharee.network.model.responses.MovieResponse

// Implement here all methods to be overridden & implemented by the repository
interface ShareeDataSource {
    suspend fun create(name: String)

    suspend fun popularMovies(apiKey: String): MovieResponse
}