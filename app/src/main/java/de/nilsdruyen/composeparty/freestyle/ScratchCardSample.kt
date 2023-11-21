package de.nilsdruyen.composeparty.freestyle

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.View
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme
import timber.log.Timber

val url = "https://picsum.photos/id/31/1036/2021"

@Composable
fun ScratchCardSample() {
//    var pointerOffset by remember { mutableStateOf(Offset.Zero) }
//    var pressed by remember { mutableStateOf(false) }
//    val radius by animateDpAsState(
//        targetValue = if (pressed) 120.dp else 0.dp,
//        label = "radiusAnimation"
//    )
//    val position by animateIntAsState(targetValue = 0)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(Color.Black),
        contentAlignment = Alignment.Center,
    ) {
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

        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
        )
        DrawMe()
    }

}

data class Line(val start: Offset, val end: Offset)

@Composable
fun DrawMe() {
    val currentView = LocalView.current
    var captureIt by remember { mutableStateOf(false) }
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var percentage by remember { mutableFloatStateOf(1f) }
    var showCanvas by remember { mutableStateOf(true) }
    val fadeOut by remember(percentage) { derivedStateOf { percentage <= 0.5f } }
    val alpha = animateFloatAsState(
        targetValue = if (fadeOut) 0f else 1f,
        animationSpec = tween(durationMillis = 2000),
        label = "animatedAlpha",
    ) {
        showCanvas = false
    }

    LaunchedEffect(capturedBitmap) {
        capturedBitmap?.let {
            percentage = percentTransparent(it, 200)
            Timber.d("OH SHIT $percentage")
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

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Blue.copy(alpha = .7f))
    ) {
//        if (showCanvas) {
        RubbMe(
            modifier = Modifier
                .fillMaxSize()
                .alpha(.6f),
            alpha = alpha,
        ) { captureIt = true }
//        }
        Text(
            text = "$percentage %",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 32.dp),
            color = Color.White,
        )
    }
}

@Composable
fun RubbMe(
    modifier: Modifier,
    alpha: State<Float>,
    capture: () -> Unit,
) {
    val lines = remember { mutableStateListOf<Line>() }
    val imagePainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .size(Size.ORIGINAL)
            .build()
    )
    val state = imagePainter.state
    if (state is AsyncImagePainter.State.Success) {
        val overlayImageBitmap =
            state.result.drawable
                .toBitmap()
                .asImageBitmap()
        val imageBrush = ShaderBrush(ImageShader(overlayImageBitmap))

        Canvas(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Red)
                .pointerInput("drawing") {
                    detectDragGestures(
                        onDragEnd = {
                            capture()
                        }
                    ) { change, dragAmount ->
                        change.consume()

                        val line = Line(
                            start = change.position - dragAmount,
                            end = change.position
                        )
                        lines.add(line)
                    }
                }
        ) {
            drawImage(overlayImageBitmap)
            drawRect(
                color = Color.Black,
                size = this.size,
                alpha = alpha.value,
            )
            lines.forEach {
                drawLine(
                    brush = imageBrush,
                    start = it.start,
                    end = it.end,
                    strokeWidth = 120f,
                    cap = StrokeCap.Round,
                    alpha = alpha.value,
                )
            }
        }
    }
}

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


//class PathProperties(
//    var strokeWidth: Float = 20f,
//    var color: Color = Color.Black,
//    var alpha: Float = 1f,
//    var strokeCap: StrokeCap = StrokeCap.Round,
//    var strokeJoin: StrokeJoin = StrokeJoin.Round,
//) {
//
//    fun copy(
//        strokeWidth: Float = this.strokeWidth,
//        color: Color = this.color,
//        alpha: Float = this.alpha,
//        strokeCap: StrokeCap = this.strokeCap,
//        strokeJoin: StrokeJoin = this.strokeJoin,
//    ) = PathProperties(
//        strokeWidth, color, alpha, strokeCap, strokeJoin,
//    )
//}
//
//enum class MotionEvent {
//    Idle, Down, Move, Up
//}
//
//enum class DrawMode {
//    Draw, Touch, Erase
//}
//
//@Composable
//fun DrawingApp() {
//
//    /**
//     * Paths that are added, this is required to have paths with different options and paths
//     *  ith erase to keep over each other
//     */
//    val paths = remember { mutableStateListOf<Pair<androidx.compose.ui.graphics.Path, PathProperties>>() }
//
//    /**
//     * Paths that are undone via button. These paths are restored if user pushes
//     * redo button if there is no new path drawn.
//     *
//     * If new path is drawn after this list is cleared to not break paths after undoing previous
//     * ones.
//     */
//    val pathsUndone = remember { mutableStateListOf<Pair<androidx.compose.ui.graphics.Path, PathProperties>>() }
//
//    /**
//     * Canvas touch state. [MotionEvent.Idle] by default, [MotionEvent.Down] at first contact,
//     * [MotionEvent.Move] while dragging and [MotionEvent.Up] when first pointer is up
//     */
//    var motionEvent by remember { mutableStateOf(MotionEvent.Idle) }
//
//    /**
//     * Current position of the pointer that is pressed or being moved
//     */
//    var currentPosition by remember { mutableStateOf(Offset.Unspecified) }
//
//    /**
//     * Previous motion event before next touch is saved into this current position.
//     */
//    var previousPosition by remember { mutableStateOf(Offset.Unspecified) }
//
//    /**
//     * Draw mode, erase mode or touch mode to
//     */
//    var drawMode by remember { mutableStateOf(DrawMode.Draw) }
//
//    /**
//     * Path that is being drawn between [MotionEvent.Down] and [MotionEvent.Up]. When
//     * pointer is up this path is saved to **paths** and new instance is created
//     */
//    var currentPath by remember { mutableStateOf(Path()) }
//
//    /**
//     * Properties of path that is currently being drawn between
//     * [MotionEvent.Down] and [MotionEvent.Up].
//     */
//    var currentPathProperty by remember { mutableStateOf(PathProperties()) }
//
//    Column(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Canvas(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.White)
//                .padding(8.dp)
//                .pointerInput(Unit) {
//                    detectDragGestures(
//                        onDragStart = { offset ->
//                            motionEvent = MotionEvent.Down
//                            currentPosition = offset
//                            previousPosition = offset
//                        },
//                        onDrag = { pointerInputChange, offset ->
//                            motionEvent = MotionEvent.Move
//                            currentPosition = pointerInputChange.position
//
//                            if (drawMode == DrawMode.Touch) {
//                                val change = pointerInputChange.positionChange()
//                                Timber.d("DRAG: $change")
//                                paths.forEach { entry ->
//                                    val path: androidx.compose.ui.graphics.Path = entry.first
//                                    path.translate(change)
//                                }
//                                currentPath.translate(change)
//                            }
//                            pointerInputChange.consume()
//                        },
//                        onDragEnd = {
//                            motionEvent = MotionEvent.Up
//                        }
//                    )
//                }) {
//            when (motionEvent) {
//                MotionEvent.Down -> {
//                    if (drawMode != DrawMode.Touch) {
//                        currentPath.moveTo(currentPosition.x, currentPosition.y)
//                    }
//                    previousPosition = currentPosition
//                }
//
//                MotionEvent.Move -> {
//                    if (drawMode != DrawMode.Touch) {
//                        if (previousPosition != Offset.Unspecified) {
//                            currentPath.quadraticBezierTo(
//                                previousPosition.x,
//                                previousPosition.y,
//                                (previousPosition.x + currentPosition.x) / 2,
//                                (previousPosition.y + currentPosition.y) / 2
//                            )
//                        }
//                    }
//                    previousPosition = currentPosition
//                }
//
//                MotionEvent.Up -> {
//                    if (drawMode != DrawMode.Touch) {
//                        currentPath.lineTo(currentPosition.x, currentPosition.y)
//
//                        // Pointer is up save current path
////                        paths[currentPath] = currentPathProperty
//                        paths.add(Pair(currentPath, currentPathProperty))
//
//                        // Since paths are keys for map, use new one for each key
//                        // and have separate path for each down-move-up gesture cycle
//                        currentPath = Path()
//
//                        // Create new instance of path properties to have new path and properties
//                        // only for the one currently being drawn
//                        currentPathProperty = PathProperties(
//                            strokeWidth = currentPathProperty.strokeWidth,
//                            color = currentPathProperty.color,
//                            strokeCap = currentPathProperty.strokeCap,
//                            strokeJoin = currentPathProperty.strokeJoin,
//                        )
//                    }
//
//                    // Since new path is drawn no need to store paths to undone
//                    pathsUndone.clear()
//
//                    // If we leave this state at MotionEvent.Up it causes current path to draw
//                    // line from (0,0) if this composable recomposes when draw mode is changed
//                    currentPosition = Offset.Unspecified
//                    previousPosition = currentPosition
//                    motionEvent = MotionEvent.Idle
//                }
//
//                else -> Unit
//            }
//
//            with(drawContext.canvas.nativeCanvas) {
//                val checkPoint = saveLayer(null, null)
//
//                paths.forEach {
//                    val path = it.first
//                    val property = it.second
//
//                    drawPath(
//                        color = property.color,
//                        path = path,
//                        style = Stroke(
//                            width = property.strokeWidth,
//                            cap = property.strokeCap,
//                            join = property.strokeJoin
//                        )
//                    )
//                }
//
//                if (motionEvent != MotionEvent.Idle) {
//                    drawPath(
//                        color = currentPathProperty.color,
//                        path = currentPath,
//                        style = Stroke(
//                            width = currentPathProperty.strokeWidth,
//                            cap = currentPathProperty.strokeCap,
//                            join = currentPathProperty.strokeJoin
//                        )
//                    )
//                }
//                restoreToCount(checkPoint)
//            }
//        }
//    }
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