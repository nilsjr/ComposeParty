package de.nilsdruyen.composeparty.buttons.sticky.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.nilsdruyen.composeparty.buttons.sticky.engine.LightMode
import de.nilsdruyen.composeparty.buttons.sticky.engine.LightRig
import de.nilsdruyen.composeparty.buttons.sticky.engine.LightSettings
import kotlin.math.roundToInt

/** One slider row: how to read a value from settings, how to write it back and how to show it. */
private class LightSlider(
    val label: String,
    val range: ClosedFloatingPointRange<Float>,
    val read: (LightSettings) -> Float,
    val write: (LightSettings, Float) -> LightSettings,
    val format: (Float) -> String,
)

private val percent: (Float) -> String = { "${(it * 100).roundToInt()}%" }

private val LIGHT_SLIDERS = listOf(
    LightSlider("Intensity", 0f..2f, { it.intensity }, { s, v -> s.copy(intensity = v) }, percent),
    LightSlider("Horizontal", 0f..1f, { it.x }, { s, v -> s.copy(x = v) }, percent),
    LightSlider("Vertical", 0f..1f, { it.y }, { s, v -> s.copy(y = v) }, percent),
    LightSlider("Height", 80f..600f, { it.height }, { s, v -> s.copy(height = v) }, { "${it.roundToInt()}dp" }),
    LightSlider("Key light", 0f..1.5f, { it.key }, { s, v -> s.copy(key = v) }, percent),
)

/** Controls for the movable light: mode, position, intensity, height and the fixed key light. */
@Composable
fun LightPanel(rig: LightRig, modifier: Modifier = Modifier) {
    val colors = LocalLabColors.current
    val settings = rig.settings

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .border(1.dp, colors.line, RoundedCornerShape(18.dp))
            .padding(18.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text("Light", color = colors.ink, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            TextButton(onClick = rig::reset) { Text("Reset", color = colors.accent, fontFamily = FontFamily.Monospace) }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            LightMode.entries.forEach { mode ->
                FilterChip(
                    selected = settings.mode == mode,
                    onClick = { rig.settings = settings.copy(mode = mode) },
                    label = { Text(if (mode == LightMode.FollowPointer) "Follows pointer" else "Fixed position") },
                )
            }
        }

        LIGHT_SLIDERS.forEach { slider ->
            val value = slider.read(settings)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(slider.label, color = colors.ink, fontSize = 14.sp, modifier = Modifier.width(96.dp))
                Slider(
                    value = value,
                    onValueChange = { rig.settings = slider.write(rig.settings, it) },
                    valueRange = slider.range,
                    colors = SliderDefaults.colors(thumbColor = colors.accent, activeTrackColor = colors.accent),
                    modifier = Modifier.weight(1f),
                )
                Text(
                    slider.format(value),
                    color = colors.inkSoft,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.End,
                    modifier = Modifier.width(56.dp),
                )
            }
        }

        Text(
            "Horizontal and vertical place the fixed light. While it follows your pointer, they set where it rests " +
                "when your pointer leaves. Height spreads the highlight; key light is the steady studio light from the top left.",
            color = colors.inkSoft,
            fontSize = 13.sp,
        )
    }
}
