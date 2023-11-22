package org.github.guillaumeprog.vrview.shader

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Renderer(private val image: Bitmap): GLSurfaceView.Renderer {

    companion object {
        private val QUAD_VERTICES = floatArrayOf(
            -0.5f,  0.5f,      // top left
            -0.5f, -0.5f,      // bottom left
             0.5f, -0.5f,      // bottom right
             0.5f,  0.5f       // top right
        )
    }

    private lateinit var square: TexturedSquare

    override fun onSurfaceCreated(g: GL10?, config: EGLConfig?) {
        square = TexturedSquare(QUAD_VERTICES, image)
    }

    override fun onSurfaceChanged(g: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(g: GL10?) {
        square.draw()
    }

}