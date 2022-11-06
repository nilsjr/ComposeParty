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
import de.nilsdruyen.composeparty.freestyle.ComposeLogo
import de.nilsdruyen.composeparty.freestyle.DashedLine
import de.nilsdruyen.composeparty.freestyle.EqualizerSample
import de.nilsdruyen.composeparty.freestyle.FlingAnimation
import de.nilsdruyen.composeparty.freestyle.HeartRate
import de.nilsdruyen.composeparty.freestyle.MeshSample
import de.nilsdruyen.composeparty.freestyle.ProgressSample
import de.nilsdruyen.composeparty.freestyle.SensorSample
import de.nilsdruyen.composeparty.freestyle.ShaderSample
import de.nilsdruyen.composeparty.freestyle.SpringAnimation
import de.nilsdruyen.composeparty.freestyle.SquareFun
import de.nilsdruyen.composeparty.isles.IsleExample
import de.nilsdruyen.composeparty.layouts.CollapsingToolbarLayoutExample
import de.nilsdruyen.composeparty.layouts.SampleStaggeredGridLayout
import de.nilsdruyen.composeparty.material.ScrollableScaffold
import de.nilsdruyen.composeparty.math.GravitySample
import de.nilsdruyen.composeparty.paths.PathPawAnimation
import de.nilsdruyen.composeparty.paths.ScaleShapeSample
import de.nilsdruyen.composeparty.paths.VectorDemo
import de.nilsdruyen.composeparty.swipe.SwipeDemo
import de.nilsdruyen.composeparty.text.FontScaleDemo
import de.nilsdruyen.composeparty.text.TextAnimationSample
import de.nilsdruyen.composeparty.text.TextBrushing
import de.nilsdruyen.composeparty.text.WrappingText

val demoItems = mapOf<String, @Composable () -> Unit>(
    "ScaleShapeSample" to { ScaleShapeSample() },
    "EqualizerSample" to { EqualizerSample() },
    "GravitySample" to { GravitySample() },
    "PeopleCardSample" to { PeopleCardSample() },
    "AddToCartButtonDemo" to { AddToCartButtonDemo() },
    "Card Sample" to { CardTiltSample() },
    "ChipButton" to { ChipButton() },
    "Clock" to { ClockSample() },
    "CollapsingToolbarLayout" to { CollapsingToolbarLayoutExample() },
    "ComposeLogo" to { ComposeLogo() },
    "DashedLine" to { DashedLine() },
    "DynamicHue Sample" to { DynamicHueSample() },
    "DynamicPointMesh" to { MeshSample() },
    "Fling Anim" to { FlingAnimation() },
    "Font Scale Demo" to { FontScaleDemo() },
    "HeartRate" to { HeartRate() },
    "IsleExample" to { IsleExample() },
    "LoadingButtonDemo" to { LoadingButtonDemo() },
    "Orbital" to { MoveableSample() },
    "Path Paw Anim" to { PathPawAnimation() },
    "Progress Sample" to { ProgressSample() },
    "ScrollableScaffold" to { ScrollableScaffold() },
    "Sensor Sample" to { SensorSample() },
    "Shader Sample" to { ShaderSample() },
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