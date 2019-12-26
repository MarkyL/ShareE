package com.mark.sharee.network.endpoint

import com.mark.sharee.network.model.responses.MovieResponse

class ShareeEndpoint constructor(private val shareeService: ShareeService) {


    suspend fun create(name: String) {
        shareeService.create(name)
    }

    suspend fun popularMovies(apiKey: String): MovieResponse {
        return shareeService.popularMovies(apiKey)
    }
}