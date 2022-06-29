package com.kmj.ssgssg.extension

import android.content.Context
import android.content.res.Resources.getSystem
import com.kmj.ssgssg.base.ConfirmDialog

val Number.dp: Float get() = ( getSystem().displayMetrics.density * this.toFloat())

fun Context.showDialog(builder: ConfirmDialog.() -> Unit) {
    ConfirmDialog(this).apply {
        builder()
        show()
    }
}