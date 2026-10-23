package de.nilsdruyen.composeparty.buttons.sticky.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.changedToDown
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import de.nilsdruyen.composeparty.buttons.sticky.core.TAU
import de.nilsdruyen.composeparty.buttons.sticky.effects.EffectLayer
import de.nilsdruyen.composeparty.buttons.sticky.effects.LabelStyle
import de.nilsdruyen.composeparty.buttons.sticky.engine.LightState
import de.nilsdruyen.composeparty.buttons.sticky.engine.StageController
import de.nilsdruyen.composeparty.buttons.sticky.render.LiquidUniforms
import de.nilsdruyen.composeparty.buttons.sticky.render.createLiquidRenderEffect
import de.nilsdruyen.composeparty.buttons.sticky.render.drawStageBackdrop
import de.nilsdruyen.composeparty.buttons.sticky.render.isLiquidShaderSupported
import kotlin.math.cos
import kotlin.math.sin

/**
 * The interactive stage. It only forwards input, drives the frame clock and draws;
 * every decision lives in [StageController] and the effects.
 *
 * Layers, bottom to top: backdrop, liquid (shader), overlay, button label.
 */
@Composable
fun StageView(
    controller: StageController,
    modifier: Modifier = Modifier,
    onFrame: () -> Unit = {},
) {
    val colors = LocalLabColors.current
    val density = LocalDensity.current.density
    val shaderSupported = remember { isLiquidShaderSupported() }

    var tick by remember { mutableLongStateOf(0L) }
    var stageSize by remember { mutableStateOf(Size.Zero) }
    var buttonBox by remember { mutableStateOf(Rect.Zero) }

    LaunchedEffect(controller) {
        var lastNanos = -1L
        while (true) {
            withFrameNanos { now ->
                val dt = if (lastNanos < 0) 0f else ((now - lastNanos) / 1e9f).coerceAtMost(MAX_DT)
                lastNanos = now
                if (stageSize != Size.Zero && buttonBox != Rect.Zero) {
                    controller.step(dt, stageSize, buttonBox)
                    tick++
                    onFrame()
                }
            }
        }
    }

    val effect = controller.effect
    val meta = effect.meta

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .onSizeChanged { stageSize = it.toSize() / density }
            .pointerInput(controller, density) {
                awaitPointerEventScope {
                    var touchDown: Offset? = null
                    while (true) {
                        // Initial pass observes without consuming, so the button still receives its click.
                        val event = awaitPointerEvent(PointerEventPass.Initial)
                        val change = event.changes.firstOrNull() ?: continue
                        val position = change.position / density
                        val isTouch = change.type == PointerType.Touch
                        if (isTouch && change.changedToDown()) touchDown = change.position
                        // A finger dragging on the stage must not scroll the page (the web's `touch-action: none`).
                        // Half the slop keeps us ahead of the parent scroll while plain taps still click.
                        val down = touchDown
                        if (isTouch && change.pressed && down != null &&
                            (change.position - down).getDistance() > viewConfiguration.touchSlop / 2
                        ) {
                            change.consume()
                        }
                        when (event.type) {
                            PointerEventType.Exit -> controller.onPointerExited()
                            PointerEventType.Release -> controller.onPointerReleased(isTouch)
                            PointerEventType.Move, PointerEventType.Press, PointerEventType.Enter -> {
                                if (!isTouch || change.pressed) controller.onPointerMoved(position)
                            }
                        }
                    }
                }
            },
    ) {
        Canvas(Modifier.matchParentSize()) {
            observe(tick)
            scale(density, Offset.Zero) { drawStageBackdrop(colors.backdrop, controller.light, stageSize) }
        }

        val material = meta.material
        if (meta.layer == EffectLayer.Liquid && material != null) {
            Canvas(
                Modifier.matchParentSize().graphicsLayer {
                    observe(tick)
                    val light = controller.light
                    renderEffect = if (shaderSupported && light != null) {
                        createLiquidRenderEffect(
                            LiquidUniforms(size, density, light, controller.frame?.time ?: 0f, material, colors.backdrop),
                        )
                    } else {
                        null
                    }
                },
            ) {
                observe(tick)
                val frame = controller.frame ?: return@Canvas
                val ink = if (shaderSupported) Color.Black else material.fallbackColor
                scale(density, Offset.Zero) { with(effect) { drawLiquid(frame, ink) } }
            }
        }

        Canvas(Modifier.matchParentSize()) {
            observe(tick)
            val frame = controller.frame ?: return@Canvas
            scale(density, Offset.Zero) {
                with(effect) { drawOverlay(frame, colors.overlay) }
                controller.light?.takeUnless { it.isFollowing }?.let { drawLightMarker(it, colors.accent) }
                if (frame.pointer.isDemo) drawCircle(colors.ink.copy(alpha = 0.45f), 14f, frame.pointer.position, style = Stroke(1.5f))
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .onGloballyPositioned { buttonBox = it.boundsInParent().let { b -> Rect(b.topLeft / density, b.bottomRight / density) } }
                // After onGloballyPositioned, so the pose moves the button (and its touch target) but not the layout box.
                .graphicsLayer {
                    observe(tick)
                    val pose = effect.pose()
                    translationX = pose.offset.x * density
                    translationY = pose.offset.y * density
                    rotationZ = pose.rotation
                }
                .clip(RoundedCornerShape(50))
                .drawBehind {
                    observe(tick)
                    if (meta.isSolidButton) drawSolidButton(lerp(colors.button, colors.warn, effect.pose().heat), controller.light, buttonBox, density)
                }
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    role = Role.Button,
                    onClick = controller::onButtonActivated,
                )
                .padding(horizontal = 36.dp, vertical = 18.dp),
        ) {
            val isPixel = meta.labelStyle == LabelStyle.Pixel
            Text(
                text = if (isPixel) effect.label.uppercase() else effect.label,
                color = meta.labelColor.takeOrElse { colors.ground },
                modifier = Modifier.graphicsLayer {
                    observe(tick)
                    val labelOffset = effect.pose().labelOffset
                    translationX = labelOffset.x * density
                    translationY = labelOffset.y * density
                },
                fontSize = if (isPixel) 13.sp else 17.sp,
                fontWeight = if (isPixel) FontWeight.Normal else FontWeight.SemiBold,
                fontFamily = if (isPixel) FontFamily.Monospace else FontFamily.Default,
            )
        }
    }
}

/** A regular pill button with a glossy glint that tracks the light. [box] is the button's layout box in dp. */
private fun DrawScope.drawSolidButton(color: Color, light: LightState?, box: Rect, density: Float) {
    drawRect(color)
    if (light == null) return
    val center = (light.position - box.topLeft) * density
    val radius = GLINT_RADIUS * density
    val alpha = light.strength.coerceIn(0f, 1f) * 0.45f
    drawRect(Brush.radialGradient(0f to Color.White.copy(alpha = alpha), 0.75f to Color.Transparent, center = center, radius = radius))
}

/** Small sun glyph where a parked light sits, so its position is visible while adjusting it. */
private fun DrawScope.drawLightMarker(light: LightState, color: Color) {
    val stroke = Stroke(1.5f)
    drawCircle(color, 6f, light.position, style = stroke)
    repeat(8) { i ->
        val angle = i / 8f * TAU
        val direction = Offset(cos(angle), sin(angle))
        drawLine(color, light.position + direction * 10f, light.position + direction * 14f, strokeWidth = 1.5f)
    }
}

/**
 * Reading a state value inside a draw or layer block makes Compose re-run that block when the
 * value changes. The frame tick is read this way so only the draw phase runs per frame,
 * never composition.
 */
@Suppress("UNUSED_PARAMETER")
private fun observe(value: Long) = Unit

private const val MAX_DT = 0.05f
private const val GLINT_RADIUS = 90f
