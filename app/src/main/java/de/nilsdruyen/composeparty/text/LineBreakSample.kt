package de.nilsdruyen.composeparty.text

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@Composable
fun LineBreakSample(modifier: Modifier = Modifier, lineBreak: LineBreak = LineBreak.Heading) {
    Box(modifier) {
        Text(
            text = "Entdecke Frühlingsprodukte für euer Outdoor-Abenteuer",
            style = TextStyle.Default.copy(lineBreak = lineBreak, hyphens = Hyphens.Auto)
        )
    }
}

@Preview
@Composable
private fun LineBreakSamplePreview() {
    ComposePartyTheme {
        Surface {
            Column {
                LineBreakSample(Modifier.width(100.dp), LineBreak.Heading)
                HorizontalDivider()
                LineBreakSample(Modifier.width(100.dp), LineBreak.Simple)
                HorizontalDivider()
                LineBreakSample(Modifier.width(100.dp), LineBreak.Paragraph)
            }
        }
    }
}