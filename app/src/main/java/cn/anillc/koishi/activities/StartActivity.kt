package cn.anillc.koishi.activities


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cn.anillc.koishi.R
import cn.anillc.koishi.ui.composables.asset.AccentBackground
import cn.anillc.koishi.ui.theming.KoishiTheme

class StartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StartCompose()
        }
    }
}

val LocalNavHostController = compositionLocalOf<NavHostController?> { null }

@Composable
fun StartCompose() {
    // Navigation control
    val nav = rememberNavController()

    // Content
    KoishiTheme {
        CompositionLocalProvider(
            LocalNavHostController provides nav
        ) {
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = nav,
                startDestination = "welcome"
            ) {
                composable("welcome") { TabWelcome() }
                composable("request_permission") { TabRequestPermission() }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabScaffold(
    enabled: Boolean = true,
    prev: Boolean = true,
    next: String = "",
    content: @Composable () -> Unit
) {
    val colorSurface = MaterialTheme.colorScheme.surface

    val nav = LocalNavHostController.current!!

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .drawBehind {
                        drawLine(
                            colorSurface,
                            Offset(0f, 0f),
                            Offset(size.width, 0f),
                            1 * density // 1dp
                        )
                    },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                tonalElevation = Dp(0f)
            ) {
                if (prev) {
                    TextButton(
                        enabled = enabled,
                        onClick = { nav.navigateUp() }
                    ) {
                        Text(stringResource(R.string.koishi_common_prev))
                    }
                }
                Spacer(Modifier.weight(1f, true))
                if (next.isNotEmpty()) {
                    TextButton(
                        enabled = enabled,
                        onClick = { nav.navigate(next) }
                    ) {
                        Text(stringResource(R.string.koishi_common_next))
                    }
                }
            }
        }
    ) {
        Box(Modifier.fillMaxSize()) {
            AccentBackground(Modifier.align(Alignment.Center))
            content()
        }
    }
}

@Composable
fun TabWelcome() {
    TabScaffold(
        next = "request_permission",
        prev = false
    ) {
        // TODO
    }
}

@Composable
fun TabRequestPermission() {
    TabScaffold {
        // TODO
    }
}

@Preview
@Composable
fun StartComposePreview() {
    StartCompose()
}
