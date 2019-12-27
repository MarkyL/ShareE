package com.mark.sharee.data

import com.mark.sharee.data.interfaces.ShareeDataSource
import com.mark.sharee.data.remote.ShareeRemoteDataSource
import com.mark.sharee.network.model.responses.GeneralResponse
import com.mark.sharee.network.model.responses.LoginResponse
import com.mark.sharee.network.model.responses.MovieResponse


class ShareeRepository constructor(private val remoteDataSource: ShareeRemoteDataSource) : ShareeDataSource{

    override suspend fun create(name: String): GeneralResponse {
        return remoteDataSource.create(name)
    }

    override suspend fun login(phoneNumber: String, uuid: String): LoginResponse {
        return remoteDataSource.login(phoneNumber, uuid)
    }
    override suspend fun popularMovies(apiKey: String): MovieResponse {
        return remoteDataSource.popularMovies(apiKey)
    }

}