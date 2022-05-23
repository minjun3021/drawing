package com.example.drwaing.extension

import android.content.Context
import android.content.res.Resources.getSystem
import com.example.drwaing.base.ConfirmDialog

val Number.dp: Float get() = ( getSystem().displayMetrics.density * this.toFloat())

fun Context.showDialog(builder: ConfirmDialog.() -> Unit) {
    ConfirmDialog(this).apply {
        builder()
        show()
    }
}