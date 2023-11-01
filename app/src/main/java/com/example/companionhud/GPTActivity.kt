package com.example.companionhud
//
//import android.graphics.Bitmap
//import android.os.Bundle
//import android.view.ViewTreeObserver
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.text.BasicTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.asImageBitmap
//import androidx.compose.ui.platform.*
//import androidx.compose.ui.unit.dp
//import androidx.core.view.drawToBitmap
//import androidx.core.view.updateLayoutParams
//
//class GPTActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MyComposable()
//        }
//    }
//}
//
//@Composable
//fun MyComposable() {
//    var text by remember { mutableStateOf("Hello, Compose!") }
//    var bitmap: Bitmap? by remember { mutableStateOf(null) }
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        BasicTextField(
//            value = text,
//            onValueChange = { newText ->
//                text = newText
//            }
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        MyCapturableComposable(text) { capturedBitmap ->
//            bitmap = capturedBitmap
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Display the captured bitmap (if available)
//        if (bitmap != null) {
//            Image(
//                bitmap = bitmap!!.asImageBitmap(),
//                contentDescription = null
//            )
//        }
//    }
//}
//
//@Composable
//fun MyCapturableComposable(text: String, onBitmapCaptured: (Bitmap) -> Unit) {
//    val density = LocalDensity.current.density
//    val size = with(LocalDensity.current) { 200.dp.toPx() }
//
//    val bitmap = rememberUpdatedState(captureToBitmap(size, density) {
//        // Place your Composable content here
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(text, color = Color.Black)
//        }
//    })
//
//    onBitmapCaptured(bitmap.value)
//}
//
//@Composable
//fun captureToBitmap(size: Float, density: Float, content: @Composable () -> Unit): Bitmap {
//    val composition by rememberUpdatedState(Recomposer.current())
//    val view = remember { ComposeView(LocalView.current, composition) }
//    view.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
//
//    val pixelSize = (size * density).toInt()
//    view.updateLayoutParams { width = pixelSize; height = pixelSize }
//
//    var bitmap: Bitmap? = null
//
//    DisposableEffect(view) {
//        val context = view.context
//        val activity = context as ComponentActivity
//        val viewTreeObserver = view.viewTreeObserver
//        val listener = ViewTreeObserver.OnGlobalLayoutListener {
//            if (bitmap == null) {
//                bitmap = view.drawToBitmap()
//                onDispose {
//                    viewTreeObserver.removeOnGlobalLayoutListener(this)
//                }
//            }
//        }
//        viewTreeObserver.addOnGlobalLayoutListener(listener)
//        onDispose {
//            viewTreeObserver.removeOnGlobalLayoutListener(listener)
//        }
//    }
//
//    view.setContent {
//        content()
//    }
//
//    return bitmap ?: Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Return a default bitmap if not captured
//}
