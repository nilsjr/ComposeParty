package de.nilsdruyen.composeparty.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

val placeholders = listOf("Hallodaw ", "Sir da", "Tes dt")
val elements = List(22) {
    "${placeholders[it % 3]} $it"
}

@Composable
fun SampleStaggeredGridLayout() {
    Column(
        Modifier
            .statusBarsPadding()
            .padding(top = 16.dp)
    ) {
        StaggeredHorizontalGrid(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            rows = 2
        ) {
            elements.forEach {
                Text(
                    it,
                    modifier = Modifier
                        .padding(4.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
                        .clickable { }
                        .padding(8.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        StaggeredHorizontalGrid(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            rows = 4
        ) {
            elements.forEach {
                Text(
                    it,
                    modifier = Modifier
                        .padding(4.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
                        .clickable { }
                        .padding(8.dp)
                )
            }
        }
    }

}

@Preview
@Composable
fun PreviewLayout() {
    ComposePartyTheme {
        SampleStaggeredGridLayout()
    }
}

@Composable
fun StaggeredHorizontalGrid(
    modifier: Modifier = Modifier,
    @androidx.annotation.IntRange(from = 1) rows: Int,
    content: @Composable () -> Unit,
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val rowWidths = IntArray(rows) { 0 }
        val rowHeights = IntArray(rows) { 0 }

        val placeables = measurables.map { measurable ->
            val row = shortestRow(rowWidths)
            val placeable = measurable.measure(constraints)
            rowWidths[row] += placeable.width
            if (rowHeights[row] < placeable.height) {
                rowHeights[row] = placeable.height
            }
            placeable
        }
        val width = rowWidths.maxOrNull()?.coerceIn(
            constraints.minWidth,
            constraints.maxWidth
        ) ?: constraints.minWidth
        val height = rowHeights.sum()

        layout(
            width = width,
            height = height
        ) {
            val rowX = IntArray(rows) { 0 }
            placeables.forEach { placeable ->
                val row = shortestRow(rowX)
                placeable.place(
                    x = rowX[row],
                    y = rowHeights.slice(0 until row).sum()
                )
                rowX[row] += placeable.width
            }
        }
    }
}

private fun shortestRow(colHeights: IntArray): Int {
    var minHeight = 10000
    var column = 0
    colHeights.forEachIndexed { index, height ->
        if (height < minHeight) {
            minHeight = height
            column = index
        }
    }
    return column
}
