package de.nilsdruyen.composeparty.cards

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import de.nilsdruyen.composeparty.R
import de.nilsdruyen.composeparty.utils.lerp

private val maxAngleRange = -5f..5f
private val iconSize = 40.dp
private const val moveValue = 40f

@Preview(showBackground = true, backgroundColor = 0xFFFDFDFD)
@Composable
fun CardTiltSample() {
    val center = remember { mutableStateOf(Offset.Zero) }
    var rotationX by remember { mutableStateOf(.5f) }
    var rotationY by remember { mutableStateOf(.5f) }
    val elementOffset by remember {
        derivedStateOf {
            val x = lerp(-moveValue, moveValue, rotationY)
            val y = lerp(-moveValue, moveValue, rotationX)
            Offset(x, y)
        }
    }

    val animatedRotationX by animateFloatAsState(targetValue = rotationX)
    val rotateY by animateFloatAsState(targetValue = rotationY)
    val animatedElementOffset by animateOffsetAsState(targetValue = elementOffset)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(fraction = .7f)
                .aspectRatio(.7f)
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
                    }) { change, _ ->
                        val (x, y) = change.position.calculateTilt(center.value)
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
                    modifier = Modifier.fillMaxSize(),
                )
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset {
                            animatedElementOffset
                                .round()
                                .times(.3f)
                        }
                ) {
                    drawDots()
                    drawCircle(
                        Color.White.copy(alpha = .3f),
                        radius = size.width / 3,
                        style = Stroke(width = 40f)
                    )
                    drawCircle(
                        Color.White.copy(alpha = .5f),
                        radius = size.width / 3 + 40f,
                        style = Stroke(width = 5f)
                    )
                }
                Box(
                    Modifier
                        .size(iconSize)
                        .align(Alignment.Center)
                        .offset {
                            animatedElementOffset
                                .round()
                                .times(.9f)
                        }
                        .graphicsLayer {
                            this.rotationX = animatedRotationX.toDegrees(true)
                            this.rotationY = rotateY.toDegrees()
                        }
                        .clip(CircleShape)
                        .background(Color.Red.copy(alpha = .7f))
                )
            }
        }
        // controls
//        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
//            Text(
//                text = "$rotationX - $rotationY",
//                modifier = Modifier.align(Alignment.CenterHorizontally)
//            )
//            Slider(
//                value = rotationX,
//                onValueChange = { rotationX = it },
//                valueRange = 0f..1f,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp)
//            )
//            Slider(
//                value = rotationY,
//                onValueChange = { rotationY = it },
//                valueRange = 0f..1f,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp)
//            )
//        }
    }
}

private fun Offset.calculateTilt(center: Offset): Pair<Float, Float> {
    val verticalRange = 0f..(center.x * 2)
    val horizontalRange = 0f..(center.y * 2)

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

private fun DrawScope.drawDots() {

}