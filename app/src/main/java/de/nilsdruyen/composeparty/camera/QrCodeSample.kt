package de.nilsdruyen.composeparty.camera

import android.content.Context
import android.util.Size
import android.view.Surface
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.NoOpUpdate
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import timber.log.Timber
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Composable
fun QrCodeSample(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val previewView = remember { PreviewView(context) }
    val analysisExecutor = Executors.newSingleThreadExecutor()
    val barcodeFormats =
        intArrayOf(Barcode.FORMAT_QR_CODE, Barcode.FORMAT_EAN_8, Barcode.FORMAT_EAN_13)

    val configuration = LocalConfiguration.current
    val screenSize = Size(configuration.screenWidthDp, configuration.screenHeightDp)
    val rotation = when (configuration.orientation) {
        in 45..134 -> Surface.ROTATION_270
        in 135..224 -> Surface.ROTATION_180
        in 225..314 -> Surface.ROTATION_90
        else -> Surface.ROTATION_0
    }

    val preview = Preview.Builder()
        .setResolutionSelector(
            ResolutionSelector.Builder()
                .setResolutionStrategy(ResolutionStrategy.HIGHEST_AVAILABLE_STRATEGY)
                .setResolutionFilter { supportedSizes, _ ->
                    supportedSizes.filter {
                        it.width <= screenSize.width && it.height <= screenSize.height
                    }
                }
                .build()
        )
        .setTargetRotation(rotation)
        .build()

    val imageAnalysis = ImageAnalysis.Builder()
        .setResolutionSelector(
            ResolutionSelector.Builder().setResolutionStrategy(
                ResolutionStrategy(
                    Size(1280, 720),
                    ResolutionStrategy.FALLBACK_RULE_CLOSEST_HIGHER_THEN_LOWER
                )
            ).build()
        )
        .build()
        .also {
            it.setAnalyzer(
                analysisExecutor,
                QRCodeAnalyzer(
                    barcodeFormats = barcodeFormats,
                    onSuccess = { barcode ->
                        it.clearAnalyzer()
                        Timber.d("BARCODE: ${barcode.rawValue}")
                    },
                    onFailure = { exception ->
//                        onFailure(exception)
                    },
                    onPassCompleted = { failureOccurred ->
//                        onPassCompleted(failureOccurred)
                    }
                )
            )
        }

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            CameraSelector.DEFAULT_BACK_CAMERA,
            preview,
            imageAnalysis
        )
        preview.surfaceProvider = previewView.surfaceProvider
    }

    LifecycleStartEffect(Unit) {
        onStopOrDispose {
            analysisExecutor.shutdown()
        }
    }

    Box(modifier.fillMaxSize()) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            update = NoOpUpdate
        )
    }
}

internal suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                try {
                    val provider = cameraProvider.get()
                    continuation.resume(provider)
                } catch (e: Exception) {
                    continuation.resumeWithException(e)
                }
            }, ContextCompat.getMainExecutor(this))
        }
    }

internal class QRCodeAnalyzer(
    private val barcodeFormats: IntArray,
    private val onSuccess: ((Barcode) -> Unit),
    private val onFailure: ((Exception) -> Unit),
    private val onPassCompleted: ((Boolean) -> Unit)
) : ImageAnalysis.Analyzer {

    private val barcodeScanner by lazy {
        val optionsBuilder = if (barcodeFormats.size > 1) {
            BarcodeScannerOptions.Builder()
                .setBarcodeFormats(barcodeFormats.first(), *barcodeFormats.drop(1).toIntArray())
        } else {
            BarcodeScannerOptions.Builder()
                .setBarcodeFormats(barcodeFormats.firstOrNull() ?: Barcode.FORMAT_UNKNOWN)
        }
        try {
            BarcodeScanning.getClient(optionsBuilder.build())
        } catch (e: Exception) { // catch if for some reason MlKitContext has not been initialized
            onFailure(e)
            null
        }
    }

    @Volatile
    private var failureOccurred = false
    private var failureTimestamp = 0L

    @ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        if (imageProxy.image == null) return

        // throttle analysis if error occurred in previous pass
        if (failureOccurred && System.currentTimeMillis() - failureTimestamp < 1000L) {
            imageProxy.close()
            return
        }

        failureOccurred = false
        barcodeScanner?.let { scanner ->
            scanner.process(imageProxy.toInputImage())
                .addOnSuccessListener { codes ->
                    codes.firstNotNullOfOrNull { it }?.let { onSuccess(it) }
                }
                .addOnFailureListener {
                    failureOccurred = true
                    failureTimestamp = System.currentTimeMillis()
                    onFailure(it)
                }
                .addOnCompleteListener {
                    onPassCompleted(failureOccurred)
                    imageProxy.close()
                }
        }
    }

    @ExperimentalGetImage
    @Suppress("UnsafeCallOnNullableType")
    private fun ImageProxy.toInputImage() =
        InputImage.fromMediaImage(image!!, imageInfo.rotationDegrees)
}