package de.nilsdruyen.composeparty.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import glm_.value
import timber.log.Timber

@Composable
fun AutoSizeTextSample() {
    Box(Modifier.fillMaxSize()) {
        AutoSizeText(text = "Hallo Nils was geht bei dir?", modifier = Modifier.width(100.dp))
    }
}

@Composable
fun AutoSizeText(text: String, modifier: Modifier) {
    val density = LocalDensity.current
    val sizeRequiredToDraw = remember { mutableStateOf(IntSize(0, 0)) }

    BoxWithConstraints(modifier = modifier.background(Color.Red.copy(alpha = .3f))) {
        val canvasWidth = constraints.maxWidth * density.density
        val canvasHeight = constraints.maxHeight * density.density
        val scale = remember(sizeRequiredToDraw.value, maxWidth, maxHeight) {
            val widthRatio = canvasWidth / sizeRequiredToDraw.value.width
            val heightRatio = canvasHeight / sizeRequiredToDraw.value.height
            kotlin.math.min(widthRatio, heightRatio)
        }
        Timber.d("$density draw text $canvasWidth $canvasHeight $scale")
        Text(
            modifier = Modifier
                .wrapContentSize()
                .unconstrainedSize()
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                }
                .padding(8.dp)
                .background(Color.White),
            fontSize = 50.sp, // max size
            text = text,
            maxLines = 1,
            color = Color.Black,
            onTextLayout = {
                Timber.d("layout ${it.size}")
                sizeRequiredToDraw.value = it.size
            }
        )
    }
}


fun Modifier.unconstrainedSize() = this.then(UnconstrainedSizeModifier())

class UnconstrainedSizeModifier : LayoutModifier {

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val placeable = measurable.measure(
            constraints.copy(
                maxWidth = Constraints.Infinity,
                maxHeight = Constraints.Infinity
            )
        )
        return layout(placeable.width, placeable.height) {
            placeable.place(0, 0)
        }
    }
}

@Preview
@Composable
fun AutoSizeTextPreview() {
    AutoSizeText(text = "Hallo Test", Modifier.fillMaxWidth())
}