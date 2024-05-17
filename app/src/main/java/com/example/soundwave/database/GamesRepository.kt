package com.example.soundwave.database

import com.example.soundwave.model.Developer
import com.example.soundwave.model.DeveloperDetails
import com.example.soundwave.model.Game
import com.example.soundwave.model.GameDetails
import com.example.soundwave.model.GameResponse
import com.example.soundwave.network.GameDBApiService

interface GameRepository {
    suspend fun getGames(): GameResponse<Game>
    suspend fun getDevelopers(): GameResponse<Developer>
    suspend fun getGameDetails(id: String): GameDetails
    suspend fun getDeveloperDetails(id: String): DeveloperDetails
}

class GamesRepository(private val apiService: GameDBApiService) : GameRepository{
    override suspend fun getGames(): GameResponse<Game> {
        return apiService.getGames();
    }

    override suspend fun getDevelopers(): GameResponse<Developer> {
        return apiService.getDeveloper();
    }

    override suspend fun getGameDetails(id: String): GameDetails {
        return apiService.getGameDetails(id);
    }

    override suspend fun getDeveloperDetails(id: String): DeveloperDetails {
        return apiService.getDeveloperDetails(id);
    }
}


interface SavedGameRepository{
    suspend fun getSavedGames(): List<Game>
    suspend fun insertGame(game: Game)
    suspend fun getGame(id: String): Game
    suspend fun deleteGame(id: String)
}
class FavoriteGameRepository(private val gameDao: GameDao) : SavedGameRepository {
    override suspend fun getSavedGames(): List<Game> {
        return gameDao.getFavoriteGame()
    }

    override suspend fun insertGame(game: Game) {
        gameDao.insertFavoriteGame(game)
    }

    override suspend fun getGame(id: String): Game {
        return gameDao.getGame(id)
    }

    override suspend fun deleteGame(id: String) {
        gameDao.deleteFavoriteGame(id)
    }

}