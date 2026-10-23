package de.nilsdruyen.composeparty.buttons.sticky.render

import android.graphics.RuntimeShader
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.asComposeRenderEffect

fun isLiquidShaderSupported(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

fun createLiquidRenderEffect(uniforms: LiquidUniforms): RenderEffect? =
    if (isLiquidShaderSupported()) AgslLiquidShader.renderEffect(uniforms) else null

/** AGSL runtime shaders exist from Android 13 (API 33). Kept in its own object so older devices never load it. */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private object AgslLiquidShader {
    private val shader = RuntimeShader(LIQUID_SHADER_SOURCE)

    fun renderEffect(uniforms: LiquidUniforms): RenderEffect {
        uniforms.forEach { name, values -> shader.setFloatUniform(name, values) }
        // The shader's 12-tap rings undersample a sharp mask at high densities (ghost edges, sawtooth
        // highlights); a Gaussian pre-blur gives them a smooth field, like the web's blur passes.
        val blurRadius = MASK_BLUR_DP * uniforms.density
        val blur = android.graphics.RenderEffect.createBlurEffect(blurRadius, blurRadius, Shader.TileMode.DECAL)
        return android.graphics.RenderEffect
            .createChainEffect(android.graphics.RenderEffect.createRuntimeShaderEffect(shader, "content"), blur)
            .asComposeRenderEffect()
    }
}

private const val MASK_BLUR_DP = 3f
