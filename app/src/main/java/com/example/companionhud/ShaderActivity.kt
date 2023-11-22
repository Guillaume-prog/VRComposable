package com.example.companionhud

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.viewinterop.AndroidView
import org.github.guillaumeprog.vrview.ShaderView
import org.github.guillaumeprog.vrview.shader.Renderer

class ShaderActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val image = BitmapFactory.decodeResource(resources, R.drawable.photo_mountain)

        setContent {
            ShaderView(
                image = image,
                modifier = Modifier.fillMaxWidth().aspectRatio(1.0f)
            )
        }
    }

}