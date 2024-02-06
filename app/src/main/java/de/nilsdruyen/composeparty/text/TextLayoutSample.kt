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
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@Composable
fun TextLayoutSample(modifier: Modifier = Modifier) {
    var showColumn by remember { mutableStateOf(false) }

    CustomLayout(modifier) {
        Text("Nils addwdwada", modifier = Modifier.background(Color.Red))
        Text("Thomas", modifier = Modifier.background(Color.Blue))
    }
}

@Composable
fun CustomLayout(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(content = content, modifier) { m, c ->
        val placeables = mutableListOf<Placeable>()

        val maxW = m.any {
             it.minIntrinsicWidth(c.minHeight) > c.maxWidth / m.size
        }

        val constraint = if (maxW) {
            Constraints(minWidth = c.maxWidth, maxWidth = c.maxWidth)
        } else {
            Constraints(minWidth = c.maxWidth / 2, maxWidth = c.maxWidth)
        }

        m.fastForEach { measurable ->
            val placeable = measurable.measure(constraint)
            placeables.add(placeable)
        }

        val width = c.maxWidth
        val height = if (maxW) {
            placeables.sumOf { it.measuredHeight }
        } else {
            placeables.first().measuredHeight
        }

        var x = 0
        var y = 0
        layout(width, height) {
            placeables.fastForEach { p ->
                p.placeRelative(x, y)
                if (!maxW) {
                    x += p.measuredWidth
                }
                if (maxW) {
                    y += p.measuredHeight
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