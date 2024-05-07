package com.example.soundwave.network

import com.example.soundwave.model.Album
import com.example.soundwave.model.AlbumDetails
import com.example.soundwave.model.AlbumResponse
import com.example.soundwave.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AlbumDBApiService {

    @GET("popular")
    suspend fun getPopularAlbums(
        @Query("api_key")
        apiKey: String = Constants.API_KEY
    ): AlbumResponse<Album>
    @GET("browse/new-releases")
    suspend fun getNewAblums(
        @Query("api_key")
        apiKey: String = Constants.API_KEY
    ): AlbumResponse<Album>

    @GET("{id}")
    suspend fun getAlbumDetails(
        @Path("id") albumId: String,
        @Query("api_key") apiKey: String = Constants.API_KEY
    ): AlbumDetails
}