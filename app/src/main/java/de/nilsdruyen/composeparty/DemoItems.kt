package de.nilsdruyen.composeparty

import androidx.compose.runtime.Composable
import de.nilsdruyen.composeparty.animations.AnimatedVectorDrawable
import de.nilsdruyen.composeparty.animations.TextAnim
import de.nilsdruyen.composeparty.buttons.AddToCartButtonDemo
import de.nilsdruyen.composeparty.buttons.ChipButton
import de.nilsdruyen.composeparty.buttons.LoadingButtonDemo
import de.nilsdruyen.composeparty.buttons.PreviewLoadingButton
import de.nilsdruyen.composeparty.freestyle.ComposeLogo
import de.nilsdruyen.composeparty.freestyle.DashedLine
import de.nilsdruyen.composeparty.freestyle.DynamicPointMesh
import de.nilsdruyen.composeparty.freestyle.HeartRate
import de.nilsdruyen.composeparty.freestyle.MenuToClose
import de.nilsdruyen.composeparty.freestyle.MeshBlockify
import de.nilsdruyen.composeparty.freestyle.SquareFun
import de.nilsdruyen.composeparty.isles.IsleExample
import de.nilsdruyen.composeparty.layouts.SampleStaggeredGridLayout
import de.nilsdruyen.composeparty.material.ScrollableScaffold
import de.nilsdruyen.composeparty.paths.VectorDemo
import de.nilsdruyen.composeparty.text.TextBrushing
import de.nilsdruyen.composeparty.text.WrappingText

val demoItems = mapOf<String, @Composable () -> Unit>(
    "AVD Sample" to { AnimatedVectorDrawable() },
    "AddToCartButtonDemo" to { AddToCartButtonDemo() },
    "ChipButton" to { ChipButton() },
    "ComposeLogo" to { ComposeLogo() },
    "DashedLine" to { DashedLine() },
    "DynamicPointMesh" to { DynamicPointMesh() },
    "HeartRate" to { HeartRate() },
    "IsleExample" to { IsleExample() },
    "LoadingButtonDemo" to { LoadingButtonDemo() },
    "MenuToClose" to { MenuToClose() },
    "MeshBlockify" to { MeshBlockify() },
    "PreviewLoadingButton" to { PreviewLoadingButton() },
    "ScrollableScaffold" to { ScrollableScaffold() },
    "SquareFun" to { SquareFun() },
    "StaggeredGridLayout" to { SampleStaggeredGridLayout() },
    "TextAnim" to { TextAnim() },
    "TextBrushing" to { TextBrushing() },
    "VectorDemo" to { VectorDemo() },
    "Wrapping Text" to { WrappingText() },
)