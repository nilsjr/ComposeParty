package de.nilsdruyen.composeparty.buttons.sticky.render

/**
 * Single-pass liquid shader, shared by Android (AGSL) and every Skiko target (SkSL).
 *
 * The effect draws a coverage mask (any opaque colour = liquid) into a graphics layer and this
 * shader turns it into a lit surface:
 *  1. three rings of taps around each pixel estimate a blurred height field *and* its gradient,
 *     so no separate blur passes are needed (a ring's weighted directions are a gradient filter)
 *  2. normals come from that gradient; Blinn-Phong for a fixed key light and a movable light
 *  3. soft reflections of both lights, Fresnel rim and thin-film sheen
 *  4. glass: the stage backdrop is rebuilt here and refracted through the normals
 *  5. a soft drop shadow cast away from the movable light
 *
 * Coordinates are layer pixels with y pointing down. Lengths in uniforms are in dp and scaled
 * by `uPx` so the look is density independent. Keep this file in sync with
 * `swift/Sources/StickyButtons/Render/LiquidShader.metal`.
 */
internal const val LIQUID_SHADER_SOURCE: String = """
uniform shader content;

uniform float2 uSize;          // layer size in px
uniform float uPx;             // px per dp
uniform float4 uLight;         // xy: movable light (px), z: strength 0..2, w: height (dp)
uniform float uKey;            // key light strength 0..1.5
uniform float uTime;           // seconds
uniform float3 uBase;
uniform float3 uSpecular;
uniform float3 uRim;
uniform float4 uSurface;       // shininess, environment, metal, iridescence
uniform float4 uBody;          // diffuse, subsurface, bump, threshold
uniform float4 uOptics;        // shadow, transmission, refraction, unused
uniform float3 uBackdrop;      // stage surface colour
uniform float4 uDot;           // dot grid colour (rgb + alpha)
uniform float4 uLightTint;     // light pool colour (rgb + alpha)

const int TAPS = 12;
const float TAU = 6.2831853;

float coverage(float2 p) {
    return content.eval(p).a;
}

// x: mean coverage on a ring of radius r, yz: coverage gradient (per px) estimated from the ring.
float3 ring(float2 p, float r) {
    float sum = 0.0;
    float2 gradient = float2(0.0);
    for (int i = 0; i < TAPS; i++) {
        float angle = TAU * float(i) / float(TAPS);
        float2 direction = float2(cos(angle), sin(angle));
        float c = coverage(p + direction * r);
        sum += c;
        gradient += c * direction;
    }
    return float3(sum / float(TAPS), gradient * (2.0 / (float(TAPS) * r)));
}


// Rebuilds the stage background (light pool + 26 dp dot grid centred on the stage) in dp.
float3 backdrop(float2 dp, float2 lightDp) {
    float pool = uLightTint.a * clamp(uLight.z, 0.0, 1.0) * clamp(1.0 - length(dp - lightDp) / 320.0, 0.0, 1.0);
    float3 color = mix(uBackdrop, uLightTint.rgb, pool);
    float2 cell = dp - uSize / uPx * 0.5;
    float2 nearest = cell - 26.0 * floor(cell / 26.0 + 0.5);
    float dotCoverage = 1.0 - smoothstep(1.0, 1.4, length(nearest));
    return mix(color, uDot.rgb, dotCoverage * uDot.a);
}

half4 main(float2 p) {
    float shininess = uSurface.x;
    float environment = uSurface.y;
    float metalness = uSurface.z;
    float iridescence = uSurface.w;
    float diffuseAmount = uBody.x;
    float subsurface = uBody.y;
    float bump = uBody.z;
    float threshold = uBody.w;
    float transmission = uOptics.y;
    float refraction = uOptics.z;

    float c0 = coverage(p);
    float3 r1 = ring(p, 4.0 * uPx);
    float3 r2 = ring(p, 9.0 * uPx);
    float3 r3 = ring(p, 18.0 * uPx);

    float fine = 0.4 * c0 + 0.35 * r1.x + 0.25 * r2.x;
    float coarse = 0.5 * r2.x + 0.5 * r3.x;
    float alpha = max(c0, smoothstep(threshold - 0.05, threshold + 0.05, fine));

    float strength = clamp(uLight.z, 0.0, 1.0);
    float2 awayFromLight = normalize(p - uLight.xy + 0.001);
    float2 shadowDirection = normalize(mix(float2(0.45, 0.8), awayFromLight, strength));
    float shadow = ring(p - shadowDirection * 11.0 * uPx, 12.0 * uPx).x * uOptics.x;

    if (alpha < 0.002) {
        return half4(0.0, 0.0, 0.0, shadow);
    }

    // Height = sqrt(fine) * 0.55 + coarse * 0.45; sqrt lifts thin strands into round cords.
    float rootFine = sqrt(fine + 0.002);
    float h = rootFine * 0.55 + coarse * 0.45;
    float2 gradientFine = 0.6 * r1.yz + 0.4 * r2.yz;
    float2 gradientCoarse = r3.yz;
    float2 slope = (gradientFine * (0.275 / rootFine) + gradientCoarse * 0.45) * uPx;
    float3 n = normalize(float3(-slope * bump * 6.0, 1.0));

    float3 view = float3(0.0, 0.0, 1.0);
    float3 keyLight = normalize(float3(-0.45, -0.62, 0.65));
    float3 movableLight = normalize(float3(uLight.xy - p, uLight.w * uPx));
    float lightDistance = length(uLight.xy - p) / uPx;
    float reach = uLight.w * 1.15;
    float falloff = uLight.z / (1.0 + lightDistance * lightDistance / (reach * reach));

    float diffuse = max(dot(n, keyLight), 0.0) * 0.55 * uKey + max(dot(n, movableLight), 0.0) * 0.8 * falloff;
    float specularKey = pow(max(dot(n, normalize(keyLight + view)), 0.0), shininess) * 0.6 * uKey;
    float specularMovable = pow(max(dot(n, normalize(movableLight + view)), 0.0), shininess * 1.5) * 1.7 * falloff;

    // Soft reflections of the two lights themselves: wide, smooth lobes that move with the lights.
    float3 reflected = reflect(-view, n);
    float sheen = pow(max(dot(reflected, movableLight), 0.0), 6.0) * falloff * 0.5
                + pow(max(dot(reflected, keyLight), 0.0), 6.0) * 0.4 * uKey;
    float sky = smoothstep(-0.9, 0.6, -reflected.y);
    float fresnel = pow(1.0 - clamp(n.z, 0.0, 1.0), 2.5);

    float3 dielectric = uBase * (0.12 + diffuse * diffuseAmount) + uBase * subsurface * h;
    float3 metal = uBase * (0.25 + sky * 0.9);
    float3 color = mix(dielectric, metal, metalness);

    if (transmission > 0.0) {
        // Convex lens: broad curvature magnifies the body, fine curvature bends the rim.
        float2 lensSlope = n.xy * 0.5 - gradientCoarse * uPx * 27.0;
        float2 dp = p / uPx;
        float2 lightDp = uLight.xy / uPx;
        float2 bend = -lensSlope * refraction;
        float spread = 1.0 + fresnel * 0.12;
        float3 transmitted = float3(
            backdrop(dp + bend, lightDp).r,
            backdrop(dp + bend * spread, lightDp).g,
            backdrop(dp + bend * spread * spread, lightDp).b);
        // Light travels further through the rim, so edges absorb more and outline the glass.
        transmitted *= mix(float3(1.0), uBase, 0.15) * (1.0 - smoothstep(0.02, 0.45, fresnel) * 0.72);
        color = mix(color, transmitted, transmission);
    }

    color += uSpecular * (specularKey + specularMovable);
    // Clear glass reflects ~4% head-on, so its reflections live almost entirely at grazing angles.
    float reflectance = mix(0.25 + 0.75 * fresnel + metalness * 0.5, 0.04 + fresnel, transmission);
    color += uSpecular * sheen * environment * reflectance;
    color += uRim * fresnel;
    float3 film = 0.5 + 0.5 * cos(TAU * (float3(0.0, 0.33, 0.67) + fresnel * 1.8 + h * 0.6 + uTime * 0.04));
    color += film * fresnel * iridescence;

    color = clamp(color, 0.0, 1.0);
    return half4(color * alpha, alpha + shadow * (1.0 - alpha));
}
"""
