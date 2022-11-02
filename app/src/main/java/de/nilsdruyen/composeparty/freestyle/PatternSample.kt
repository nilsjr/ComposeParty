package de.nilsdruyen.composeparty.freestyle

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.animations.DynamicHueSpec
import de.nilsdruyen.composeparty.paths.TWO_PI
import de.nilsdruyen.composeparty.utils.lerp
import de.nilsdruyen.composeparty.utils.map
import glm_.glm

@Preview
@Composable
fun PatternSample() {
    Column {
        PatternCard()
    }
}

@Composable
fun PatternCard() {
    val spec = DynamicHueSpec(
        dotCount = 10,
        minDotRad = 2f,
        maxDotRad = 4f,
        xAbsVariance = 1f,
        yAbsVariance = 2f,
        xFreq = 1f,
        yFreq = 1f,
        hueMin = 70f,
        hueMax = 360f,
        hueSat = 70f,
        hueValue = 100f
    )
    val time = 1f
    val count = 20
    val countRange = 0 until count
    val sizeRange = 5..15
    val dots = remember { Array(count) { IntArray(count) } }
    countRange.forEach { x ->
        countRange.forEach { y ->
            dots[x][y] = sizeRange.random()
        }
    }

    val gradientColors = listOf(Color.Blue, Color.DarkGray)
    val brush = Brush.linearGradient(gradientColors)

    Box(
        Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .aspectRatio(1.5f)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(32.dp))
                .background(brush)
                .graphicsLayer {
                    rotationZ = 30f
                }
        ) {
            dots.forEachIndexed { x, ints ->
                ints.forEachIndexed { y, dotSize ->
                    val u = x / (countRange.last).toFloat()
                    val v = y / (countRange.last).toFloat()
                    val posX = lerp(min = 0f - 200f, max = size.width * 1.4f, norm = u)
                    val posY = lerp(min = 0f - 200f, max = size.height * 1.4f, norm = v)

                    val effectiveRad = map(
                        (glm.sin((v + time * 20) * TWO_PI) + glm.cos((u + time * 20) * TWO_PI)).toFloat(),
                        -2f,
                        2f,
                        spec.minDotRad,
                        spec.maxDotRad
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
                        radius = dotSize * 2f,
                        center = Offset(posX, posY),
                        blendMode = BlendMode.Difference,
                    )
                }
            }
        }
    }
}