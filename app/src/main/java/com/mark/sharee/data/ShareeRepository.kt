package com.mark.sharee.data

import com.mark.sharee.data.interfaces.ShareeDataSource
import com.mark.sharee.data.remote.ShareeRemoteDataSource
import com.mark.sharee.network.model.responses.MovieResponse


class ShareeRepository constructor(private val remoteDataSource: ShareeRemoteDataSource) : ShareeDataSource{

    override suspend fun create(name: String) {
        remoteDataSource.create(name)
    }

    override suspend fun popularMovies(apiKey: String): MovieResponse {
        return remoteDataSource.popularMovies(apiKey)
    }

}