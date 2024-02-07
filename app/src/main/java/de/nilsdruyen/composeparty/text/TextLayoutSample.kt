package de.nilsdruyen.composeparty.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMaxOfOrNull
import androidx.compose.ui.util.fastSumBy
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@Composable
fun TextLayoutSample(modifier: Modifier = Modifier) {
    var showColumn by remember { mutableStateOf(false) }

    SameSizeElementFlowRow(modifier) {
        Text("Nils addwdwada", modifier = Modifier.background(Color.Red))
        Text("Thomas", modifier = Modifier.background(Color.Blue))
    }
}

@Composable
fun SameSizeElementFlowRow(
    modifier: Modifier = Modifier,
    mainAxisSpacing: Dp = 0.dp,
    crossAxisSpacing: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    Layout(content = content, modifier = modifier) { measurables, constraints ->
        val mainAxisSpacingPx = mainAxisSpacing.roundToPx()
        val crossAxisSpacingPx = crossAxisSpacing.roundToPx()

        val totalSpacingPx = mainAxisSpacingPx * (measurables.size - 1)
        val allowedItemWidth = (constraints.maxWidth - totalSpacingPx) / measurables.size

        val itemExceedsMaxWidth = measurables.fastAny { measurable ->
            measurable.maxIntrinsicWidth(constraints.minHeight) > allowedItemWidth
        }

        val placeables = measurables.fastMap { measurable ->
            val constraint = when {
                itemExceedsMaxWidth -> Constraints.fixedWidth(constraints.maxWidth)
                else -> Constraints.fixedWidth(allowedItemWidth)
            }
            measurable.measure(constraint)
        }

        val width = constraints.maxWidth
        val height = if (itemExceedsMaxWidth) {
            placeables.fastSumBy { it.measuredHeight } + (measurables.size - 1) * crossAxisSpacingPx
        } else {
            placeables.fastMaxOfOrNull { it.measuredHeight } ?: 0
        }

        var x = 0
        var y = 0
        layout(width, height) {
            placeables.fastForEach { placeable ->
                placeable.placeRelative(x, y)
                if (!itemExceedsMaxWidth) {
                    x += placeable.width + mainAxisSpacingPx
                } else {
                    y += placeable.height + crossAxisSpacingPx
                }
            }
        }
    }
}

@PreviewFontScale
@Composable
private fun TextLayoutSamplePreview() {
    ComposePartyTheme {
        Surface {
            TextLayoutSample(Modifier.width(280.dp))
        }
    }
}