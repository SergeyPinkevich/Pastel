package app.pastel.screen.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import app.pastel.R
import app.pastel.navigation.Screen
import app.pastel.ui.PastelTheme
import app.pastel.util.firstBaselineHeight
import app.pastel.widget.about.LinkItem

private val ABOUT_TITLE_COLOR = Color(0xFF4ABD7E)

private const val ABOUT_LINK = "https://work.antonandirene.com/colormatch/2/"
private const val PRIVACY_POLICY_LINK = "https://html-preview.github.io/?url=https://raw.githubusercontent.com/SergeyPinkevich/Pastel/refs/heads/main/app/src/main/assets/privacy_policy.html"
private const val TERMS_OF_SERVICE_LINK = "https://html-preview.github.io/?url=https://raw.githubusercontent.com/SergeyPinkevich/Pastel/refs/heads/main/app/src/main/assets/terms_of_service.html"

@Composable
fun AboutScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = PastelTheme.colors.backgroundColor)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stringResource(id = R.string.about_title).uppercase(),
                fontSize = 84.sp,
                fontWeight = FontWeight.Black,
                color = ABOUT_TITLE_COLOR
            )
            CloseButton(navController)
        }
        Column {
            Text(
                text = stringResource(id = R.string.about_disclaimer).uppercase(),
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier
                    .padding(top = 32.dp, bottom = 16.dp, end = 16.dp)
                    .firstBaselineHeight(24.dp),
                color = PastelTheme.colors.textColor
            )
            LinkItem(resId = R.string.about_link, url = ABOUT_LINK)
            Spacer(modifier = Modifier.weight(1f))
            LinkItem(
                resId = R.string.privacy_policy,
                url = PRIVACY_POLICY_LINK,
            )
            LinkItem(
                resId = R.string.terms_of_service,
                url = TERMS_OF_SERVICE_LINK,
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }
    }
}

@Composable
private fun CloseButton(navController: NavController) {
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
            contentDescription = null,
            tint = PastelTheme.colors.textColor
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun AboutScreenPreview() {
    AboutScreen(navController = NavController(LocalContext.current))
}