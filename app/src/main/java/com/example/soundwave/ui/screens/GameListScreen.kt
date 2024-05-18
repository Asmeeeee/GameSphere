package com.example.soundwave.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.soundwave.model.Game
import com.example.soundwave.viewmodel.GameListUiState

var searchText = ""
var minMetacriticScore = 0
var maxMetacriticScore = 100
@Composable
fun GameListScreen(gameListUiState: GameListUiState,
                   onGameListItemClicked: (Game) -> Unit,
                   onSearchButtonClicked: () -> Unit,
                   modifier: Modifier = Modifier
) {
    Column {
        FilterApi(onSearchButtonClicked)
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
                MetacriticBadge(score = game.metacritic)
            }
        }
    }
}

@Composable
fun FilterApi(onSearchButtonClicked: () -> Unit){
    SearchBar(onSearchButtonClicked = onSearchButtonClicked)
    MetacriticFilter()
}

@Composable
fun SearchBar(
    onSearchButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf(searchText) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(8.dp)
    ) {
        TextField(
            value = text,
            onValueChange = {
                text = it
                searchText = it
            },
            placeholder = { Text(text = "Search...") },
            singleLine = true,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = { onSearchButtonClicked() },
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(text = "Search")
        }
    }
}

@Composable
fun MetacriticFilter() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = "Min: ")
        var minScore by remember { mutableStateOf(minMetacriticScore.toString()) }
        TextField(
            value = minScore,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { value ->
                val newValue = value.filter { it.isDigit() }
                if (newValue.isNotEmpty()) {
                    minMetacriticScore = newValue.toInt()
                    minScore = newValue
                } else {
                    minMetacriticScore = 0
                    minScore = ""
                }
                Log.i("test", minMetacriticScore.toString())
            },
            modifier = Modifier.width(65.dp)
        )
        Text(text = "Max: ")
        var maxScore by remember { mutableStateOf(maxMetacriticScore.toString()) }
        TextField(
            value = maxScore,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { value ->
                val newValue = value.filter { it.isDigit() }
                if (newValue.isNotEmpty()) {
                    maxMetacriticScore = newValue.toInt()
                    maxScore = newValue
                } else {
                    maxMetacriticScore = 100
                    maxScore = ""
                }
                Log.i("test", maxMetacriticScore.toString())
            },
            modifier = Modifier.width(65.dp)
        )
    }
}

@Composable
fun MetacriticBadge(score: Int) {
    val backgroundColor = when {
        score >= 75 -> Color.Green
        score >= 50 -> Color.Yellow
        else -> Color.Red
    }
    Box(
        modifier = Modifier
            .size(30.dp)
            .background(color = backgroundColor, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = score.toString(),
            color = Color.White,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
    }
}