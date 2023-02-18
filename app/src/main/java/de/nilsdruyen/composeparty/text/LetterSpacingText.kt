package de.nilsdruyen.composeparty.text

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@Composable
fun LetterSpacingText() {
    Box(Modifier.fillMaxWidth()) {
        Text(
            "123 456 789 0",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(top = 8.dp),
            letterSpacing = 1.sp,
        )
        Text(
            "XXX XXX XXX X",
            modifier = Modifier.align(Alignment.CenterStart),
            letterSpacing = 0.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xfff)
@Composable
fun LetterSpacingTextPreview() {
    ComposePartyTheme {
        LetterSpacingText()
    }
}