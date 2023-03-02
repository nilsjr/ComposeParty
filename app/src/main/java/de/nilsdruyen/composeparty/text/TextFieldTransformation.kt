package de.nilsdruyen.composeparty.text

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme
import de.nilsdruyen.composeparty.utils.Centered

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun TextFieldTransformation() {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val maxLength = 10
    var input by remember { mutableStateOf(TextFieldValue("")) }

    val clearFocus = {
        keyboardController?.hide()
        focusManager.clearFocus()
    }

    LaunchedEffect(input) {
        if (input.text.length >= maxLength) {
            clearFocus()
        }
    }

    Centered(Modifier.systemBarsPadding()) {
        Column {
            Button(
                onClick = { focusManager.clearFocus() },
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text("Clear focus")
            }
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = input,
                label = { Text("Label") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = { Text("Placeholder") },
                trailingIcon = {
                    AnimatedVisibility(
                        input.text.isNotEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        IconButton(onClick = { input = TextFieldValue("") }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                            )
                        }
                    }
                },
                onValueChange = {
                    if (it.text.length <= maxLength && it.text.all { c -> c.isDigit() }) {
                        input = it
                    }
                },
                visualTransformation = CustomTransformation(maxLength),
                supportingText = {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text("Input: ${input.text}", Modifier.alignByBaseline())
                        Text("${input.text.length}/$maxLength")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions {
                    clearFocus()
                },
                singleLine = true,
            )
        }
    }
}

class CustomTransformation(val maxLength: Int) : VisualTransformation {

    private val color = Color.Red
    private val mask = List(maxLength) { "X" }.joinToString("").formattedNumber()
    private val spaceIndexes = mask.mapIndexedNotNull { index, c ->
        if (c == ' ') index else null
    }.mapIndexed { index, spaceIndex ->
        if (index == 0) spaceIndex - 1 else ((spaceIndex - 1) - index)
    }

    override fun filter(text: AnnotatedString): TransformedText {
        if (text.isEmpty()) return TransformedText(emptyMask(), emptyMapping)
        val trimmed =
            if (text.text.length >= maxLength) text.text.substring(0 until maxLength) else text.text

        val annotatedString = AnnotatedString.Builder().run {
            pushStyle(SpanStyle(letterSpacing = 2.sp))
            for (i in trimmed.indices) {
                append(trimmed[i])
                if (spaceIndexes.contains(i)) append(" ")
            }
            pushStyle(SpanStyle(color = color, letterSpacing = 1.sp))
            try {
                append(mask.takeLast(mask.length - length))
            } catch (e: IllegalArgumentException) {
                println("Error ${e.localizedMessage}")
            }
            toAnnotatedString()
        }

        val currentLength = text.length
        val offsetList = generateOffset(maxLength)
        val offsetMapping = object : OffsetMapping {

            override fun originalToTransformed(offset: Int): Int {
                return if (offsetList.isEmpty()) {
                    offset
                } else {
                    offsetList.getOrNull(offset) ?: (offsetList.last() + 1)
                }.coerceAtMost(maxLength + spaceIndexes.size)
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offsetList.isEmpty()) return offset
                return when (val index = offsetList.indexOf(offset)) {
                    -1 -> offsetList.size
                    else -> index
                }.coerceAtMost(currentLength)
            }
        }

        return TransformedText(annotatedString, offsetMapping)
    }

    private fun emptyMask(): AnnotatedString {
        return AnnotatedString.Builder().run {
            pushStyle(SpanStyle(color = color, letterSpacing = 1.sp))
            append(mask)
            toAnnotatedString()
        }
    }

    private val emptyMapping = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int = 0

        override fun transformedToOriginal(offset: Int): Int = 0
    }
}

fun String.formattedNumber(): String {
    return windowed(3, 3, partialWindows = true)
        .windowed(2, 1)
        .chunked(2)
        .joinToString(" ") { parts ->
            val first = parts.first()
            if (first.all { it.length == 3 }) {
                val endNumber = parts.getOrNull(1)?.run { this[1] } ?: ""
                StringBuilder().apply {
                    append(first.joinToString(" "))
                    if (endNumber.length < 3) {
                        append(endNumber)
                    }
                }.toString()
            } else {
                first.joinToString("")
            }
        }
}

private fun generateOffset(maxLength: Int): List<Int> {
    var offset = 0
    val list = mutableListOf<Int>()

    (1..maxLength).forEach {
        list.add(it + offset)
        if (it != 0 && it % 3 == 0) offset++
    }

    return list
        .windowed(3, 3, partialWindows = true)
        .map { window -> if (window.size < 3) window.map { it - 1 } else window }
        .flatten()
        .map { it - 1 }
}

@Preview(showBackground = true, backgroundColor = 0xfff)
@Composable
fun TextFieldPreview() {
    ComposePartyTheme {
        Column(Modifier.fillMaxWidth()) {
            TextFieldTransformation()
        }
    }
}