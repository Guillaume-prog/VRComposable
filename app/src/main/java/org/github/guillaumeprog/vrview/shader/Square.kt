package org.github.guillaumeprog.vrview.shader

import android.opengl.GLES20
import android.util.Log
import org.intellij.lang.annotations.Language

class Square(vertices: FloatArray, private val color: FloatArray) {

    @Language("GLSL")
    private val vertexShaderCode = """
        attribute vec4 aPosition;
        
        void main() {
            gl_Position = aPosition;
        }
    """.trimIndent()

    @Language("GLSL")
    private val fragmentShaderCode = """
        precision mediump float;
        uniform vec4 uColor;
        
        void main() {
            gl_FragColor = uColor;
        }
    """.trimIndent()

    companion object {
        private const val COORDS_PER_VERTEX = 3
        private const val VERTEX_STRIDE = COORDS_PER_VERTEX * 4

        private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3)
    }

    private val vertices = vertices.toFloatBuffer()

    private val program: Int

    private val aPositionHandle: Int
    private val uColorHandle: Int

    init {
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        program = GLES20.glCreateProgram()
        GLES20.glAttachShader(program, vertexShader)
        GLES20.glAttachShader(program, fragmentShader)
        GLES20.glLinkProgram(program)

        // Get handles for the different params in the shaders
        GLES20.glUseProgram(program)

        aPositionHandle = GLES20.glGetAttribLocation(program, "aPosition").also {
            if (it == -1) Log.e("Square.init", "Failed to retrieve position handle")
        }

        uColorHandle = GLES20.glGetUniformLocation(program, "uColor").also {
            if (it == -1) Log.e("Square.init", "Failed to retrieve color handle")
        }
    }

    fun draw() {
        Log.d("Square", "program: $program, aPosition: $aPositionHandle, uColor: $uColorHandle")
        GLES20.glUseProgram(program)

        GLES20.glGetUniformLocation(program, "uColor").also { uColor ->
            Log.d("Square", "$uColor")

            // Pass color to the color uniform in the fragment shader
            GLES20.glUniform4fv(uColor, 1, color, 0)
        }

        with(aPositionHandle) {
            GLES20.glEnableVertexAttribArray(this)

            // Pass vertices to Position attribute in vertex shader
            GLES20.glVertexAttribPointer(
                this,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                VERTEX_STRIDE,
                vertices
            )

            // Draw vertices in the order giver by drawOrder
            GLES20.glDrawElements(
                GLES20.GL_TRIANGLES,
                drawOrder.size,
                GLES20.GL_UNSIGNED_SHORT,
                drawOrder.toShortBuffer()
            )

            GLES20.glDisableVertexAttribArray(this)
        }
    }
}