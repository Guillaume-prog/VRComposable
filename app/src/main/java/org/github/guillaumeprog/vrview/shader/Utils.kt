package org.github.guillaumeprog.vrview.shader

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

fun FloatArray.toFloatBuffer(): FloatBuffer {
    return this.let { arr ->
        ByteBuffer.allocateDirect(arr.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(arr)
                position(0)
            }
        }
    }
}

fun ShortArray.toShortBuffer(): ShortBuffer {
    return this.let { arr ->
        ByteBuffer.allocateDirect(arr.size * 2).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(arr)
                position(0)
            }
        }
    }
}

fun loadShader(type: Int, shaderCode: String): Int {
    val shader = GLES20.glCreateShader(type)
    GLES20.glShaderSource(shader, shaderCode)
    GLES20.glCompileShader(shader)
    return shader
}