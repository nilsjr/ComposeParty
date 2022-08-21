package de.nilsdruyen.composeparty.freestyle

import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun MenuToClose() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        var showMenu by remember { mutableStateOf(true) }

        val density = LocalDensity.current
        val initialHeight = 58.dp
        val endY = with(density) { initialHeight.roundToPx().toFloat() }
        val strokeWidth = with(density) { (10.dp).roundToPx().toFloat() }

        @Composable
        fun springSpec(visibilityThreshold: Float? = null): SpringSpec<Float> =
            spring(0.4f, 500f, visibilityThreshold)

        val lineOneEndY by animateFloatAsState(
            targetValue = if (showMenu) 0f else endY,
            animationSpec = springSpec()
        )
        val lineTwoAlpha by animateFloatAsState(
            targetValue = if (showMenu) 1f else 0f,
            animationSpec = springSpec(0f)
        )
        val lineTwoScale by animateFloatAsState(
            targetValue = if (showMenu) 1f else 0f,
            animationSpec = springSpec()
        )
        val lineThreeEndY by animateFloatAsState(
            targetValue = if (showMenu) endY else 0f,
            animationSpec = springSpec()
        )

        Canvas(
            Modifier
                .size(height = initialHeight, width = 64.dp)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { showMenu = !showMenu })
                }
        ) {
            val (width, height) = size

            fun line(start: Offset, end: Offset, alpha: Float = 1f) {
                drawLine(
                    color = Color.White,
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round,
                    start = start,
                    end = end,
                    alpha = alpha
                )
            }
            line(
                start = Offset(x = 0f, y = 0f),
                end = Offset(x = width, y = lineOneEndY)
            )
            withTransform({
                scale(lineTwoScale, Offset(x = 0f, y = height / 2))
            }) {
                line(
                    start = Offset(x = 0f, y = height / 2),
                    end = Offset(x = width, y = height / 2),
                    alpha = lineTwoAlpha
                )
            }
            line(
                start = Offset(x = 0f, y = height),
                end = Offset(x = width, y = lineThreeEndY)
            )
        }
    }
}