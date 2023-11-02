package com.example.companionhud

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.drawToBitmap
import androidx.core.view.size

@Composable
fun VrBox(baseWidth: Int, baseHeight: Int, eyeDistance: Int, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .width((baseWidth / 2 + eyeDistance).pxToDp.dp)
            .height(baseHeight.pxToDp.dp)
            .background(Color.Gray)
    ) {
        content.invoke()
    }
}

@Composable
fun Screenshot(onResult: (Bitmap) -> Unit, content: @Composable () -> Unit) {
    var image: Bitmap? by remember { mutableStateOf(null) }
    var composeView: ComposeView? by remember { mutableStateOf(null) }

    LaunchedEffect(image, composeView?.width, composeView?.height) {
        if(composeView?.width != 0 && composeView?.height != 0)
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