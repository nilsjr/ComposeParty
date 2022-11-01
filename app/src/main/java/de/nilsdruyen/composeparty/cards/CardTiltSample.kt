package de.nilsdruyen.composeparty.cards

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import de.nilsdruyen.composeparty.R
import timber.log.Timber

private val degRange = -180f..180f
private val maxAngleRange = -4f..4f

@Composable
fun CardTiltSample() {
    val density = LocalDensity.current
    val iconSize = 40.dp
    val sizeOffset = with(density) { iconSize.toPx() / 2 }
    val center = remember { mutableStateOf(Offset.Zero) }
    var rotationX by remember { mutableStateOf(.5f) }
    var rotationY by remember { mutableStateOf(.5f) }

    val moveValue = 40f
    val boxOffset by remember {
        derivedStateOf {
            val x = lerp(-moveValue, moveValue, rotationY)
            val y = lerp(-moveValue, moveValue, rotationX)
            center.value - Offset(sizeOffset, sizeOffset) + Offset(x, y)
        }
    }
    val animatedRotationX by animateFloatAsState(targetValue = rotationX)
    val rotateY by animateFloatAsState(targetValue = rotationY)
    val animatedBoxOffset by animateOffsetAsState(targetValue = boxOffset)

//    Timber.d("center: ${center.value}")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(fraction = .7f)
                .aspectRatio(.8f)
                .align(Alignment.Center)
                .onGloballyPositioned {
                    center.value = Offset(
                        (it.size.width / 2).toFloat(), (it.size.height / 2).toFloat()
                    )
                }
                .graphicsLayer {
                    this.rotationX = animatedRotationX.toDegrees(true)
                    this.rotationY = rotateY.toDegrees()
                }
                .pointerInput(Unit) {
                    detectDragGestures(onDragEnd = {
                        rotationX = .5f
                        rotationY = .5f
                    }) { change, dragAmount ->
                        val (x, y) = change.position.calculateTilt(center.value)
                        Timber.d("tilt: $x $y")
                        rotationX = y
                        rotationY = x
                        change.consume()
                    }
                },
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        ) {
            Box {
                Image(
                    painter = painterResource(id = R.drawable.bg_forest),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1.2f)
                )
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            this.rotationX = animatedRotationX.toDegrees(true)
                            this.rotationY = rotateY.toDegrees()
                        }
                ) {
                    drawCircle(
                        Color.White.copy(alpha = .3f),
                        radius = size.width / 3,
                        style = Stroke(width = 10f)
                    )
                }
                Box(
                    Modifier
                        .offset { animatedBoxOffset.round() }
                        .graphicsLayer {
                            this.rotationX = animatedRotationX.toDegrees(true)
                            this.rotationY = rotateY.toDegrees()
                        }
                        .size(iconSize)
                        .clip(CircleShape)
                        .background(Color.Red.copy(alpha = .7f)))
            }
        }
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            Text(
                "${rotationX.toInt()} - ${rotationY.toInt()}",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Slider(
                value = rotationX,
                onValueChange = {
                    rotationX = it
                },
                valueRange = degRange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Slider(
                value = rotationY,
                onValueChange = {
                    rotationY = it
                },
                valueRange = degRange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

private fun Offset.calculateTilt(center: Offset): Pair<Float, Float> {
    val verticalRange = 0f..(center.x * 2)
    val horizontalRange = 0f..(center.y * 2)

    val horizontalDiff = x - center.x
    val verticalDiff = y - center.y

    val tiltLeft = horizontalDiff < 0
    val tiltTop = verticalDiff < 0
    Timber.d("$tiltLeft - $tiltTop")

    val cappedX = x.coerceIn(horizontalRange)
    val cappedY = y.coerceIn(verticalRange)

    val valueX = (cappedX / horizontalRange.endInclusive)
    val valueY = (cappedY / verticalRange.endInclusive)

    return Pair(valueX, valueY)
}

private fun Float.toDegrees(inverted: Boolean = false): Float {
    return if (!inverted) {
        lerp(maxAngleRange.start, maxAngleRange.endInclusive, this)
    } else {
        lerp(maxAngleRange.endInclusive, maxAngleRange.start, this)
    }
}

fun lerp(a: Float, b: Float, f: Float) = a + f * (b - a)