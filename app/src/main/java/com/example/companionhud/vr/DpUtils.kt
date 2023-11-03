package com.example.companionhud.vr

import android.content.res.Resources

val Int.pxToDp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.dpToPx: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()