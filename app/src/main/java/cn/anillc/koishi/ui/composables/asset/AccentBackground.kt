package cn.anillc.koishi.ui.composables.asset

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import cn.anillc.koishi.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AccentBackground(
    modifier: Modifier = Modifier
) {
    val visibleState = remember {
        MutableTransitionState(false).apply {
            // Start the animation immediately.
            targetState = true
        }
    }

    val infiniteTransition = rememberInfiniteTransition()
    val size by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val scaleSpring = spring<Float>(stiffness = 50f)

    Box(modifier) {
        AnimatedVisibility(
            visibleState = visibleState,
            enter = scaleIn(scaleSpring),
            exit = scaleOut(scaleSpring)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_koishi_background),
                contentDescription = "Koishi Background",
                modifier = Modifier
                    .alpha(0.1f)
                    .align(Alignment.Center)
                    .fillMaxSize(size)
            )
        }
    }
}

@Preview
@Composable
fun AccentBackgroundPreview() {
    AccentBackground(Modifier.fillMaxSize())
}
