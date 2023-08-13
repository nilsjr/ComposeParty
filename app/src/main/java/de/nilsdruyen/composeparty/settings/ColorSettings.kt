package de.nilsdruyen.composeparty.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme
import de.nilsdruyen.composeparty.ui.theme.raw
import de.nilsdruyen.composeparty.ui.theme.rememberColorState
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorSettings() {
    val colorState by rememberColorState()
    val controller = rememberColorPickerController()

    var selectedColor by remember(colorState) { mutableStateOf(colorState.primary) }

    LaunchedEffect(selectedColor) {
        Timber.d("Selected color ${selectedColor.raw()}")
//        controller.(selectedColor)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Theme") },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null)
                    }
                }
            )
        },
        modifier = Modifier.systemBarsPadding()
    ) {
        Column(
            modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Column(
                Modifier
                    .weight(.1f)
                    .verticalScroll(rememberScrollState())
            ) {
                ColorItem("primary", colorState.primary) { selectedColor = it }
                ColorItem("onPrimary", colorState.onPrimary) { selectedColor = it }
                ColorItem("primaryContainer", colorState.primaryContainer) { selectedColor = it }
                ColorItem("surface", colorState.surface) { selectedColor = it }
            }
//        Box(Modifier.weight(.5f)) {
//            key(selectedColor) {
//                HsvColorPicker(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .aspectRatio(1f)
//                        .padding(12.dp),
//                    controller = controller,
//                    drawOnPosSelected = {
//                        drawColorIndicator(
//                            controller.selectedPoint.value,
//                            controller.selectedColor.value,
//                        )
//                    },
//                    initialColor = selectedColor,
//                    onColorChanged = { colorEnvelope: ColorEnvelope ->
//                        // do something
//                        colorEnvelope.color
//                    }
//                )
//            }
//        }
        }
    }
}

@Composable
fun ColorItem(label: String, color: Color, onClick: (Color) -> Unit = {}) {
    Surface(
        onClick = { onClick(color) },
        tonalElevation = 8.dp,
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = color.raw(),
                letterSpacing = 2.sp,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ColorSettingsPreview() {
    ComposePartyTheme {
        ColorSettings()
    }
}