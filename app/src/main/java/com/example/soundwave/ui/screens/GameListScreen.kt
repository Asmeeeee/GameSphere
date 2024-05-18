package com.example.soundwave.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.soundwave.model.Game
import com.example.soundwave.viewmodel.GameListUiState


@Composable
fun GameListScreen(gameListUiState: GameListUiState,
                   onGameListItemClicked: (Game) -> Unit,
                   modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        when(gameListUiState) {
            is GameListUiState.Success -> {
                items(gameListUiState.games) { game ->
                    GameListItemCard(
                        game = game,
                        onGameListItemClicked,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            is GameListUiState.Loading -> {
                item {
                    Text(
                        text = "Loading...",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            is GameListUiState.Error -> {
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
fun GameListItemCard(game: Game,
                     onGameListItemClicked: (Game) -> Unit,
                     modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = {
            onGameListItemClicked(game)
        }
    ) {
        Row {
            Box {
                AsyncImage(
                    model = game.background_image ?: "No image",
                    contentDescription = game.name,
                    modifier = modifier
                        .width(92.dp)
                        .height(138.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Column {
                Text(
                    text = game.name ?: "Unknown Game",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = game.released ?: "Unknown released date",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
    }
}