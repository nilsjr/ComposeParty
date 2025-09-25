package de.nilsdruyen.composeparty.layouts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme
import de.nilsdruyen.composeparty.utils.Centered
import timber.log.Timber

private val items = listOf("Nils", "Kristian", "Johannes", "Thomas")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownLayout(expanded: Boolean = false) {
    var selectedText by remember { mutableStateOf(items.first()) }
    var expanded by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(expanded) }

    LaunchedEffect(sheetState, showBottomSheet) {
        Timber.d("showBottomSheet: $showBottomSheet")
        if (showBottomSheet) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    Scaffold {
        Centered(Modifier.padding(it)) {
            Box {
                DropDownSelectedItem(selectedText) {
//                    expanded = true
                    showBottomSheet = true
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.align(Alignment.TopEnd),
//                    properties = PopupProperties()
                ) {
                    items.forEach {
                        DropdownMenuItem(text = { Text(it) }, onClick = { selectedText = it })
                    }
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState,
            ) {
                Column(
                    Modifier
                    .weight(1f, fill = false)
                    .verticalScroll(rememberScrollState())
                ) {
                    Text(LoremIpsum(400).values.first())
                }
                items.forEach {
                    Text(
                        text = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedText = it
                                showBottomSheet = false
                            }
                            .padding(16.dp)
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun DropDownSelectedItem(selectedText: String, onClick: () -> Unit) {
    OutlinedTextField(
        value = selectedText,
        onValueChange = {},
        readOnly = true,
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent =
                        waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        onClick()
                    }
                }
            }
            .padding(16.dp),
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        },
    )
//    ConstraintLayout(modifier = Modifier.fillMaxWidth(.7f)) {
//        val (button, clickArea, label) = createRefs()
//        Row(
//            modifier = Modifier
//                .constrainAs(button) {
//                    width = Dimension.fillToConstraints
//                    height = Dimension.value(54.dp)
//                    linkTo(parent.top, parent.bottom)
//                    linkTo(parent.start, parent.end)
//                }
//                .border(1.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp)),
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            Spacer(modifier = Modifier.width(16.dp))
//            Icon(imageVector = Icons.Default.Face, contentDescription = null)
//            Spacer(modifier = Modifier.width(16.dp))
//            Text(text = selectedText, Modifier.weight(1f))
//            Spacer(modifier = Modifier.width(16.dp))
//            Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null)
//            Spacer(modifier = Modifier.width(16.dp))
//        }
//        Text(
//            text = "Label",
//            modifier = Modifier
//                .constrainAs(label) {
//                    start.linkTo(button.start, 8.dp)
//                    linkTo(button.top, button.top)
//                }
//                .background(Color.White)
//                .padding(horizontal = 8.dp),
//            style = MaterialTheme.typography.labelSmall,
//        )
//        Box(
//            modifier = Modifier
//                .constrainAs(clickArea) {
//                    width = Dimension.fillToConstraints
//                    height = Dimension.fillToConstraints
//                    linkTo(button.start, button.end)
//                    linkTo(button.top, button.bottom)
//                }
//                .clip(RoundedCornerShape(8.dp))
//                .clickable { onClick() }
//        )
//    }
}

@Preview
@Composable
fun DropDownLayoutPreview() {
    ComposePartyTheme {
        DropDownLayout(expanded = true)
    }
}