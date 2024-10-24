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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import app.pastel.state.GameUIState
import app.pastel.state.RoundUIState
import app.pastel.ui.PastelTheme
import app.pastel.widget.game.ColorPalette
import app.pastel.widget.game.CountdownAnimation
import app.pastel.widget.game.RoundHistory
import app.pastel.widget.game.RoundInformation
import app.pastel.widget.game.TextButton
import kotlinx.coroutines.delay

const val COLOR_ANIMATION_DURATION_IN_MS = 1500
private const val TUTORIAL_DELAY_IN_MS = 4000L

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
            GameState.REMEMBER -> RememberScreen(uiState, currentRound, viewModel)
            GameState.GUESS -> GuessScreen(uiState, currentRound, viewModel)
            GameState.ROUND_RESULT -> RoundResultScreen(uiState, currentRound, viewModel)
            GameState.ROUND_FINISHED -> RoundFinishedScreen(uiState, currentRound, viewModel)
            GameState.FINISHED -> GameFinishedScreen(uiState, viewModel, navController)
            else -> {}
        }
    }
}

@Composable
private fun RememberScreen(
    uiState: GameUIState,
    currentRound: RoundUIState,
    viewModel: GameViewModel
) {
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

@Composable
private fun GuessScreen(
    uiState: GameUIState,
    currentRound: RoundUIState,
    viewModel: GameViewModel
) {
    var showTutorial by remember { mutableStateOf(false) }
    LaunchedEffect(showTutorial) {
        if (!showTutorial) {
            delay(TUTORIAL_DELAY_IN_MS)
            showTutorial = true
        }
    }
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
            onColorConfirm = viewModel::onColorConfirm,
            showTutorial = showTutorial,
            onTouch = { showTutorial = false }
        )
    }
}

@Composable
private fun RoundResultScreen(
    uiState: GameUIState,
    currentRound: RoundUIState,
    viewModel: GameViewModel
) {
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
            style = TextStyle(color = PastelTheme.colors.textColor, fontSize = 32.sp),
            fontWeight = FontWeight.Black,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 36.dp)
        )
    }
}

@Composable
private fun RoundFinishedScreen(
    uiState: GameUIState,
    currentRound: RoundUIState,
    viewModel: GameViewModel
) {
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

@Composable
private fun GameFinishedScreen(
    uiState: GameUIState,
    viewModel: GameViewModel,
    navController: NavController
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = PastelTheme.colors.backgroundColor)) {
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
                contentDescription = null,
                tint = PastelTheme.colors.textColor
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = uiState.totalScore.toString(),
                style = TextStyle(PastelTheme.colors.textColor, fontSize = 128.sp),
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