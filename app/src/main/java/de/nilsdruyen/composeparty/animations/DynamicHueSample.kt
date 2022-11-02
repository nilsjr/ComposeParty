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

data class DynamicHueSpec(
    val dotCount: Int = 25,
    val minDotRad: Float = 2f,
    val maxDotRad: Float = 10f,
    val xAbsVariance: Float = 10f,
    val yAbsVariance: Float = 10f,
    val xFreq: Float = 1f,
    val yFreq: Float = 2f,
    val hueMin: Float = 160f,
    val hueMax: Float = 360f,
    val hueSat: Float = 70f,
    val hueValue: Float = 100f,
)

@Composable
fun DynamicHueSample() {
    DynamicHueSample(modifier = Modifier.fillMaxSize())
}

@Composable
fun DynamicHueSample(
    modifier: Modifier = Modifier,
    speed: Float = .1f,
    spec: DynamicHueSpec = DynamicHueSpec()
) {
    val advance = remember { AnimationState(0f) }

    LaunchedEffect(Unit) {
        while (isActive) {
            advance.animateTo(
                targetValue = advance.value + speed,
                animationSpec = tween(7000, 50, easing = LinearEasing),
                sequentialAnimation = true
            )
        }
    }

    Canvas(modifier = modifier) {
        drawDynamicHue(
            time = advance.value,
            size = size,
            spec = spec,
        )
    }
}

fun DrawScope.drawDynamicHue(
    time: Float,
    size: Size,
    spec: DynamicHueSpec,
) {
    val rel = size.width / size.height
    val verticalDotCount = (spec.dotCount / rel).toInt()
    (0 until spec.dotCount).forEach { x ->
        (0 until verticalDotCount).forEach { y ->
            val u = x / (spec.dotCount - 1).toFloat()
            val v = y / (verticalDotCount - 1).toFloat()

            val posX = lerp(min = 0f, max = size.width, norm = u)
            val posY = lerp(min = 0f, max = size.height, norm = v)

            val effectiveRad = map(
                (sin((v + time * 20) * TWO_PI) + glm.cos((u + time * 20) * TWO_PI)).toFloat(),
                -2f,
                2f,
                spec.minDotRad,
                spec.maxDotRad
            )
            val shiftedX = posX + map(
                sin((u * spec.xFreq + time * 10) * TWO_PI).toFloat(),
                -1f,
                1f,
                -spec.xAbsVariance,
                spec.xAbsVariance
            )
            val shiftedY = posY + map(
                glm.cos((v * spec.yFreq + time * 10) * TWO_PI).toFloat(),
                -1f,
                1f,
                -spec.yAbsVariance,
                spec.yAbsVariance
            )
            val color = Color.hsv(
                hue = map(
                    value = effectiveRad,
                    sourceMin = spec.minDotRad,
                    sourceMax = spec.maxDotRad,
                    destMin = spec.hueMin,
                    destMax = spec.hueMax,
                ),
                saturation = spec.hueSat / 100f,
                value = spec.hueValue / 100f
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
    DynamicHueSample(modifier = Modifier.fillMaxSize())
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