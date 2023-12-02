package de.nilsdruyen.composeparty.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@Composable
fun LazyGridIntrinsicSample() {
    val density = LocalDensity.current
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.background(Color.Red),
    ) {
        items(buildList()) {
            val height = with(density) { it.toDp() }
            Box(
                modifier = Modifier
//                    .fillMaxSize()
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
                    .background(Color.Yellow)
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(height)
                        .background(Color.Black)
                        .border(1.dp, Color.White)
                ) {}
            }
        }
    }
}

val list = listOf(20, 40, 60, 30).map { it * 4 }
private fun buildList(): List<Int> = List(10) { list.random() }

@Preview
@Composable
private fun LazyGridPreview() {
    ComposePartyTheme {
        LazyGridIntrinsicSample()
    }
}