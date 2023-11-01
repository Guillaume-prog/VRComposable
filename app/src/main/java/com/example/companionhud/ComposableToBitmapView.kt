package com.example.companionhud;

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.core.view.size


class ComposableToBitmapView(context: Context, bounds: Rect, composable: @Composable () -> Unit) : View(context) {

    private val composeView = ComposeView(context)

    init {
        // Set up the ComposeView with the Composable content
        composeView.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

        composeView.setContent {
            Box(modifier = Modifier.size(bounds.width().dp, bounds.height().dp)) {
                composable.invoke()
            }
        }
    }

    fun captureBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        composeView.draw(canvas)
        return bitmap
    }
}