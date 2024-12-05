package app.pastel.screen.menu

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import app.pastel.navigation.Screen
import app.pastel.widget.menu.MainMenu

@Composable
fun MainMenuScreen(
    context: Context,
    navController: NavController,
    viewModel: SoundViewModel = hiltViewModel()
) {
    val soundState by viewModel.soundState.collectAsState()
    MainMenu(
        context = context,
        soundState = soundState,
        onSoundClick = viewModel::onSoundClick,
        onPlayClick = { navController.navigate(Screen.GameScreen) },
        onStatsClick = { navController.navigate(Screen.StatsScreen) },
        onAboutClick = { navController.navigate(Screen.AboutScreen) },
    )
}