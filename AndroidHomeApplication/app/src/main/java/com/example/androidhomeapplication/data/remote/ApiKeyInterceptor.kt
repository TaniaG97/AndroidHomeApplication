package com.example.androidhomeapplication.data.remote

import okhttp3.Interceptor
import okhttp3.Response

private const val API_KEY = "89f113560486f0b91694ad4221fe6dca"

class ApiKeyInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val origin = chain.request()
        val urlBuilder = origin.url.newBuilder()
        val url = urlBuilder
            .addQueryParameter("api_key", API_KEY)
            .build()

        val requestBuilder = origin.newBuilder()
            .url(url)

        val request = requestBuilder.build()

        return chain.proceed(request)
    }
}