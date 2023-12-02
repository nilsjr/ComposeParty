package de.nilsdruyen.composeparty.freestyle

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import de.nilsdruyen.composeparty.utils.hapticFeedback
import de.nilsdruyen.composeparty.utils.rememberShakeState

@Composable
fun ShakeSample() {
    val view = LocalView.current
    val isShaked by rememberShakeState()
    val color by animateColorAsState(targetValue = if (isShaked) Color.Yellow else Color.Black)

    LaunchedEffect(isShaked) {
        if (isShaked) view.hapticFeedback()
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(color))
}