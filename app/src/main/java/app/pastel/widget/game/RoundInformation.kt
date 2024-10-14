package app.pastel.widget.game

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.pastel.R
import app.pastel.state.GameState
import app.pastel.ui.PastelTheme

@Composable
fun RoundInformation(
    state: GameState,
    round: Int,
    totalRounds: Int,
    totalScore: Int,
    previousTotalScore: Int,
    modifier: Modifier = Modifier
) {
    var totalScoreValue by remember { mutableIntStateOf(previousTotalScore) }

    LaunchedEffect(totalScore) {
        totalScoreValue = totalScore
    }

    val animatedTotalScore by animateIntAsState(
        targetValue = totalScoreValue,
        animationSpec = tween(durationMillis = 900),
        label = "total_score_animation"
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            stringResource(id = R.string.round_text).uppercase() + " $round/$totalRounds",
            fontSize = 20.sp,
            color = PastelTheme.colors.textColor,
            style = TextStyle(fontWeight = FontWeight.Black)
        )
        Text(
            text = if (state == GameState.ROUND_RESULT) animatedTotalScore.toString() else totalScore.toString(),
            fontSize = 20.sp,
            color = PastelTheme.colors.textColor,
            style = TextStyle(fontWeight = FontWeight.Black)
        )
    }
}

@Preview
@Composable
private fun RoundInformationPreview() {
    RoundInformation(
        state = GameState.GUESS,
        round = 1,
        totalRounds = 10,
        totalScore = 0,
        previousTotalScore = 0
    )
}