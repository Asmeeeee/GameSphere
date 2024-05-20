package com.example.soundwave.ui.screens

import android.util.Log
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
import com.example.soundwave.model.Developer
import com.example.soundwave.model.Game
import com.example.soundwave.viewmodel.DeveloperListUiState
import com.example.soundwave.viewmodel.GameListUiState

@Composable
fun DeveloperListScreen(developerListUiState: DeveloperListUiState,
                   onGameListItemClicked: (Developer) -> Unit,
                   onSearchButtonClicked: () -> Unit,
                   modifier: Modifier = Modifier
) {

    Log.d("DeveloperListScreen", "DeveloperListScreen: $developerListUiState")
    Column {
        FilterApi(onSearchButtonClicked)
        LazyColumn(modifier = modifier) {
            when(developerListUiState) {
                is DeveloperListUiState.Success -> {
                    items(developerListUiState.developers) { developers ->
                        DeveloperListItemCard(
                            developer = developers,
                            onGameListItemClicked,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                is DeveloperListUiState.Loading -> {
                    item {
                        Text(
                            text = "Loading...",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                is DeveloperListUiState.Error -> {
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
fun DeveloperListItemCard(developer: Developer,
                     onGameListItemClicked: (Developer) -> Unit,
                     modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = {
            onGameListItemClicked(developer)
        }
    ) {
        Row {
            Box {
                AsyncImage(
                    model = developer.image_background ?: "No image",
                    contentDescription = developer.name,
                    modifier = modifier
                        .width(92.dp)
                        .height(138.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Column {
                Text(
                    text = developer.name ?: "Unknown Game",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = developer.age.toString() ?: "Unknown released date",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.size(8.dp))

            }
        }
    }
}
