package app.pastel.screen.game

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import app.pastel.R
import app.pastel.navigation.Screen
import app.pastel.state.GameState
import app.pastel.widget.game.ColorPalette
import app.pastel.widget.game.CountdownAnimation
import app.pastel.widget.game.RoundHistory
import app.pastel.widget.game.RoundInformation
import app.pastel.widget.game.TextButton

const val COLOR_ANIMATION_DURATION_IN_MS = 1500

@Suppress("LongMethod")
@Composable
fun GameScreen(navController: NavController, viewModel: GameViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val currentRound = uiState.rounds[uiState.round - 1]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { layoutCoordinates ->
                viewModel.onGloballyPositioned(layoutCoordinates)
            }
    ) {
        when (uiState.state) {
            GameState.REMEMBER -> {
                Box {
                    ColorToRemember(color = currentRound.color)
                    CountdownAnimation(onComplete = viewModel::onCountdownComplete)
                    RoundInformation(
                        state = uiState.state,
                        round = uiState.round,
                        totalRounds = uiState.totalRounds,
                        totalScore = uiState.totalScore,
                        previousTotalScore = uiState.totalScore - currentRound.score,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
            GameState.GUESS -> {
                Box {
                    ColorPalette(
                        gameState = uiState.state,
                        paletteBitmap = currentRound.paletteBitmap,
                        roundScore = currentRound.score,
                        color = currentRound.color,
                        colorOffset = currentRound.colorOffset,
                        guessColor = currentRound.guessColor,
                        guessColorOffset = currentRound.guessColorOffset,
                        onColorGuess = viewModel::onColorGuess,
                        onColorConfirm = viewModel::onColorConfirm
                    )
                }
            }
            GameState.ROUND_RESULT -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    ColorPalette(
                        gameState = uiState.state,
                        paletteBitmap = currentRound.paletteBitmap,
                        roundScore = currentRound.score,
                        color = currentRound.color,
                        colorOffset = currentRound.colorOffset,
                        guessColor = currentRound.guessColor,
                        guessColorOffset = currentRound.guessColorOffset,
                        onColorGuess = viewModel::onColorGuess,
                        onColorConfirm = viewModel::onColorConfirm
                    )
                    RoundInformation(
                        state = uiState.state,
                        round = uiState.round,
                        totalRounds = uiState.totalRounds,
                        totalScore = uiState.totalScore,
                        previousTotalScore = uiState.totalScore - currentRound.score,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Text(
                        text = stringArrayResource(id = currentRound.resultRes).random().uppercase(),
                        style = TextStyle(color = Color.Black, fontSize = 32.sp),
                        fontWeight = FontWeight.Black,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 36.dp)
                    )
                }
            }

            GameState.ROUND_FINISHED -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    ColorPalette(
                        gameState = uiState.state,
                        paletteBitmap = currentRound.paletteBitmap,
                        roundScore = currentRound.score,
                        color = currentRound.color,
                        colorOffset = currentRound.colorOffset,
                        guessColor = currentRound.guessColor,
                        guessColorOffset = currentRound.guessColorOffset,
                        onColorGuess = viewModel::onColorGuess,
                        onColorConfirm = viewModel::onColorConfirm
                    )
                    RoundInformation(
                        state = uiState.state,
                        round = uiState.round,
                        totalRounds = uiState.totalRounds,
                        totalScore = uiState.totalScore,
                        previousTotalScore = uiState.totalScore - currentRound.score,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    if (uiState.round == uiState.totalRounds) {
                        TextButton(
                            textRes = R.string.finish_game_button,
                            onClick = viewModel::onGameFinishClick,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 36.dp)
                        )
                    } else {
                        TextButton(
                            textRes = R.string.next_round_button,
                            onClick = viewModel::onNextRoundClick,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 36.dp)
                        )
                    }
                }
            }
            GameState.FINISHED -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.MenuScreen) {
                                popUpTo(Screen.GameScreen) {
                                    inclusive = true
                                }
                            }
                        },
                        modifier = Modifier
                            .padding(top = 12.dp, end = 12.dp)
                            .size(36.dp)
                            .align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = uiState.totalScore.toString(),
                            style = TextStyle(color = Color.Black, fontSize = 128.sp),
                            fontWeight = FontWeight.Black
                        )
                        TextButton(
                            textRes = R.string.share_result_button,
                            onClick = { shareResult() }
                        )
                        RoundHistory(rounds = uiState.rounds, modifier = Modifier.weight(1f))
                        TextButton(
                            textRes = R.string.play_again_button,
                            onClick = viewModel::onStartGame,
                            modifier = Modifier.padding(bottom = 60.dp)
                        )
                    }
                }
            }
            else -> {}
        }
    }
}

@Composable
private fun ColorToRemember(color: Color) {
    val animatedColor by animateColorAsState(
        targetValue = color,
        animationSpec = tween(durationMillis = COLOR_ANIMATION_DURATION_IN_MS),
        label = "color_to_remember_animation"
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(animatedColor)
    )
}

private fun shareResult() {
    // TODO implement capturing bitmap out of rounds result
}