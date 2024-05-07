package com.example.soundwave.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soundwave.model.Album
import com.example.soundwave.viewmodel.AlbumListUiState


@Composable
fun AlbumListScreen(albumListUiState: AlbumListUiState,
                    onAlbumListItemClicked: (Album) -> Unit,
                    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        when(albumListUiState) {
            is AlbumListUiState.Success -> {
                items(albumListUiState.albums) { album ->
                    AlbumListItemCard(
                        album = album,
                        onAlbumListItemClicked,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            is AlbumListUiState.Loading -> {
                item {
                    Text(
                        text = "Loading...",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            is AlbumListUiState.Error -> {
                item {
                    Text(
                        text = "Error: Something went wrong!",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumListItemCard(album: Album,
                      onAlbumListItemClicked: (Album) -> Unit,
                      modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = {
            onAlbumListItemClicked(album)
        }
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
                    text = album.title,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = album.releaseDate,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
    }
}