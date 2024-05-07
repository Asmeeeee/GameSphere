package com.example.soundwave.database

import android.content.Context
import com.example.soundwave.network.AlbumDBApiService
import com.example.themoviedbv24.utils.Constants
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

interface AppContainer {
    val albumRepository: AlbumRepository
    val savedAlbumRepository: SavedAlbumRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer{

    fun getLoggerInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    val albumDBJson = Json {
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
        .addConverterFactory(albumDBJson.asConverterFactory("application/json".toMediaType()))
        .baseUrl(Constants.ALBUM_LIST_BASE_URL)
        .build()

    private val retrofitService: AlbumDBApiService by lazy {
        retrofit.create(AlbumDBApiService::class.java)
    }
    override val albumRepository: AlbumRepository by lazy {
        AlbumsRepository(retrofitService)
    }

    override val savedAlbumRepository: SavedAlbumRepository by lazy{
        FavoriteAlbumRepository(AlbumDatabase.getDatabase(context).albumDao())
    }
}