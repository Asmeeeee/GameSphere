package com.example.soundwave.database
import android.util.Log
import com.example.soundwave.model.Developer
import com.example.soundwave.model.DeveloperDetails
import com.example.soundwave.model.Game
import com.example.soundwave.model.GameDetails
import com.example.soundwave.model.GameResponse
import com.example.soundwave.network.GameDBApiService

interface GameRepository {
    suspend fun getGames(search: String, metaCritic: String): GameResponse<Game>
    suspend fun getDevelopers(search: String, metaCritic: String): GameResponse<Developer>
    suspend fun getGameDetails(id: String): GameDetails
    suspend fun getDeveloperDetails(id: String): DeveloperDetails
}

class GamesRepository(private val apiService: GameDBApiService) : GameRepository{
    override suspend fun getGames(search: String, metaCritic: String): GameResponse<Game> {
        Log.d("GameRepository", "getGames metaCritique: $metaCritic")
        val response = apiService.getGames(search, metaCritic)
        return response
    }

    override suspend fun getDevelopers(search: String, metaCritic: String): GameResponse<Developer> {
        val response = apiService.getDeveloper(search, metaCritic)
        Log.d("GameRepository", "getDevelopers response: $response")
        return response
    }

    override suspend fun getGameDetails(id: String): GameDetails {
        val response = apiService.getGameDetails(id)
        Log.d("GameRepository", "getGameDetails response: $response")
        return response
    }

    override suspend fun getDeveloperDetails(id: String): DeveloperDetails {
        val response = apiService.getDeveloperDetails(id)
        Log.d("GameRepository", "getDeveloperDetails response: $response")
        return response
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