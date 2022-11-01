package de.nilsdruyen.composeparty.animations

import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateTo
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.paths.TWO_PI
import de.nilsdruyen.composeparty.utils.lerp
import de.nilsdruyen.composeparty.utils.map
import glm_.glm
import glm_.glm.sin
import kotlinx.coroutines.isActive

private val minDotRad: Float = 2f
private val maxDotRad: Float = 10f
private val xAbsVariance: Float = 10f
private val yAbsVariance: Float = 10f
private val xFreq: Float = 1f
private val yFreq: Float = 2f
private val hueMax: Float = 200f
private val hueMin: Float = 90f
private val hueSat: Float = 60f
private val hueValue: Float = 100f

@Composable
fun DynamicHueSample() {
    DynamicHueSample(Modifier.fillMaxSize())
}

@Composable
fun DynamicHueSample(modifier: Modifier = Modifier) {
    val advance = remember { AnimationState(0f) }

    LaunchedEffect(Unit) {
        while (isActive) {
            advance.animateTo(
                targetValue = advance.value + .1f,
                animationSpec = tween(7000, 50, easing = LinearEasing),
                sequentialAnimation = true
            )
        }
    }

    Canvas(modifier = modifier) {
        drawDynamicHue(
            time = advance.value,
            size = size,
            dotCount = 45,
        )
    }
}

fun DrawScope.drawDynamicHue(
    time: Float,
    size: Size,
    dotCount: Int,
) {
    val rel = size.width / size.height
    val verticalDotCount = (dotCount / rel).toInt()
    (0 until dotCount).forEach { x ->
        (0 until verticalDotCount).forEach { y ->
            val u = x / (dotCount - 1).toFloat()
            val v = y / (verticalDotCount - 1).toFloat()

            val posX = lerp(min = 0f, max = size.width, norm = u)
            val posY = lerp(min = 0f, max = size.height, norm = v)

            val effectiveRad = map(
                (sin((v + time * 20) * TWO_PI) + glm.cos((u + time * 20) * TWO_PI)).toFloat(),
                -2f,
                2f,
                minDotRad,
                maxDotRad
            )
            val shiftedX = posX + map(
                sin((u * xFreq + time * 10) * TWO_PI).toFloat(),
                -1f,
                1f,
                -xAbsVariance,
                xAbsVariance
            )
            val shiftedY = posY + map(
                glm.cos((v * yFreq + time * 10) * TWO_PI).toFloat(),
                -1f,
                1f,
                -yAbsVariance,
                yAbsVariance
            )
            val color = Color.hsv(
                hue = map(
                    value = effectiveRad,
                    sourceMin = minDotRad,
                    sourceMax = maxDotRad,
                    destMin = hueMax,
                    destMax = hueMin
                ),
                saturation = hueSat / 100f,
                value = hueValue / 100f
            )
            drawCircle(
                color = color,
                radius = effectiveRad,
                center = Offset(shiftedX, shiftedY)
            )
        }
    }
}

@Preview
@Composable
fun PreviewDynamicHueSample() {
    DynamicHueSample(Modifier.fillMaxSize())
}

@Preview
@Composable
fun PreviewDynamicHueSample2() {
    DynamicHueSample(
        Modifier
            .width(400.dp)
            .aspectRatio(1.8f)
    )
}