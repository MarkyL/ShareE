package com.mark.sharee.di

import android.content.Context
import android.util.Log
import com.example.sharee.BuildConfig
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.mark.sharee.core.Constants
import com.mark.sharee.core.ShareeApplication
import com.mark.sharee.data.ShareeRepository
import com.mark.sharee.data.remote.ShareeRemoteDataSource
import com.mark.sharee.model.User
import com.mark.sharee.model.poll.Question
import com.mark.sharee.network.endpoint.ShareeEndpoint
import com.mark.sharee.network.endpoint.ShareeService
import com.mark.sharee.utils.RuntimeTypeAdapterFactory
import kotlinx.serialization.UnstableDefault
import okhttp3.Cache
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

val retrofitModule = module {

    single<Call.Factory> {
        val cacheFile = cacheFile(androidContext())
        val cache = cache(cacheFile)
        okhttp(cache)
    }

    single { retrofit(get(), Constants.BASE_URL) }

    single { get<Retrofit>().create(ShareeService::class.java) }

    single { ShareeEndpoint(get()) }

    single { ShareeRemoteDataSource(get()) }

    single { ShareeRepository(get()) }
}

private val logging: Interceptor
    get() = HttpLoggingInterceptor().apply {


        level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
    }

private fun cacheFile(context: Context) = File(context.filesDir, "my_own_created_cache").apply {
    if (!this.exists())
        mkdirs()
}

private fun cache(cacheFile: File) = Cache(cacheFile, Constants.CACHE_FILE_SIZE)

@UseExperimental(UnstableDefault::class)
private fun retrofit(callFactory: Call.Factory, baseUrl: String) = Retrofit.Builder()
    .callFactory(callFactory)
    .baseUrl(baseUrl)
    .addConverterFactory(
        GsonConverterFactory.create(
            GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setPrettyPrinting().create()
        )
    )
    .build()

private fun okhttp(cache: Cache) = OkHttpClient.Builder()
    .addInterceptor { chain ->
        val request = chain.request()
        User.me()?.let {
            request.newBuilder().addHeader("verificationToken", it.getToken()).build()
        }
        chain.proceed(request)
    }
    .addNetworkInterceptor(logging)
    .addInterceptor(
        LoggingInterceptor.Builder()
            .setLevel(Level.BASIC)
            .log(Log.INFO)
            .request("Request")
            .response("Response")
            .build()
    )
    .cache(cache)
    .build()

class ApiLogger : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        val logName = "ApiLogger"
        if (message.startsWith("{") || message.startsWith("[")) {
            try {
                val prettyPrintJson = GsonBuilder().setPrettyPrinting()
                    .create().toJson(JsonParser().parse(message))
                Log.d(logName, prettyPrintJson)
            } catch (m: JsonSyntaxException) {
                Log.d(logName, message)
            }
        } else {
            Log.d(logName, message)
            return
        }
    }
}