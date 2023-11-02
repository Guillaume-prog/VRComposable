package com.example.companionhud

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.graphics.asImageBitmap
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
        Log.d("Sizes", "$image, ${composeView?.width}px, ${composeView?.height}")
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

@Composable
fun BifocalView(image: Bitmap, width: Int, height: Int, eyeDistance: Int) {
    Row(
        modifier = Modifier
            .width(width.pxToDp.dp)
            .height(height.pxToDp.dp)
            .background(Color.DarkGray),
        horizontalArrangement = Arrangement.Center
    ) {
        EyeImage(image, eyeDistance, true, modifier=Modifier.fillMaxHeight())
        EyeImage(image, eyeDistance, false, modifier=Modifier.fillMaxHeight())
    }
}

@Composable
fun EyeImage(image: Bitmap, eyeDistance: Int, isLeftEye: Boolean, modifier: Modifier = Modifier) {
    val eyeX = if (isLeftEye) 0 else eyeDistance
    val eyeWidth = image.width - eyeDistance
    val croppedImage = Bitmap.createBitmap(image, eyeX, 0, eyeWidth, image.height)

    Image(bitmap = croppedImage.asImageBitmap(), contentDescription = "Eye", modifier = modifier)
}