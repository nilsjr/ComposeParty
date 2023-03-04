package de.nilsdruyen.composeparty.text

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldColors() {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var input by remember { mutableStateOf(TextFieldValue("")) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                focusRequester.requestFocus()
            }) {
                Text("request focus")
            }
            OutlinedTextField(
                value = input,
                onValueChange = { change: TextFieldValue -> input = change },
                label = { Text("Label") },
                placeholder = { Text("Placeholder") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .padding(horizontal = 32.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedPlaceholderColor = Color.Yellow,
                    unfocusedPlaceholderColor = Color.Yellow,
                    focusedLabelColor = Color.Red,
                    unfocusedLabelColor = Color.Red.copy(alpha = .3f),
                    disabledLabelColor = Color.Red.copy(alpha = .1f),
                    focusedTrailingIconColor = Color.Blue,
                    unfocusedTrailingIconColor = Color.Blue.copy(alpha = .3f),
                ),
                trailingIcon = {
                    AnimatedVisibility(
                        visible = input.text.isNotEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        IconButton(onClick = { input = TextFieldValue() }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = Color.Magenta
                            )
                        }
                    }
                }
            )
            Button(onClick = {
                focusManager.clearFocus()
//                focusRequester.freeFocus()
            }) {
                Text("clear focus")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}