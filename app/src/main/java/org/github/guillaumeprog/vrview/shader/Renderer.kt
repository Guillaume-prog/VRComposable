package org.github.guillaumeprog.vrview.shader

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.Log
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Renderer: GLSurfaceView.Renderer {

    companion object {
        private val QUAD_VERTICES = floatArrayOf(
            -0.5f,  0.5f, 0.0f,      // top left
            -0.5f, -0.5f, 0.0f,      // bottom left
             0.5f, -0.5f, 0.0f,      // bottom right
             0.5f,  0.5f, 0.0f       // top right
        )

        private val RED = floatArrayOf(1.0f, 0.0f, 0.0f, 1.0f)
    }

    private lateinit var square: Square

    override fun onSurfaceCreated(g: GL10?, config: EGLConfig?) {
        square = Square(QUAD_VERTICES, RED)
    }

    override fun onSurfaceChanged(g: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(g: GL10?) {
        square.draw()
    }

}