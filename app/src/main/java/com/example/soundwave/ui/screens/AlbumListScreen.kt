package com.example.soundwave.ui.screens

import android.util.Log

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.example.soundwave.model.Album
import com.example.soundwave.spotify.SpotifyViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.example.soundwave.spotify.SimpleTrack


import com.example.soundwave.viewmodel.AlbumListUiState


@Composable
fun AlbumListScreen(albumListUiState: AlbumListUiState,
                    onAlbumListItemClicked: (Album) -> Unit,
                    spotifyViewModel: SpotifyViewModel,
                    modifier: Modifier = Modifier


) {

    spotifyViewModel.getUserTopTracks()
    spotifyViewModel.topTracks
    //spotifyViewModel.getSelectedAlbum()



    spotifyViewModel.getAlbumTracks()
  //  spotifyViewModel.getSelectedArtist()
    val albumTracks by spotifyViewModel.albumTracks.observeAsState()






    spotifyViewModel.getSelectedArtistAlbums()

    Log.d("Track12435123613", albumTracks.toString())



        // Check if albumTracks is not null
    LazyColumn(modifier = modifier) {
        // Check if albumTracks is not null
        albumTracks?.let { tracksResponse ->
            // Ensure that the response contains a list of items
            tracksResponse.items?.let { tracks ->
                itemsIndexed(tracks) { index, track ->
                    AlbumListItemCard(
                        SimpleTrack = track,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }









}




@Composable
fun AlbumListItemCard(
    SimpleTrack: SimpleTrack,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,

    ) {
        Row {
//            Box {
//                AsyncImage(
//                    model = Constants.POSTER_IMAGE_BASE_URL + Constants.POSTER_IMAGE_WIDTH + album.posterPath,
//                    contentDescription = album.title,
//                    modifier = modifier
//                        .width(92.dp)
//                        .height(138.dp),
//                    contentScale = ContentScale.Crop
//                )
//            }
            Column {
                Text(
                    text = SimpleTrack.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = SimpleTrack.artists[0].name,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
    }
}