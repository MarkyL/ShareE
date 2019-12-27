package com.mark.sharee.data.mock

import com.mark.sharee.data.interfaces.ShareeDataSource
import com.mark.sharee.network.endpoint.ShareeEndpoint
import com.mark.sharee.network.model.responses.GeneralResponse
import com.mark.sharee.network.model.responses.LoginResponse
import com.mark.sharee.network.model.responses.MovieResponse

class ShareeMockDataSource : ShareeDataSource {

    override suspend fun create(name: String): GeneralResponse {
        return GeneralResponse(name)
    }

    override suspend fun login(phoneNumber: String, uuid: String): LoginResponse {
        return LoginResponse(phoneNumber, uuid)
    }

    override suspend fun popularMovies(apiKey: String): MovieResponse {
        return MovieResponse(0, ArrayList())
    }

}