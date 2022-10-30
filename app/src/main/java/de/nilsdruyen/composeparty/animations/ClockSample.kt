package de.nilsdruyen.composeparty.animations

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import timber.log.Timber
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Preview(showBackground = true, showSystemUi = false, backgroundColor = 0xFFFFFFFF)
@Composable
fun ClockSample() {
    val infiniteTransition = rememberInfiniteTransition()
    val fraction by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val pointerFraction = (fraction - 1f).absoluteValue
    val angle = (fraction % 1f) * 360f
    val shrink = fraction < 1f
    val boxAmount = 12
    val angleStep = 360f / boxAmount
    val startAtFraction = 1f / boxAmount
    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.Center)
        ) {
            val halfWidth = size.width / 2
            val halfHeight = size.height / 2
            val start = Offset(halfWidth, halfHeight)
            val end = Offset(
                x = (halfWidth * pointerFraction) * cos((angle).toRad()) + size.center.x,
                y = (halfHeight * pointerFraction) * sin((angle).toRad()) + size.center.y,
            )
            val boxes = buildList {
                repeat(boxAmount) {
                    val tickAngle = minOf(angle, it * angleStep)
                    val tickStartAt = 1f - startAtFraction * it
                    val alpha = if (shrink) {
                        if (tickAngle < (angle + 30f)) 1f else .3f
                    } else {
                        if (tickAngle > angle) .3f else 1f
                    }
                    val tickFraction = if (shrink) {
                        (fraction + tickStartAt).coerceIn(0f, 1f)
                    } else {
                        (fraction + tickStartAt).coerceIn(0f, 1f)
                    }
                    val endOffset = Offset(
                        x = halfWidth * cos((tickAngle).toRad()) + this@Canvas.size.center.x,
                        y = halfHeight * sin((tickAngle).toRad()) + this@Canvas.size.center.y,
                    )
                    val tickOffset = lerp(start, endOffset, tickFraction)

                    add(Tick(tickOffset, alpha))
                }
            }
            drawCircle(Color.Red.copy(alpha = .3f))
            drawLine(Color.Blue, start, end, 20f, StrokeCap.Round)
            boxes.forEach {
                Timber.d("tick: $it")
                drawCircle(Color.Blue, 10f, it.offset, it.alpha)
            }
        }
        Text(
            text = "${(fraction * 100).roundToInt()}% - ${angle.roundToInt()} - $shrink",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            fontSize = 40.sp,
        )
        Text(
            text = "${(pointerFraction * 100).roundToInt()}",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
        )
    }
}

private fun Float.toRad(): Float = this * (Math.PI / 180f).toFloat()

data class Tick(
    val offset: Offset,
    val alpha: Float,
)