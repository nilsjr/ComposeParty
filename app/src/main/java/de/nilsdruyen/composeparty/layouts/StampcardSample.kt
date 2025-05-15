package de.nilsdruyen.composeparty.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.R
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@Composable
fun StampcardSample(modifier: Modifier = Modifier) {
    Scaffold {
        Card(
            modifier
                .fillMaxWidth()
                .padding(it)
                .padding(PaddingValues(16.dp))
        ) {
            Spacer(Modifier.size(16.dp))
            Row(Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Friends Pawty collector", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.weight(1f))
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Black)) {
                            append("5")
                        }
                        append(" left")
                    },
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(Modifier.size(16.dp))
            StampList(Modifier.fillMaxWidth())
            Spacer(Modifier.size(16.dp))
            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                )
            ) {
                Text(
                    "\uD83E\uDD73 Yippeeh! You earned a reward will be available from tomorrow, 08:00 o’clock.",
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(Modifier.size(16.dp))
        }
    }
}

private data class Stamp(
    val earned: Boolean,
    val isReward: Boolean,
)

@Composable
private fun StampList(modifier: Modifier = Modifier) {
    val stamps = listOf(
        Stamp(earned = true, isReward = false),
        Stamp(earned = true, isReward = false),
        Stamp(earned = true, isReward = false),
        Stamp(earned = true, isReward = true),
        Stamp(earned = false, isReward = false),
        Stamp(earned = false, isReward = false),
        Stamp(earned = false, isReward = false),
        Stamp(earned = false, isReward = false),
        Stamp(earned = false, isReward = true),
    )
    Box(modifier = modifier) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(20.dp)
                .align(Alignment.Center)
                .background(MaterialTheme.colorScheme.secondaryContainer)
        )
        LazyRow(
            Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            items(stamps) {
                StampItem(it)
            }
        }
    }
}

@Composable
private fun StampItem(stamp: Stamp, modifier: Modifier = Modifier) {
    val backgroundColor = if (stamp.earned) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.secondary
    }
    Box(
        modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .padding(4.dp),
        contentAlignment = Alignment.Center,
    ) {
        val resourceId = when {
            stamp.isReward -> R.drawable.ic_reward
            stamp.earned -> R.drawable.ic_check
            else -> R.drawable.ic_paw
        }
        Icon(
            painter = painterResource(resourceId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Preview
@Composable
private fun StampcardSamplePreview() {
    ComposePartyTheme {
        StampcardSample()
    }
}