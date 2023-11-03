package com.example.companionhud.vr

import android.graphics.Bitmap
import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import org.intellij.lang.annotations.Language

@Language("AGSL")
val distortionShaderCode = """
// Shader parameters
uniform shader texture;
uniform float k1;
uniform float k2;

// Shader I/O
uniform vec2 resolution;

vec2 applyDistortion(vec2 uv) {
    // Convert the texture coordinates to the range [-1, 1] and center them
    vec2 centered = 2.0 * uv - vec2(1.0, 1.0);

    // Calculate and apply the radial distortion effect
    float r = length(centered);  // The distance from the center
    vec2 distorted = centered * (1.0 + k1 * r + k2 * r * r);

    // Convert the texture coordinates back to the range [0, 1]
    return distorted * 0.5 + vec2(0.5, 0.5);
}


bool isClamped(float2 tc, float threshold) {
    return abs(0.5 - tc.x) <= threshold && abs(0.5 - tc.y) <= threshold;
}


vec4 main(vec2 coord) {
    vec2 uv = coord / resolution;
    vec2 distorted = applyDistortion(uv);

    // Use clamping to keep only the center part
    if (isClamped(distorted, 0.5)) {
        return texture.eval(distorted * resolution);
    } else {
        return vec4(0.0);
    }
}
""".trimIndent()

@Composable
fun DistortionView(image: Bitmap, k1: Float, k2: Float, modifier: Modifier = Modifier) {
    val distortionShader = remember {
        RuntimeShader(distortionShaderCode).apply {
            setFloatUniform("k1", k1)
            setFloatUniform("k2", k2)
        }
    }

    Image(
        modifier = Modifier
            .graphicsLayer {
                clip = true
                renderEffect = RenderEffect.createRuntimeShaderEffect(
                    distortionShader,
                    "texture"
                ).asComposeRenderEffect()
            }
            .onGloballyPositioned {
                distortionShader.setFloatUniform(
                    "resolution",
                    it.size.width.toFloat(),
                    it.size.height.toFloat()
                )
            }
            .then(modifier),
        bitmap = image.asImageBitmap(),
        contentDescription = "Distorted image"
    )
}