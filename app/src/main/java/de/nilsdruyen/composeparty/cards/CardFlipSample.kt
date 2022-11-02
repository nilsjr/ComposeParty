package de.nilsdruyen.composeparty.cards

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CardFlipSample() {
    val front: @Composable () -> Unit = {
        Text(
            text = "Front", Modifier
                .fillMaxSize()
                .background(Color.Green)
        )
    }
    val back: @Composable () -> Unit = {
        Text(
            text = "Back", Modifier
                .fillMaxSize()
                .background(Color.Red)
        )
    }

    var flip by remember { mutableStateOf(false) }
    val rotation = animateFloatAsState(
        targetValue = if (flip) 180f else 0f,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing,
        )
    )
    Card(
        onClick = {
            flip = !flip
        },
        modifier = Modifier
            .padding(vertical = 64.dp, horizontal = 8.dp)
            .graphicsLayer {
                rotationY = rotation.value
                cameraDistance = 12f * density
            },
    ) {
        if (rotation.value <= 90f) {
            Box(Modifier.fillMaxSize()) {
                front()
            }
        } else {
            Box(
                Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        rotationY = 180f
                    },
            ) {
                back()
            }
        }
    }
}