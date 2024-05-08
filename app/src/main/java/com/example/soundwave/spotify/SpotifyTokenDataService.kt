package com.example.soundwave.spotify

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface SpotifyTokenDataService {
    @POST("/api/token")
    suspend fun getTokenData(
        @Body request: JsonObject
    ): Response<JsonObject> // Adjust based on your response model
}

object SpotifyTokenDataServiceProvider {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://accounts.spotify.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val instance: SpotifyTokenDataService by lazy {
        retrofit.create(SpotifyTokenDataService::class.java)
    }
}