package com.example.drwaing.extension

import android.content.res.Resources.getSystem

val Number.dp: Float get() = ( getSystem().displayMetrics.density * this.toFloat())
