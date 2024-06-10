package org.github.guillaumeprog.vrview.shader

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLUtils
import android.util.Log

class StereoDistort(program: Int) {

    private val uTextureHandle: Int
    private val k1Handle: Int = -1
    private val k2Handle: Int = -1
    private val eyeDistanceHandle: Int = -1

    private val textureIds = IntArray(1)

    init {
        GLES20.glUseProgram(program)
        // Texture
        uTextureHandle = GLES20.glGetUniformLocation(program, "uTexture")

        val error = GLES20.glGetError()
        if (error != GLES20.GL_NO_ERROR) {
            Log.e("StereoRenderer", "OpenGL error: $error")
        }

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glGenTextures(textureIds.size, textureIds, 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0])

        // Distortion parameters
        //k1Handle = GLES20.glGetUniformLocation(program, "k1")
        //k2Handle = GLES20.glGetUniformLocation(program, "k2")

        // Eye distance
        //eyeDistanceHandle = GLES20.glGetUniformLocation(program, "eyeDistance")

        Log.d("StereoRenderer", "$program, $uTextureHandle, $k1Handle, $k2Handle, $eyeDistanceHandle")
    }

    fun draw() {
        // Push data to program
        GLES20.glUniform1i(uTextureHandle, 0)
    }

    // Public setters
    // =============================================================================================

    fun setTexture(bitmap: Bitmap) {
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)
        // bitmap.recycle()
    }

    fun setDistortionParams(k1: Float, k2: Float) {
        GLES20.glUniform1f(k1Handle, k1)
        GLES20.glUniform1f(k2Handle, k2)
    }

    fun setEyeDistance(eyeDistance: Float) {
        GLES20.glUniform1f(eyeDistanceHandle, eyeDistance)
    }
}