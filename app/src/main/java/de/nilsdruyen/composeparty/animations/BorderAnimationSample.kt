package de.nilsdruyen.composeparty.animations

import android.graphics.Matrix
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.SweepGradientShader
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@Composable
fun BorderAnimationSample() {
    Box(Modifier.fillMaxSize()) {
        var size by remember { mutableStateOf(Size.Zero) }
        var round by remember { mutableStateOf(false) }
        val cornerPercent by animateIntAsState(if (round) 50 else 8)
        val shader = SweepGradientShader(
            Offset(size.width / 2, size.height / 2),
            listOf(Color.Transparent, Color(0xFFF44336)),
            listOf(0.3f, 1f),
        )

        val brush = animateBrushRotation(shader = shader, size = size)

        OutlinedButton(
            modifier = Modifier
                .align(Alignment.Center)
                .size(200.dp)
                .onSizeChanged {
                    size = Size(it.width.toFloat(), it.height.toFloat())
                },
            border = BorderStroke(2.dp, brush),
            onClick = { round = !round },
            shape = RoundedCornerShape(cornerPercent)
        ) {
            Text(
                text = "Rotating Border",
                color = MaterialTheme.colorScheme.onSurface,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}

@Composable
fun animateBrushRotation(shader: Shader, size: Size): ShaderBrush {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val (matrix, brush) = remember(shader, size) {
        Matrix().also {
            shader.getLocalMatrix(it)
        } to ShaderBrush(shader)
    }

    matrix.postRotate(angle, size.width / 2, size.height / 2)
    shader.setLocalMatrix(matrix)

    return brush
}

@Preview
@Composable
private fun BorderAnimationSamplePreview() {
    ComposePartyTheme {
        BorderAnimationSample()
    }
}