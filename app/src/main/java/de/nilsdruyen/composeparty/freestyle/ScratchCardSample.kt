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
        AsyncImage(model = url, contentDescription = null)
        AnimatedVisibility(
            visible = showCanvas,
            exit = fadeOut(tween(3000))
        ) {
            Overlay { showCanvas = false }
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
        if (percentage <= .4f) hideCanvas()
    }

    LaunchedEffect(capturedBitmap) {
        capturedBitmap?.let {
            percentage = percentTransparent(it, 100)
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
            text = "$percentage",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 32.dp),
            color = Color.White,
        )
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

@Preview
@Composable
private fun ScratchCardSamplePreview() {
    ComposePartyTheme {
        ScratchCardSample()
    }
}