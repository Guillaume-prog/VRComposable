package com.example.companionhud

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun ContentBox(modifier: Modifier) {
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
            .background(Color.Red)
            .then(modifier)
    ) {
        Image(
            painter = painterResource(id = R.drawable.photo_mountain),
            contentDescription = "Background mountain",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

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