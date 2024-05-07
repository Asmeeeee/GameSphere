package com.example.soundwave.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.soundwave.AlbumDBApplication
import com.example.soundwave.database.AlbumRepository
import com.example.soundwave.database.SavedAlbumRepository
import com.example.soundwave.model.Album
import com.example.soundwave.model.AlbumDetails
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


sealed interface AlbumListUiState {
    data class Success(val albums: List<Album>) : AlbumListUiState
    object Error : AlbumListUiState
    object Loading : AlbumListUiState
}

sealed interface SelectedAlbumUiState {
    data class Success(val albumDetail: AlbumDetails, val is_Favorite: Boolean) : SelectedAlbumUiState
    object Error : SelectedAlbumUiState
    object Loading : SelectedAlbumUiState
}


class AlbumDBViewModel(private val albumsRepository: AlbumRepository, private val savedAlbumRepository: SavedAlbumRepository) : ViewModel()  {
    var albumListUiState: AlbumListUiState by mutableStateOf(AlbumListUiState.Loading)
        private set

    var selectedAlbumUiState: SelectedAlbumUiState by mutableStateOf(SelectedAlbumUiState.Loading)
        private set

    init {
        getNewAblums()
    }

    fun getNewAblums() {
        viewModelScope.launch {
            albumListUiState = AlbumListUiState.Loading
            albumListUiState = try {
                AlbumListUiState.Success(albumsRepository.getNewAblums().results)
            } catch (e: IOException) {
                AlbumListUiState.Error
            } catch (e: HttpException) {
                AlbumListUiState.Error
            }
        }
    }

    fun getPopularAlbums() {
        viewModelScope.launch {
            albumListUiState = AlbumListUiState.Loading
            albumListUiState = try {
                AlbumListUiState.Success(albumsRepository.getPopularAlbums().results)
            } catch (e: IOException) {
                AlbumListUiState.Error
            } catch (e: HttpException) {
                AlbumListUiState.Error
            }
        }
    }

    fun setSelectedAlbumDetail(idAlbum: String) {
        viewModelScope.launch {
            selectedAlbumUiState = SelectedAlbumUiState.Loading
            selectedAlbumUiState = try {
                SelectedAlbumUiState.Success(albumsRepository.getAlbumDetails(idAlbum), savedAlbumRepository.getAlbum(idAlbum) != null)
            } catch (e: IOException) {
                SelectedAlbumUiState.Error
            } catch (e: HttpException) {
                SelectedAlbumUiState.Error
            }
        }
    }

    fun getSavedAlbums(){
        viewModelScope.launch {
            albumListUiState = AlbumListUiState.Loading
            albumListUiState = try {
                AlbumListUiState.Success(savedAlbumRepository.getSavedAlbums())
            } catch (e: IOException) {
                AlbumListUiState.Error
            }
            catch (e: HttpException) {
                AlbumListUiState.Error
            }
        }
    }

    fun saveAlbum(albumDetails: AlbumDetails){
        viewModelScope.launch {
            try{
                savedAlbumRepository.inserAlbum(albumDetails.toAlbum())
                selectedAlbumUiState = SelectedAlbumUiState.Success(albumDetails, true)
            } catch (e: IOException) {
                AlbumListUiState.Error
            }
            catch (e: HttpException) {
                AlbumListUiState.Error
            }
        }
    }

    fun deleteAlbum(albumDetails: AlbumDetails){
        viewModelScope.launch {
            savedAlbumRepository.deleteAlbum(albumDetails.id)
            selectedAlbumUiState = SelectedAlbumUiState.Success(albumDetails, false)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AlbumDBApplication)
                val albumRepository = application.container.albumRepository
                val savedAlbumRepository = application.container.savedAlbumRepository
                AlbumDBViewModel(albumsRepository = albumRepository, savedAlbumRepository = savedAlbumRepository)
            }
        }
    }
}

