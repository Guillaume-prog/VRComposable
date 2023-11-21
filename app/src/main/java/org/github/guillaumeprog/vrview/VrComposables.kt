package org.github.guillaumeprog.vrview

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
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.drawToBitmap

@Composable
fun VrBox(baseSize: IntSize, eyeDistance: Int, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .width((baseSize.width / 2 + eyeDistance).pxToDp.dp)
            .height(baseSize.height.pxToDp.dp)
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
fun BifocalView(image: Bitmap, size: IntSize, eyeDistance: Int, distortionParams: Pair<Float, Float>) {
    Row(
        modifier = Modifier
            .width(size.width.pxToDp.dp)
            .height(size.height.pxToDp.dp)
            .background(Color.Black),
        horizontalArrangement = Arrangement.Center
    ) {
        val eyeMod = Modifier.width((size.width/2).pxToDp.dp).fillMaxHeight()
        EyeImage(image, eyeDistance, distortionParams, true, modifier=eyeMod)
        EyeImage(image, eyeDistance, distortionParams, false, modifier=eyeMod)
    }
}

@Composable
fun EyeImage(
    image: Bitmap,
    eyeDistance: Int,
    distortionParams: Pair<Float, Float>,
    isLeftEye: Boolean,
    modifier: Modifier = Modifier
) {
    val eyeX = if (isLeftEye) 0 else eyeDistance
    val eyeWidth = image.width - eyeDistance
    val croppedImage = Bitmap.createBitmap(image, eyeX, 0, eyeWidth, image.height)

    val (k1, k2) = distortionParams

    DistortionView(image = croppedImage, k1 = k1, k2 = k2, modifier = modifier)
}