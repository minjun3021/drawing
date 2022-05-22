package com.example.drwaing.view.draw

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import com.example.drwaing.extension.dp

class ColorDrawable(
    private val color: Int,
    private val hasBorder: Boolean = false,
) : Drawable() {

    var selected = false
        set(value) {
            field = value
            invalidateSelf()
        }

    private val marginSize get() = if (selected) 0.dp else 4.dp
    private val radiusSize get() = if (selected) 22.dp else 20.dp

    private val colorPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        this.color = this@ColorDrawable.color
    }

    private val borderPaint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        this.color = Palette.border
    }


    override fun draw(canvas: Canvas) {
        canvas.drawRoundRect(
            bounds.left.toFloat() + marginSize,
            bounds.top.toFloat() + marginSize,
            bounds.right.toFloat() - marginSize,
            bounds.bottom.toFloat() - marginSize,
            radiusSize,
            radiusSize,
            colorPaint
        )
        if (hasBorder) {
            canvas.drawRoundRect(
                bounds.left.toFloat() + marginSize,
                bounds.top.toFloat() + marginSize,
                bounds.right.toFloat() - marginSize,
                bounds.bottom.toFloat() - marginSize,
                radiusSize,
                radiusSize,
                borderPaint
            )
        }
    }

    override fun setAlpha(p0: Int) {
        colorPaint.alpha = p0
        borderPaint.alpha = p0
    }

    override fun setColorFilter(p0: ColorFilter?) {}

    override fun getOpacity(): Int = PixelFormat.OPAQUE
}