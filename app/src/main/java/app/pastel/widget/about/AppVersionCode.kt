package app.pastel.widget.about

import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.pastel.R
import app.pastel.ui.PastelTheme

@Composable
fun AppVersionCode() {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0))
    } else {
        packageManager.getPackageInfo(context.packageName, 0)
    }
    Text(
        text = stringResource(id = R.string.version_text).uppercase() + " ${packageInfo.versionName}",
        fontSize = 32.sp,
        fontWeight = FontWeight.Black,
        modifier = Modifier.padding(bottom = 16.dp),
        color = PastelTheme.colors.textColor
    )
}