package app.pastel.util

import android.media.MediaPlayer
import androidx.annotation.RawRes
import app.pastel.R
import app.pastel.state.SoundSettings

fun MediaPlayer.playSound(soundSettings: SoundSettings) {
    if (isPlaying) {
        stop()
        prepare()
    }
    if (soundSettings == SoundSettings.ON) {
        start()
    }
}

enum class Sound(@RawRes val resId: Int) {
    CLICK(R.raw.click)
}