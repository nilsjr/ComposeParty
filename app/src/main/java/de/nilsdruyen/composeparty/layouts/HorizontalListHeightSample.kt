package de.nilsdruyen.composeparty.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

val data = listOf(
    "Test",
    "Test\nTest\nTest",
    "Test Test Test",
    "Test Test \nTest Test Test",
    "Test",
    "Test Test \nTest Test \nTest",
    "Test Test Test Test Test",
)

@Composable
fun HorizontalListHeightSample() {
    val density = LocalDensity.current

    val minimumHeightState = remember { MinimumHeightState() }
    val minimumHeightStateModifier = Modifier.minimumHeightModifier(
        minimumHeightState,
        density
    )

    Column {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(data) {
                ListEntry(it, modifier = minimumHeightStateModifier)
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            data.forEach {
                ListEntry(
                    value = it,
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
//                        .width(IntrinsicSize.Max)
                )
            }
        }
    }
}

@Composable
fun CustomRow() {
    // TODO: add custom impl
}

@Composable
private fun ListEntry(value: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier
        .background(Color.Red)
        .padding(16.dp)) {
        Text(text = value)
    }
}

@Preview
@Composable
private fun HorizontalListHeightSamplePreview() {
    ComposePartyTheme {
        HorizontalListHeightSample()
    }
}

fun Modifier.minimumHeightModifier(state: MinimumHeightState, density: Density) =
    onSizeChanged { size ->
        val itemHeight = with(density) { size.height.toDp() }
        if (itemHeight > (state.minHeight ?: 0.dp)) {
            state.minHeight = itemHeight
        }
    }.heightIn(min = state.minHeight ?: Dp.Unspecified)

class MinimumHeightState(minHeight: Dp? = null) {
    var minHeight by mutableStateOf(minHeight)
}