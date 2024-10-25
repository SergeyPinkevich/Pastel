package app.pastel.screen.menu

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import app.pastel.navigation.Screen
import app.pastel.widget.menu.MainMenu
import app.pastel.widget.menu.MainMenuViewModel

@Composable
fun MainMenuScreen(
    context: Context,
    navController: NavController,
    viewModel: MainMenuViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    MainMenu(
        context = context,
        state = uiState,
        onSoundClick = viewModel::onSoundClick,
        onPlayClick = { navController.navigate(Screen.GameScreen) },
        onStatsClick = { navController.navigate(Screen.StatsScreen) },
        onAboutClick = { navController.navigate(Screen.AboutScreen) },
    )
}