package com.example.companionhud.vr

import android.graphics.Bitmap
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.drawToBitmap

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
        //Log.d("Sizes", "$image, ${composeView?.width}px, ${composeView?.height}")
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
            .background(Color.Black),
        horizontalArrangement = Arrangement.Center
    ) {
        val eyeMod = Modifier.width((width/2).pxToDp.dp).fillMaxHeight()
        EyeImage(image, eyeDistance, true, modifier=eyeMod)
        EyeImage(image, eyeDistance, false, modifier=eyeMod)
    }
}

@Composable
fun EyeImage(image: Bitmap, eyeDistance: Int, isLeftEye: Boolean, modifier: Modifier = Modifier) {
    val eyeX = if (isLeftEye) 0 else eyeDistance
    val eyeWidth = image.width - eyeDistance
    val croppedImage = Bitmap.createBitmap(image, eyeX, 0, eyeWidth, image.height)

    DistortionView(image = croppedImage, k1 = 0.215f, k2 = 0.215f, modifier = modifier)
}