package de.nilsdruyen.composeparty.animations

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntOffset
import androidx.compose.animation.core.keyframesWithSpline
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import de.nilsdruyen.composeparty.R
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Stable
inline fun Offset.round(): IntOffset = IntOffset(x.roundToInt(), y.roundToInt())

private val duration = 800
private val delay = 0
private val duration2 = 800
private val delay2 = 0

@Preview
@Composable
fun CardAnimation(modifier: Modifier = Modifier) {
    var hide by remember { mutableStateOf(false) }

    // card1 red
    var transitionState by remember {
        mutableStateOf(MutableTransitionState(hide))
    }
    val transition = rememberTransition(transitionState)
    val animatedPosition by transition.animateIntOffset(transitionSpec = {
        when {
            false isTransitioningTo true -> {
                keyframesWithSpline {
                    durationMillis = duration
                    IntOffset(0, 0) at 0
                    IntOffset(0, 0) at 100
                    IntOffset(0, 150) at 225
                    IntOffset(0, 250) at 350
                }
            }

            else -> {
                tween(duration)
            }
        }
    }) {
        if (it) IntOffset(0, 200) else IntOffset(0, 0)
    }
    val animatedScale by transition.animateFloat(transitionSpec = { tween(duration) }) {
        if (it) 0.5f else 1f
    }
    val animatedAlpha by transition.animateFloat(transitionSpec = {
        tween(
            500,
            delayMillis = 500,
        )
    }) {
        if (it) 0f else 1f
    }

    // card2 blue
    var transitionState2 by remember {
        mutableStateOf(MutableTransitionState(hide))
    }
    val transition2 = rememberTransition(transitionState2)
    val animatedPosition2 by transition2.animateIntOffset(transitionSpec = {
        when {
            false isTransitioningTo true -> {
                keyframesWithSpline {
                    durationMillis = duration2
                    delayMillis = delay2
                    IntOffset(0, 200) at 0
                    IntOffset(0, 50) at 250
                    IntOffset(0, -150) at 500
                }
            }

            else -> {
                tween(duration2)
            }
        }
    }) {
        if (it) IntOffset(0, 0) else IntOffset(0, 200)
    }
    val animatedScale2 by transition2.animateFloat(transitionSpec = {
        tween(
            duration2,
            delayMillis = delay2
        )
    }) {
        if (it) 1f else .3f
    }

    LaunchedEffect(hide) {
        delay(2000L)
        if (hide) {
            hide = false
            transitionState.targetState = true
            transitionState2.targetState = true
        } else {
            hide = true
            transitionState.targetState = false
            transitionState2.targetState = false
        }
    }

    Scaffold(modifier = modifier) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Box(Modifier.border(1.dp, color = Color.Green)) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(32.dp)
                        .graphicsLayer {
                            translationX = animatedPosition2.x.toFloat()
                            translationY = animatedPosition2.y.toFloat()
                            scaleX = animatedScale2
                            scaleY = animatedScale2
                        },
                    colors = CardDefaults.cardColors(containerColor = Color.Blue),
                ) {}
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(32.dp)
                        .graphicsLayer {
                            translationX = animatedPosition.x.toFloat()
                            translationY = animatedPosition.y.toFloat()
                            scaleX = animatedScale
                            scaleY = animatedScale
                            alpha = animatedAlpha
                        },
                    colors = CardDefaults.cardColors(containerColor = Color.Red),
                ) {}
            }
            Box {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.dog_walking))
                val progress by animateLottieCompositionAsState(
                    composition,
                    iterations = LottieConstants.IterateForever,
                    speed = 1f,
                )
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier
                        .size(170.dp)
                        .border(1.dp, color = Color.Green)
                )
            }
            Spacer(Modifier.size(100.dp))
//            Box {
//                AnimatedContent(hide, transitionSpec = {
//                    if (targetState) {
//                        expandVertically { height -> height/2 } togetherWith
//                                slideOutVertically { height -> -height/2 }
//                    } else {
//                        slideInVertically { height -> -height } togetherWith
//                                slideOutVertically { height -> height }
//                    } using SizeTransform(clip = false) { initialSize, targetSize ->
//                        if (targetState) {
//                            keyframes {
//                                // Expand horizontally first.
//                                IntSize(targetSize.width, initialSize.height) at 0
//                                IntSize(targetSize.width / 2, initialSize.height / 2) at 500
//                                IntSize(targetSize.width, initialSize.height) at 1000
//                                durationMillis = 1000
//                            }
//                        } else {
//                            keyframes {
//                                // Shrink vertically first.
//                                IntSize(targetSize.width, initialSize.height) at 0
//                                IntSize(targetSize.width / 2, initialSize.height / 2) at 500
//                                IntSize(targetSize.width, initialSize.height) at 1000
//                                durationMillis = 1000
//                            }
//                        }
//                    }
//                }) {
//                    if (it) {
//                        Card(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(200.dp)
//                                .padding(32.dp),
//                            colors = CardDefaults.cardColors(containerColor = Color.Blue),
//                        ) {}
//                    } else {
//                        Card(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(200.dp)
//                                .padding(32.dp),
//                            colors = CardDefaults.cardColors(containerColor = Color.Red),
//                        ) {}
//                    }
//                }
//            }
        }
    }
}