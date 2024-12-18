package app.pastel.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.pastel.screen.about.AboutScreen
import app.pastel.screen.game.GameScreen
import app.pastel.screen.menu.MainMenuScreen
import app.pastel.screen.menu.SoundViewModel
import app.pastel.screen.stats.StatsScreen
import kotlin.reflect.KClass

@Composable
fun Navigation(context: Context, navController: NavHostController, startDestination: KClass<*>) {
    NavHost(navController, startDestination) {
        composable<Screen.MenuScreen> {
            MainMenuScreen(context = context, navController = navController)
        }
        composable<Screen.GameScreen> {
            val soundViewModel: SoundViewModel = hiltViewModel()
            GameScreen(
                context = context,
                navController = navController,
                soundSettings = soundViewModel.soundState.value
            )
        }
        composable<Screen.StatsScreen> {
            StatsScreen(navController = navController)
        }
        composable<Screen.AboutScreen> {
            val soundViewModel: SoundViewModel = hiltViewModel()
            AboutScreen(
                context = context,
                navController = navController,
                soundSettings = soundViewModel.soundState.value
            )
        }
    }
}