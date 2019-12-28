package com.mark.sharee.data.mock

import androidx.constraintlayout.solver.state.State
import com.example.sharee.BuildConfig
import kotlinx.io.IOException
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull


class FakeInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var response: Response? = null
        if (BuildConfig.DEBUG) {
            val responseString: String
            // Get Request URI.
            val uri = chain.request().url.toUri()
            // Get Query String.
            val query = uri.query
            // Parse the Query String.
            val parsedQuery =
                query.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (parsedQuery[0].equals("id", ignoreCase = true) && parsedQuery[1].equals(
                    "1",
                    ignoreCase = true
                )
            ) {
                responseString = TEACHER_ID_1
            } else if (parsedQuery[0].equals("id", ignoreCase = true) && parsedQuery[1].equals(
                    "2",
                    ignoreCase = true
                )
            ) {
                responseString = TEACHER_ID_2
            } else {
                responseString = ""
            }

            response = Response.Builder()
                .code(200)
                .message(responseString)
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(
                    ResponseBody.create(
                        "application/json".toMediaTypeOrNull(),
                        responseString.toByteArray()
                    )
                )
                .addHeader("content-type", "application/json")
                .build()
        } else {
            response = chain.proceed(chain.request())
        }

        return response
    }

    companion object {
        // FAKE RESPONSES.
        private val TEACHER_ID_1 = "{\"id\":1,\"age\":28,\"name\":\"Victor Apoyan\"}"
        private val TEACHER_ID_2 = "{\"id\":1,\"age\":16,\"name\":\"Tovmas Apoyan\"}"
    }
}