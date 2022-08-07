package de.nilsdruyen.composeparty.freestyle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.modifiers.blockify
import de.nilsdruyen.composeparty.nyancat.NyanCatParty

@Composable
fun MeshBlockify() {
    Box(
        modifier = Modifier
            .padding(horizontal = 32.dp, vertical = 8.dp)
            .fillMaxWidth()
            .aspectRatio(2f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .blockify()
    ) {
        NyanCatParty()
    }
}