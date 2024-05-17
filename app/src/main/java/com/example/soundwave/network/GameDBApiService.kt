package com.example.soundwave.network

import com.example.soundwave.model.Developer
import com.example.soundwave.model.DeveloperDetails
import com.example.soundwave.model.Game
import com.example.soundwave.model.GameDetails
import com.example.soundwave.model.GameResponse
import com.example.soundwave.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GameDBApiService {

    @GET("games")
    suspend fun getGames(
        @Query("key")
        apiKey: String = Constants.API_KEY
    ): GameResponse<Game>
    @GET("games/{id}")
    suspend fun getGameDetails(
        @Path("id") gameId: String,
        @Query("key")
        apiKey: String = Constants.API_KEY
    ): GameDetails

    @GET("developers")
    suspend fun getDeveloper(
        @Query("key")
        apiKey: String = Constants.API_KEY
    ): GameResponse<Developer>

    @GET("developers/{id}")
    suspend fun getDeveloperDetails(
        @Path("id") developerId: String,
        @Query("key") apiKey: String = Constants.API_KEY
    ): DeveloperDetails
}