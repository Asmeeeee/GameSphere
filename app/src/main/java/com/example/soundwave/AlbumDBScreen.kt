package com.example.soundwave

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.soundwave.ui.screens.AlbumDetailScreen
import com.example.soundwave.ui.screens.AlbumListScreen
import com.example.soundwave.viewmodel.AlbumDBViewModel

enum class AlbumDBScreen(@StringRes val title: Int) {
    List(title = R.string.popular_albums),
    Grid(title = R.string.popular_albums),
    Detail(title = R.string.album_detail),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDBAppBar(
    currentScreen: AlbumDBScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    navigateToListScreen: () -> Unit,
    albumDBViewModel: AlbumDBViewModel,
    modifier: Modifier = Modifier
) {
    var menuExpanded by remember { mutableStateOf(false) }
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        actions = {
            if (currentScreen.name == AlbumDBScreen.List.name || currentScreen.name == AlbumDBScreen.Grid.name) {
                IconButton(onClick = {
                    navigateToListScreen()
                }) {
                    Icon(
                        imageVector = Icons.Filled.List ,
                        contentDescription = stringResource(id = R.string.more_vert)
                    )
                }
                IconButton(onClick = {
                    menuExpanded = !menuExpanded
                }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Open Menu to select different movie lists"
                    )
                }
                DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                    DropdownMenuItem(
                        onClick = {
                            albumDBViewModel.getPopularAlbums()
                            menuExpanded = false

                        },
                        text = {
                            Text(stringResource(R.string.popular_albums))
                        }
                    )
                    DropdownMenuItem(
                        onClick = {
                            albumDBViewModel.getTopRatedAlbums()
                            menuExpanded = false

                        },
                        text = {
                            Text(stringResource(R.string.top_rated_albums))
                        }
                    )
                    DropdownMenuItem(
                        onClick = {
                            albumDBViewModel.getSavedAlbums()
                            menuExpanded = false

                        },
                        text = {
                            Text(stringResource(R.string.saved_albums))
                        }
                    )
                }
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlbumDBApp(
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = AlbumDBScreen.valueOf(
        backStackEntry?.destination?.route ?: AlbumDBScreen.List.name
    )

    val albumDBViewModel: AlbumDBViewModel = viewModel(factory = AlbumDBViewModel.Factory)

    Scaffold(
        topBar = {
            AlbumDBAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null && currentScreen.name != AlbumDBScreen.List.name && currentScreen.name != AlbumDBScreen.Grid.name,
                navigateUp = { navController.navigateUp() },
                navigateToListScreen = { navController.navigate(AlbumDBScreen.List.name) },
                albumDBViewModel = albumDBViewModel
            )
        }
    ) { innerPadding ->
        val albumDBViewModel: AlbumDBViewModel = viewModel(factory = AlbumDBViewModel.Factory)

        NavHost(
            navController = navController,
            startDestination = AlbumDBScreen.List.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = AlbumDBScreen.List.name) {
                AlbumListScreen(
                    albumListUiState = albumDBViewModel.albumListUiState,
                    onAlbumListItemClicked = {
                        albumDBViewModel.setSelectedAlbumDetail(it.id)
                        navController.navigate(AlbumDBScreen.Detail.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }
            composable(route = AlbumDBScreen.Detail.name) {
                AlbumDetailScreen(
                    albumDBViewModel = albumDBViewModel,
                    selectedAlbumUiState = albumDBViewModel.selectedAlbumUiState,
                    modifier = Modifier
                )
            }
        }
    }
}