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
import com.example.soundwave.ui.screens.GameDetailScreen
import com.example.soundwave.ui.screens.GameGridScreen
import com.example.soundwave.ui.screens.GameListScreen
import com.example.soundwave.viewmodel.GameDBViewModel

enum class GameDBScreen(@StringRes val title: Int) {
    List(title = R.string.games),
    Grid(title = R.string.games),
    Detail(title = R.string.game_detail),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDBAppBar(
    currentScreen: GameDBScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    navigateToListScreen: () -> Unit,
    navigateToGridScreen: () -> Unit,
    gameDBViewModel: GameDBViewModel,
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
            if (currentScreen.name == GameDBScreen.List.name || currentScreen.name == GameDBScreen.Grid.name) {
                IconButton(onClick = {
                    if(currentScreen.name == GameDBScreen.List.name) navigateToGridScreen() else navigateToListScreen()
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
                        contentDescription = "Open Menu to select different game lists"
                    )
                }
                DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                    DropdownMenuItem(
                        onClick = {
                            gameDBViewModel.getGames()
                            menuExpanded = false

                        },
                        text = {
                            Text(stringResource(R.string.games))
                        }
                    )
                    DropdownMenuItem(
                        onClick = {
                            gameDBViewModel.getSavedGames()
                            menuExpanded = false

                        },
                        text = {
                            Text(stringResource(R.string.saved_games))
                        }
                    )
                }
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GameDBApp(
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = GameDBScreen.valueOf(
        backStackEntry?.destination?.route ?: GameDBScreen.List.name
    )

    val gameDBViewModel: GameDBViewModel = viewModel(factory = GameDBViewModel.Factory)

    Scaffold(
        topBar = {
            GameDBAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null && currentScreen.name != GameDBScreen.List.name && currentScreen.name != GameDBScreen.Grid.name,
                navigateUp = { navController.navigateUp() },
                navigateToListScreen = { navController.navigate(GameDBScreen.List.name) },
                navigateToGridScreen = { navController.navigate(GameDBScreen.Grid.name) },
                gameDBViewModel = gameDBViewModel
            )
        }
    ) { innerPadding ->
        val gameDBViewModel: GameDBViewModel = viewModel(factory = GameDBViewModel.Factory)

        NavHost(
            navController = navController,
            startDestination = GameDBScreen.List.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = GameDBScreen.List.name) {
                GameListScreen(
                    gameListUiState = gameDBViewModel.gameListUiState,
                    onGameListItemClicked = { game ->
                        gameDBViewModel.setSelectedGameDetail(game.id)
                        navController.navigate(GameDBScreen.Detail.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }
            composable(route = GameDBScreen.Grid.name){
                GameGridScreen(
                    gameListUiState = gameDBViewModel.gameListUiState,
                    onGameListItemClicked = {
                        gameDBViewModel.setSelectedGameDetail(it.id)
                        navController.navigate(GameDBScreen.Detail.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
            composable(route = GameDBScreen.Detail.name) {
                GameDetailScreen(
                    gameDBViewModel = gameDBViewModel,
                    selectedGameUiState = gameDBViewModel.selectedGameUiState,
                    modifier = Modifier
                )
            }
        }
    }
}