package com.example.soundwave.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.soundwave.R
import com.example.soundwave.viewmodel.AlbumDBViewModel
import com.example.soundwave.viewmodel.SelectedAlbumUiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    albumDBViewModel: AlbumDBViewModel,
    selectedAlbumUiState: SelectedAlbumUiState,
    modifier: Modifier = Modifier
) {
    val selectedAlbumUiState = albumDBViewModel.selectedAlbumUiState
    when (selectedAlbumUiState) {
        is SelectedAlbumUiState.Success -> {
            val uriHandler = LocalUriHandler.current
            Column(Modifier.width(IntrinsicSize.Max)) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(0.dp)) {
                    AsyncImage(
                        model = selectedAlbumUiState.albumDetail.images.get(0).url,
                        contentDescription = selectedAlbumUiState.albumDetail.title,
                        modifier = modifier,
                        contentScale = ContentScale.Crop
                    )
                }
                Text(
                    text = selectedAlbumUiState.albumDetail.title,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.size(8.dp))
                Row{
                    selectedAlbumUiState.albumDetail.genres.forEach { genre ->
                        Badge{ Text(text = genre,) }
                        Spacer(modifier = Modifier.size(8.dp))
                    }
                }
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = selectedAlbumUiState.albumDetail.releaseDate,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.size(8.dp))
                Button(
                    onClick = { uriHandler.openUri(selectedAlbumUiState.albumDetail.uri) },
                    modifier = modifier.fillMaxWidth()) {
                    Text(text = stringResource(id = R.string.open_uri))
                }
                Spacer(modifier = Modifier.size(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Favorite", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.size(8.dp))
                    Switch(checked = selectedAlbumUiState.is_Favorite, onCheckedChange = {
                        if(it){
                            albumDBViewModel.saveAlbum(selectedAlbumUiState.albumDetail)
                        }else{
                            albumDBViewModel.deleteAlbum(selectedAlbumUiState.albumDetail)
                        }
                    })
                }
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
        is SelectedAlbumUiState.Loading -> {
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )
        }
        is SelectedAlbumUiState.Error -> {
            Text(
                text = "Error...",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}