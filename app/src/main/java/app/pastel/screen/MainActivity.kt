package app.pastel.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import app.pastel.state.MainMenuUIState
import app.pastel.state.SoundSettings
import app.pastel.widget.MainMenu

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var state by remember { mutableStateOf(MainMenuUIState()) }
            MainMenu(
                context = this,
                state = state,
                onSoundClick = {
                    val updatedSoundSettings = if (state.soundSettings == SoundSettings.ON) {
                        SoundSettings.OFF
                    } else {
                        SoundSettings.ON
                    }
                    state = state.copy(soundSettings = updatedSoundSettings)
                }
            )
            window.statusBarColor = Color.White.toArgb()
        }
    }
}

