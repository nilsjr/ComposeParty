package de.nilsdruyen.composeparty.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.layouts.custom.NilsFlowRow
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

data class Data(val text: String, val items: List<String>)

val test = Data(
    text = "Hallo Nils",
    items = listOf(
        "Bla bla bla bla bla bla alkba lk alk skla slk a dadwaawdawdawdwa",
        "ALKdmwldk amlkmaw l wdawdawd",
        "ALKdmwldk amlkmaw l wdawdawd",
    ),
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IntrinsicBug(model: Data, modifier: Modifier) {
    Card(modifier) {
        Row(Modifier.fillMaxWidth()) {
            NilsFlowRow(
                modifier = Modifier.weight(1f)
//                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                model.items.forEach { Text(it, Modifier.background(Color.Yellow)) }
            }
            Text("Hello")
        }
        Row(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(model.text)
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.Bottom)
                    .background(Color.Blue)
            )
        }
    }
}

@Preview
@Composable
private fun IntrinsicBugWithPreview() {
    ComposePartyTheme {
        IntrinsicBug(
            test,
            modifier = Modifier
                .height(IntrinsicSize.Max)
        )
    }
}

@Preview
@Composable
private fun IntrinsicBugWithoutPreview() {
    ComposePartyTheme {
        IntrinsicBug(
            test,
            modifier = Modifier
        )
    }
}

@Preview
@Composable
private fun IntrinsicBugListPreview() {
    ComposePartyTheme {
        Row(
            Modifier
                .horizontalScroll(rememberScrollState())
                .height(IntrinsicSize.Max)
        ) {
            IntrinsicBug(
                test,
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight()
            )
            IntrinsicBug(
                test,
                modifier = Modifier
                    .width(200.dp)
                    .fillMaxHeight()
            )
        }
    }
}