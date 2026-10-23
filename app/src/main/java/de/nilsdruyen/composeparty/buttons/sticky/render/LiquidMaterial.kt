package de.nilsdruyen.composeparty.buttons.sticky.render

import androidx.compose.ui.graphics.Color

/**
 * Surface preset for the liquid shader. Colours are linear-ish RGB components in 0..1.
 * [transmission] (0..1) and [refraction] (dp) make the surface see-through.
 */
data class LiquidMaterial(
    val base: Color,
    val specular: Color,
    val rim: Color,
    val shininess: Float,
    val environment: Float,
    val metal: Float,
    val iridescence: Float,
    val diffuse: Float,
    val subsurface: Float,
    val bump: Float,
    val threshold: Float,
    val shadow: Float,
    val transmission: Float = 0f,
    val refraction: Float = 0f,
    /** Flat colour used where the shader is unavailable (Android below 13). */
    val fallbackColor: Color,
) {
    companion object {
        val Symbiote = LiquidMaterial(
            base = Color(0.018f, 0.02f, 0.028f), specular = Color(0.92f, 0.95f, 1f), rim = Color(0.1f, 0.14f, 0.27f),
            shininess = 90f, environment = 0.95f, metal = 0.12f, iridescence = 0.24f, diffuse = 0.25f,
            subsurface = 0f, bump = 5f, threshold = 0.38f, shadow = 0.32f,
            fallbackColor = Color(0xFF0B0C10),
        )

        val Ferrofluid = LiquidMaterial(
            base = Color(0.006f, 0.006f, 0.008f), specular = Color.White, rim = Color(0.16f, 0.18f, 0.23f),
            shininess = 140f, environment = 1.15f, metal = 0.55f, iridescence = 0.06f, diffuse = 0.15f,
            subsurface = 0f, bump = 7f, threshold = 0.4f, shadow = 0.35f,
            fallbackColor = Color(0xFF08080A),
        )

        val Mercury = LiquidMaterial(
            base = Color(0.68f, 0.7f, 0.74f), specular = Color.White, rim = Color(0.1f, 0.11f, 0.14f),
            shininess = 70f, environment = 1.2f, metal = 0.92f, iridescence = 0f, diffuse = 0.6f,
            subsurface = 0f, bump = 6f, threshold = 0.36f, shadow = 0.3f,
            fallbackColor = Color(0xFFA9ADB6),
        )

        val Jelly = LiquidMaterial(
            base = Color(0.26f, 0.21f, 0.9f), specular = Color(1f, 0.98f, 1f), rim = Color(0.35f, 0.3f, 0.75f),
            shininess = 55f, environment = 0.7f, metal = 0f, iridescence = 0.04f, diffuse = 0.85f,
            subsurface = 0.55f, bump = 4.5f, threshold = 0.36f, shadow = 0.28f,
            fallbackColor = Color(0xFF4537E6),
        )

        val Glass = LiquidMaterial(
            base = Color(0.82f, 0.9f, 1f), specular = Color.White, rim = Color(0.08f, 0.1f, 0.14f),
            shininess = 160f, environment = 0.75f, metal = 0f, iridescence = 0.03f, diffuse = 0f,
            subsurface = 0f, bump = 6f, threshold = 0.36f, shadow = 0.14f,
            transmission = 1f, refraction = 34f,
            fallbackColor = Color.White.copy(alpha = 0.35f),
        )
    }
}

/** Stage background colours the glass shader rebuilds for refraction. Must match [drawStageBackdrop]. */
data class BackdropColors(val surface: Color, val dot: Color, val lightTint: Color)
