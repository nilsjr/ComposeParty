package de.nilsdruyen.composeparty.layouts

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object StaggeredGrid {
    enum class Orientation {
        HORIZONTAL,
        VERTIcAL
    }
}

@Composable
fun StaggeredGrid(
    spanCount: Int,
    orientation: StaggeredGrid.Orientation,
    modifier: Modifier = Modifier,
    itemSpacing: Dp = 0.dp,
    content: @Composable () -> Unit,
) {
    when (orientation) {
        StaggeredGrid.Orientation.HORIZONTAL -> StaggeredHorizontalGrid(
            rows = spanCount,
            content = content,
            modifier = modifier.horizontalScroll(rememberScrollState()),
            itemSpacing = itemSpacing,
        )
        StaggeredGrid.Orientation.VERTIcAL -> StaggeredVerticalGrid(
            columns = spanCount,
            content = content,
            modifier = modifier,
        )
    }
}