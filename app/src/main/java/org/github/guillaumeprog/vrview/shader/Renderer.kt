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

class Renderer: GLSurfaceView.Renderer {

    companion object {
        private const val TAG = "StereoRenderer"
    }

    private var program = -1

    private lateinit var square: Square
    private lateinit var stereo: StereoDistort

    override fun onSurfaceCreated(g: GL10?, config: EGLConfig?) {
        createProgramWithShaders()

        square = Square(program)
        stereo = StereoDistort(program)
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

    override fun onSurfaceChanged(g: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(g: GL10?) {
        GLES20.glUseProgram(program)

        square.startDrawing()
        stereo.draw()
        square.stopDrawing()
    }

}