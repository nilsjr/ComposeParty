package de.nilsdruyen.composeparty.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.nilsdruyen.composeparty.settings.component.rememberEditableColorScheme
import de.nilsdruyen.composeparty.settings.component.toColor
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorSettings(onBack: () -> Unit) {
    val context = LocalContext.current
    var editableColorScheme by rememberEditableColorScheme(isSystemInDarkTheme())
    var selectedColorKey by remember(editableColorScheme) { mutableStateOf<String?>(null) }
    var input by remember(selectedColorKey) { mutableStateOf(editableColorScheme.selected[selectedColorKey]) }
    var saveTheme by remember { mutableStateOf(false) }


    LaunchedEffect(saveTheme) {
        if (saveTheme) {
//            context.updateColor(ThemeEntity(light = editableColorScheme, dark = emptyMap()))
            onBack()
        }
    }

    selectedColorKey?.let { key ->
        AlertDialog(
            onDismissRequest = { selectedColorKey = null },
            title = { Text(key) },
            text = {
                input?.let {
                    TextField(
                        value = it,
                        onValueChange = { change -> input = change }
                    )
                }
            },
            confirmButton = {
                TextButton({
                    editableColorScheme = editableColorScheme.copy(
                        editableColorScheme.selected.toMutableMap().apply {
                            input?.let { this[key] = it }
                        }
                    )
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton({ selectedColorKey = null }) {
                    Text("Cancel")
                }
            },
        )
    }

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text("Theme") },
                actions = {
                    IconButton(
                        onClick = { editableColorScheme = editableColorScheme.reset() }
                    ) {
                        Icon(imageVector = Icons.Default.DarkMode, contentDescription = null)
                    }
                    IconButton(
                        onClick = { editableColorScheme = editableColorScheme.reset() }
                    ) {
                        Icon(imageVector = Icons.Default.Redo, contentDescription = null)
                    }
                    IconButton(
                        onClick = { saveTheme = true }
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null)
                    }
                }
            )
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            editableColorScheme.selected.forEach { (key, colorHex) ->
                ColorItem(key, colorHex) { selectedColorKey = key }
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
private fun ColorItem(key: String, hex: String, onClick: (String) -> Unit = {}) {
    Surface(
        onClick = { onClick(key) },
        tonalElevation = 2.dp,
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = key,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = hex,
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
                    .background(hex.toColor())
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ColorSettingsPreview() {
    ComposePartyTheme {
        ColorSettings {}
    }
}