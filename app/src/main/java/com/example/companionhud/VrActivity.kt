package com.example.companionhud

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.companionhud.ui.theme.CompanionHUDTheme
import org.github.guillaumeprog.vrview.VrView

class VrActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullscreenHorizontal(this)

        val eyeDistance = intent.extras?.getInt("eyeDistance") ?: 0

        setContent {
            CompanionHUDTheme {

                val size = remember { mutableStateOf(IntSize(0, 0)) }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .onGloballyPositioned { size.value = it.size },
                    contentAlignment = Alignment.Center
                ) {

                    VrView(size = size.value, k1 = 0.215f, k2 = 0.215f, eyeDistance = eyeDistance) {
                        ContentBox(modifier = Modifier.fillMaxSize())
                    }

                }
            }
        }
    }
}

fun setFullscreenHorizontal(activity: Activity) {
    // Set screen orientation horizontal
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

    // Set full screen
    WindowInsetsControllerCompat(activity.window, activity.window.decorView).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}