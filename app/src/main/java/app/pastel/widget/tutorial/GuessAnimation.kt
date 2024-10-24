package app.pastel.widget.tutorial

import android.graphics.Bitmap
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val SIZE_CHANGE_IN_MS = 500
private const val MOVING_IN_MS = 2000
private const val BREAK_BETWEEN_ANIMATION_IN_MS = 2000

@Suppress("LongMethod", "CyclomaticComplexMethod")
@Composable
fun GuessAnimation(bitmap: Bitmap, circleRadiusInPx: Float) {
    val horizontalCenter = (bitmap.width.toFloat() / 2) - (circleRadiusInPx / 2)
    val startPoint = Offset(x = horizontalCenter, y = bitmap.height.toFloat() * 0.35f)
    val endPoint = Offset(x = horizontalCenter, y = bitmap.height.toFloat() * 0.65f)
    val startColor = Color(bitmap.getPixel(startPoint.x.toInt(), startPoint.y.toInt()))
    val endColor = Color(bitmap.getPixel(endPoint.x.toInt(), endPoint.y.toInt()))

    val verticalPosition = remember { Animatable(startPoint.y) }
    val color = remember { Animatable(startColor) }
    val radius = remember { Animatable(0f) }

    val positionAnimationSpec = tween<Float>(durationMillis = MOVING_IN_MS)
    val colorAnimationSpec = tween<Color>(durationMillis = MOVING_IN_MS)
    val radiusAnimationSpec = tween<Float>(durationMillis = SIZE_CHANGE_IN_MS)

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            while (true) {
                // Step 1: Scale up the circle
                radius.animateTo(
                    targetValue = circleRadiusInPx,
                    animationSpec = radiusAnimationSpec
                )

                // Step 2: Move and change color
                val positionAndColorAnimation = launch {
                    launch {
                        verticalPosition.animateTo(
                            targetValue = endPoint.y,
                            animationSpec = positionAnimationSpec
                        )
                    }
                    launch {
                        color.animateTo(
                            targetValue = endColor,
                            animationSpec = colorAnimationSpec
                        )
                    }
                }
                positionAndColorAnimation.join()

                // Step 3: Scale down the circle
                radius.animateTo(
                    targetValue = 0f,
                    animationSpec = radiusAnimationSpec
                )

                // Reset for repeat
                verticalPosition.snapTo(startPoint.y)
                color.snapTo(startColor)
                radius.snapTo(0f)

                delay(BREAK_BETWEEN_ANIMATION_IN_MS.toLong())
            }
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            color = color.value,
            radius = radius.value,
            center = Offset(x = horizontalCenter, y = verticalPosition.value)
        )
    }
}