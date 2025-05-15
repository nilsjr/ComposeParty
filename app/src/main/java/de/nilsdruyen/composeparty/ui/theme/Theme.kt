package de.nilsdruyen.composeparty.ui.theme

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.effects.EdgeToEdgeStyleDisposableEffect

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun ComposePartyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    EdgeToEdgeStyleDisposableEffect(darkSystemBarStyle = darkTheme)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Preview
@Composable
private fun ThemePreview() {
    val colors = listOf(
        "Primary" to MaterialTheme.colorScheme.primary,
        "OnPrimary" to MaterialTheme.colorScheme.onPrimary,
        "PrimaryContainer" to MaterialTheme.colorScheme.primaryContainer,
        "OnPrimaryContainer" to MaterialTheme.colorScheme.onPrimaryContainer,
        "InversePrimary" to MaterialTheme.colorScheme.inversePrimary,

        "Secondary" to MaterialTheme.colorScheme.secondary,
        "OnSecondary" to MaterialTheme.colorScheme.onSecondary,
        "SecondaryContainer" to MaterialTheme.colorScheme.secondaryContainer,
        "OnSecondaryContainer" to MaterialTheme.colorScheme.onSecondaryContainer,

        "Tertiary" to MaterialTheme.colorScheme.tertiary,
        "OnTertiary" to MaterialTheme.colorScheme.onTertiary,
        "TertiaryContainer" to MaterialTheme.colorScheme.tertiaryContainer,
        "OnTertiaryContainer" to MaterialTheme.colorScheme.onTertiaryContainer,

        "Background" to MaterialTheme.colorScheme.background,
        "OnBackground" to MaterialTheme.colorScheme.onBackground,

        "Surface" to MaterialTheme.colorScheme.surface,
        "OnSurface" to MaterialTheme.colorScheme.onSurface,
        "SurfaceVariant" to MaterialTheme.colorScheme.surfaceVariant,
        "OnSurfaceVariant" to MaterialTheme.colorScheme.onSurfaceVariant,
        "SurfaceTint" to MaterialTheme.colorScheme.surfaceTint,
        "SurfaceDim" to MaterialTheme.colorScheme.surfaceDim,
        "SurfaceBright" to MaterialTheme.colorScheme.surfaceBright,
        "SurfaceContainer" to MaterialTheme.colorScheme.surfaceContainer,
        "SurfaceContainerHigh" to MaterialTheme.colorScheme.surfaceContainerHigh,
        "SurfaceContainerHighest" to MaterialTheme.colorScheme.surfaceContainerHighest,
        "SurfaceContainerLow" to MaterialTheme.colorScheme.surfaceContainerLow,
        "SurfaceContainerLowest" to MaterialTheme.colorScheme.surfaceContainerLowest,

        "Error" to MaterialTheme.colorScheme.error,
        "OnError" to MaterialTheme.colorScheme.onError,
        "ErrorContainer" to MaterialTheme.colorScheme.errorContainer,
        "OnErrorContainer" to MaterialTheme.colorScheme.onErrorContainer,

        "Outline" to MaterialTheme.colorScheme.outline,
        "OutlineVariant" to MaterialTheme.colorScheme.outlineVariant,
        "InverseSurface" to MaterialTheme.colorScheme.inverseSurface,
        "InverseOnSurface" to MaterialTheme.colorScheme.inverseOnSurface,
        "Scrim" to MaterialTheme.colorScheme.scrim,
    )
    ComposePartyTheme {
        Column {
            colors.forEach {
                Surface(
                    Modifier
                        .fillMaxWidth(),
                    color = it.second,
                ) {
                    Text(
                        it.first,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.height(20.dp),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}