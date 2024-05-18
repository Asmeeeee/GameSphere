package com.example.soundwave.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.soundwave.model.Game

@Dao
interface GameDao {

    @Query("SELECT * FROM favorite_games")
    suspend fun getFavoriteGame(): List<Game>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteGame(game: Game)

    @Query("SELECT * FROM favorite_games WHERE id = :id")
    suspend fun getGame(id: String): Game

    @Query("DELETE FROM favorite_games WHERE id = :id")
    suspend fun deleteFavoriteGame(id: String)
}