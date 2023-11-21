package de.nilsdruyen.composeparty.freestyle

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

const val FIRE_SHADER = """
uniform float iTime;
uniform vec2 iResolution;

float noise(vec3 p)
{
	vec3 i = floor(p);
	vec4 a = dot(i, vec3(1., 57., 21.)) + vec4(0., 57., 21., 78.);
	vec3 f = cos((p-i)*acos(-1.))*(-.5)+.5;
	a = mix(sin(cos(a)*a),sin(cos(1.+a)*(1.+a)), f.x);
	a.xy = mix(a.xz, a.yw, f.y);
	return mix(a.x, a.y, f.z);
}

float sphere(vec3 p, vec4 spr)
{
	return length(spr.xyz-p) - spr.w;
}

float flame(vec3 p)
{
	float d = sphere(p*vec3(1.,.5,1.), vec4(.0,-1.,.0,1.));
	return d + (noise(p+vec3(.0,iTime*2.,.0)) + noise(p*3.)*.5)*.25*(p.y) ;
}

float scene(vec3 p)
{
	return min(100.-length(p) , abs(flame(p)) );
}

vec4 raymarch(vec3 org, vec3 dir)
{
	float d = 0.0, glow = 0.0, eps = 0.02;
	vec3  p = org;
	bool glowed = false;
	
	for(int i=0; i<64; i++)
	{
		d = scene(p) + eps;
		p += d * dir;
		if( d>eps )
		{
			if(flame(p) < .0)
				glowed=true;
			if(glowed)
       			glow = float(i)/64.;
		}
	}
	return vec4(p,glow);
}

half4 main(float2 fragCoord)
{
    float2 fragCoord = iResolution.xy - fragCoord;
	vec2 v = -1.0 + 2.0 * fragCoord.xy / iResolution.xy;
	v.x *= iResolution.x/iResolution.y;
	
	vec3 org = vec3(0., -2., 4.);
	vec3 dir = normalize(vec3(v.x*1.6, -v.y, -1.5));
	
	vec4 p = raymarch(org, dir);
	float glow = p.w;
	
	vec4 col = mix(vec4(1.,.5,.1,1.), vec4(0.1,.5,1.,1.), p.y*.02+.4);
	
    vec4 fragColor;
	fragColor = mix(vec4(0.), col, pow(glow*2.,4.));
	//fragColor = mix(vec4(1.), mix(vec4(1.,.5,.1,1.),vec4(0.1,.5,1.,1.),p.y*.02+.4), pow(glow*2.,4.));
    return fragColor;
}
"""

@Composable
fun FireAnimationShaderSample() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        FireAnimationDemo()
    } else {
        Text("only supported on android 13")
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun FireAnimationDemo() {
    var animatableShaderBrush by remember { mutableStateOf(AnimatableShaderBrush()) }
    val infiniteTransition = rememberInfiniteTransition()
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 50f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 50_000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    LaunchedEffect(time) {
        animatableShaderBrush = animatableShaderBrush.setTime(time)
    }
    Text(
        "THIS TEXT IS\nON FIRE\n".repeat(4),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        style = TextStyle(
            brush = animatableShaderBrush, fontSize = 48.sp, textAlign = TextAlign.Center
        )
    )
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class AnimatableShaderBrush(val time: Float = -1f) : ShaderBrush() {
    private var internalShader: RuntimeShader? = null
    private var previousSize: Size? = null

    override fun createShader(size: Size): Shader {
        val shader = if (internalShader == null || previousSize != size) {
            RuntimeShader(FIRE_SHADER).apply {
                setFloatUniform("iResolution", size.width, size.height)
            }
        } else {
            internalShader!!
        }
        shader.setFloatUniform("iTime", time)
        internalShader = shader
        previousSize = size
        return shader
    }

    fun setTime(newTime: Float): AnimatableShaderBrush {
        return AnimatableShaderBrush(newTime).apply {
            this@apply.internalShader = this@AnimatableShaderBrush.internalShader
            this@apply.previousSize = this@AnimatableShaderBrush.previousSize
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is AnimatableShaderBrush) return false
        if (other.internalShader != this.internalShader) return false
        if (other.previousSize != this.previousSize) return false
        if (other.time != this.time) return false
        return true
    }
}