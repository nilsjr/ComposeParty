package de.nilsdruyen.composeparty.settings.component

import android.content.Context
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import de.nilsdruyen.composeparty.ui.theme.DarkColors
import de.nilsdruyen.composeparty.ui.theme.LightColors
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEmpty
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "compose_party_settings")
private val COLOR_SCHEME = stringPreferencesKey("color_scheme")
private val DYNAMIC_ENABLED = booleanPreferencesKey("dynamic_enabled")

object ThemeSettings {

    val resetLight = LightColors.toMap()
    val resetDark = DarkColors.toMap()
}

suspend fun Context.updateColor(colors: ThemeEntity) {
    val raw = Json.encodeToString(colors)
    dataStore.edit { it[COLOR_SCHEME] = raw }
}

@Composable
fun rememberColorScheme(isDarkTheme: Boolean): MutableState<ColorScheme> {
    val context = LocalContext.current
    val defaultTheme = if (isDarkTheme) DarkColors else LightColors
    val state = remember { mutableStateOf(defaultTheme) }

    LaunchedEffect("INITIAL_COLOR_SCHEME") {
        context.dataStore.data
            .map { it[COLOR_SCHEME] }
            .onEmpty {
                Timber.d("Set default colors")
                val raw = Json.encodeToString(ThemeEntity(defaultTheme.toMap(), emptyMap()))
                context.dataStore.edit {
                    it[COLOR_SCHEME] = raw
                }
            }
    }

    LaunchedEffect("COLOR_SCHEME") {
        context.dataStore.data
            .mapNotNull { it[COLOR_SCHEME] }
            .map { Json.decodeFromString<ThemeEntity>(it) }
            .collect { entity ->
                Timber.d("Colors update")
                state.value = if (isDarkTheme) {
                    entity.dark.toScheme()
                } else {
                    entity.light.toScheme()
                }
            }
    }

    return state
}

@Composable
fun rememberEditableColorScheme(isDarkTheme: Boolean): MutableState<ColorSchemeState> {
    val context = LocalContext.current
    val default = if (isDarkTheme) DarkColors else LightColors
    val state = remember { mutableStateOf(ColorSchemeState(default.toMap(), isDarkTheme)) }

    LaunchedEffect("INITIAL_COLOR_SCHEME") {
        context.dataStore.data
            .map { it[COLOR_SCHEME] }
            .onEmpty {
                Timber.d("Set default colors")
                val raw = Json.encodeToString(ThemeEntity(LightColors.toMap(), emptyMap()))
                context.dataStore.edit {
                    it[COLOR_SCHEME] = raw
                }
            }
    }

    LaunchedEffect("COLOR_SCHEME") {
        val scheme = context.dataStore.data
            .mapNotNull { it[COLOR_SCHEME] }
            .map { Json.decodeFromString<ThemeEntity>(it) }
            .first()
        state.value = ColorSchemeState(
            selected = if(isDarkTheme) scheme.dark else scheme.light,
            isDarkTheme = isDarkTheme,
        )
    }

    return state
}