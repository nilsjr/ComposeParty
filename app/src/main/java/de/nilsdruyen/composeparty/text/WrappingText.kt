package de.nilsdruyen.composeparty.text

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun WrappingText() {
    Row(
        Modifier
            .padding(16.dp)
            .statusBarsPadding()
    ) {
        Text(
            "Bestellnummer: 1234567890d dakawd ",
            modifier = Modifier.weight(1f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Text(text = "Preisss wdaw", Modifier.wrapContentSize())
    }
}