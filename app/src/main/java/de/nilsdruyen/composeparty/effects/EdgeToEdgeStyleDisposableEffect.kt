package de.nilsdruyen.composeparty.effects

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode

@Composable
@NonRestartableComposable
fun EdgeToEdgeStyleDisposableEffect(darkSystemBarStyle: Boolean) {
    if (!LocalInspectionMode.current) {
        val activity = LocalContext.current.findActivity() as ComponentActivity
        DisposableEffect(darkSystemBarStyle) {
            activity.setEdgeToEdgeStyle(darkSystemBarStyle)
            onDispose {
                activity.setEdgeToEdgeStyle(!darkSystemBarStyle)
            }
        }
    }
}

tailrec fun Context.findActivity(): Activity = when (this) {
    is Activity -> this
    else -> (this as ContextWrapper).baseContext.findActivity()
}

private fun ComponentActivity.setEdgeToEdgeStyle(darkSystemBarStyle: Boolean) {
    enableEdgeToEdge(
        statusBarStyle = SystemBarStyle.auto(
            lightScrim = Color.TRANSPARENT,
            darkScrim = Color.TRANSPARENT,
            detectDarkMode = { darkSystemBarStyle },
        ),
        navigationBarStyle = SystemBarStyle.auto(
            lightScrim = DefaultLightScrim,
            darkScrim = DefaultDarkScrim,
            detectDarkMode = { darkSystemBarStyle },
        )
    )
}

/**
 * Copy of internal default color [androidx.activity.DefaultLightScrim]
 */
private val DefaultLightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

/**
 * Copy of internal default color [androidx.activity.DefaultDarkScrim]
 */
private val DefaultDarkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b)
