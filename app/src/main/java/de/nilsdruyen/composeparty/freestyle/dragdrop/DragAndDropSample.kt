package de.nilsdruyen.composeparty.freestyle.dragdrop

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlin.math.roundToInt

@Composable
fun DragAndDropSample() {
    LongPressDraggable(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.systemBarsPadding(),
        ) {
            People(
                Modifier
                    .fillMaxWidth()
                    .weight(.6f)
            )
            Products(
                Modifier
                    .fillMaxWidth()
                    .weight(.6f)
            )
        }
    }
}

@Composable
fun People(modifier: Modifier = Modifier) {
    var peopleState by remember { mutableStateOf(peoplesData) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(8.dp),
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(peopleState) { person ->
            PersonCard(
                person = person,
                onDrop = {
                    val index = peopleState.indexOf(person)
                    val products = person.products + it
                    val list = peopleState.toMutableList().apply {
                        this[index] = this[index].copy(products = products)
                    }.toMutableList()
                    peopleState = list
                },
            )
        }
    }
}

@Composable
fun Products(modifier: Modifier = Modifier) {
    val products by remember { mutableStateOf(productsData.map { ProductUi(it) }) }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items(products) { ProductCard(it) }
    }
}

@Composable
fun PersonCard(person: Person, onDrop: (Product) -> Unit) {
    DropTarget<Product>(Modifier) { isInBound, product ->
        val scale = if (isInBound) 1.2f else 1f
        val scaleTransition = animateFloatAsState(
            targetValue = scale,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow,
            ),
            label = "scaleTransition",
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .scale(scaleTransition.value)
        ) {
            AsyncImage(
                model = person.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(32.dp)),
            )
            Text(
                text = person.name,
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.CenterHorizontally),
            )
        }

        person.badge?.let {
            Box(
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .background(Color.Blue.copy(alpha = .8f))
            ) {
                Text(text = it)
            }
        }
    }
}

@Composable
fun ProductCard(productUi: ProductUi) {
    DragTarget(modifier = Modifier, onDrop = {}) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .clip(RoundedCornerShape(50))
                .border(1.dp, MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.surface)
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(productUi.product.name)
            Text(productUi.product.price.toString() + "€")
            Checkbox(checked = false, onCheckedChange = {})
        }
    }
}

data class Person(
    val name: String,
    val imageUrl: String,
    val products: List<Product> = emptyList(),
) {

    val badge: String?
        get() {
            if (products.isEmpty()) return null
            val total = products.sumOf { it.price }
            return "${products.size} ${total.roundToInt()} €"
        }
}

data class Product(
    val name: String,
    val price: Double,
)

data class ProductUi(
    val product: Product,
    val selected: Boolean = false,
)