package com.example.soundwave.database

import com.example.soundwave.model.Album
import com.example.soundwave.model.AlbumDetails
import com.example.soundwave.model.AlbumResponse
import com.example.soundwave.network.AlbumDBApiService

interface AlbumRepository {
    suspend fun getPopularAlbums(): AlbumResponse<Album>
    suspend fun getTopRatedAlbums(): AlbumResponse<Album>
    suspend fun getAlbumDetails(id: String): AlbumDetails
}


class AlbumsRepository(private val apiService: AlbumDBApiService) : AlbumRepository{
    override suspend fun getPopularAlbums(): AlbumResponse<Album> {
        return apiService.getPopularAlbums();
    }

    override suspend fun getTopRatedAlbums(): AlbumResponse<Album> {
        return apiService.getTopRatedAlbums();
    }

    override suspend fun getAlbumDetails(id: String): AlbumDetails {
        return apiService.getMovieDetail(id);
    }
}