package de.nilsdruyen.composeparty.animations

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.paths.PawAnimatedIcon
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProgressAnimationSample(modifier: Modifier = Modifier) {
    var progress by remember { mutableFloatStateOf(0f) }
    var isAnimating by remember { mutableStateOf(false) }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        finishedListener = {
            isAnimating = false
        },
    )

    Box(
        modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        PawAnimatedIcon(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(250.dp),
            color = Color.DarkGray
        )
//        PawIcon(
//            progress = progress,
//            modifier = Modifier
//                .align(Alignment.Center)
//                .size(450.dp),
//            color = Color.DarkGray
//        )
        Text(
            text = "progress: $progress",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(64.dp)
        )
        Slider(
            value = progress,
            onValueChange = {
                isAnimating = true
                progress = it
            },
            valueRange = 0f..1f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .align(Alignment.BottomCenter)
        )
        Column(Modifier.align(Alignment.Center)) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            )
            Spacer(Modifier.size(4.dp))
            LinearWavyProgressIndicator(
                progress = { animatedProgress },
                amplitude = {
                    if (isAnimating) {
                        1f
                    } else {
                        0f
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            )
            Spacer(Modifier.size(4.dp))
            CircularWavyProgressIndicator(
                { animatedProgress },
                Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.size(4.dp))
        }

    }
}

@Preview
@Composable
private fun ProgressAnimationSamplePreview() {
    ComposePartyTheme {
        Scaffold {
            ProgressAnimationSample(Modifier.padding(it))
        }
    }
}