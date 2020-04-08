package com.mark.sharee.utils

import com.google.gson.Gson
import com.mark.sharee.navigation.ActivityNavigator
import com.mark.sharee.network.adapter.ServerException
import org.koin.core.KoinComponent
import retrofit2.HttpException
import timber.log.Timber

class ErrorHandler(val navigator: ActivityNavigator): KoinComponent {

    fun handleError(throwable: Throwable) {
        val gson = Gson()
        Timber.i("throwable ")
        when (throwable) {
            is HttpException -> {
                throwable.response()?.errorBody()?.let {
                    val serverException = gson.fromJson(it.string(), ServerException::class.java)
                    Timber.e("ErrorHandler - $serverException")

                }
            }
            else -> {
                Timber.e("ErrorHandler - Unknown error")
            }
        }
    }

}