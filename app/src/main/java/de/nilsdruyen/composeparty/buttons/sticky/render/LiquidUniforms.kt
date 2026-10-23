package de.nilsdruyen.composeparty.buttons.sticky.render

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import de.nilsdruyen.composeparty.buttons.sticky.engine.LightState

/**
 * Everything the liquid shader needs for one frame. Platform renderers only iterate [forEach],
 * so uniform names and packing live in exactly one place.
 *
 * @property layerSize size of the drawn layer in px
 * @property density px per dp
 */
class LiquidUniforms(
    private val layerSize: Size,
    val density: Float,
    private val light: LightState,
    private val time: Float,
    private val material: LiquidMaterial,
    private val backdrop: BackdropColors,
) {
    fun forEach(consumer: (name: String, values: FloatArray) -> Unit) {
        consumer("uSize", floatArrayOf(layerSize.width, layerSize.height))
        consumer("uPx", floatArrayOf(density))
        consumer(
            "uLight",
            floatArrayOf(light.position.x * density, light.position.y * density, light.strength, light.height),
        )
        consumer("uKey", floatArrayOf(light.key))
        consumer("uTime", floatArrayOf(time))
        consumer("uBase", material.base.rgb())
        consumer("uSpecular", material.specular.rgb())
        consumer("uRim", material.rim.rgb())
        consumer("uSurface", floatArrayOf(material.shininess, material.environment, material.metal, material.iridescence))
        consumer("uBody", floatArrayOf(material.diffuse, material.subsurface, material.bump, material.threshold))
        consumer("uOptics", floatArrayOf(material.shadow, material.transmission, material.refraction, 0f))
        consumer("uBackdrop", backdrop.surface.rgb())
        consumer("uDot", backdrop.dot.rgba())
        consumer("uLightTint", backdrop.lightTint.rgba())
    }

    private fun Color.rgb() = floatArrayOf(red, green, blue)
    private fun Color.rgba() = floatArrayOf(red, green, blue, alpha)
}
