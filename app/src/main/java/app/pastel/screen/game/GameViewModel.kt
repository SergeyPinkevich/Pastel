package app.pastel.screen.game

import android.graphics.Bitmap
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import androidx.annotation.ArrayRes
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.pastel.R
import app.pastel.state.GameState
import app.pastel.state.GameUIState
import app.pastel.state.RoundUIState
import app.pastel.util.distance
import app.pastel.util.maxSaturation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.random.Random

private const val ROUND_RESULT_SHOWING_DURATION_IN_MS = 2000L
private const val ONE_HUNDRED_PERCENT = 100

@HiltViewModel
class GameViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(GameUIState())
    val uiState: StateFlow<GameUIState> = _uiState

    init {
        onStartGame()
    }

    fun onStartGame() {
        _uiState.value = GameUIState(
            state = GameState.REMEMBER,
            rounds = listOf(RoundUIState())
        )
    }

    fun onGloballyPositioned(layoutCoordinates: LayoutCoordinates) {
        viewModelScope.launch(Dispatchers.Default) {
            val width = layoutCoordinates.size.width
            val height = layoutCoordinates.size.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            // Horizontal gradient from white to the given color
            val horizontalGradient = Paint().apply {
                shader = LinearGradient(
                    0F,
                    0F,
                    width.toFloat(),
                    0F,
                    Color.White.toArgb(),
                    _uiState.value.rounds[_uiState.value.round - 1].color.maxSaturation().toArgb(),
                    Shader.TileMode.CLAMP
                )
            }

            // Vertical gradient from transparent black to fully black
            val verticalGradient = Paint().apply {
                shader = LinearGradient(
                    0F,
                    0F,
                    0F,
                    height.toFloat(),
                    Color.Transparent.toArgb(),
                    Color.Black.toArgb(),
                    Shader.TileMode.CLAMP
                )
            }

            android.graphics.Canvas(bitmap).apply {
                drawRect(0f, 0f, width.toFloat(), height.toFloat(), horizontalGradient)
                drawRect(0f, 0f, width.toFloat(), height.toFloat(), verticalGradient)
            }
            withContext(Dispatchers.Main) {
                updateColorPalette(bitmap)
            }

            if (_uiState.value.state == GameState.REMEMBER) {
                val randomX = Random.nextInt(0, bitmap.width)
                val randomY = Random.nextInt(0, bitmap.width)
                val colorOffset = Offset(x = randomX.toFloat(), y = randomY.toFloat())
                val color = Color(bitmap.getPixel(randomX, randomY))

                withContext(Dispatchers.Main) {
                    updateColorToRemember(color, colorOffset)
                }
            }
        }
    }

    fun onCountdownComplete() {
        _uiState.value = _uiState.value.copy(
            state = GameState.GUESS,
        )
    }

    fun onColorGuess(color: Color, offset: Offset) {
        _uiState.value = _uiState.value.copy(
            rounds = _uiState.value.rounds.mapIndexed { index, item ->
                if (index == _uiState.value.round - 1) {
                    item.copy(guessColor = color, guessColorOffset = offset)
                } else {
                    item
                }
            }
        )
    }

    fun onColorConfirm() {
        val roundScore = calculatePointsForRound()
        val roundResultRes = roundResultRes(roundScore)
        _uiState.value = _uiState.value.copy(
            state = GameState.ROUND_RESULT,
            totalScore = _uiState.value.totalScore + roundScore,
            rounds = _uiState.value.rounds.mapIndexed { index, item ->
                if (index == _uiState.value.round - 1) {
                    item.copy(score = roundScore, resultRes = roundResultRes)
                } else {
                    item
                }
            }
        )

        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                delay(ROUND_RESULT_SHOWING_DURATION_IN_MS)
            }
            withContext(Dispatchers.Main) {
                _uiState.value = _uiState.value.copy(
                    state = GameState.ROUND_FINISHED,
                )
            }
        }
    }

    fun onNextRoundClick() {
        _uiState.value = _uiState.value.copy(
            state = GameState.REMEMBER,
            round = _uiState.value.round + 1,
            rounds = _uiState.value.rounds.plus(RoundUIState())
        )
    }

    fun onGameFinishClick() {
        _uiState.value = _uiState.value.copy(
            state = GameState.FINISHED,
        )
    }

    @Suppress("MagicNumber")
    @ArrayRes
    private fun roundResultRes(roundScore: Int): Int {
        return when (roundScore) {
            100 -> R.array.outstanding_scores
            in 85..99 -> R.array.high_scores
            in 70..85 -> R.array.medium_scores
            in 50..69 -> R.array.low_scores
            else -> R.array.very_low_scores
        }
    }

    private fun calculatePointsForRound(): Int {
        val currentRound = _uiState.value.rounds[_uiState.value.round - 1]

        val distance = currentRound.guessColorOffset.distance(currentRound.colorOffset)
        val maxDistance = Offset.Zero.distance(
            Offset(
                x = currentRound.paletteBitmap.width.toFloat(),
                y = currentRound.paletteBitmap.height.toFloat()
            )
        )
        val normalizedDistance = (distance / maxDistance)
        return ONE_HUNDRED_PERCENT - (normalizedDistance * ONE_HUNDRED_PERCENT).roundToInt()
    }

    private fun updateColorToRemember(color: Color, colorOffset: Offset) {
        _uiState.value = _uiState.value.copy(
            rounds = _uiState.value.rounds.mapIndexed { index, item ->
                if (index == _uiState.value.round - 1) {
                    item.copy(color = color, colorOffset = colorOffset)
                } else {
                    item
                }
            }
        )
    }

    private fun updateColorPalette(bitmap: Bitmap) {
        _uiState.value = _uiState.value.copy(
            rounds = _uiState.value.rounds.mapIndexed { index, item ->
                if (index == _uiState.value.round - 1) {
                    item.copy(paletteBitmap = bitmap)
                } else {
                    item
                }
            }
        )
    }
}