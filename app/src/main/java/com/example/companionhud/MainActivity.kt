
package com.example.companionhud

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.companionhud.ui.theme.CompanionHUDTheme
import org.github.guillaumeprog.vrview.Screenshot
import org.github.guillaumeprog.vrview.VrBox
import org.github.guillaumeprog.vrview.pxToDp

class MainActivity : ComponentActivity() {

    private val tag = "MainActivity-LOG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val screenAspectRatio = windowManager.currentWindowMetrics.bounds.let {
            it.height().toFloat() / it.width()
        }

        setContent {
            CompanionHUDTheme {
                // A surface container using the 'background' color from the theme
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                        .background(Color.White)
                ) {

                    var width by remember { mutableStateOf(0) }
                    val height = (width / screenAspectRatio).toInt()
                    val size = IntSize(width, height)
                    var eyeDistance by remember {
                        mutableStateOf(60f)
                    }

                    Column(
                        modifier = Modifier
                            .onGloballyPositioned { width = it.size.width },
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        var image: Bitmap? by remember { mutableStateOf(null) }

                        Text("Main content", fontSize = 24.sp, fontWeight = FontWeight.Bold)

                        Box(
                            modifier = Modifier
                                .width(width.pxToDp.dp)
                                .height(height.pxToDp.dp)
                                .background(Color.DarkGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Screenshot(onResult = { image = it }) {
                                VrBox(
                                    baseSize = size,
                                    eyeDistance = eyeDistance.toInt()
                                ) {
                                    ContentBox(Modifier.fillMaxSize())
                                }
                            }
                        }

                        Divider(color = Color.Black, thickness = 1.dp)

                        Text(
                            "Stereoscopic view",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        if (image != null) {
                            /*BifocalView(
                                image = image!!,
                                size = size,
                                eyeDistance = eyeDistance.toInt(),
                                distortionParams = Pair(0.215f, 0.215f)
                            )*/
                        } else {
                            Text("No image yet ...")
                        }

                        Text("Adjust eye distance: ${eyeDistance.toInt()}")

                        Slider(
                            value = eyeDistance,
                            valueRange = 0f..100f,
                            steps = 99,
                            onValueChange = { eyeDistance = it }
                        )
                    }
                    
                    Button(onClick = {
                        startActivity(Intent(this@MainActivity, VrActivity::class.java).apply {
                            putExtra("eyeDistance", eyeDistance.toInt())
                        })
                    }, Modifier.align(Alignment.BottomEnd)) {
                        Text(text = "Fullscreen demo")
                    }

                }
            }
        }
    }
}