package de.nilsdruyen.composeparty.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@Composable
fun RowSample(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(Modifier.weight(1f, false)) {
            Text(
                "Test Test Test Test Test Test Test Test".repeat(3),
                modifier = Modifier
                    .weight(1f, false)
                    .alignByBaseline()
            )
            Box(
                Modifier
                    .size(10.dp)
                    .background(Color.Red)
                    .alignByBaseline()
            )
            Text("Sample", Modifier.alignByBaseline(), fontSize = 4.sp)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            Modifier
                .size(10.dp)
                .background(Color.Red)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun RowSamplePreview() {
    ComposePartyTheme {
        RowSample(Modifier.fillMaxWidth())
    }
}