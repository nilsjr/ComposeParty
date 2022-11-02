package de.nilsdruyen.composeparty.cards

import android.view.MotionEvent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import de.nilsdruyen.composeparty.R
import de.nilsdruyen.composeparty.animations.DynamicHueSample
import de.nilsdruyen.composeparty.animations.DynamicHueSpec
import de.nilsdruyen.composeparty.utils.lerp
import kotlinx.coroutines.launch

private const val moveValue = 100f
private val maxAngleRange = -10f..10f
private val gradientColors = listOf(
    Color.Black,
    Color.Blue.copy(alpha = .8f),
    Color.Black,
    Color.Blue.copy(alpha = .6f)
)

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true, backgroundColor = 0xFFFDFDFD)
@Composable
fun CardTiltSample() {
    val scope = rememberCoroutineScope()
    var center by remember { mutableStateOf(Offset.Zero) }
    val rotation = remember { Animatable(Offset(.5f, .5f), Offset.VectorConverter) }
    val elementOffset by remember {
        derivedStateOf {
            val x = lerp(-moveValue, moveValue, rotation.value.y)
            val y = lerp(-moveValue, moveValue, rotation.value.x)
            Offset(x, y)
        }
    }

    val animatedRotationX by animateFloatAsState(targetValue = rotation.value.x)
    val animatedRotationY by animateFloatAsState(targetValue = rotation.value.y)
    val animatedElementOffset by animateOffsetAsState(targetValue = elementOffset)

    val brush = remember(rotation.value.x, rotation.value.y) {
        object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                val widthOffset = size.width * rotation.value.x
                val heightOffset = size.height * rotation.value.y
                return LinearGradientShader(
                    colors = gradientColors,
                    from = Offset(widthOffset, heightOffset),
                    to = Offset(widthOffset + size.width, heightOffset + size.height),
                    tileMode = TileMode.Mirror
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(fraction = .8f)
                .aspectRatio(.7f)
                .align(Alignment.Center)
                .onGloballyPositioned {
                    center = Offset((it.size.width / 2).toFloat(), (it.size.height / 2).toFloat())
                }
                .graphicsLayer {
                    this.rotationX = animatedRotationX.toDegrees(true)
                    this.rotationY = animatedRotationY.toDegrees()
                }
                .pointerInteropFilter {
                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            val offset = Offset(it.x, it.y).calculateTilt(center)
                            scope.launch {
                                rotation.animateTo(
                                    offset,
                                    spring(.4f, Spring.StiffnessMedium)
                                )
                            }
                        }
                        MotionEvent.ACTION_MOVE -> {
                            val offset = Offset(it.x, it.y).calculateTilt(center)
                            scope.launch {
                                rotation.animateTo(offset, tween(0))
                            }
                        }
                        MotionEvent.ACTION_UP -> {
                            scope.launch {
                                rotation.animateTo(
                                    Offset(.5f, .5f),
                                    spring(.35f, Spring.StiffnessLow)
                                )
                            }
                        }
                        else -> {}
                    }
                    true
                },
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        ) {
            Box(
                Modifier
                    .background(Color.Black)
                    .background(brush)
            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.bg_forest),
//                    contentDescription = null,
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier.fillMaxSize(),
//                )
                DynamicHueSample(
                    modifier = Modifier.fillMaxSize(),
                    speed = .1f,
                    spec = DynamicHueSpec(
                        dotCount = 15,
                        minDotRad = 10f,
                        maxDotRad = 35f,
                        xAbsVariance = 15f,
                        yAbsVariance = 20f,
                        xFreq = 1f,
                        yFreq = 2f,
                        hueMin = 230f,
                        hueMax = 295f,
                        hueSat = 80f,
                        hueValue = 90f,
                    ),
                )
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            this.rotationX = animatedRotationX.toDegrees(true)
                            this.rotationY = animatedRotationY.toDegrees()
                        }
                ) {
                    drawCircle(
                        color = Color.White.copy(alpha = .3f),
                        center = animatedElementOffset + Offset(size.width / 2, size.height / 2),
                        radius = size.width / 4,
                    )
                    drawCircle(
                        color = Color.White.copy(alpha = .2f),
                        center = animatedElementOffset.times(.7f) + Offset(
                            size.width / 2,
                            size.height / 2
                        ),
                        radius = size.width / 4 + 30f,
                        style = Stroke(width = 30f)
                    )
                    drawCircle(
                        color = Color.White.copy(alpha = .2f),
                        center = (animatedElementOffset.times(.5f) + Offset(
                            size.width / 2,
                            size.height / 2
                        )),
                        radius = size.width / 2.4f,
                        style = Stroke(width = 10f)
                    )
                }
                Icon(
                    painterResource(id = R.drawable.ic_kotlin_logo),
                    contentDescription = null,
                    tint = Color.White.copy(alpha = .9f),
                    modifier = Modifier
                        .offset {
                            animatedElementOffset
                                .round()
                                .times(1.2f)
                        }
                        .graphicsLayer {
                            this.rotationX = animatedRotationX.toDegrees(true)
                            this.rotationY = animatedRotationY.toDegrees()
                        }
                        .size(80.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

private fun Offset.calculateTilt(center: Offset): Offset {
    val verticalRange = 0f..(center.x * 2)
    val horizontalRange = 0f..(center.y * 2)
    val valueX = x.coerceIn(horizontalRange) / horizontalRange.endInclusive
    val valueY = y.coerceIn(verticalRange) / verticalRange.endInclusive
    return Offset(valueY, valueX)
}

private fun Float.toDegrees(inverted: Boolean = false): Float {
    return if (!inverted) {
        lerp(maxAngleRange.start, maxAngleRange.endInclusive, this)
    } else {
        lerp(maxAngleRange.endInclusive, maxAngleRange.start, this)
    }
}