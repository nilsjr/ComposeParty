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
import de.nilsdruyen.composeparty.material.ScrollableScaffold
import de.nilsdruyen.composeparty.paths.VectorDemo
import de.nilsdruyen.composeparty.text.TextBrushing
import de.nilsdruyen.composeparty.text.WrappingText

val demoItems = mapOf<String, @Composable () -> Unit>(
    "AddToCartButtonDemo" to { AddToCartButtonDemo() },
    "ChipButton" to { ChipButton() },
    "ComposeLogo" to { ComposeLogo() },
    "DashedLine" to { DashedLine() },
    "DynamicPointMesh" to { DynamicPointMesh() },
    "MeshBlockify" to { MeshBlockify() },
    "HeartRate" to { HeartRate() },
    "IsleExample" to { IsleExample() },
    "LoadingButtonDemo" to { LoadingButtonDemo() },
    "MenuToClose" to { MenuToClose() },
    "PreviewLoadingButton" to { PreviewLoadingButton() },
    "ScrollableScaffold" to { ScrollableScaffold() },
    "SquareFun" to { SquareFun() },
    "TextAnim" to { TextAnim() },
    "VectorDemo" to { VectorDemo() },
    "Wrapping Text" to { WrappingText() },
    "TextBrushing" to { TextBrushing() },
    "AVD Sample" to { AnimatedVectorDrawable() },
)