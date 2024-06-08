package de.nilsdruyen.composeparty.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme
import de.nilsdruyen.composeparty.utils.Centered

private val items = listOf("Nils", "Kristian", "Johannes", "Thomas")

@Composable
fun DropDownLayout() {
    var selectedText by remember { mutableStateOf(items.first()) }
    var expanded by remember { mutableStateOf(false) }

    Centered {
        Box {
            DropDownSelectedItem(selectedText) {
                expanded = true
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                items.forEach {
                    DropdownMenuItem(text = { Text(it) }, onClick = { selectedText = it })
                }
            }
        }
    }
}

@Composable
fun DropDownSelectedItem(selectedText: String, onClick: () -> Unit) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth(.7f)) {
        val (button, clickArea, label) = createRefs()
        Row(
            modifier = Modifier
                .constrainAs(button) {
                    width = Dimension.fillToConstraints
                    height = Dimension.value(54.dp)
                    linkTo(parent.top, parent.bottom)
                    linkTo(parent.start, parent.end)
                }
                .border(1.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Icon(imageVector = Icons.Default.Face, contentDescription = null)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = selectedText, Modifier.weight(1f))
            Spacer(modifier = Modifier.width(16.dp))
            Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null)
            Spacer(modifier = Modifier.width(16.dp))
        }
        Text(
            text = "Label",
            modifier = Modifier
                .constrainAs(label) {
                    start.linkTo(button.start, 8.dp)
                    linkTo(button.top, button.top)
                }
                .background(Color.White)
                .padding(horizontal = 8.dp),
            style = MaterialTheme.typography.labelSmall,
        )
        Box(
            modifier = Modifier
                .constrainAs(clickArea) {
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                    linkTo(button.start, button.end)
                    linkTo(button.top, button.bottom)
                }
                .clip(RoundedCornerShape(8.dp))
                .clickable { onClick() }
        )
    }
}

@Preview
@Composable
fun DropDownLayoutPreview() {
    ComposePartyTheme {
        DropDownLayout()
    }
}