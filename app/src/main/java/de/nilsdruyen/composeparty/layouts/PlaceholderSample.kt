package de.nilsdruyen.composeparty.layouts

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material3.fade
import com.google.accompanist.placeholder.material3.placeholder
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme
import de.nilsdruyen.composeparty.utils.Centered

@Composable
fun PlaceholderSample() {
    var visible by remember {
        mutableStateOf(false)
    }
    Centered {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .heightIn(80.dp)
                .border(width = 1.dp, shape = RoundedCornerShape(16.dp), color = Color.LightGray)
                .padding(16.dp)
                .clickable { visible = !visible },
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Nils is super toll",
                modifier = Modifier.placeholder(
                    visible = visible,
                    highlight = PlaceholderHighlight.fade(),
                ),
                style = MaterialTheme.typography.headlineLarge,
            )
            Text(
                "Test underline",
                modifier = Modifier.placeholder(
                    visible = visible,
                    highlight = PlaceholderHighlight.fade(),
                ),
            )
            Text(
                "Test underline",
                modifier = Modifier.placeholder(
                    visible = visible,
                    highlight = PlaceholderHighlight.fade(),
                ),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F6F6)
@Composable
fun PlaceholderSamplePreview() {
    ComposePartyTheme {
        PlaceholderSample()
    }
}