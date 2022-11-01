package de.nilsdruyen.composeparty.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.utils.Centered

@Composable
fun ChipButton() {
    var isSelected by remember { mutableStateOf(false) }

    Centered {

        Button(
            onClick = { isSelected = !isSelected },
            modifier = Modifier
                .wrapContentSize()
//            .animateContentSize()
        ) {
//        AnimatedContent(targetState = isSelected) { isSelected ->
            Row {
                AnimatedVisibility(visible = isSelected) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        modifier = Modifier.align(Alignment.CenterVertically),
                        tint = Color.White,
                        contentDescription = "Thin checkmark"
                    )
                }
//                if (isSelected) {
//                    Icon(
//                        imageVector = Icons.Filled.Add,
//                        modifier = Modifier.align(Alignment.CenterVertically),
//                        tint = Color.White,
//                        contentDescription = "Thin checkmark"
//                    )
//                }
                Text(
                    text = "Text",
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .align(Alignment.CenterVertically)
                )
            }
//        }
        }
    }
}