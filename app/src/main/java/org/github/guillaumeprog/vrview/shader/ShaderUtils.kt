package org.github.guillaumeprog.vrview.shader

import android.opengl.GLES20
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class ShaderUtils {

    companion object {
        private const val TAG = "ShaderUtils"

        fun checkShaderCompilation(shaderId: Int, shaderType: String) {
            val compileStatus = IntArray(1)
            GLES20.glGetShaderiv(shaderId, GLES20.GL_COMPILE_STATUS, compileStatus, 0)
            if (compileStatus[0] != GLES20.GL_TRUE) {
                val infoLog = GLES20.glGetShaderInfoLog(shaderId)
                Log.e(TAG, "Error compiling $shaderType shader: $infoLog")
            }
        }

        fun checkProgramInfoLog(programId: Int, operation: String) {
            val linkStatus = IntArray(1)
            GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, linkStatus, 0)
            if (linkStatus[0] != GLES20.GL_TRUE) {
                val infoLog = GLES20.glGetProgramInfoLog(programId)
                Log.e(TAG, "Error $operation: $infoLog")
            }
        }

        fun loadShader(type: Int, shaderCode: String): Int {
            Log.d("CODE", shaderCode.trimIndent())
            val shader = GLES20.glCreateShader(type)
            GLES20.glShaderSource(shader, shaderCode.trimIndent())
            GLES20.glCompileShader(shader)
            return shader
        }
    }

}

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