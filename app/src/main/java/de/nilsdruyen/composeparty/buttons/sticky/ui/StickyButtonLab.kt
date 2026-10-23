package de.nilsdruyen.composeparty.buttons.sticky.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.nilsdruyen.composeparty.buttons.sticky.effects.EffectDescriptor
import de.nilsdruyen.composeparty.buttons.sticky.effects.EffectRegistry
import de.nilsdruyen.composeparty.buttons.sticky.effects.Readout
import de.nilsdruyen.composeparty.buttons.sticky.engine.LightRig
import de.nilsdruyen.composeparty.buttons.sticky.engine.StageController
import de.nilsdruyen.composeparty.buttons.sticky.render.isLiquidShaderSupported
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

/** Playground entry point: provides the lab colors and respects edge-to-edge insets. */
@Preview
@Composable
fun StickyButtonSample() {
    val colors = if (isSystemInDarkTheme()) LabColors.Dark else LabColors.Light
    ComposePartyTheme {
        CompositionLocalProvider(LocalLabColors provides colors) {
            StickyButtonLab(Modifier.systemBarsPadding())
        }
    }
}

/** One stage per registered effect. Use interactive mode to see it animate; a static render shows a single frame. */
@Preview(widthDp = 380, heightDp = 440)
@Composable
private fun StickyButtonStagePreview(@PreviewParameter(EffectPreviewProvider::class) descriptor: EffectDescriptor) {
    CompositionLocalProvider(LocalLabColors provides LabColors.Light) {
        StageView(remember(descriptor) { StageController(LightRig(), descriptor.create()) }, Modifier.fillMaxSize())
    }
}

private class EffectPreviewProvider : PreviewParameterProvider<EffectDescriptor> {
    override val values = EffectRegistry.all.asSequence()
    override fun getDisplayName(index: Int) = EffectRegistry.all[index].meta.name
}

@Composable
fun StickyButtonLab(modifier: Modifier = Modifier) {
    val colors = LocalLabColors.current
    val controller = remember { StageController(LightRig(), EffectRegistry.all.first().create()) }
    var selected by remember { mutableStateOf(EffectRegistry.all.first()) }
    var readout by remember { mutableStateOf(Readout("", "")) }
    // Plain counter, not state: incrementing it every frame must not trigger recomposition.
    val frameCounter = remember { IntArray(1) }

    fun select(descriptor: EffectDescriptor) {
        selected = descriptor
        controller.selectEffect(descriptor.create())
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
            .fillMaxSize()
            .background(colors.ground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier.widthIn(max = 980.dp).fillMaxWidth()) {
            Text("INTERACTION STUDY · MOUSE & TOUCH", color = colors.inkSoft, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
            Text("Sticky Button Lab", color = colors.ink, fontSize = 44.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 44.sp)
            Text(
                "Buttons that notice you coming. Move your cursor toward the button or drag a finger across the stage. " +
                    "The liquid and glass ones are lit by a light you can steer below.",
                color = colors.inkSoft,
            )

            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                EffectRegistry.all.forEach { descriptor ->
                    FilterChip(
                        selected = descriptor == selected,
                        onClick = { select(descriptor) },
                        label = { Text(descriptor.meta.name.uppercase(), fontFamily = FontFamily.Monospace, fontSize = 12.sp) },
                    )
                }
            }

            if (!isLiquidShaderSupported()) {
                Text("Lighting needs Android 13+", color = colors.inkSoft, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
            }

            StageView(
                controller = controller,
                modifier = Modifier.fillMaxWidth().height(440.dp),
                onFrame = {
                    // Refresh the text readout a few times per second instead of every frame.
                    if (++frameCounter[0] % READOUT_EVERY_FRAMES == 0) readout = controller.effect.readout()
                },
            )

            ReadoutRow(controller, readout)
            LightPanel(controller.lightRig, Modifier.fillMaxWidth())

            Text(selected.meta.name, color = colors.ink, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
            Text(selected.meta.description, color = colors.ink)
            Text("Technique: ${selected.meta.technique}", color = colors.inkSoft, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
        }
    }
}

@Composable
private fun ReadoutRow(controller: StageController, readout: Readout) {
    val colors = LocalLabColors.current
    val frame = controller.frame
    val distance = if (frame?.pointer?.isActive == true) "${frame.distance.toInt()}dp" else "—"
    val proximity = ((frame?.proximity ?: 0f) * 100).toInt() / 100f
    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
        listOf("Distance" to distance, "Proximity" to proximity.toString(), readout.label to readout.value).forEach { (label, value) ->
            Text("$label  $value", color = colors.ink, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
        }
    }
}

private const val READOUT_EVERY_FRAMES = 6
