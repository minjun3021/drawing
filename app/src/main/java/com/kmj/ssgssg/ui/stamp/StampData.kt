package com.kmj.ssgssg.ui.stamp

import android.graphics.drawable.Drawable

data class StampData(
    val stampTag :String,
    val stampID : Int,
    val stamp96ID:Int,
    var constantState: String?=null
    )