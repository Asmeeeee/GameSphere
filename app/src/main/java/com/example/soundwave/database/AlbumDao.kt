package com.example.soundwave.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.soundwave.model.Album

@Dao
interface AlbumDao {

    @Query("SELECT * FROM favorite_albums")
    suspend fun getFavoriteAlbums(): List<Album>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteAlbum(album: Album)

    @Query("SELECT * FROM favorite_albums WHERE id = :id")
    suspend fun getAlbum(id: String): Album

    @Query("DELETE FROM favorite_albums WHERE id = :id")
    suspend fun deleteFavoriteAlbum(id: String)
}