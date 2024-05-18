package com.example.soundwave.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.soundwave.GameDBApplication
import com.example.soundwave.database.GameRepository
import com.example.soundwave.database.SavedGameRepository
import com.example.soundwave.model.Developer
import com.example.soundwave.model.Game
import com.example.soundwave.model.GameDetails
import com.example.soundwave.ui.screens.maxMetacriticScore
import com.example.soundwave.ui.screens.minMetacriticScore
import com.example.soundwave.ui.screens.searchText
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


sealed interface GameListUiState {
    data class Success(val games: List<Game>) : GameListUiState
    object Error : GameListUiState
    object Loading : GameListUiState
}

sealed interface DeveloperListUiState {
    data class Success(val developers: List<Developer>) : DeveloperListUiState
    object Error : DeveloperListUiState
    object Loading : DeveloperListUiState
}

sealed interface SelectedGameUiState {
    data class Success(val gameDetails: GameDetails, val is_Favorite: Boolean) : SelectedGameUiState
    object Error : SelectedGameUiState
    object Loading : SelectedGameUiState
}


class GameDBViewModel(private val gameRepository: GameRepository, private val savedGameRepository: SavedGameRepository) : ViewModel()  {
    var gameListUiState: GameListUiState by mutableStateOf(GameListUiState.Loading)
        private set

    var selectedGameUiState: SelectedGameUiState by mutableStateOf(SelectedGameUiState.Loading)
        private set

    init {
        getGames()
    }

    fun getGames() {
        viewModelScope.launch {
            gameListUiState = GameListUiState.Loading
            gameListUiState = try {
                if(minMetacriticScore == 0) minMetacriticScore++
                val metaCriticScore = minMetacriticScore.toString() +", "+ maxMetacriticScore
                GameListUiState.Success(gameRepository.getGames(searchText, metaCriticScore).results)
            } catch (e: IOException) {
                GameListUiState.Error
            } catch (e: HttpException) {
                GameListUiState.Error
            }
        }
    }


    fun setSelectedGameDetail(idGame: String) {
        viewModelScope.launch {
            selectedGameUiState = SelectedGameUiState.Loading
            selectedGameUiState = try {
                SelectedGameUiState.Success(gameRepository.getGameDetails(idGame), savedGameRepository.getGame(idGame) != null)
            } catch (e: IOException) {
                SelectedGameUiState.Error
            } catch (e: HttpException) {
                SelectedGameUiState.Error
            }
        }
    }

    fun getSavedGames(){
        viewModelScope.launch {
            gameListUiState = GameListUiState.Loading
            gameListUiState = try {
                GameListUiState.Success(savedGameRepository.getSavedGames())
            } catch (e: IOException) {
                GameListUiState.Error
            }
            catch (e: HttpException) {
                GameListUiState.Error
            }
        }
    }

    fun saveGame(gameDetails: GameDetails){
        viewModelScope.launch {
            try{
                savedGameRepository.insertGame(gameDetails.toGame())
                selectedGameUiState = SelectedGameUiState.Success(gameDetails, true)
            } catch (e: IOException) {
                GameListUiState.Error
            }
            catch (e: HttpException) {
                GameListUiState.Error
            }
        }
    }

    fun deleteGame(gameDetails: GameDetails){
        viewModelScope.launch {
            savedGameRepository.deleteGame(gameDetails.id)
            selectedGameUiState = SelectedGameUiState.Success(gameDetails, false)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GameDBApplication)
                val gameRepository = application.container.gameRepository
                val savedGameRepository = application.container.savedGameRepository
                GameDBViewModel(gameRepository = gameRepository, savedGameRepository = savedGameRepository)
            }
        }
    }
}

