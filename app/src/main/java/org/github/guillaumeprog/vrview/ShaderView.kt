package org.github.guillaumeprog.vrview

import android.graphics.Bitmap
import android.opengl.GLSurfaceView
import android.util.Log
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import org.github.guillaumeprog.vrview.shader.Renderer

@Composable
fun ShaderView(image: Bitmap, modifier: Modifier = Modifier) {
    val renderer = remember { Renderer(image) }

    Column {
        AndroidView(
            factory = { context ->
                GLSurfaceView(context).apply {
                    setEGLContextClientVersion(2)
                    setRenderer(renderer)

                    renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
                }
            },
            modifier = modifier
        )

        Image(bitmap = image.asImageBitmap(), contentDescription = "", Modifier.fillMaxWidth())
    }
}