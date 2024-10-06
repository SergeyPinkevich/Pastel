package app.pastel.widget.game

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import app.pastel.screen.game.COLOR_ANIMATION_DURATION_IN_MS
import kotlinx.coroutines.delay

private const val SECONDS_TO_REMEMBER = 3
private const val COUNT_ANIMATION_DELAY_IN_MS = 500L
private const val COUNT_ANIMATION_IN_MS = 1000
private const val ALPHA_ANIMATION_IN_MS = 500

@Composable
fun CountdownAnimation(onComplete: () -> Unit) {
    var count by remember { mutableIntStateOf(SECONDS_TO_REMEMBER) }
    var alpha by remember { mutableFloatStateOf(1f) }
    var showText by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(COLOR_ANIMATION_DURATION_IN_MS.toLong())
        showText = true
        for (i in SECONDS_TO_REMEMBER downTo 0) {
            if (i == 0) {
                onComplete()
            }
            count = i
            alpha = 1f
            delay(COUNT_ANIMATION_DELAY_IN_MS)
            alpha = 0f
            delay(COUNT_ANIMATION_DELAY_IN_MS)
        }
    }

    val animatedCount by animateIntAsState(
        targetValue = count,
        animationSpec = tween(durationMillis = COUNT_ANIMATION_IN_MS),
        label = "color_to_remember_count"
    )
    val animatedAlpha by animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(durationMillis = ALPHA_ANIMATION_IN_MS),
        label = "color_to_remember_alpha"
    )

    if (showText) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = animatedCount.toString(),
                color = Color.White.copy(alpha = animatedAlpha),
                fontWeight = FontWeight.Black,
                fontSize = 128.sp
            )
        }
    }
}

@Preview
@Composable
private fun CountdownAnimationPreview() {
    CountdownAnimation(onComplete = {})
}