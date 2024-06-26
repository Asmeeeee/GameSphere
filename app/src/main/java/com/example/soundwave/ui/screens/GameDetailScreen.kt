package com.example.soundwave.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.soundwave.viewmodel.GameDBViewModel
import com.example.soundwave.viewmodel.SelectedGameUiState
import androidx.core.text.HtmlCompat
import com.example.soundwave.model.Game


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailScreen(
    gameDBViewModel: GameDBViewModel,
    selectedGameUiState: SelectedGameUiState,
    modifier: Modifier = Modifier
) {
    val selectedGameUiState = gameDBViewModel.selectedGameUiState
    when (selectedGameUiState) {
        is SelectedGameUiState.Success -> {
            val uriHandler = LocalUriHandler.current
            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
                    .width(IntrinsicSize.Max)) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(0.dp)) {
                    AsyncImage(
                        model = selectedGameUiState.gameDetails.background_image,
                        contentDescription = selectedGameUiState.gameDetails.name,
                        modifier = modifier
                            .height(250.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Text(
                    text = selectedGameUiState.gameDetails.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.size(8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = selectedGameUiState.gameDetails.released,
                        style = MaterialTheme.typography.bodySmall
                    )
                    IconButton(
                        onClick = {
                            val isFavorite = !selectedGameUiState.is_Favorite
                            if (isFavorite) {
                                gameDBViewModel.saveGame(selectedGameUiState.gameDetails)
                            } else {
                                gameDBViewModel.deleteGame(selectedGameUiState.gameDetails)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (selectedGameUiState.is_Favorite) {
                                Icons.Filled.Favorite
                            } else {
                                Icons.Outlined.FavoriteBorder
                            },
                            contentDescription = "Favorite"
                        )
                    }
                }
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = HTMLText(selectedGameUiState.gameDetails.description),
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.size(8.dp))
                Button(
                    onClick = { uriHandler.openUri(selectedGameUiState.gameDetails.website) },
                    modifier = modifier.fillMaxWidth()) {
                    Text(text = stringResource(id = R.string.open_uri))
                }
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
        is SelectedGameUiState.Loading -> {
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )
        }
        is SelectedGameUiState.Error -> {
            Text(
                text = "Error...",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun HTMLText(html: String): String{
    return HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
}


