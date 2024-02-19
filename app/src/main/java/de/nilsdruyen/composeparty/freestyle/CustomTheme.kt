package de.nilsdruyen.composeparty.freestyle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Deck
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTheme(modifier: Modifier = Modifier) {
    val shape = CustomShape(
        CornerSize(8.dp),
    )
    val mediumShape = CustomShape(
        CornerSize(16.dp),
    )

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = md_theme_light_primary,
            onPrimary = md_theme_light_onPrimary,
            primaryContainer = md_theme_light_primaryContainer,
            onPrimaryContainer = md_theme_light_onPrimaryContainer,
            secondary = md_theme_light_secondary,
            onSecondary = md_theme_light_onSecondary,
            secondaryContainer = md_theme_light_secondaryContainer,
            onSecondaryContainer = md_theme_light_onSecondaryContainer,
            tertiary = md_theme_light_tertiary,
            onTertiary = md_theme_light_onTertiary,
            tertiaryContainer = md_theme_light_tertiaryContainer,
            onTertiaryContainer = md_theme_light_onTertiaryContainer,
            error = md_theme_light_error,
            errorContainer = md_theme_light_errorContainer,
            onError = md_theme_light_onError,
            onErrorContainer = md_theme_light_onErrorContainer,
            background = md_theme_light_background,
            onBackground = md_theme_light_onBackground,
            surface = md_theme_light_surface,
            onSurface = md_theme_light_onSurface,
            surfaceVariant = md_theme_light_surfaceVariant,
            onSurfaceVariant = md_theme_light_onSurfaceVariant,
            outline = md_theme_light_outline,
            inverseOnSurface = md_theme_light_inverseOnSurface,
            inverseSurface = md_theme_light_inverseSurface,
            inversePrimary = md_theme_light_inversePrimary,
            surfaceTint = md_theme_light_surfaceTint,
            outlineVariant = md_theme_light_outlineVariant,
            scrim = md_theme_light_scrim,
        ),
        shapes = Shapes(
            extraSmall = shape,
            small = shape,
            medium = mediumShape,
            large = shape,
            extraLarge = shape,
        ),
        typography = Typography,
    ) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Toll") })
            },
            modifier = modifier,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(onClick = {}, shape = shape) {
                    Text("Test")
                }
                OutlinedButton(onClick = {}, shape = shape) {
                    Text("Test")
                }
                Card(
                    Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Test", modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                FloatingActionButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.Deck, contentDescription = null)
                }
            }
        }
    }
}

private class CustomShape(
    val corner: CornerSize,
) : CornerBasedShape(
    topStart = corner,
    topEnd = corner,
    bottomEnd = corner,
    bottomStart = corner,
) {

    override fun copy(
        topStart: CornerSize,
        topEnd: CornerSize,
        bottomEnd: CornerSize,
        bottomStart: CornerSize
    ): CornerBasedShape = CustomShape(
        corner = topStart
    )

    override fun createOutline(
        size: Size,
        topStart: Float,
        topEnd: Float,
        bottomEnd: Float,
        bottomStart: Float,
        layoutDirection: LayoutDirection
    ): Outline = if (topStart + topEnd + bottomStart + bottomEnd == 0.0f) {
        Outline.Rectangle(size.toRect())
    } else {
        val s = 32f
        val d = 12f
        val w = size.width
        val h = size.height
        Outline.Generic(
            Path().apply {
                moveTo(0f, s)
                lineTo(s, 0f)
                lineTo(w - s, 0f)
                lineTo(w, s)
                lineTo(w, h - s)
                lineTo(w - s, h)
                lineTo(s, h)
                lineTo(0f, h - s)
                close()
                moveTo(0f, (s / 2) - (d / 2))
                lineTo(0f, (s / 2) + (d / 2))
                lineTo((s / 2) + (d / 2), 0f)
                lineTo((s / 2) - (d / 2), 0f)
                close()
                moveTo(w, h - (s / 2) - (d / 2))
                lineTo(w, h - (s / 2) + (d / 2))
                lineTo(w - (s / 2) + (d / 2), h)
                lineTo(w - (s / 2) - (d / 2), h)
                close()
            }
        )
    }
}

@Preview
@Composable
private fun CustomThemePreview() {
    CustomTheme(Modifier.fillMaxSize())
}

val md_theme_light_primary = Color(0xFF006D3D)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFF75FCAA)
val md_theme_light_onPrimaryContainer = Color(0xFF00210F)
val md_theme_light_secondary = Color(0xFF4F6353)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFD2E8D4)
val md_theme_light_onSecondaryContainer = Color(0xFF0D1F13)
val md_theme_light_tertiary = Color(0xFF4C6708)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFCCEF85)
val md_theme_light_onTertiaryContainer = Color(0xFF141F00)
val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_onErrorContainer = Color(0xFF410002)
val md_theme_light_background = Color(0xFFFBFDF8)
val md_theme_light_onBackground = Color(0xFF191C19)
val md_theme_light_surface = Color(0xFFFBFDF8)
val md_theme_light_onSurface = Color(0xFF191C19)
val md_theme_light_surfaceVariant = Color(0xFFDCE5DB)
val md_theme_light_onSurfaceVariant = Color(0xFF414942)
val md_theme_light_outline = Color(0xFF717971)
val md_theme_light_inverseOnSurface = Color(0xFFF0F1EC)
val md_theme_light_inverseSurface = Color(0xFF2E312E)
val md_theme_light_inversePrimary = Color(0xFF56DF90)
val md_theme_light_shadow = Color(0xFF000000)
val md_theme_light_surfaceTint = Color(0xFF006D3D)
val md_theme_light_outlineVariant = Color(0xFFC0C9BF)
val md_theme_light_scrim = Color(0xFF000000)

val seed = Color(0xFF00AC63)