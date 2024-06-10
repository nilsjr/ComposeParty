package de.nilsdruyen.composeparty.paths

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.IntOffset
import de.nilsdruyen.composeparty.modifiers.pulsing
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun FavoriteIconAnimationSample(modifier: Modifier = Modifier) {
    var isFavorite by remember { mutableStateOf(false) }
    var isPulsing by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

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

    LaunchedEffect(isLoading) {
        if (isLoading) {
            isPulsing = true
            delay(2_500)
            isPulsing = false
            isLoading = false
            isFavorite = !isFavorite
        }
    }

    Column(
        modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        IconButton(
            onClick = { isLoading = true },
            modifier = Modifier
        ) {
            Crossfade(isFavorite, label = "crossFade") {
                Icon(
                    imageVector = if (it) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    modifier = Modifier
                        .scale(scale)
                        .offset { IntOffset(0, position.roundToInt()) }
                )
            }
        }
        IconButton(
            onClick = { isLoading = true },
            modifier = Modifier
        ) {
            Crossfade(isFavorite, label = "crossFade") {
                Icon(
                    imageVector = if (it) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    modifier = Modifier.pulsing(isLoading)
                )
            }
        }
    }
}