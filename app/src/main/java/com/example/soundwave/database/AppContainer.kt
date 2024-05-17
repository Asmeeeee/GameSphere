package com.example.soundwave.database

import android.content.Context
import com.example.soundwave.network.GameDBApiService
import com.example.soundwave.utils.Constants
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

interface AppContainer {
    val gameRepository: GameRepository
    val savedGameRepository: SavedGameRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer{

    fun getLoggerInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    val gameDBJson = Json {
        ignoreUnknownKeys = true
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .client(
            okhttp3.OkHttpClient.Builder()
                .addInterceptor(getLoggerInterceptor())
                .connectTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                .build()
        )
        .addConverterFactory(gameDBJson.asConverterFactory("application/json".toMediaType()))
        .baseUrl(Constants.GAME_LIST_BASE_URL)
        .build()

    private val retrofitService: GameDBApiService by lazy {
        retrofit.create(GameDBApiService::class.java)
    }
    override val gameRepository: GameRepository by lazy {
        GamesRepository(retrofitService)
    }

    override val savedGameRepository: SavedGameRepository by lazy{
        FavoriteGameRepository(GameDatabase.getDatabase(context).gameDao())
    }
}