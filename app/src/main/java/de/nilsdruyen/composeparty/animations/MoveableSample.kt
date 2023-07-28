package de.nilsdruyen.composeparty.animations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

val image =
    "https://i.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI"

@Composable
fun MoveableSample() {
//    OrbitalSharedElementTransitionExample()
}

//@Composable
//private fun OrbitalSharedElementTransitionExample() {
//    var isTransformed by rememberSaveable { mutableStateOf(false) }
//    val poster = rememberContentWithOrbitalScope {
//        AsyncImage(
//            model = ImageRequest.Builder(LocalContext.current)
//                .data(image)
//                .crossfade(true)
//                .build(),
//            contentDescription = null,
//            modifier = if (isTransformed) {
//                Modifier.fillMaxSize()
//            } else {
//                Modifier.size(130.dp, 220.dp)
//            }.animateSharedElementTransition(
//                this, SpringSpec(stiffness = 500f), SpringSpec(stiffness = 500f)
//            ),
//            contentScale = ContentScale.Crop
//        )
//    }
//
//    Orbital(
//        modifier = Modifier
//            .fillMaxSize()
//            .navigationBarsPadding()
//            .clickable { isTransformed = !isTransformed }
//    ) {
//        if (isTransformed) {
//            Details(sharedElementContent = { poster() }, pressOnBack = {})
//        } else {
//            Column(
//                Modifier
//                    .fillMaxSize()
//                    .padding(20.dp),
//                horizontalAlignment = Alignment.End,
//                verticalArrangement = Arrangement.Bottom
//            ) {
//                poster()
//                AsyncImage(
//                    model = ImageRequest.Builder(LocalContext.current)
//                        .data(image)
//                        .crossfade(true)
//                        .build(),
//                    contentDescription = null,
//                    modifier = Modifier.size(130.dp, 220.dp),
//                    contentScale = ContentScale.Crop
//                )
//            }
//        }
//    }
//}

@Composable
fun Details(
    sharedElementContent: @Composable () -> Unit,
    pressOnBack: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            sharedElementContent()
        }
    }
}