package de.nilsdruyen.composeparty.text

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun FontScaleDemo() {
    val context = LocalContext.current
    val density = LocalDensity.current
    val fontSize = remember { mutableIntStateOf(20) }
    val range = 1f..80f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Font Scale: ${density.fontScale}",
            )
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = {
                context.startActivity(Intent(Settings.ACTION_SETTINGS))
            }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                )
            }
        }
        Text(
            "Text Size: ${fontSize.value} - ${fontSize.value.sp} - ${fontSize.value.scaledSp()}",
            modifier = Modifier
                .padding(16.dp),
        )
        Slider(
            value = fontSize.value.toFloat(),
            onValueChange = {
                fontSize.value = it.roundToInt()
            },
            valueRange = range,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        Text(
            text = "Hallo Kestas",
            fontSize = fontSize.value.sp,
            modifier = Modifier
                .padding(16.dp)
                .border(1.dp, Color.LightGray)
                .background(Color.Red.copy(alpha = .2f)),
        )
        Text(
            text = "Hallo Kestas",
            fontSize = fontSize.value.scaledSp(),
            modifier = Modifier
                .padding(16.dp)
                .border(1.dp, Color.LightGray)
                .background(Color.Red.copy(alpha = .2f)),
        )
    }
}

@Composable
fun Int.scaledSp(): TextUnit {
    val value: Int = this
    return with(LocalDensity.current) {
        val fontScale = this.fontScale
        val textSize = value / fontScale
        textSize.sp
    }
}