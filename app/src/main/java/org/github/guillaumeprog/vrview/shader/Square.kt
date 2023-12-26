package org.github.guillaumeprog.vrview.shader

import android.opengl.GLES20
import java.nio.FloatBuffer

class Square(program: Int) {

    companion object {
        private const val COORDS_PER_VERTEX = 2
        private const val VERTEX_STRIDE = COORDS_PER_VERTEX * 4

        private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3)
        private val drawOrderBuffer = drawOrder.toShortBuffer()

        private val QUAD_VERTICES = floatArrayOf(
            -1.0f,  1.0f,      // top left
            -1.0f, -1.0f,      // bottom left
            1.0f, -1.0f,      // bottom right
            1.0f,  1.0f       // top right
        ).toFloatBuffer()

        private val TEXTURE_COORDINATES = floatArrayOf(
            //x,    y
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
        ).toFloatBuffer()
    }

    private val aPositionHandle: Int
    private val aTexCoordsHandle: Int

    init {
        GLES20.glUseProgram(program)
        aPositionHandle = GLES20.glGetAttribLocation(program, "aPosition")
        aTexCoordsHandle = GLES20.glGetAttribLocation(program, "aTexCoords")

        pushVertices(aPositionHandle, QUAD_VERTICES)
        pushVertices(aTexCoordsHandle, TEXTURE_COORDINATES)
    }

    private fun pushVertices(handle: Int, buffer: FloatBuffer) {
        GLES20.glVertexAttribPointer(
            handle,
            COORDS_PER_VERTEX,
            GLES20.GL_FLOAT,
            false,
            VERTEX_STRIDE,
            buffer
        )
    }

    fun startDrawing() {
        GLES20.glEnableVertexAttribArray(aPositionHandle)
        GLES20.glEnableVertexAttribArray(aTexCoordsHandle)
    }

    fun stopDrawing() {
        // Draw vertices in the order giver by drawOrder
        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES,
            drawOrder.size,
            GLES20.GL_UNSIGNED_SHORT,
            drawOrderBuffer
        )

        GLES20.glDisableVertexAttribArray(aTexCoordsHandle)
        GLES20.glDisableVertexAttribArray(aPositionHandle)
    }
}