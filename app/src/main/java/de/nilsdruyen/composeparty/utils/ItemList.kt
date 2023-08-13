package de.nilsdruyen.composeparty.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.demoItems
import de.nilsdruyen.composeparty.isles.IsleExample
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Items(onItemClicked: (String) -> Unit, showSettings: () -> Unit) {
    val barState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(barState)
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Compose party") },
                actions = {
                    IconButton(onClick = showSettings) {
                        Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        ItemList(Modifier.padding(it), onItemClicked)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemList(modifier: Modifier, onItemClicked: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        demoItems
            .filter { it.key != "settings" }
            .forEach {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .height(42.dp),
                    onClick = { onItemClicked(it.key) }
                ) {
                    Box(modifier = Modifier.fillMaxHeight()) {
                        Text(
                            text = it.key,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 16.dp),
                        )
                    }
                }
            }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposePartyTheme {
        IsleExample()
    }
}