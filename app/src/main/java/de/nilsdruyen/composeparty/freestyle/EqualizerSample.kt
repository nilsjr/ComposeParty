package de.nilsdruyen.composeparty.freestyle

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun EqualizerSample() {
    Box(
        Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Equalizer(8)
        Box(
            modifier = Modifier
                .padding(bottom = 32.dp)
                .size(160.dp)
                .clip(CircleShape)
                .background(Color.Red)
                .align(Alignment.BottomCenter)
        ) {
            Equalizer(3)
        }
    }
}

@Composable
fun Equalizer(barCount: Int = 3) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(1.7f)
            .padding(16.dp)
    ) {
        repeat(barCount) {
            Bar(
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .weight(1f)
                    .fillMaxWidth(0.8f)
            )
        }
    }
}

@Composable
fun Bar(modifier: Modifier = Modifier) {
    var fraction by remember { mutableStateOf(Random.nextFloat()) }
    val animFraction = animateFloatAsState(
        targetValue = fraction,
        animationSpec = tween(durationMillis = 250, easing = LinearEasing)
    ) {
        fraction = Random.nextFloat()
    }

    LaunchedEffect(Unit) {
        fraction = Random.nextFloat()
    }

    Box(
        modifier = modifier
            .padding(1.dp)
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxHeight(fraction = animFraction.value)
    )
}