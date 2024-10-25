package app.pastel.widget.menu

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.pastel.R
import app.pastel.state.MainMenuUIState
import app.pastel.state.SoundSettings
import app.pastel.ui.PastelTheme
import app.pastel.util.firstBaselineHeight
import app.pastel.util.startOffset
import kotlinx.coroutines.delay

private val MENU_ITEM_START_OFFSET = (-6).dp
private val SOUND_SETTINGS_START_OFFSET = 60.dp

private val SOUND_ON_COLOR = Color(0xFFB5DCCD)
private val SOUND_OFF_COLOR = Color(0xFFDBB5B5)

private const val SOUND_SETTINGS_DURATION = 1000L
private const val SOUND_SETTINGS_ANIMATION_DELAY = 200L

@Composable
fun MainMenu(
    context: Context,
    state: MainMenuUIState,
    onSoundClick: () -> Unit,
    onPlayClick: () -> Unit,
    onStatsClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PastelTheme.colors.backgroundColor)
    ) {
        MenuItem(
            textRes = R.string.main_menu_play,
            fontSize = 106.sp,
            onClick = onPlayClick,
            modifier = Modifier.startOffset(MENU_ITEM_START_OFFSET)
        )
        MenuItem(
            textRes = R.string.main_menu_statistics,
            fontSize = 106.sp,
            onClick = onStatsClick,
            modifier = Modifier.startOffset(MENU_ITEM_START_OFFSET)
        )
        SoundSettingsItem(
            context = context,
            soundSettings = state.soundSettings,
            onClick = onSoundClick
        )
        Spacer(modifier = Modifier.weight(1f))
        MenuItem(
            textRes = R.string.main_menu_about,
            fontSize = 36.sp,
            onClick = onAboutClick
        )
    }
}

@Composable
private fun MenuItem(
    @StringRes textRes: Int,
    fontSize: TextUnit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    Text(
        text = stringResource(id = textRes).uppercase(),
        style = TextStyle(color = PastelTheme.colors.textColor, fontSize = fontSize),
        fontWeight = FontWeight.Black,
        maxLines = 1,
        modifier = modifier
            .firstBaselineHeight(64.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    )
}

@Composable
private fun SoundSettingsItem(context: Context, soundSettings: SoundSettings, onClick: () -> Unit) {
    var items by remember { mutableStateOf(getSoundSettingsItems(context, soundSettings)) }
    var visibleItemCount by remember { mutableIntStateOf(0) }
    var hiddenItemCount by remember { mutableIntStateOf(0) }
    var animationStarted by remember { mutableStateOf(false) }

    LaunchedEffect(animationStarted) {
        if (animationStarted) {
            // Show items one by one
            items.forEachIndexed { index, _ ->
                delay(SOUND_SETTINGS_ANIMATION_DELAY * index)
                visibleItemCount++
                hiddenItemCount--
            }

            // Delay before starting to hide the items
            delay(SOUND_SETTINGS_DURATION)

            // Hide items one by one
            items.forEachIndexed { index, _ ->
                delay(SOUND_SETTINGS_ANIMATION_DELAY * index)
                visibleItemCount--
                hiddenItemCount++
            }

            // Animation is finished, reset the state
            animationStarted = false
            visibleItemCount = 0
            hiddenItemCount = 0
            items = emptyList()
        }
    }

    Box {
        MenuItem(
            textRes = R.string.main_menu_sound,
            fontSize = 106.sp,
            onClick = {
                onClick.invoke()
                animationStarted = true
                items = getSoundSettingsItems(context, soundSettings)
            },
            modifier = Modifier.startOffset(MENU_ITEM_START_OFFSET)
        )
        Column(modifier = Modifier.padding(start = SOUND_SETTINGS_START_OFFSET)) {
            items.forEachIndexed { index, item ->
                val alpha = animateFloatAsState(
                    targetValue = if (index in hiddenItemCount..<visibleItemCount) 1f else 0f,
                    label = ""
                )
                val textColor = if (soundSettings == SoundSettings.ON) {
                    SOUND_OFF_COLOR
                } else {
                    SOUND_ON_COLOR
                }
                Text(
                    text = item,
                    style = TextStyle(color = textColor, fontSize = 106.sp),
                    fontWeight = FontWeight.Black,
                    modifier = Modifier
                        .firstBaselineHeight(64.dp)
                        .alpha(alpha.value)
                )
            }
        }
    }
}

private fun getSoundSettingsItems(context: Context, state: SoundSettings): List<String> {
    val stringRes = if (state == SoundSettings.ON) {
        R.string.main_menu_sound_on
    } else {
        R.string.main_menu_sound_off
    }
    return context.resources.getString(stringRes).map { it.uppercase() }
}

@Preview
@Composable
private fun MainMenuPreview() {
    MainMenu(
        context = LocalContext.current,
        state = MainMenuUIState(),
        onSoundClick = {},
        onPlayClick = {},
        onStatsClick = {},
        onAboutClick = {}
    )
}