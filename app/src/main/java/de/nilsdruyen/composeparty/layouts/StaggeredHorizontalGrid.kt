package de.nilsdruyen.composeparty.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StaggeredHorizontalGrid(
    @androidx.annotation.IntRange(from = 1) rows: Int,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    itemSpacing: Dp = 0.dp,
    content: @Composable () -> Unit,
) {
    val spacingInPx = with(LocalDensity.current) { itemSpacing.toPx().toInt() }

    Layout(
        content = content,
        modifier = modifier
            .background(Color.Blue)
            .padding(contentPadding)
            .background(Color.Red)
    ) { measurables, constraints ->
        val rowWidths = IntArray(rows) { 0 }
        val rowHeights = IntArray(rows) { 0 }

        val placeables = measurables.mapIndexed { index, measurable ->
            val placeable = measurable.measure(constraints)
            val lastIndex = rows - 1
            val row = rowWidths.shortestRow()

            val spacingX = if (index < measurables.size - 1) spacingInPx else 0
            val spacingY = if (row < lastIndex) spacingInPx else 0

            val outterPaddingX =
                if (rowWidths[row] == 0 || index == measurables.size - 1) spacingInPx else 0
            val outterPaddingY = if (row == 0 || row == lastIndex) spacingInPx else 0

            rowWidths[row] += placeable.width + spacingX + outterPaddingX
            if (rowHeights[row] < placeable.height) {
                rowHeights[row] = placeable.height + spacingY + outterPaddingY
            }
            placeable
        }

        val width = rowWidths.maxOrNull()?.coerceIn(constraints.minWidth, constraints.maxWidth)
            ?: constraints.minWidth
        val height = rowHeights.sum()

        layout(width = width, height = height) {
            val rowX = IntArray(rows) { 0 }

            placeables.forEachIndexed { index, placeable ->
                val row = rowX.shortestRow()
                val spacingX = if (index < measurables.size - 1) spacingInPx else 0
                val outterPaddingY = if (row == 0) spacingInPx else 0

                placeable.placeRelative(
                    x = rowX[row] + spacingInPx,
                    y = rowHeights.slice(0 until row).sum() + outterPaddingY
                )
                rowX[row] += placeable.width + spacingX
            }
        }
    }
}

private fun IntArray.shortestRow(): Int {
    return mapIndexed { index, width -> width to index }.minBy { it.first }.second
}