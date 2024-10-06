package app.pastel.screen.menu

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import app.pastel.navigation.Screen
import app.pastel.state.MainMenuUIState
import app.pastel.widget.menu.MainMenu

@Composable
fun MainMenuScreen(context: Context, navController: NavController) {
    MainMenu(
        context = context,
        state = MainMenuUIState(),
        onPlayClick = {
            navController.navigate(Screen.GameScreen)
        }
    )
}