package de.nilsdruyen.composeparty.buttons.sticky.effects

/** Pairs an effect's static metadata with a factory, so the UI can list effects without creating them. */
class EffectDescriptor(val meta: EffectMeta, val create: () -> ButtonEffect)

/** Single place to register effects. Add a descriptor here and it shows up as a tab. */
object EffectRegistry {
    val all: List<EffectDescriptor> = listOf(
        EffectDescriptor(VenomEffect.META, ::VenomEffect),
        EffectDescriptor(GlassEffect.META, ::GlassEffect),
        EffectDescriptor(PixelSwarmEffect.META, ::PixelSwarmEffect),
    )
}
