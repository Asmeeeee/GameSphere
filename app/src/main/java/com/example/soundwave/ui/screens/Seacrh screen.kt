package com.example.soundwave.ui.screens

/*
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.soundwave.spotify.Album
import com.example.soundwave.spotify.Artist
import com.example.soundwave.spotify.SpotifySearchItem
import com.example.soundwave.spotify.SpotifyViewModel
@Composable
fun SearchScreen(navController: NavController, spotifyViewModel: SpotifyViewModel) {
    var searchQuery: String by remember { mutableStateOf("") }

    Column {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth()

        )
        Button(onClick = { spotifyViewModel.search(searchQuery) }) {
            Text("Search")
        }

        val searchResults by spotifyViewModel.searchResults.observeAsState()
        searchResults?.let {
            SearchResultContent(
                items = it.items,
                onSongSelect = {songId ->
                    Log.d("analysisParalysis", songId)
                    //viewModelScope.launch {
                    spotifyViewModel.saveSelectedTrack(songId)
                    //navController.navigateToLeafScreen(LeafScreen.SongInfo)
                },
                onAlbumSelect = {albumId ->
                    spotifyViewModel.saveSelectedAlbum(albumId)
                   // navController.navigateToLeafScreen(LeafScreen.AlbumInfo)
                },
                onArtistSelect = {artistId ->
                    spotifyViewModel.saveSelectedArtist(artistId)
                    //navController.navigateToLeafScreen(LeafScreen.ArtistInfo)
                }
            )
        } ?: run{}
    }
}

@Composable
fun SearchResultContent(
    items: List<SpotifySearchItem>,
    onSongSelect: (String) -> Unit,
    onAlbumSelect: (String) -> Unit,
    onArtistSelect: (String) -> Unit
) {
    LazyColumn {
        items(items) { item ->
            when (item) {
                is SpotifySearchItem.TrackItem -> SpotifySearchItem.TrackItem(
                    track = item.track,

                )
                is SpotifySearchItem.AlbumItem -> SpotifySearchItem.AlbumItem(
                    album = item.album,

                )
                is SpotifySearchItem.ArtistItem -> SpotifySearchItem.ArtistItem(
                    artist = item.artist,

                )
            }
        }
    }
}

 */