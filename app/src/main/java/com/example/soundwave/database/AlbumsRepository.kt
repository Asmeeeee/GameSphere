package com.example.soundwave.database

import com.example.soundwave.model.Album
import com.example.soundwave.model.AlbumDetails
import com.example.soundwave.model.AlbumResponse
import com.example.soundwave.network.AlbumDBApiService

interface AlbumRepository {
    suspend fun getPopularAlbums(): AlbumResponse<Album>
    suspend fun getNewAblums(): AlbumResponse<Album>
    suspend fun getAlbumDetails(id: String): AlbumDetails
}

class AlbumsRepository(private val apiService: AlbumDBApiService) : AlbumRepository{
    override suspend fun getPopularAlbums(): AlbumResponse<Album> {
        return apiService.getPopularAlbums();
    }

    override suspend fun getNewAblums(): AlbumResponse<Album> {
        return apiService.getNewAblums();
    }

    override suspend fun getAlbumDetails(id: String): AlbumDetails {
        return apiService.getAlbumDetails(id);
    }
}


interface SavedAlbumRepository{
    suspend fun getSavedAlbums(): List<Album>
    suspend fun inserAlbum(album: Album)
    suspend fun getAlbum(id: String): Album
    suspend fun deleteAlbum(id: String)
}
class FavoriteAlbumRepository(private val albumDao: AlbumDao) : SavedAlbumRepository {
    override suspend fun getSavedAlbums(): List<Album> {
        return albumDao.getFavoriteAlbums()
    }

    override suspend fun inserAlbum(album: Album) {
        albumDao.insertFavoriteAlbum(album)
    }

    override suspend fun getAlbum(id: String): Album {
        return albumDao.getAlbum(id)
    }

    override suspend fun deleteAlbum(id: String) {
        albumDao.deleteFavoriteAlbum(id)
    }

}