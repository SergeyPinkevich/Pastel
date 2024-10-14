package app.pastel.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember

@Composable
fun AppTheme(
    colors: Colors = PastelTheme.colors,
    content: @Composable () -> Unit
) {
    val rememberedColors = remember { colors.copy() }.apply { updateColorsFrom(colors) }
    CompositionLocalProvider(
        localColors(isSystemInDarkTheme()) provides rememberedColors,
    ) {
        content()
    }
}

object PastelTheme {
    val colors: Colors
        @Composable
        @ReadOnlyComposable
        get() = localColors(isSystemInDarkTheme()).current
}