package com.example.soundwave.database

import android.content.Context
import android.util.Log
import com.example.soundwave.network.GameDBApiService
import com.example.soundwave.utils.Constants
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory;

interface AppContainer {
    val gameRepository: GameRepository
    val savedGameRepository: SavedGameRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {

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
                .build().also {
                    Log.d(
                        "RetrofitSetup",
                        "Retrofit instance created with base URL: "
                    )
                }
        )
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Constants.GAME_LIST_BASE_URL)
        .build().also {
            Log.d(
                "RetrofitSetup",
                "Retrofit instance created with base URL: ${Constants.GAME_LIST_BASE_URL}"
            )
        }


    private val retrofitService: GameDBApiService by lazy {
        Log.d("DefaultAppContainer", "$retrofit")
        retrofit.create(GameDBApiService::class.java)
    }

    override val gameRepository: GameRepository by lazy {
        Log.d("DefaultAppContainer", "Creating gameRepository")
        GamesRepository(retrofitService)
    }

    override val savedGameRepository: SavedGameRepository by lazy {
        Log.d("DefaultAppContainer", "Creating savedGameRepository")
        FavoriteGameRepository(GameDatabase.getDatabase(context).gameDao())
    }
}