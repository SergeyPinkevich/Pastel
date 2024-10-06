package app.pastel.screen.stats

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import app.pastel.R
import app.pastel.navigation.Screen
import app.pastel.util.firstBaselineHeight
import app.pastel.util.startOffset

private val STATS_TITLE_START_OFFSET = (-6).dp

private const val COUNT_ANIMATION_IN_MS = 1000

private val STATS_TITLE_COLOR = Color(0xFF4E9CCC)
private val HIGHEST_SCORE_COLOR = Color(0xFF4ABD7E)
private val GAMES_PLAYED_COLOR = Color(0xFFD4CC69)

@Suppress("LongMethod")
@Composable
fun StatsScreen(navController: NavController, viewModel: StatsViewModel = hiltViewModel()) {
    val uiState by viewModel.state.collectAsState()

    var highestScoreValue by remember { mutableStateOf(0) }
    val animatedHighestScoreValue by animateIntAsState(
        targetValue = highestScoreValue,
        animationSpec = tween(durationMillis = COUNT_ANIMATION_IN_MS),
        label = "highest_score_animation"
    )
    var gamesPlayedValue by remember { mutableStateOf(0) }
    val animatedGamesPlayedValue by animateIntAsState(
        targetValue = gamesPlayedValue,
        animationSpec = tween(durationMillis = COUNT_ANIMATION_IN_MS),
        label = "games_played_animation"
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stringResource(id = R.string.stats_title).uppercase(),
                fontSize = 84.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.startOffset(STATS_TITLE_START_OFFSET),
                color = STATS_TITLE_COLOR
            )
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
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
            }
        }
        Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(
                text = stringResource(id = R.string.stats_highest_score).uppercase(),
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .firstBaselineHeight(24.dp),
                color = Color.Black
            )
            Text(
                text = animatedHighestScoreValue.toString(),
                fontSize = 84.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.firstBaselineHeight(56.dp),
                color = HIGHEST_SCORE_COLOR
            )
            Text(
                text = stringResource(id = R.string.stats_games_played).uppercase(),
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .firstBaselineHeight(24.dp),
                color = Color.Black
            )
            Text(
                text = animatedGamesPlayedValue.toString(),
                fontSize = 84.sp,
                fontWeight = FontWeight.Black,
                color = GAMES_PLAYED_COLOR
            )
        }
        LazyColumn(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            contentPadding = PaddingValues(vertical = 48.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(uiState.previousGames) { item ->
                Row {
                    Text(
                        text = item.date,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black
                    )
                    Text(
                        text = item.score.toString(),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 36.dp)
                    )
                }
            }
        }
    }

    LaunchedEffect(uiState.highestScore) {
        highestScoreValue = uiState.highestScore
        gamesPlayedValue = uiState.gamesPlayed
    }
}

@Preview(showBackground = true)
@Composable
private fun StatsScreenPreview() {
    StatsScreen(navController = NavController(LocalContext.current))
}