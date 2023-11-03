
package com.example.companionhud

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.companionhud.ui.theme.CompanionHUDTheme
import com.example.companionhud.vr.BifocalView
import com.example.companionhud.vr.Screenshot
import com.example.companionhud.vr.VrBox
import com.example.companionhud.vr.pxToDp

class MainActivity : ComponentActivity() {

    private val tag = "MainActivity-LOG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val screenAspectRatio = windowManager.currentWindowMetrics.bounds.let {
            it.height().toFloat() / it.width()
        }

        val mountainImage = BitmapFactory.decodeResource(resources, R.drawable.photo_mountain)

        setContent {
            CompanionHUDTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    var width by remember { mutableStateOf(0) }
                    val height = (width / screenAspectRatio).toInt()
                    val eyeDistance = 60

                    Column(
                        modifier = Modifier
                            .padding(10.dp)
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
                                    baseWidth = width,
                                    baseHeight = height,
                                    eyeDistance = eyeDistance
                                ) {
                                    ContentBox(Modifier.fillMaxSize())
                                }
                            }
                        }

                        Divider(color = Color.White, thickness = 1.dp)

                        Text(
                            "Stereoscopic view",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        if (image != null) {
                            BifocalView(
                                image = image!!,
                                width = width,
                                height = height,
                                eyeDistance = eyeDistance
                            )
                        } else {
                            Text("No image yet ...")
                        }
                    }

                }
            }
        }
    }
}