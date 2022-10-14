package de.nilsdruyen.composeparty.freestyle

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Spring.StiffnessVeryLow
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun FlingAnimation() {
    val offset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                coroutineScope {
                    while (true) {
                        val position = awaitPointerEventScope {
                            awaitFirstDown().position
                        }
                        launch {
                            offset.animateTo(
                                position, spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = StiffnessVeryLow
                                )
                            )
                        }
                    }
                }
            }
    ) {
        Box(
            modifier = Modifier
                .offset { offset.value.round() }
                .size(100.dp)
                .background(Color.Red, CircleShape),
        )
    }
}