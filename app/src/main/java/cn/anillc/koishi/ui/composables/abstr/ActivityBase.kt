package cn.anillc.koishi.ui.composables.abstr

import androidx.compose.runtime.Composable
import cn.anillc.koishi.ui.theming.KoishiTheme

@Composable
fun ActivityBase(
    content: @Composable () -> Unit
) {
    KoishiTheme {
        DefaultSurface(content = content)
    }
}
