package org.github.guillaumeprog.vrview.shader

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLUtils
import android.util.Log
import org.intellij.lang.annotations.Language
import java.nio.FloatBuffer

class TexturedSquare(vertices: FloatArray, image: Bitmap) {

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

    @Language("GLSL")
    private val fragmentShaderCode = """
        precision highp float;
        
        uniform sampler2D uTexture;
        varying vec2 vTexCoords;
        
        void main() {
            //gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);
            gl_FragColor = texture2D(uTexture, vTexCoords);
        }
    """.trimIndent()

    companion object {
        private const val COORDS_PER_VERTEX = 2
        private const val VERTEX_STRIDE = COORDS_PER_VERTEX * 4

        private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3)
        private val drawOrderBuffer = drawOrder.toShortBuffer()

        private val TEXTURE_COORDINATES = floatArrayOf(
            //x,    y
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
        )
    }

    private val program: Int

    private val aPositionHandle: Int
    private val aTexCoordsHandle: Int
    private val uTextureHandle: Int

    private val textureIds = IntArray(1)

    init {
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        program = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)

            GLES20.glLinkProgram(it)

            // Check link status
            val linkStatus = IntArray(1)
            GLES20.glGetProgramiv(it, GLES20.GL_LINK_STATUS, linkStatus, 0)

            if (linkStatus[0] != GLES20.GL_TRUE) {
                val infoLog = GLES20.glGetProgramInfoLog(it)
                Log.e("Square", "Error linking program: $infoLog")
            }
        }

        // Initialize vertex data
        GLES20.glUseProgram(program)
        aPositionHandle = GLES20.glGetAttribLocation(program, "aPosition")
        aTexCoordsHandle = GLES20.glGetAttribLocation(program, "aTexCoords")

        pushVertices(aPositionHandle, vertices.toFloatBuffer())
        pushVertices(aTexCoordsHandle, TEXTURE_COORDINATES.toFloatBuffer())

        // Initialize texture
        uTextureHandle = GLES20.glGetUniformLocation(program, "uTexture")

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glGenTextures(textureIds.size, textureIds, 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0])
        setTexture(image)

        Log.d("Square.init", "texture handle: $uTextureHandle")
    }

    fun draw() {
        GLES20.glUseProgram(program)

        GLES20.glEnableVertexAttribArray(aPositionHandle)
        GLES20.glEnableVertexAttribArray(aTexCoordsHandle)

        // Push data to program
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0])
        GLES20.glUniform1i(uTextureHandle, 0)

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

    fun setTexture(bitmap: Bitmap) {
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)
//        bitmap.recycle()
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
}