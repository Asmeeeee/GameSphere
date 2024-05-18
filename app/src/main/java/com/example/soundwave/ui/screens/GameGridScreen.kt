package com.example.soundwave.ui.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.soundwave.model.Game
import com.example.soundwave.viewmodel.GameListUiState

@Composable
fun GameGridScreen(gameListUiState: GameListUiState,
                   onGameListItemClicked: (Game) -> Unit,
                   modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp)
    ) {
        when(gameListUiState) {
            is GameListUiState.Success -> {
                items(gameListUiState.games) { game ->
                    GameGridItemCard(
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
fun GameGridItemCard(game: Game,
                     onGameGridItemClicked: (Game) -> Unit,
                     modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = {
            onGameGridItemClicked(game)
        }
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = game.background_image,
                contentDescription = game.name,
                modifier = Modifier
                    .width(138.dp)
                    .height(207.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = game.name,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}