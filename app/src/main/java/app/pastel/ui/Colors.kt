package app.pastel.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

class Colors(
    textColor: Color,
    backgroundColor: Color,
) {
    var textColor by mutableStateOf(textColor)
        private set
    var backgroundColor by mutableStateOf(backgroundColor)
        private set

    fun copy(
        textColor: Color = this.textColor,
        backgroundColor: Color = this.backgroundColor,
    ): Colors = Colors(textColor, backgroundColor)

    fun updateColorsFrom(other: Colors) {
        textColor = other.textColor
        backgroundColor = other.backgroundColor
    }
}

fun lightColors() = Colors(
    textColor = Color.Black,
    backgroundColor = Color.White,
)

fun darkColors() = Colors(
    textColor = Color.White,
    backgroundColor = Color.Black,
)

internal fun localColors(isDark: Boolean) = staticCompositionLocalOf {
    if (isDark) darkColors() else lightColors()
}