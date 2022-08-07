package de.nilsdruyen.composeparty

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme
import de.nilsdruyen.composeparty.utils.ItemList

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            val splashScreen = installSplashScreen()
//            splashScreen.setKeepOnScreenCondition { true }
//        }

        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ComposePartyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val screen = remember { mutableStateOf("") }

                    BackHandler(screen.value.isNotEmpty()) {
                        screen.value = ""
                    }

                    Crossfade(targetState = screen.value) {
                        if (it.isNotEmpty()) {
                            demoItems[it]?.invoke()
                        } else {
                            ItemList { path ->
                                screen.value = path
                            }
                        }
                    }
                }
            }
        }
    }
}