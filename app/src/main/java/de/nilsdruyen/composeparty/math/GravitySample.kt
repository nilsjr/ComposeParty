package de.nilsdruyen.composeparty.math

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import de.nilsdruyen.composeparty.utils.animationTimeMillis
import kotlin.math.pow

//val earthGravity = 9.80665f  // m/s2
const val velocity = 200f

@Preview
@Composable
fun GravitySample() {
    val ball = remember { Ball(Offset.Zero, 80f, Color.Red) }

    val millis by animationTimeMillis()
    val direction = remember { mutableStateOf(0) }  // 0 down, 1 up
    val elapsedTime = remember { mutableStateOf(0f) }
    val lastVelocity = remember { mutableStateOf(0f) }

    val seconds = millis / 1000f
    val timeDiff = seconds - elapsedTime.value
    val y = if (direction.value == 0) {
        timeDiff.pow(2) * velocity
    } else {
        timeDiff.pow(2) * (velocity - lastVelocity.value * timeDiff)
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        if (y + ball.radius >= size.height) {
            direction.value = 1
            elapsedTime.value = timeDiff
            lastVelocity.value = velocity * timeDiff
        }

        drawCircle(
            color = ball.color,
            radius = ball.radius,
            center = ball.offset + Offset(size.width / 2, ball.radius + y)
        )
    }
}

data class Ball(
    val offset: Offset,
    val radius: Float,
    val color: Color,
)