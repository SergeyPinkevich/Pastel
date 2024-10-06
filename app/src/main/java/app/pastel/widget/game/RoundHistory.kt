package app.pastel.widget.game

import androidx.compose.animation.core.Spring.StiffnessMediumLow
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.pastel.state.RoundUIState
import kotlin.math.abs
import kotlin.math.roundToInt

private val COLOR_CIRCLE_REGULAR = 72.dp
private val COLOR_CIRCLE_EXPANDED = 96.dp

private const val HORIZONTAL_THRESHOLD_TO_ANIMATE_IN_PX = 180f

@Composable
fun RoundHistory(rounds: List<RoundUIState>, modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()
    val snapFlingBehavior = rememberSnapFlingBehavior(listState)

    var isContentVisible by remember { mutableStateOf(true) }

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenWidthInPx = with(LocalDensity.current) { screenWidth.toPx() }
    val horizontalPadding = screenWidth / 2 - COLOR_CIRCLE_REGULAR / 2
    val horizontalPaddingInPx = with(LocalDensity.current) { horizontalPadding.toPx() }

    val expandedIndex by remember {
        derivedStateOf {
            listState.layoutInfo.visibleItemsInfo.find { item ->
                val itemOffsetFromCenter = abs(horizontalPaddingInPx + item.offset - (screenWidthInPx / 2).roundToInt())
                itemOffsetFromCenter < HORIZONTAL_THRESHOLD_TO_ANIMATE_IN_PX
            }?.index
        }
    }

    LazyRow(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = horizontalPadding),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        flingBehavior = snapFlingBehavior
    ) {
        itemsIndexed(rounds) { index, round ->
            RoundHistoryItem(
                round,
                isExpanded = expandedIndex == index,
                isTextVisible = isContentVisible
            )
        }
    }

    LaunchedEffect(Unit) {
        listState.scrollToItem(rounds.size / 2)
    }

    LaunchedEffect(listState.isScrollInProgress) {
        isContentVisible = !listState.isScrollInProgress
    }
}

@Composable
private fun RoundHistoryItem(round: RoundUIState, isExpanded: Boolean, isTextVisible: Boolean) {
    val contentAlpha by animateFloatAsState(if (isTextVisible) 1f else 0f, label = "score_value_alpha")
    val size by animateDpAsState(
        targetValue = if (isExpanded) COLOR_CIRCLE_EXPANDED else COLOR_CIRCLE_REGULAR,
        animationSpec = spring(stiffness = StiffnessMediumLow, visibilityThreshold = Dp.VisibilityThreshold),
        label = "round_history_item_size"
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxHeight()
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(round.color)
        )
        Text(
            text = round.score.toString(),
            style = TextStyle(color = Color.Black, fontSize = 32.sp),
            fontWeight = FontWeight.Black,
            modifier = Modifier
                .padding(vertical = 12.dp)
                .alpha(contentAlpha)
        )
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(round.guessColor)
        )
    }
}