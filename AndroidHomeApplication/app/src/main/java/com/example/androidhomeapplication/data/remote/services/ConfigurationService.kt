package com.example.androidhomeapplication.data.remote.services

import com.example.androidhomeapplication.data.remote.response.ConfigurationResponse
import retrofit2.http.GET

interface ConfigurationService {
    @GET("configuration")
    suspend fun loadConfiguration(): ConfigurationResponse
}