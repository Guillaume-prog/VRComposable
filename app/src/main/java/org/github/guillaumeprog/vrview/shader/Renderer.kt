package org.github.guillaumeprog.vrview.shader

import android.content.res.Resources
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.util.Log
import androidx.annotation.RawRes
import com.example.companionhud.R
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Renderer(): GLSurfaceView.Renderer {

    companion object {
        private const val TAG = "StereoRenderer"
    }

    private var program = -1

    private lateinit var square: Square

    private var uTextureHandle = -1
    private var k1Handle = -1
    private var k2Handle = -1
    private var eyeDistanceHandle = -1

    private val textureIds = IntArray(1)

    override fun onSurfaceCreated(g: GL10?, config: EGLConfig?) {
        createProgramWithShaders()
        square = Square(program)

        initParameters()
        Log.d(TAG, "$uTextureHandle, $k1Handle, $k2Handle, $eyeDistanceHandle")
    }

    private fun createProgramWithShaders() {
        val vertexShader = ShaderUtils.loadShader(GLES20.GL_VERTEX_SHADER, ShaderCode.vertex)
        val fragmentShader = ShaderUtils.loadShader(GLES20.GL_FRAGMENT_SHADER, ShaderCode.fragment)

        ShaderUtils.checkShaderCompilation(vertexShader, "Vertex Shader")
        ShaderUtils.checkShaderCompilation(fragmentShader, "Fragment Shader")

        program = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)

            GLES20.glLinkProgram(it)
            ShaderUtils.checkProgramInfoLog(it, "Program Linking")
        }
    }

    private fun initParameters() {
        // Texture
        Log.d(TAG, "ping")
        uTextureHandle = GLES20.glGetUniformLocation(program, "uTexture")

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glGenTextures(textureIds.size, textureIds, 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0])

        // Distortion parameters
        //k1Handle = GLES20.glGetUniformLocation(program, "k1")
        //k2Handle = GLES20.glGetUniformLocation(program, "k2")

        // Eye distance
        //eyeDistanceHandle = GLES20.glGetUniformLocation(program, "eyeDistance")

    }

    override fun onSurfaceChanged(g: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

    }

    override fun onDrawFrame(g: GL10?) {
        GLES20.glUseProgram(program)
        square.startDrawing()

        // Push data to program
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0])
        GLES20.glUniform1i(uTextureHandle, 0)

        square.stopDrawing()
    }

    // Public setters
    // =============================================================================================

    fun setTexture(bitmap: Bitmap) {
        Log.d(TAG, "ping 2")
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