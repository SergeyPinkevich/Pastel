package app.pastel.widget.about

import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.pastel.util.firstBaselineHeight

private val LINK_COLOR = Color(0xFF3083F0)

@Composable
fun LinkItem(@StringRes resId: Int, url: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    Text(
        text = stringResource(id = resId).uppercase(),
        fontSize = 32.sp,
        fontWeight = FontWeight.Black,
        modifier = modifier
            .firstBaselineHeight(24.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(url)
                }
                context.startActivity(intent)
            },
        color = LINK_COLOR
    )
}