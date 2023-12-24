package de.nilsdruyen.composeparty.freestyle

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import de.nilsdruyen.composeparty.R
import de.nilsdruyen.composeparty.modifiers.snowfall

@Preview
@Composable
fun SnowFallSample() {
    val context = LocalContext.current
    val snowflakes = listOfNotNull(
        context.getDrawable(R.drawable.snowflake01),
        context.getDrawable(R.drawable.snowflake02),
        context.getDrawable(R.drawable.snowflake03),
        context.getDrawable(R.drawable.snowflake04),
    ).map {
        it.apply { setBounds(0, 0, intrinsicWidth, intrinsicHeight) }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .snowfall(snowflakes)
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_forest),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .withOverlay(
                    brush = Brush.verticalGradient(
                        .00f to Color(0x00000000),
                        .5f to Color(0x0A000000),
                        .9f to Color(0x7A000000),
                        1f to Color(0xE5000000),
                    )
                )
        )
    }
}