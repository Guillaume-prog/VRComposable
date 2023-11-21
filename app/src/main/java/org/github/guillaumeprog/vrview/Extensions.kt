package org.github.guillaumeprog.vrview

import android.content.res.Resources
import androidx.annotation.RawRes

val Int.pxToDp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.dpToPx: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Resources.getRawTextFile(@RawRes resource: Int): String =
    openRawResource(resource).bufferedReader().use { it.readText() }