package de.nilsdruyen.composeparty.settings.component

import androidx.compose.runtime.Stable
import de.nilsdruyen.composeparty.ui.theme.DarkColors
import de.nilsdruyen.composeparty.ui.theme.LightColors
import kotlinx.serialization.Serializable

@Stable
data class ColorSchemeState(
    val selected: Map<String, String>,
    val isDarkTheme: Boolean,
) {

    fun reset(): ColorSchemeState {
        val default = if (isDarkTheme) DarkColors else LightColors
        return copy(selected = default.toMap())
    }
}

@Serializable
data class ThemeEntity(
    val light: Map<String, String>,
    val dark: Map<String, String>,
)