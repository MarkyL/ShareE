package com.mark.sharee.data.remote

import com.mark.sharee.data.interfaces.ShareeDataSource
import com.mark.sharee.network.endpoint.ShareeEndpoint
import com.mark.sharee.network.model.responses.GeneralResponse
import com.mark.sharee.network.model.responses.MovieResponse

class ShareeRemoteDataSource constructor(private val endpoint: ShareeEndpoint) : ShareeDataSource {

    override suspend fun create(name: String): GeneralResponse {
        return endpoint.create(name)
    }

    override suspend fun popularMovies(apiKey: String): MovieResponse {
        return endpoint.popularMovies(apiKey)
    }

}