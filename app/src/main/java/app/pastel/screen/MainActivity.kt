package app.pastel.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import app.pastel.navigation.Navigation
import app.pastel.navigation.Screen
import app.pastel.ui.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            AppTheme {
                Navigation(
                    context = this,
                    navController = navController,
                    startDestination = Screen.MenuScreen.javaClass.kotlin
                )
            }
        }
    }
}

