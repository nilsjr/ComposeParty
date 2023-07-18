package de.nilsdruyen.composeparty.animations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Text
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.paths.PawAnimatedIcon
import de.nilsdruyen.composeparty.paths.PawIcon

@Composable
fun ProgressAnimationSample() {
    var progress by remember { mutableFloatStateOf(0f) }

    Box(
        Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        PawAnimatedIcon(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(250.dp),
            color = Color.DarkGray
        )
        PawIcon(
            progress = progress,
            modifier = Modifier
                .align(Alignment.Center)
                .size(450.dp),
            color = Color.DarkGray
        )
        Text(
            text = "progress: $progress",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(64.dp)
        )
        Slider(
            value = progress,
            onValueChange = { progress = it },
            valueRange = 0f..1f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .align(Alignment.BottomCenter)
        )
    }
}