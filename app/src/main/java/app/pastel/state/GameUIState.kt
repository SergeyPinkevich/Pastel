package app.pastel.state

import android.graphics.Bitmap
import androidx.annotation.ArrayRes
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import app.pastel.R
import app.pastel.util.randomColor

data class GameUIState(
    val state: GameState = GameState.UNDEFINED,
    val round: Int = 1,
    val totalRounds: Int = 10,
    val rounds: List<RoundUIState> = emptyList(),
    val totalScore: Int = 0,
)

data class RoundUIState(
    val paletteBitmap: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888),
    val color: Color = randomColor(),
    val guessColor: Color = Color.Unspecified,
    val score: Int = 0,
    val colorOffset: Offset = Offset.Unspecified,
    val guessColorOffset: Offset = Offset.Unspecified,
    @ArrayRes val resultRes: Int = R.array.outstanding_scores
)

enum class GameState {
    UNDEFINED,
    REMEMBER,
    GUESS,
    ROUND_RESULT,
    ROUND_FINISHED,
    FINISHED
}