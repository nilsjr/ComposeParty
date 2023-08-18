package de.nilsdruyen.composeparty.layouts

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme
import kotlin.math.roundToInt

@Composable
fun CustomLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier,
        content = content,
        measurePolicy = flowLayoutMeasurePolicy(),
    )
}

@Composable
fun TestFlowRow(
    modifier: Modifier = Modifier,
    horizontalGap: Dp = 0.dp,
    verticalGap: Dp = 0.dp,
    alignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable () -> Unit,
) = Layout(modifier = modifier, content = content) { measurables, constraints ->
    val horizontalGapPx = horizontalGap.toPx().roundToInt()
    val verticalGapPx = verticalGap.toPx().roundToInt()

    val rows = mutableListOf<Row>()
    var rowConstraints = constraints
    var rowPlaceables = mutableListOf<Placeable>()

    measurables.forEach { measurable ->
        val placeable = measurable.measure(Constraints(maxWidth = constraints.maxWidth))
        if (placeable.measuredWidth !in rowConstraints.minWidth..rowConstraints.maxWidth) {
            rows += Row(rowPlaceables, horizontalGapPx)
            rowConstraints = constraints
            rowPlaceables = mutableListOf()
        }
        val consumedWidth = placeable.measuredWidth + horizontalGapPx
        rowConstraints = rowConstraints.offset(horizontal = -consumedWidth)
        rowPlaceables.add(placeable)
    }
    rows += Row(rowPlaceables, horizontalGapPx)

    val width = constraints.maxWidth
    val height = (rows.sumOf { row -> row.height } + (rows.size - 1) * verticalGapPx)
        .coerceAtMost(constraints.maxHeight)

    layout(width, height) {
        var y = 0
        rows.forEach { row ->
            val offset = alignment.align(row.width, width, layoutDirection)
            var x = offset
            row.placeables.forEach { placeable ->
                placeable.placeRelative(x, y)
                x += placeable.width + horizontalGapPx
            }
            y += row.height + verticalGapPx
        }
    }
}

private class Row(
    val placeables: List<Placeable>,
    val horizontalGapPx: Int,
) {
    val width by lazy(mode = LazyThreadSafetyMode.NONE) {
        placeables.sumOf { it.width } + (placeables.size - 1) * horizontalGapPx
    }

    val height by lazy(mode = LazyThreadSafetyMode.NONE) {
        placeables.maxOfOrNull { it.height } ?: 0
    }
}

fun flowLayoutMeasurePolicy() = MeasurePolicy { measurables, constraints ->
    val placeables = measurables.map { measurable ->
        measurable.measure(constraints)
    }

    var row = 0
    var rowWidth = 0
    placeables.forEach {
        if ((rowWidth + it.measuredWidth) < constraints.maxWidth) {
            rowWidth = it.measuredWidth
        } else {
            row++
        }
    }

    val height = row * placeables.minBy { it.measuredHeight }.measuredHeight

    layout(constraints.maxWidth, height) {
        var yPos = 0
        var xPos = 0
        var maxY = 0

        placeables.forEach { placeable ->
            if (xPos + placeable.width > constraints.maxWidth) {
                xPos = 0
                yPos += maxY
                maxY = 0
            }
            placeable.placeRelative(x = xPos, y = yPos)

            xPos += placeable.width
            if (maxY < placeable.height) {
                maxY = placeable.height
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun CustomLayoutPreview() {
    ComposePartyTheme {
        TestFlowRow(
            modifier = Modifier
        ) {
            Text("1 Nils Nils")
            Text("2 Nils alkdmwlak ml wadlakmw")
            Text("3 Nils Nils")
            Text("3 Nils Nils")
        }
    }
}