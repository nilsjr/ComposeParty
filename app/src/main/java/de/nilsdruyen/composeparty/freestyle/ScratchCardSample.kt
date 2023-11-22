package de.nilsdruyen.composeparty.freestyle

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.View
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme
import timber.log.Timber

const val url = "https://picsum.photos/id/31/1036/2021"

enum class MotionEvent {
    Idle, Down, Move, Up
}

@Composable
fun ScratchCardSample() {
    var showCanvas by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(Color.Black),
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(
            model = url,
            contentDescription = null,
        )
        AnimatedVisibility(
            visible = showCanvas,
            exit = fadeOut(tween(3000))
        ) {
            Overlay {
                showCanvas = false
            }
        }
    }
}

@Composable
fun Overlay(hideCanvas: () -> Unit) {
    val currentView = LocalView.current
    var captureIt by remember { mutableStateOf(false) }
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var percentage by remember { mutableFloatStateOf(1f) }
    val paths = remember { mutableStateListOf<Path>() }
    var currentPath by remember { mutableStateOf<Path?>(null) }
    var currentPosition by remember { mutableStateOf(Offset.Unspecified) }
    var previousPosition by remember { mutableStateOf(Offset.Unspecified) }
    var motionEvent by remember { mutableStateOf(MotionEvent.Idle) }

    LaunchedEffect(motionEvent) {
        Timber.d("event: $motionEvent")
    }

    LaunchedEffect(percentage) {
        if (percentage <= .5f) hideCanvas()
    }

    LaunchedEffect(capturedBitmap) {
        capturedBitmap?.let {
            percentage = percentTransparent(it, 50)
            Timber.d("\uD83D\uDD25 OH SHIT $percentage")
            capturedBitmap = null
        }
    }

    LaunchedEffect(captureIt) {
        if (captureIt) {
            captureIt = false
            currentView.capture(
                onBitmapReady = { bitmap: Bitmap -> capturedBitmap = bitmap },
                onBitmapError = {}
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            motionEvent = MotionEvent.Down
                            currentPosition = it
                            previousPosition = it
                            currentPath = Path().apply {
                                moveTo(currentPosition.x, currentPosition.y)
                            }
                        },
                        onDrag = { change, _ ->
                            motionEvent = MotionEvent.Move
                            currentPosition = change.position
                            change.consume()
                        },
                        onDragEnd = {
                            motionEvent = MotionEvent.Up
                            captureIt = true
                        },
                    )
                }
        ) {
            when (motionEvent) {
                MotionEvent.Idle -> Unit
                MotionEvent.Down -> {
                    previousPosition = currentPosition
                }

                MotionEvent.Move -> {
                    if (currentPosition == Offset.Unspecified) {
                        currentPosition = Offset(200f, 200f)
                    }
                    if (previousPosition != Offset.Unspecified) {
                        currentPath?.quadraticBezierTo(
                            previousPosition.x,
                            previousPosition.y,
                            (previousPosition.x + currentPosition.x) / 2,
                            (previousPosition.y + currentPosition.y) / 2
                        )
                    }
                    previousPosition = currentPosition
                }

                MotionEvent.Up -> {
                    currentPath?.lineTo(currentPosition.x, currentPosition.y)
                    currentPath?.let { paths.add(it) }
                    currentPath = null

                    currentPosition = Offset.Unspecified
                    previousPosition = currentPosition

                    motionEvent = MotionEvent.Idle
                }
            }

            with(drawContext.canvas.nativeCanvas) {
                val checkPoint = saveLayer(null, null)
                drawRect(
                    color = Color.Black,
                    size = drawContext.size,
                )
                paths.forEach {
                    drawPath(
                        color = Color.Transparent,
                        path = it,
                        style = Stroke(
                            width = 120f,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round,
                        ),
                        blendMode = BlendMode.Clear
                    )
                }
                currentPath?.let {
                    drawPath(
                        color = Color.Transparent,
                        path = it,
                        style = Stroke(
                            width = 120f,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round,
                        ),
                        blendMode = BlendMode.Clear
                    )
                }
                restoreToCount(checkPoint)
            }
        }
        Text(
            text = "$percentage %",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 32.dp),
            color = Color.White,
        )

    }
}

//        BoxWithConstraints(
//            Modifier
//                .fillMaxSize()
//                .padding(8.dp)
//                .pointerInput("dragging") {
//                    detectDragGestures(
//                        onDragStart = {
//                            pressed = true
//                            pointerOffset = it
//                        },
//                        onDragEnd = {
//                            pressed = false
//                        }
//                    ) { change, dragAmount ->
//                        pointerOffset += dragAmount
//                    }
//                }
////                .onSizeChanged {
////                    pointerOffset = Offset(it.width / 2f, it.height / 2f)
////                }
////                .graphicsLayer(alpha = 0.99f)
//                .graphicsLayer {
//                    compositingStrategy = CompositingStrategy.Offscreen
//                }
//                .drawWithContent {
//                    drawContent()
//                    // draws a fully black area with a small keyhole at pointerOffset that’ll show part of the UI.
////                    if (radius == 0.dp) {
////                        drawRect(Color.Black)
////                    } else {
////                        drawRect(
////                            brush = Brush.radialGradient(
////                                listOf(Color.Transparent, Color.Black),
////                                center = pointerOffset,
////                                radius = radius.toPx(),
////                            ),
//////                        cornerRadius = CornerRadius(cornerRadius, cornerRadius),
////                        )
////                    }
//
////                    drawRect(
////                        color = Color.Black,
////                        size = Size(size.width / 2f, 100f),
////                        blendMode = BlendMode.SrcIn
////                    )
//                }
////                .drawWithCache {
////                    val path = Path()
////
////                    path.moveTo(0f, 0f)
////                    path.lineTo(size.width / 2f, size.height / 2f)
////                    path.lineTo(size.width, 0f)
////                    path.close()
////
////                    onDrawBehind {
////                        drawPath(path, Color.Magenta, style = Stroke(width = 10f))
//            //                    }
////                }
//        ) {
//            val size = with(LocalDensity.current) {
//                Size(maxWidth.toPx(), maxHeight.toPx())
//            }
//            val url = "https://picsum.photos/id/31/${size.width.toInt()}/${size.height.toInt()}"
//            Timber.d("\uD83D\uDD25 load $url")
////            AsyncImage(
////                model = url,
////                contentDescription = null,
////                modifier = Modifier.fillMaxSize(),
////                contentScale = ContentScale.Crop,
////            )
//        }

//data class Line(val start: Offset, val end: Offset)
//
//@Composable
//fun DrawMe() {
//    val currentView = LocalView.current
//    var captureIt by remember { mutableStateOf(false) }
//    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
//    var percentage by remember { mutableFloatStateOf(1f) }
//    var showCanvas by remember { mutableStateOf(true) }
//    val fadeOut by remember(percentage) { derivedStateOf { percentage <= 0.5f } }
//    val alpha = animateFloatAsState(
//        targetValue = if (fadeOut) 0f else 1f,
//        animationSpec = tween(durationMillis = 2000),
//        label = "animatedAlpha",
//    ) {
//        showCanvas = false
//    }
//
//    LaunchedEffect(capturedBitmap) {
//        capturedBitmap?.let {
//            percentage = percentTransparent(it, 200)
//            Timber.d("OH SHIT $percentage")
//            capturedBitmap = null
//        }
//    }
//
//    LaunchedEffect(captureIt) {
//        if (captureIt) {
//            captureIt = false
//            currentView.capture(
//                onBitmapReady = { bitmap: Bitmap -> capturedBitmap = bitmap },
//                onBitmapError = {}
//            )
//        }
//    }
//
//    Box(
//        Modifier
//            .fillMaxSize()
//            .background(Color.Blue.copy(alpha = .7f))
//    ) {
////        if (showCanvas) {
//        RubbMe(
//            modifier = Modifier
//                .fillMaxSize()
//                .alpha(.6f),
//            alpha = alpha,
//        ) { captureIt = true }
////        }
//        Text(
//            text = "$percentage %",
//            modifier = Modifier
//                .align(Alignment.TopCenter)
//                .padding(top = 32.dp),
//            color = Color.White,
//        )
//    }
//}
//
//@Composable
//fun RubbMe(
//    modifier: Modifier,
//    alpha: State<Float>,
//    capture: () -> Unit,
//) {
//    val lines = remember { mutableStateListOf<Line>() }
//    val imagePainter = rememberAsyncImagePainter(
//        model = ImageRequest.Builder(LocalContext.current)
//            .data(url)
//            .size(Size.ORIGINAL)
//            .build()
//    )
//    val state = imagePainter.state
//    if (state is AsyncImagePainter.State.Success) {
//        val overlayImageBitmap =
//            state.result.drawable
//                .toBitmap()
//                .asImageBitmap()
//        val imageBrush = ShaderBrush(ImageShader(overlayImageBitmap))
//
//        Canvas(
//            modifier = modifier
//                .fillMaxSize()
//                .background(Color.Red)
//                .pointerInput("drawing") {
//                    detectDragGestures(
//                        onDragEnd = {
//                            capture()
//                        }
//                    ) { change, dragAmount ->
//                        change.consume()
//
//                        val line = Line(
//                            start = change.position - dragAmount,
//                            end = change.position
//                        )
//                        lines.add(line)
//                    }
//                }
//        ) {
//            drawImage(overlayImageBitmap)
//            drawRect(
//                color = Color.Black,
//                size = this.size,
//                alpha = alpha.value,
//            )
//            lines.forEach {
//                drawLine(
//                    brush = imageBrush,
//                    start = it.start,
//                    end = it.end,
//                    strokeWidth = 120f,
//                    cap = StrokeCap.Round,
//                    alpha = alpha.value,
//                )
//            }
//        }
//    }
//}

//@Composable
//private fun MyComposable() {
//    val overlayImage = "https://picsum.photos/id/31/1036/2021"
//    val baseImage = "https://picsum.photos/id/31/1036/2021"
//
//    val overlayPainter = rememberAsyncImagePainter(
//        model = ImageRequest.Builder(LocalContext.current)
//            .data(overlayImage)
//            .size(coil.size.Size.ORIGINAL) // Set the target size to load the image at.
//            .build()
//    )
//    val basePainter = rememberAsyncImagePainter(
//        model = ImageRequest.Builder(LocalContext.current)
//            .data(baseImage)
//            .size(coil.size.Size.ORIGINAL) // Set the target size to load the image at.
//            .build()
//    )
//
//    val overlayImageLoadedState = overlayPainter.state
//    val baseImageLoadedState = basePainter.state
//
//    if (
//        baseImageLoadedState is AsyncImagePainter.State.Success &&
//        overlayImageLoadedState is AsyncImagePainter.State.Success
//    ) {
//
//        SideEffect {
//            println("🔥 COMPOSING...")
//        }
//
//        val baseImageBitmap =
//            baseImageLoadedState.result.drawable.toBitmap()
//                .asImageBitmap()
//        val overlayImageBitmap =
//            overlayImageLoadedState.result.drawable
//                .toBitmap()
//                .asImageBitmap()
//
//        EraseBitmapSample(
//            baseImageBitmap = baseImageBitmap,
//            overlayImageBitmap = overlayImageBitmap,
//            modifier = Modifier
//                .fillMaxWidth()
//                .aspectRatio(4 / 3f)
//        )
//    }
//}
//
//@Composable
//fun EraseBitmapSample(
//    overlayImageBitmap: ImageBitmap,
//    baseImageBitmap: ImageBitmap,
//    modifier: Modifier
//) {
//
//    var matchPercent by remember {
//        mutableStateOf(100f)
//    }
//
//    BoxWithConstraints(modifier) {
//
//        // Path used for erasing. In this example erasing is faked by drawing with canvas color
//        // above draw path.
//        val erasePath = remember { Path() }
//
//        var motionEvent by remember { mutableStateOf(de.nilsdruyen.composeparty.freestyle.MotionEvent.Idle) }
//        // This is our motion event we get from touch motion
//        var currentPosition by remember { mutableStateOf(Offset.Unspecified) }
//        // This is previous motion event before next touch is saved into this current position
//        var previousPosition by remember { mutableStateOf(Offset.Unspecified) }
//
//        val imageWidth = constraints.maxWidth
//        val imageHeight = constraints.maxHeight
//
//
//        val drawImageBitmap = remember {
//            Bitmap.createScaledBitmap(
//                overlayImageBitmap.asAndroidBitmap(),
//                imageWidth,
//                imageHeight,
//                false
//            )
//                .asImageBitmap()
//        }
//
//        // Pixels of scaled bitmap, we scale it to composable size because we will erase
//        // from Composable on screen
//        val originalPixels: IntArray = remember {
//            val buffer = IntArray(imageWidth * imageHeight)
//            drawImageBitmap
//                .readPixels(
//                    buffer = buffer,
//                    startX = 0,
//                    startY = 0,
//                    width = imageWidth,
//                    height = imageHeight
//                )
//
//            buffer
//        }
//
//        val erasedBitmap: ImageBitmap = remember {
//            Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888).asImageBitmap()
//        }
//
//        val canvas: Canvas = remember {
//            Canvas(erasedBitmap)
//        }
//
//        val paint = remember {
//            Paint()
//        }
//
//        val erasePaint = remember {
//            Paint().apply {
//                blendMode = BlendMode.Clear
//                this.style = PaintingStyle.Stroke
//                strokeWidth = 30f
//            }
//        }
//
//
//        canvas.apply {
//            val nativeCanvas = this.nativeCanvas
//            val canvasWidth = nativeCanvas.width.toFloat()
//            val canvasHeight = nativeCanvas.height.toFloat()
//
//
//            when (motionEvent) {
//
//                MotionEvent.Down -> {
//                    erasePath.moveTo(currentPosition.x, currentPosition.y)
//                    previousPosition = currentPosition
//
//                }
//                MotionEvent.Move -> {
//
//                    erasePath.quadraticBezierTo(
//                        previousPosition.x,
//                        previousPosition.y,
//                        (previousPosition.x + currentPosition.x) / 2,
//                        (previousPosition.y + currentPosition.y) / 2
//
//                    )
//                    previousPosition = currentPosition
//                }
//
//                MotionEvent.Up -> {
//                    erasePath.lineTo(currentPosition.x, currentPosition.y)
//                    currentPosition = Offset.Unspecified
//                    previousPosition = currentPosition
//                    motionEvent = MotionEvent.Idle
//
////                    matchPercent = compareBitmaps(
////                        originalPixels,
////                        erasedBitmap,
////                        imageWidth,
////                        imageHeight
////                    )
//                }
//                else -> Unit
//            }
//
//            with(canvas.nativeCanvas) {
//                drawColor(android.graphics.Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
//
//                drawImageRect(
//                    image = drawImageBitmap,
//                    dstSize = IntSize(canvasWidth.toInt(), canvasHeight.toInt()),
//                    paint = paint
//                )
//
//                drawPath(
//                    path = erasePath,
//                    paint = erasePaint
//                )
//            }
//        }
//
//        val canvasModifier = Modifier.pointerInput(Unit){
//            detectDragGestures(
//                onDragStart = { offset ->
//                    motionEvent = MotionEvent.Down
//                    currentPosition = offset
//                },
//                onDrag = { pointerInputChange, offset ->
//                    motionEvent = MotionEvent.Move
//                    currentPosition = pointerInputChange.position
//                    pointerInputChange.consume()
//                },
//                onDragEnd = {
//                    motionEvent = MotionEvent.Up
//                },
//            )
//        }
//
//        Image(
//            bitmap = baseImageBitmap,
//            contentDescription = null
//        )
//
//        Image(
//            modifier = canvasModifier
//                .clipToBounds()
//                .matchParentSize()
//                .border(2.dp, Color.Green),
//            bitmap = erasedBitmap,
//            contentDescription = null,
//            contentScale = ContentScale.FillBounds
//        )
//
//    }
//
//    Text(
//        text = "Bitmap match ${matchPercent}%",
//        color = Color.Red,
//        fontSize = 22.sp,
//    )
//}


@Preview
@Composable
private fun ScratchCardSamplePreview() {
    ComposePartyTheme {
        ScratchCardSample()
    }
}

private fun percentTransparent(bm: Bitmap, scale: Int): Float {
    val width = bm.width
    val height = bm.height

    // size of sample rectangles
    val xStep = width / scale
    val yStep = height / scale

    // center of the first rectangle
    val xInit = xStep / 2
    val yInit = yStep / 2

    // center of the last rectangle
    val xEnd = width - xStep / 2
    val yEnd = height - yStep / 2
    var totalTransparent = 0
    var x = xInit
    while (x <= xEnd) {
        var y = yInit
        while (y <= yEnd) {
            if (bm.getPixel(x, y) == android.graphics.Color.BLACK) {
                totalTransparent++
            }
            y += yStep
        }
        x += xStep
    }
    return totalTransparent.toFloat() / (scale * scale)
}

private fun View.capture(onBitmapReady: (Bitmap) -> Unit, onBitmapError: (Exception) -> Unit) {
    try {
        val temporalBitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)

        val location = IntArray(2)
        this.getLocationInWindow(location)
        val viewRectangle =
            Rect(location[0], location[1], location[0] + this.width, location[1] + this.height)
        val onPixelCopyListener: PixelCopy.OnPixelCopyFinishedListener =
            PixelCopy.OnPixelCopyFinishedListener { copyResult ->
                if (copyResult == PixelCopy.SUCCESS) {
                    onBitmapReady(temporalBitmap)
                } else {
                    error("Error while copying pixels, copy result: $copyResult")
                }
            }

        PixelCopy.request(
            (this.context as Activity).window,
            viewRectangle,
            temporalBitmap,
            onPixelCopyListener,
            Handler(Looper.getMainLooper())
        )
    } catch (exception: Exception) {
        onBitmapError(exception)
    }
}