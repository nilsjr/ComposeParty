package de.nilsdruyen.composeparty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import de.nilsdruyen.composeparty.animations.AnimatedRow
import de.nilsdruyen.composeparty.animations.ClockSample
import de.nilsdruyen.composeparty.animations.DynamicHueSample
import de.nilsdruyen.composeparty.animations.LayoutAnimationSample
import de.nilsdruyen.composeparty.animations.MoveableSample
import de.nilsdruyen.composeparty.animations.ProgressAnimationSample
import de.nilsdruyen.composeparty.animations.TextAnim
import de.nilsdruyen.composeparty.buttons.AddToCartButtonDemo
import de.nilsdruyen.composeparty.buttons.ChipButton
import de.nilsdruyen.composeparty.buttons.LoadingButtonDemo
import de.nilsdruyen.composeparty.buttons.TextButtonSample
import de.nilsdruyen.composeparty.cards.CardTiltSample
import de.nilsdruyen.composeparty.cards.PeopleCardSample
import de.nilsdruyen.composeparty.cards.TeaserSample
import de.nilsdruyen.composeparty.freestyle.CanvasSample
import de.nilsdruyen.composeparty.freestyle.ComposeLogo
import de.nilsdruyen.composeparty.freestyle.DashedLine
import de.nilsdruyen.composeparty.freestyle.EqualizerSample
import de.nilsdruyen.composeparty.freestyle.FireAnimationShaderSample
import de.nilsdruyen.composeparty.freestyle.FlingAnimation
import de.nilsdruyen.composeparty.freestyle.HeartRate
import de.nilsdruyen.composeparty.freestyle.MeshSample
import de.nilsdruyen.composeparty.freestyle.ProgressSample
import de.nilsdruyen.composeparty.freestyle.PullRefreshSample
import de.nilsdruyen.composeparty.freestyle.RevealImageSample
import de.nilsdruyen.composeparty.freestyle.ScratchCardSample
import de.nilsdruyen.composeparty.freestyle.SensorSample
import de.nilsdruyen.composeparty.freestyle.ShaderSample
import de.nilsdruyen.composeparty.freestyle.ShakeSample
import de.nilsdruyen.composeparty.freestyle.SnowFallSample
import de.nilsdruyen.composeparty.freestyle.SpringAnimation
import de.nilsdruyen.composeparty.freestyle.SquareFun
import de.nilsdruyen.composeparty.freestyle.VideoSample
import de.nilsdruyen.composeparty.freestyle.dragdrop.DragAndDropSample
import de.nilsdruyen.composeparty.isles.IsleExample
import de.nilsdruyen.composeparty.layouts.AccordionSample
import de.nilsdruyen.composeparty.layouts.AdventCalendarSample
import de.nilsdruyen.composeparty.layouts.AppBarSample
import de.nilsdruyen.composeparty.layouts.BottomSheetDemo
import de.nilsdruyen.composeparty.layouts.CollapsingToolbarLayoutExample
import de.nilsdruyen.composeparty.layouts.ConstraintAnimation
import de.nilsdruyen.composeparty.layouts.DropDownLayout
import de.nilsdruyen.composeparty.layouts.HorizontalListHeightSample
import de.nilsdruyen.composeparty.layouts.HorizontalParallaxList
import de.nilsdruyen.composeparty.layouts.MotionLayoutExample
import de.nilsdruyen.composeparty.layouts.PhysicalSample
import de.nilsdruyen.composeparty.layouts.PlaceholderSample
import de.nilsdruyen.composeparty.layouts.SampleStaggeredGridLayout
import de.nilsdruyen.composeparty.layouts.ScaffoldTabSample
import de.nilsdruyen.composeparty.layouts.SharedElementSample
import de.nilsdruyen.composeparty.layouts.ZoomImageSample
import de.nilsdruyen.composeparty.material.ScrollableScaffold
import de.nilsdruyen.composeparty.math.GravitySample
import de.nilsdruyen.composeparty.paths.PathPawAnimation
import de.nilsdruyen.composeparty.paths.ScaleShapeSample
import de.nilsdruyen.composeparty.paths.VectorDemo
import de.nilsdruyen.composeparty.swipe.SwipeDemo
import de.nilsdruyen.composeparty.text.AutoSizeTextSample
import de.nilsdruyen.composeparty.text.CascadingText
import de.nilsdruyen.composeparty.text.FontScaleDemo
import de.nilsdruyen.composeparty.text.LineBreakSample
import de.nilsdruyen.composeparty.text.TextAnimationSample
import de.nilsdruyen.composeparty.text.TextBrushing
import de.nilsdruyen.composeparty.text.TextFieldColors
import de.nilsdruyen.composeparty.text.TextFieldTransformation
import de.nilsdruyen.composeparty.text.WrappingText
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme
import de.nilsdruyen.composeparty.utils.ItemList

class MainActivity : ComponentActivity() {

    private val demoItems = mapOf<String, @Composable () -> Unit>(
        "AccordionSample" to { AccordionSample() },
        "LineBreakSample" to { LineBreakSample() },
        "SharedElement" to { SharedElementSample() },
        "AppBarSample" to { AppBarSample() },
        "BottomSheetDemo" to { BottomSheetDemo() },
        "RevealImageSample" to { RevealImageSample() },
        "ShakeSample" to { ShakeSample() },
        "ZoomImageSample" to { ZoomImageSample() },
        "AdventCalendarSample" to { AdventCalendarSample() },
        "PhysicalSample" to { PhysicalSample() },
        "VideoSample" to { VideoSample() },
        "ScratchCard" to { ScratchCardSample() },
        "DragAndDropSample" to { DragAndDropSample() },
        "CascadingText" to { CascadingText() },
        "HorizontalParallaxList" to { HorizontalParallaxList() },
        "HorizontalListHeightSample" to { HorizontalListHeightSample() },
        "ScaffoldTabSample" to { ScaffoldTabSample() },
        "DropDownLayout" to { DropDownLayout() },
        "PlaceholderSample" to { PlaceholderSample() },
        "LayoutAnimationSample" to { LayoutAnimationSample() },
        "ProgressAnimationSample" to { ProgressAnimationSample() },
        "AddToCartButtonDemo" to { AddToCartButtonDemo() },
        "AutoSizeTextSample" to { AutoSizeTextSample() },
        "CanvasSample" to { CanvasSample() },
        "Card Sample" to { CardTiltSample() },
        "ChipButton" to { ChipButton() },
        "Clock" to { ClockSample() },
        "CollapsingToolbarLayout" to { CollapsingToolbarLayoutExample() },
        "ComposeLogo" to { ComposeLogo() },
        "ConstraintAnimation" to { ConstraintAnimation() },
        "DashedLine" to { DashedLine() },
        "DynamicHue Sample" to { DynamicHueSample() },
        "DynamicPointMesh" to { MeshSample() },
        "EqualizerSample" to { EqualizerSample() },
        "Fire Shader Sample" to { FireAnimationShaderSample() },
        "Fling Anim" to { FlingAnimation() },
        "Font Scale Demo" to { FontScaleDemo() },
        "GravitySample" to { GravitySample() },
        "HeartRate" to { HeartRate() },
        "IsleExample" to { IsleExample() },
        "LoadingButtonDemo" to { LoadingButtonDemo() },
        "MotionLayoutExample" to { MotionLayoutExample() },
        "Orbital" to { MoveableSample() },
        "Path Paw Anim" to { PathPawAnimation() },
        "PeopleCardSample" to { PeopleCardSample() },
        "Progress Sample" to { ProgressSample() },
        "PullRefreshSample" to { PullRefreshSample() },
        "Row Animation" to { AnimatedRow() },
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
        "TextButtonSample" to { TextButtonSample() },
        "TextFieldColors" to { TextFieldColors() },
        "TextFieldTransformation" to { TextFieldTransformation() },
        "VectorDemo" to { VectorDemo() },
        "Wrapping Text" to { WrappingText() },
//    "AVD Sample" to { AnimatedVectorDrawable() }, // TODO: not working
//    "MenuToClose" to { MenuToClose() }, // TODO: error
//    "MeshBlockify" to { MeshBlockify() }, // TODO: error
    )

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        setContent {
            ComposePartyTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .semantics { testTagsAsResourceId = true },
                    color = MaterialTheme.colorScheme.background
                ) {
                    var screen by remember { mutableStateOf("") }
                    val changeScreen = { nextScreen: String -> screen = nextScreen }
                    val scrollState = rememberScrollState()

                    BackHandler(screen.isNotEmpty()) {
                        changeScreen("")
                    }

                    Crossfade(targetState = screen, label = "mainCrossFade") { currentScreen ->
                        if (currentScreen.isNotEmpty()) {
                            demoItems[currentScreen]?.invoke()
                        } else {
                            SampleList(
                                scrollState = scrollState,
                                items = demoItems.map { it.key },
                                changeScreen = changeScreen,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SampleList(
    scrollState: ScrollState,
    items: List<String>,
    changeScreen: (String) -> Unit,
) {
    ItemList(items, scrollState) { path ->
        changeScreen(path)
    }
}