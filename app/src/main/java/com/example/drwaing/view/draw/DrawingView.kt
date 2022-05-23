package com.example.drwaing.view.draw

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import com.example.drwaing.extension.dp
import kotlin.math.abs

// https://www.youtube.com/watch?v=uJGcmGXaQ0o
class DrawingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), OnTouchListener {
    enum class Mode {
        DRAW, ERASE
    }

    companion object {
        private const val TOUCH_TOLERANCE = 4
    }

    private val points = ArrayList<PaintPoint>()
    private var currentPath: Path? = null
    private var lastXY: Pair<Float, Float>? = null
    private val lastX get() = lastXY?.first ?: 0f
    private val lastY get() = lastXY?.second ?: 0f
    private var bitmap: Bitmap? = null

    var currentColor: Int = Palette.black
    var currentSize: Float = 10.dp
    var drawMode = Mode.DRAW

    init {
        setOnTouchListener(this)
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        val x = motionEvent.x
        val y = motionEvent.y
        when (motionEvent.action) {
            MotionEvent.ACTION_MOVE -> {
                val dx = abs(x - lastX)
                val dy = abs(y - lastY)
                if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                    currentPath?.quadTo(lastX, lastY, (x + lastX) / 2, (y + lastY) / 2)
                    lastXY = Pair(x, y)
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                currentPath = null
                invalidate()
            }
            MotionEvent.ACTION_DOWN -> {
                currentPath = Path()
                currentPath?.moveTo(x, y)
                val point = PaintPoint(
                    currentPath!!,
                    createPaint()
                )
                points.add(point)
                lastXY = Pair(x, y)
                invalidate()
            }
        }
        return true
    }

    private fun createPaint(): Paint = Paint(Paint.DITHER_FLAG).apply {
        isAntiAlias = true
        color = if (drawMode == Mode.DRAW) currentColor else Palette.white
        strokeWidth = currentSize
        maskFilter = null
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isDither = true
    }

    private val paint = Paint()
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        bitmap?.let { canvas.drawBitmap(it, 0f, 0f, paint) }
        points.forEach {
            canvas.drawPath(it.path, it.paint)
        }
    }

    fun getBitmap(): Bitmap {
        val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        layout(left, top, right, bottom)
        draw(c)
        return b
    }

    fun loadBitmap(bitmap: Bitmap?) {
        this.bitmap = bitmap
    }
}
