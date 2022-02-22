package de.nilsdruyen.composeparty.paths

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun VectorDemo() {
    Box(
        modifier = Modifier
            .height(200.dp)
            .background(Color.DarkGray)
    ) {
        PawIcon(Modifier.align(Alignment.Center))
    }
}