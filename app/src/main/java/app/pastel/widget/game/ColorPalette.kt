package app.pastel.widget.game

import android.graphics.Bitmap
import androidx.compose.animation.core.AnimationVector
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.pastel.R
import app.pastel.state.GameState
import app.pastel.ui.PastelTheme
import app.pastel.util.distance
import app.pastel.widget.tutorial.GuessAnimation
import kotlinx.coroutines.delay
import kotlin.math.pow
import kotlin.math.roundToInt

private val CIRCLE_RADIUS = 36.dp
private val BIG_CIRCLE_RADIUS = 48.dp

private const val GUESS_SIZE_ANIMATION_IN_MS = 400
private const val DASH_LINE_ANIMATION_IN_MS = 900
private const val CONFIRM_ANIMATION_IN_MS = 1000

private const val DASH_LINE_STROKE_LENGTH_IN_PX = 15f
private const val DASH_LINE_SPACE_LENGTH_IN_PX = 10f

private const val HORIZONTAL_BUFFER_FOR_TEXT_IN_PX = 180f
private const val VERTICAL_BUFFER_FOR_TEXT_IN_PX = 100f

@Suppress("LongMethod", "CyclomaticComplexMethod")
@Composable
fun ColorPalette(
    gameState: GameState,
    paletteBitmap: Bitmap,
    roundScore: Int,
    color: Color,
    colorOffset: Offset,
    guessColor: Color,
    guessColorOffset: Offset,
    onColorGuess: (Color, Offset) -> Unit,
    onColorConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    showTutorial: Boolean = false,
    onTouch: () -> Unit = {}
) {
    var guessOffset by remember { mutableStateOf(guessColorOffset) }
    val state by remember { mutableStateOf(gameState) }
    val textMeasurer = rememberTextMeasurer()

    var guessRadius by remember { mutableStateOf(CIRCLE_RADIUS) }
    val animatedGuessRadius by animateDpAsState(
        targetValue = guessRadius,
        animationSpec = tween(GUESS_SIZE_ANIMATION_IN_MS),
        label = "guess_circle_radius_animation"
    )

    var targetOffset by remember { mutableStateOf(guessColorOffset) }
    val animatedOffset by animateValueAsState(
        targetValue = if (state == GameState.ROUND_RESULT) targetOffset else colorOffset,
        typeConverter = TwoWayConverter(
            convertToVector = { AnimationVector(it.x, it.y) },
            convertFromVector = { Offset(it.v1, it.v2) }
        ),
        animationSpec = tween(DASH_LINE_ANIMATION_IN_MS),
        label = "dash_line_to_target_color_animation"
    )

    val textColor = PastelTheme.colors.textColor

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(PastelTheme.colors.backgroundColor)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    onTouch.invoke()
                    if (state == GameState.GUESS) {
                        val tapToConfirm = isTapToConfirm(
                            currentTouch = offset,
                            previousTouch = guessOffset,
                            radius = CIRCLE_RADIUS
                                .toPx()
                                .roundToInt()
                        )
                        if (tapToConfirm) {
                            onColorConfirm.invoke()
                        } else {
                            guessOffset = offset

                            val x = offset.x.toInt()
                            val y = offset.y.toInt()

                            if (x in 0 until paletteBitmap.width && y in 0 until paletteBitmap.height) {
                                val pixelColor = paletteBitmap.getPixel(x, y)
                                val updatedGuessColor = Color(pixelColor)
                                onColorGuess.invoke(updatedGuessColor, offset)
                            }
                        }
                    }
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        guessRadius = CIRCLE_RADIUS
                    },
                    onDrag = { change, _ ->
                        onTouch.invoke()
                        if (state == GameState.GUESS) {
                            guessOffset = change.position
                            guessRadius = BIG_CIRCLE_RADIUS

                            val x = change.position.x.toInt()
                            val y = change.position.y.toInt()

                            if (x in 0 until paletteBitmap.width && y in 0 until paletteBitmap.height) {
                                val pixelColor = paletteBitmap.getPixel(x, y)
                                val updatedGuessColor = Color(pixelColor)
                                onColorGuess.invoke(updatedGuessColor, change.position)
                            }
                        }
                    }
                )
            }
    ) {
        drawCircle(
            color = guessColor,
            radius = animatedGuessRadius.toPx(),
            center = guessColorOffset
        )
        if (state == GameState.ROUND_RESULT || state == GameState.ROUND_FINISHED) {
            targetOffset = colorOffset

            drawCircle(color = color, radius = CIRCLE_RADIUS.toPx(), center = colorOffset)

            drawLine(
                color = textColor,
                start = guessColorOffset,
                end = animatedOffset,
                strokeWidth = 3f,
                pathEffect = PathEffect.dashPathEffect(
                    floatArrayOf(DASH_LINE_STROKE_LENGTH_IN_PX, DASH_LINE_SPACE_LENGTH_IN_PX), 0f
                )
            )

            drawText(
                textMeasurer = textMeasurer,
                text = "+$roundScore",
                style = TextStyle(
                    color = textColor,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black
                ),
                topLeft = calculateTextOffset(
                    from = guessColorOffset,
                    to = colorOffset,
                    radiusInPx = CIRCLE_RADIUS.toPx(),
                    screenWidth = paletteBitmap.width,
                    screenHeight = paletteBitmap.height
                )
            )
        }
    }

    if (showTutorial) {
        if (guessColor == Color.Unspecified) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.tutorial_guess).uppercase(),
                    style = TextStyle(
                        color = textColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .width(300.dp)
                        .align(Alignment.Center)
                )
                GuessAnimation(
                    bitmap = paletteBitmap,
                    circleRadiusInPx = with(LocalDensity.current) { CIRCLE_RADIUS.toPx() },
                )
            }
        } else if (state == GameState.GUESS) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.tutorial_confirm).uppercase(),
                    style = TextStyle(
                        color = textColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(180.dp)
                )
            }
        }
    }

    LaunchedEffect(showTutorial) {
        while (true) {
            if (showTutorial && state == GameState.GUESS && guessColor != Color.Unspecified) {
                guessRadius = CIRCLE_RADIUS
                delay(CONFIRM_ANIMATION_IN_MS.toLong())
                guessRadius = CIRCLE_RADIUS + 16.dp
                delay(CONFIRM_ANIMATION_IN_MS.toLong())
            } else {
                break
            }
        }
    }
}

/**
 * Checks if the current tap is inside the previous tap. If that's the case we consider it
 * as a user confirmation and make a transition to round results.
 *
 * @return true if the current tap is inside the previous tap, false otherwise
 */
private fun isTapToConfirm(currentTouch: Offset, previousTouch: Offset, radius: Int): Boolean {
    val horizontalDelta = (currentTouch.x - previousTouch.x).pow(2)
    val verticalDelta = (currentTouch.y - previousTouch.y).pow(2)
    return horizontalDelta + verticalDelta < radius.toDouble().pow(2)
}

@Suppress("MagicNumber")
private fun calculateTextOffset(
    from: Offset,
    to: Offset,
    radiusInPx: Float,
    screenWidth: Int,
    screenHeight: Int
): Offset {
    val midPointX = (from.x + 0.5 * (to.x - from.x)).toFloat()
    val midPointY = (from.y + 0.5 * (to.y - from.y)).toFloat()

    val tooCloseToEachOther = from.distance(to) < (3.5 * radiusInPx)
    val tooCloseToRightEdge = midPointX > screenWidth - HORIZONTAL_BUFFER_FOR_TEXT_IN_PX
    val tooCloseToLeftEdge = midPointX < HORIZONTAL_BUFFER_FOR_TEXT_IN_PX
    val tooCloseToTopEdge = midPointY < VERTICAL_BUFFER_FOR_TEXT_IN_PX
    val tooCloseToBottomEdge = midPointY > screenHeight - VERTICAL_BUFFER_FOR_TEXT_IN_PX

    val textOffsetX: Float
    val textOffsetY: Float

    if (tooCloseToEachOther) {
        textOffsetY = if (tooCloseToBottomEdge) {
            midPointY - 3f * radiusInPx
        } else if (tooCloseToTopEdge) {
            midPointY + 2f * radiusInPx
        } else {
            midPointY + 2f * radiusInPx
        }
        textOffsetX = if (tooCloseToLeftEdge) {
            midPointX + 2f * radiusInPx
        } else if (tooCloseToRightEdge) {
            midPointX - 3f * radiusInPx
        } else {
            midPointX
        }
    } else {
        textOffsetY = if (tooCloseToBottomEdge) {
            midPointY - 2f * radiusInPx
        } else if (tooCloseToTopEdge) {
            midPointY + radiusInPx
        } else {
            midPointY - (0.25f * radiusInPx)
        }
        textOffsetX = if (tooCloseToLeftEdge) {
            midPointX + radiusInPx
        } else if (tooCloseToRightEdge) {
            midPointX - 2f * radiusInPx
        } else {
            midPointX + (0.5f * radiusInPx)
        }
    }

    return Offset(x = textOffsetX, y = textOffsetY)
}