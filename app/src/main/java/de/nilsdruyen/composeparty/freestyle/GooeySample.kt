package de.nilsdruyen.composeparty.freestyle

import android.graphics.ComposePathEffect
import android.graphics.CornerPathEffect
import android.graphics.DiscretePathEffect
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toComposePathEffect
import androidx.compose.ui.tooling.preview.Preview
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@Composable
fun GooeySample() {
    val path = remember {
        Path().apply {
            addOval(Rect(Offset(150f, 150f), radius = 60f))
        }
    }
    val path2 = remember {
        Path().apply {
            addOval(Rect(Offset(270f, 150f), radius = 60f))
        }
    }

    path2.op(path, path2, PathOperation.Union)

    val pm = android.graphics.PathMeasure(path2.asAndroidPath(), true)
    val discretePathEffect = DiscretePathEffect(pm.length / 20f, 0f)
    val cornerPathEffect = CornerPathEffect(50f)

    val pathEffect = ComposePathEffect(cornerPathEffect, discretePathEffect)

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        drawCircle(Color.Red, radius = 60f)
        drawCircle(
            color = Color.Red,
            radius = 60f,
            center = this.center + Offset(140f, 0f)
        )

        drawPath(path, Color.Blue)
        drawPath(
            path2,
            Color.Red,
            style = Stroke(width = 10f, pathEffect = pathEffect.toComposePathEffect())
        )
    }
}

@Preview
@Composable
private fun GooeySamplePreview() {
    ComposePartyTheme {
        GooeySample()
    }
}