package de.nilsdruyen.composeparty.freestyle

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.draw.DrawResult
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.nilsdruyen.composeparty.utils.Centered

@Composable
fun ShaderSample() {
    Centered {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            Text(
                modifier = Modifier.padding(16.dp),
                fontSize = 20.sp,
                text = "Oh no! Gradient Shader Fluff requires Android 13 (API 33)!"
            )
        } else {
            ShaderSample(modifier = Modifier.fillMaxSize())
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ShaderSample(modifier: Modifier = Modifier) {
    val gradientShader: RuntimeShader = remember { warpSpeedShader() }
    val gradientShaderBrush: ShaderBrush = remember { ShaderBrush(gradientShader) }

    SimpleSketchWithCache(speed = 0.08f, modifier = modifier.fillMaxSize()) { time ->
        gradientShader.setFloatUniform(
            "iResolution",
            this.size.width, this.size.height
        )
        gradientShader.setFloatUniform("iTime", time.value)

        onDrawBehind {
            drawRect(brush = gradientShaderBrush)
        }
    }
}

@Composable
fun SimpleSketchWithCache(
    modifier: Modifier = Modifier,
    speed: Float = 0.01f,
    onBuildDrawCache: CacheDrawScope.(time: State<Float>) -> DrawResult
) {
    val time = remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        do {
            withFrameMillis {
                time.value = time.value + speed
            }
        } while (true)
    }

    Box(modifier = modifier.drawWithCache { onBuildDrawCache(time) })
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun warpSpeedShader() = RuntimeShader(
    """
        // 'Warp Speed 2'
        // David Hoskins 2015.
        // License Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.

        // Fork of:-   https://www.shadertoy.com/view/Msl3WH
        //----------------------------------------------------------------------------------------
        uniform float2 iResolution;      // Viewport resolution (pixels)
        uniform float  iTime;            // Shader playback time (s)

        vec4 main( in float2 fragCoord )
        {
            float s = 0.0, v = 0.0;
            vec2 uv = (fragCoord / iResolution.xy) * 2.0 - 1.;
            float time = (iTime-2.0)*58.0;
            vec3 col = vec3(0);
            vec3 init = vec3(sin(time * .0032)*.3, .35 - cos(time * .005)*.3, time * 0.002);
            for (int r = 0; r < 100; r++) 
            {
                vec3 p = init + s * vec3(uv, 0.05);
                p.z = fract(p.z);
                // Thanks to Kali's little chaotic loop...
                for (int i=0; i < 10; i++)	p = abs(p * 2.04) / dot(p, p) - .9;
                v += pow(dot(p, p), .7) * .06;
                col +=  vec3(v * 0.2+.4, 12.-s*2., .1 + v * 1.) * v * 0.00003;
                s += .025;
            }
            return vec4(clamp(col, 0.0, 1.0), 1.0);
        }
    """.trimIndent()
)