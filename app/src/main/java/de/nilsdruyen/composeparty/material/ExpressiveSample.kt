package de.nilsdruyen.composeparty.material

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.LoadingIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExpressiveSample(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        // Default
        LoadingIndicator()

        // With 2 shapes only
        LoadingIndicator(
            polygons = LoadingIndicatorDefaults.IndeterminateIndicatorPolygons.take(2)
        )

        // Default
        ContainedLoadingIndicator()

        // Custom Container Color
        ContainedLoadingIndicator(
            containerColor = Color.Cyan
        )
    }
}

@Preview
@Composable
private fun ExpressiveSamplePreview() {
    ComposePartyTheme {
        ExpressiveSample(Modifier.fillMaxSize())
    }
}