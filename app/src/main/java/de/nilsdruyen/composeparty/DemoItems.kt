package de.nilsdruyen.composeparty

import androidx.compose.runtime.Composable
import de.nilsdruyen.composeparty.animations.ClockSample
import de.nilsdruyen.composeparty.animations.DynamicHueSample
import de.nilsdruyen.composeparty.animations.MoveableSample
import de.nilsdruyen.composeparty.animations.TextAnim
import de.nilsdruyen.composeparty.buttons.AddToCartButtonDemo
import de.nilsdruyen.composeparty.buttons.ChipButton
import de.nilsdruyen.composeparty.buttons.LoadingButtonDemo
import de.nilsdruyen.composeparty.cards.CardTiltSample
import de.nilsdruyen.composeparty.cards.PeopleCardSample
import de.nilsdruyen.composeparty.cards.TeaserSample
import de.nilsdruyen.composeparty.freestyle.*
import de.nilsdruyen.composeparty.isles.IsleExample
import de.nilsdruyen.composeparty.layouts.CollapsingToolbarLayoutExample
import de.nilsdruyen.composeparty.layouts.SampleStaggeredGridLayout
import de.nilsdruyen.composeparty.material.ScrollableScaffold
import de.nilsdruyen.composeparty.math.GravitySample
import de.nilsdruyen.composeparty.paths.PathPawAnimation
import de.nilsdruyen.composeparty.paths.ScaleShapeSample
import de.nilsdruyen.composeparty.paths.VectorDemo
import de.nilsdruyen.composeparty.swipe.SwipeDemo
import de.nilsdruyen.composeparty.text.AutoSizeTextSample
import de.nilsdruyen.composeparty.text.FontScaleDemo
import de.nilsdruyen.composeparty.text.TextAnimationSample
import de.nilsdruyen.composeparty.text.TextBrushing
import de.nilsdruyen.composeparty.text.WrappingText

val demoItems = mapOf<String, @Composable () -> Unit>(
    "Fire Shader Sample" to { FireAnimationShaderSample() },
    "AddToCartButtonDemo" to { AddToCartButtonDemo() },
    "AutoSizeTextSample" to { AutoSizeTextSample() },
    "CanvasSample" to { CanvasSample() },
    "Card Sample" to { CardTiltSample() },
    "ChipButton" to { ChipButton() },
    "Clock" to { ClockSample() },
    "CollapsingToolbarLayout" to { CollapsingToolbarLayoutExample() },
    "ComposeLogo" to { ComposeLogo() },
    "DashedLine" to { DashedLine() },
    "DynamicHue Sample" to { DynamicHueSample() },
    "DynamicPointMesh" to { MeshSample() },
    "EqualizerSample" to { EqualizerSample() },
    "Fling Anim" to { FlingAnimation() },
    "Font Scale Demo" to { FontScaleDemo() },
    "GravitySample" to { GravitySample() },
    "HeartRate" to { HeartRate() },
    "IsleExample" to { IsleExample() },
    "LoadingButtonDemo" to { LoadingButtonDemo() },
    "Orbital" to { MoveableSample() },
    "Path Paw Anim" to { PathPawAnimation() },
    "PeopleCardSample" to { PeopleCardSample() },
    "Progress Sample" to { ProgressSample() },
    "ScaleShapeSample" to { ScaleShapeSample() },
    "ScrollableScaffold" to { ScrollableScaffold() },
    "Sensor Sample" to { SensorSample() },
    "Shader Sample" to { ShaderSample() },
    "SnowFallSample" to { SnowFallSample() },
    "Spring Animation" to { SpringAnimation() },
    "SquareFun" to { SquareFun() },
    "StaggeredGridLayout" to { SampleStaggeredGridLayout() },
    "Swipe Game" to { SwipeDemo() },
    "Teaser Card" to { TeaserSample() },
    "Text Animation" to { TextAnimationSample() },
    "TextAnim" to { TextAnim() },
    "TextBrushing" to { TextBrushing() },
    "VectorDemo" to { VectorDemo() },
    "Wrapping Text" to { WrappingText() },
//    "AVD Sample" to { AnimatedVectorDrawable() }, // TODO: not working
//    "MenuToClose" to { MenuToClose() }, // TODO: error
//    "MeshBlockify" to { MeshBlockify() }, // TODO: error
)