package de.nilsdruyen.composeparty.modifiers

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

@Composable
fun Modifier.pulsing(isLoading: Boolean): Modifier {
    val scale by animateFloatAsState(
        targetValue = if (isLoading) 1.2f else 1f,
        animationSpec = if (isLoading) infiniteRepeatable(
            animation = tween(300),
            repeatMode = RepeatMode.Reverse
        ) else tween(300),
        label = "pulseAnimation",
    )

    val position by animateFloatAsState(
        targetValue = if (isLoading) -4f else 0f,
        animationSpec = if (isLoading) infiniteRepeatable(
            animation = tween(300),
            repeatMode = RepeatMode.Reverse
        ) else tween(300),
        label = "pulseAnimation",
    )
    return this
        .scale(scale)
        .offset { IntOffset(0, position.roundToInt()) }
}
