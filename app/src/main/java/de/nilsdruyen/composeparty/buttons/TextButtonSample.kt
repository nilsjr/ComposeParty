package de.nilsdruyen.composeparty.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun TextButtonSample() {
    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { },
                modifier = Modifier
                    .testTag("button_1")
            ) {
                Text("Continue", modifier = Modifier.testTag("button_text_1"))
            }
            Button(
                onClick = { },
                modifier = Modifier
                    .height(56.dp)
                    .testTag("button_2")
            ) {
                Text("Continue 2", modifier = Modifier.testTag("button_text_2"))
            }
        }
    }
}