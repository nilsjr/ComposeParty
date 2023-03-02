package de.nilsdruyen.composeparty.layouts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@Composable
fun ColumnSample() {
    var visible by remember { mutableStateOf(true) }
    BoxWithConstraints {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.Green),
//        verticalArrangement = Arrangement.SpaceEvenly
        ) {
//            val modifierBox1 : Modifier = if (this@BoxWithConstraints.maxHeight > 760.dp)
//                Modifier.weight(1f)
//            else
//                Modifier.heightIn(160.dp)
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .sizeIn(minHeight = 300.dp)
                    .background(Color.Red),
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { visible = !visible },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Nils")
                }
                Button(
                    onClick = { visible = !visible },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Nils")
                }
                Button(
                    onClick = { visible = !visible },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Nils")
                }
                Button(
                    onClick = { visible = !visible },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Nils")
                }
            }
            AnimatedVisibility(visible = visible) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .requiredHeight(500.dp)
                        .background(Color.Blue)
                )
            }
        }
    }
}

@Preview
@Composable
fun ColumnSamplePreview() {
    ComposePartyTheme {
        ColumnSample()
    }
}