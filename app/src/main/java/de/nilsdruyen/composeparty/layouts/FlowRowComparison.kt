package de.nilsdruyen.composeparty.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRowComparison(modifier: Modifier = Modifier) {
    Column {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
                .then(modifier),
            crossAxisAlignment = FlowCrossAxisAlignment.End,
            crossAxisSpacing = 8.dp,
            mainAxisSpacing = 8.dp,
        ) {
            Items()
        }
        androidx.compose.foundation.layout.FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Blue)
                .then(modifier),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Items()
        }
    }
}

@Composable
private fun Items() {
    Text("1000 €")
    Text("1000 €")
    Text("1000 €")
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun FlowRowComparisonPreview() {
    ComposePartyTheme {
        FlowRowComparison(Modifier.fillMaxWidth())
    }
}