package de.nilsdruyen.composeparty.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

val placeholders = listOf("Hallodaw ", "Sir da", "Tes dt")
val elements = List(32) {
    "${placeholders[it % 3]} $it"
}

@Composable
fun SampleStaggeredGridLayout() {
    Column(
        Modifier
            .statusBarsPadding()
            .padding(top = 16.dp)
    ) {
        StaggeredGrid(
            spanCount = 3,
            orientation = StaggeredGrid.Orientation.HORIZONTAL,
            itemSpacing = 12.dp,
        ) {
            elements.forEach {
                Text(
                    text = it,
                    modifier = Modifier
                        .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
                        .clickable { }
                        .padding(8.dp)
                )
            }
        }
    }
}

@Preview
@Preview(fontScale = .25f)
@Preview(fontScale = 1.5f)
@Preview(fontScale = 2f)
@Composable
fun PreviewLayout() {
    ComposePartyTheme {
        SampleStaggeredGridLayout()
    }
}

