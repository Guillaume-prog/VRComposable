package com.example.companionhud.vr

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun VrView(
    size: IntSize,
    k1: Float,
    k2: Float,
    eyeDistance: Int,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .width(size.width.pxToDp.dp)
            .height(size.height.pxToDp.dp),
        contentAlignment = Alignment.Center
    ) {
        var image: Bitmap? by remember { mutableStateOf(null) }

        // Base Content
        Screenshot(onResult = { image = it }) {
            VrBox(
                baseSize = size,
                eyeDistance = eyeDistance
            ) {
                content.invoke()
            }
        }

        // Bifocal view overlayed
        if (image != null) {
            BifocalView(
                image = image!!,
                size = size,
                eyeDistance = eyeDistance,
                distortionParams = Pair(k1, k2)
            )
        }
    }
}