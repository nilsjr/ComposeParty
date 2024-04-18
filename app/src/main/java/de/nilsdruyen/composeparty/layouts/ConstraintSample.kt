package de.nilsdruyen.composeparty.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@Composable
fun ConstraintSample(modifier: Modifier = Modifier) {
    ConstraintLayout(modifier.background(Color.White)) {
        val (image, text) = createRefs()


        Box(
            modifier = Modifier
                .constrainAs(image) {
                    width = Dimension.value(40.dp)
                    height = Dimension.value(40.dp)
                    linkTo(text.top, text.bottom)
                    linkTo(text.start, text.end)
                }
                .background(Color.Red)
        )
        Text(
            text = "Hallo",
            modifier = Modifier.constrainAs(text) {
                linkTo(parent.start, parent.end)
                linkTo(parent.top, parent.bottom)
            },
            style = MaterialTheme.typography.headlineLarge,
        )
    }
}

@Preview
@Composable
private fun ConstraintSamplePreview() {
    ComposePartyTheme {
        ConstraintSample(Modifier.fillMaxSize())
    }
}