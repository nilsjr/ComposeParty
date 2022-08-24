package de.nilsdruyen.composeparty.paths

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun PathPawAnimation() {
    Box(modifier = Modifier.fillMaxSize()) {
        PawIcon(
            modifier = Modifier.align(Alignment.Center),
            color = Color.DarkGray
        )
    }
}