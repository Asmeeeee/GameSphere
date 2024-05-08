package com.example.soundwave.spotify

import android.util.Log
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object SpotifyAuthServiceProvider {
    val retrofit: Retrofit
    val service: SpotifyService
    val accessToken: String

    init {
        retrofit = Retrofit.Builder()
            .baseUrl("https://accounts.spotify.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(SpotifyService::class.java)
        Log.d("SpotifyAuthServiceProvider", { service.toString() }.toString())

        // Fetch the access token synchronously, which is not recommended on the main thread in Android
        accessToken = runBlocking {
            val response = service.getToken(
                grantType = "client_credentials",
                clientId = "67cce911671f41adaa404b600f14aa90",
                clientSecret = "1cbc10e1bed94c1192d7b9c94fb376a5"
            )
            response.accessToken
        }

        println("Access Token: $accessToken")
    }
}
