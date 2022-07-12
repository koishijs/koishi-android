package cn.anillc.koishi.activities


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
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

data class Tab(val route: String, val content: @Composable () -> Unit)

@Composable
fun StartCompose() {
    KoishiTheme {
        StartScaffold()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScaffold() {
    val colorPrimary = MaterialTheme.colorScheme.primary

    val tabs by remember {
        mutableStateOf(
            arrayOf(
                Tab("welcome") { TabWelcome() },
                Tab("request_permission") { TabRequestPermission() }
            )
        )
    }
    val firstRoute by remember { mutableStateOf(tabs.first().route) }

    // Navigation control
    val nav = rememberNavController()
    var currentRoute by remember { mutableStateOf(firstRoute) }

    DisposableEffect(LocalLifecycleOwner.current) {
        val listener = NavController.OnDestinationChangedListener { _, navDestination, _ ->
            navDestination.route?.let { currentRoute = it }
        }

        nav.addOnDestinationChangedListener(listener)
        onDispose { nav.removeOnDestinationChangedListener(listener) }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .drawBehind {
                        drawLine(
                            colorPrimary,
                            Offset(0f, 0f),
                            Offset(size.width, 0f),
                            1 * density // 1dp
                        )
                    },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                tonalElevation = Dp(0f)
            ) {
                AnimatedVisibility(currentRoute != firstRoute) {
                    TextButton(
                        onClick = { nav.navigateUp() }
                    ) {
                        Text(stringResource(R.string.koishi_common_prev))
                    }
                }
                Spacer(Modifier.weight(1f, true))
                AnimatedVisibility(currentRoute != tabs.last().route) {
                    TextButton(
                        onClick = {
                            nav.navigate(tabs[tabs.indexOfFirst { it.route == currentRoute } + 1].route)
                        }
                    ) {
                        Text(stringResource(R.string.koishi_common_next))
                    }
                }
            }
        }
    ) {
        Box(Modifier.fillMaxSize()) {
            AccentBackground(Modifier.align(Alignment.Center))
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = nav,
                startDestination = firstRoute
            ) {
                tabs.forEach { tab ->
                    composable(tab.route) { tab.content() }
                }
            }
        }
    }
}

@Composable
fun TabWelcome() {
    // TODO
}

@Composable
fun TabRequestPermission() {
    // TODO
}

@Preview
@Composable
fun StartComposePreview() {
    StartCompose()
}
