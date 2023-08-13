package de.nilsdruyen.composeparty.ui.theme

import android.content.Context
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEmpty
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "compose_party_settings")
val COLORS = stringPreferencesKey("light_colors")
val DYNAMIC_ENABLED = booleanPreferencesKey("dynamic_enabled")

suspend fun Context.updateColor(colors: ColorSettings) {
    val raw = Json.encodeToString(colors)
    dataStore.edit {
        it[COLORS] = raw
    }
}

@Composable
fun FlexTheme(content: @Composable () -> Unit) {
    val context = LocalContext.current
    var colorScheme by remember { mutableStateOf(LightColors) }

    LaunchedEffect(Unit) {
        context.dataStore.data
            .map { it[COLORS] }
            .onEmpty {
                Timber.d("Set default colors")
                val raw = Json.encodeToString(ColorSettings(LightColors.toMap(), emptyMap()))
                context.dataStore.edit {
                    it[COLORS] = raw
                }
            }
    }

    LaunchedEffect(Unit) {
        context.dataStore.data
            .mapNotNull { it[COLORS] }
            .map { Json.decodeFromString<ColorSettings>(it) }
            .collect {
                Timber.d("Colors update")
                colorScheme = it.light.toScheme()
            }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}

@Composable
fun rememberColorState(): MutableState<ColorScheme> {
    val context = LocalContext.current
    val state = remember { mutableStateOf(DarkColors) }

    LaunchedEffect("COLORS") {
        context.dataStore.data
            .mapNotNull { it[COLORS] }
            .map { Json.decodeFromString<ColorSettings>(it) }
            .collect {
                Timber.d("Colors update")
                state.value = it.light.toScheme()
            }
    }

    return state
}

@Serializable
data class ColorSettings(
    val light: Map<String, String>,
    val dark: Map<String, String>,
)

fun Map<String, String>.toScheme(): ColorScheme = ColorScheme(
    primary = getOrDefault("primary", "#FFFFFF").toColor(),
    onPrimary = getOrDefault("onPrimary", "#FFFFFF").toColor(),
    primaryContainer = getOrDefault("primaryContainer", "#FFFFFF").toColor(),
    onPrimaryContainer = getOrDefault("onPrimaryContainer", "#FFFFFF").toColor(),
    inversePrimary = getOrDefault("inversePrimary", "#FFFFFF").toColor(),
    secondary = getOrDefault("secondary", "#FFFFFF").toColor(),
    onSecondary = getOrDefault("onSecondary", "#FFFFFF").toColor(),
    secondaryContainer = getOrDefault("secondaryContainer", "#FFFFFF").toColor(),
    onSecondaryContainer = getOrDefault("onSecondaryContainer", "#FFFFFF").toColor(),
    tertiary = getOrDefault("tertiary", "#FFFFFF").toColor(),
    onTertiary = getOrDefault("onTertiary", "#FFFFFF").toColor(),
    tertiaryContainer = getOrDefault("tertiaryContainer", "#FFFFFF").toColor(),
    onTertiaryContainer = getOrDefault("onTertiaryContainer", "#FFFFFF").toColor(),
    background = getOrDefault("background", "#FFFFFF").toColor(),
    onBackground = getOrDefault("onBackground", "#FFFFFF").toColor(),
    surface = getOrDefault("surface", "#FFFFFF").toColor(),
    onSurface = getOrDefault("onSurface", "#FFFFFF").toColor(),
    surfaceVariant = getOrDefault("surfaceVariant", "#FFFFFF").toColor(),
    onSurfaceVariant = getOrDefault("onSurfaceVariant", "#FFFFFF").toColor(),
    surfaceTint = getOrDefault("surfaceTint", "#FFFFFF").toColor(),
    inverseSurface = getOrDefault("inverseSurface", "#FFFFFF").toColor(),
    inverseOnSurface = getOrDefault("inverseOnSurface", "#FFFFFF").toColor(),
    error = getOrDefault("error", "#FFFFFF").toColor(),
    onError = getOrDefault("onError", "#FFFFFF").toColor(),
    errorContainer = getOrDefault("errorContainer", "#FFFFFF").toColor(),
    onErrorContainer = getOrDefault("onErrorContainer", "#FFFFFF").toColor(),
    outline = getOrDefault("outline", "#FFFFFF").toColor(),
    outlineVariant = getOrDefault("outlineVariant", "#FFFFFF").toColor(),
    scrim = getOrDefault("scrim", "#FFFFFF").toColor(),
)

fun ColorScheme.toMap(): Map<String, String> = mapOf(
    "primary" to primary.raw(),
    "onPrimary" to onPrimary.raw(),
    "primaryContainer" to primaryContainer.raw(),
    "onPrimaryContainer" to onPrimaryContainer.raw(),
    "inversePrimary" to inversePrimary.raw(),
    "secondary" to secondary.raw(),
    "onSecondary" to onSecondary.raw(),
    "secondaryContainer" to secondaryContainer.raw(),
    "onSecondaryContainer" to onSecondaryContainer.raw(),
    "tertiary" to tertiary.raw(),
    "onTertiary" to onTertiary.raw(),
    "tertiaryContainer" to tertiaryContainer.raw(),
    "onTertiaryContainer" to onTertiaryContainer.raw(),
    "background" to background.raw(),
    "onBackground" to onBackground.raw(),
    "surface" to surface.raw(),
    "onSurface" to onSurface.raw(),
    "surfaceVariant" to surfaceVariant.raw(),
    "onSurfaceVariant" to onSurfaceVariant.raw(),
    "surfaceTint" to surfaceTint.raw(),
    "inverseSurface" to inverseSurface.raw(),
    "inverseOnSurface" to inverseOnSurface.raw(),
    "error" to error.raw(),
    "onError" to onError.raw(),
    "errorContainer" to errorContainer.raw(),
    "onErrorContainer" to onErrorContainer.raw(),
    "outline" to outline.raw(),
    "outlineVariant" to outlineVariant.raw(),
    "scrim" to scrim.raw(),
)

fun String.toColor(): Color = Color(android.graphics.Color.parseColor(this))

@OptIn(ExperimentalStdlibApi::class)
fun Color.raw(): String = this.toArgb().toHexString()
