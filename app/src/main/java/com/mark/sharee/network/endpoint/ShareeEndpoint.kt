package com.mark.sharee.network.endpoint

import com.mark.sharee.model.User
import com.mark.sharee.network.model.requests.GeneralRequest
import com.mark.sharee.network.model.requests.LoginRequest
import com.mark.sharee.network.model.responses.GeneralResponse
import com.mark.sharee.network.model.responses.LoginResponse
import com.mark.sharee.network.model.responses.MovieResponse
import com.mark.sharee.network.model.responses.PollResponse

class ShareeEndpoint constructor(private val shareeService: ShareeService) {


    suspend fun create(name: String): GeneralResponse {
        return shareeService.create(GeneralRequest(name))
    }

    suspend fun login(phoneNumber: String, uuid: String): LoginResponse {
        return shareeService.login(LoginRequest(phoneNumber = phoneNumber, uuid = uuid))
    }

    suspend fun popularMovies(apiKey: String): MovieResponse {
        return shareeService.popularMovies(apiKey)
    }

    suspend fun poll(verificationToken: String): PollResponse {
        return shareeService.poll(verificationToken)
    }


}