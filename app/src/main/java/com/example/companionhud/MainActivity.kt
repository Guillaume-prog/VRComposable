
package com.example.companionhud

import android.content.res.Resources
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.drawToBitmap
import com.example.companionhud.ui.theme.CompanionHUDTheme

class MainActivity : ComponentActivity() {

    private val tag = "MainActivity-LOG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CompanionHUDTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        var image: Bitmap? by remember { mutableStateOf(null) }

                        Text("Main content", fontSize= 24.sp, fontWeight = FontWeight.Bold)

                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Screenshot(onResult = { image = it }) {
                                ContentBox(windowManager = windowManager)
                            }
                        }

                        Divider(color = Color.White, thickness = 1.dp)

                        Text("Screenshot of the content", fontSize= 24.sp, fontWeight = FontWeight.Bold)

                        Log.d(tag, "${image == null}")
                        if (image != null) {
                            Image(bitmap = image!!.asImageBitmap(), contentDescription = "screenshot")
                        } else {
                            Text("No image yet ...")
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun Screenshot(onResult: (Bitmap) -> Unit, content: @Composable () -> Unit) {
    var image: Bitmap? by remember { mutableStateOf(null) }
    var composeView: ComposeView? by remember { mutableStateOf(null) }

    LaunchedEffect(image) {
        image = composeView?.drawToBitmap()
        if(image != null) onResult(image!!)
    }

    AndroidView(factory = { context ->
        composeView = ComposeView(context).apply {
            setContent {
                content.invoke()
            }
        }
        composeView!!
    })
}

@Composable
fun ContentBox(windowManager: WindowManager) {
    val aspectRatio = remember {
        windowManager.currentWindowMetrics.bounds.let {
            it.height().toFloat() / it.width()
        }
    }

    val animateFloat = remember { Animatable(0f) }

    val animationSpec = remember {
        infiniteRepeatable<Float>(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    }

    LaunchedEffect(animateFloat) {
        animateFloat.animateTo(targetValue = 1f, animationSpec = animationSpec)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
            .background(Color.Red),
    ) {
        Canvas(Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.White,
                style = Fill,
                center = Offset(size.width / 2, size.height / 2),
                radius = 50f * animateFloat.value
            )

            val padding = 25f
            drawRect(
                color = Color.White,
                style = Stroke(2f),
                topLeft = Offset(padding, padding),
                size = Size(size.width - 2 * padding, size.height - 2 * padding)
            )
        }
    }
}