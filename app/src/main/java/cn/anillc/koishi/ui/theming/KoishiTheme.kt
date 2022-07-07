package cn.anillc.koishi.ui.theming

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

//region Color Schemes

private val LightColors = lightColorScheme(
    primary = Color(0xffc079f2),
    primaryContainer = Color(0xff843fb5)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xffc079f2),
    primaryContainer = Color(0xff843fb5)
)

//endregion

@Composable
fun KoishiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}
