package org.github.guillaumeprog.vrview.shader

import org.intellij.lang.annotations.Language

class ShaderCode {
    companion object {
        val fragment = testFrag
        val vertex = vertexShaderCode
    }
}

@Language("GLSL")
private val testFrag = """
    precision highp float;

    uniform sampler2D uTexture;
    varying vec2 vTexCoord;

    void main(void){
        gl_FragColor = texture2D(uTexture, vTexCoord);//vec4(1.0, 0.0, 0.0, 1.0);
    }
""".trimIndent()

@Language("GLSL")
private val fragmentShaderCode = """
precision highp float;

uniform sampler2D uTexture;
uniform vec2 iResolution;
varying vec2 vTexCoords;

uniform float k1;
uniform float k2;
uniform float eyeDistance;

vec2 applyDistortion(vec2 uv) {
    // Convert the texture coordinates to the range [-1, 1] and center them
    vec2 centered = 2.0 * uv - vec2(1.0, 1.0);

    // Calculate and apply the radial distortion effect
    float r = length(centered);  // The distance from the center
    vec2 distorted = centered * (1.0 + k1 * r + k2 * r * r);

    // Convert the texture coordinates back to the range [0, 1]
    return distorted * 0.5 + vec2(0.5, 0.5);
}

bool isClamped(vec2 tc, float threshold) {
    return abs(0.5 - tc.x) <= threshold && abs(0.5 - tc.y) <= threshold;
}

void main() {
    // Normalized pixel coordinates (from 0 to 1)
    vec2 uv = vTexCoords / iResolution.xy;
    float ed = eyeDistance / iResolution.x;

    bool isLeftEye = uv.x < 0.5;
    uv.x = mod(uv.x * 2.0, 1.0);

    vec2 tc = applyDistortion(uv);

    // Output to screen
    if (isClamped(tc, 0.5)) {
        tc.x = tc.x * (1.0 - ed) + (isLeftEye ? 0.0 : ed);
        gl_FragColor = texture2D(uTexture, tc);
    } else {
        gl_FragColor = vec4(0, 0, 0, 1);
    }
}
""".trimIndent()

@Language("GLSL")
private val vertexShaderCode = """
attribute vec4 aPosition;
attribute vec2 aTexCoords;

varying vec2 vTexCoords;

void main() {
    gl_Position = aPosition;
    vTexCoords = vec2(aTexCoords.x, (1.0 - (aTexCoords.y)));
}
""".trimIndent()