package com.example.companionhud

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.github.guillaumeprog.vrview.shader.Renderer

class ShaderActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AndroidView(
                factory = { context ->
                    GLSurfaceView(context).apply {
                        setEGLContextClientVersion(2)
                        setRenderer(Renderer())

                        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
                        //setRenderer(CustomRenderer(resources))
                    }
                },
                modifier = Modifier.fillMaxWidth().aspectRatio(1.0f)
            )
        }
    }

}