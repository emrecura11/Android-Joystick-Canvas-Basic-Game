package com.emrecura.canvassimpleagame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.min
import kotlin.math.sqrt

class JoystickView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
    }
    private val handlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.DKGRAY
    }

    private var centerX = 0f
    private var centerY = 0f
    private var radius = 0f
    private var handleX = 0f
    private var handleY = 0f

    var onMove: ((dx: Float, dy: Float) -> Unit)? = null

    init {
        setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2f
        centerY = h / 2f
        radius = min(centerX, centerY) * 0.9f
        resetHandle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Çizim
        canvas.drawCircle(centerX, centerY, radius, paint) // Ana daire
        canvas.drawCircle(handleX, handleY, radius / 4, handlePaint) // Hareket noktası
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val dx = event.x - centerX
                val dy = event.y - centerY
                val distance = sqrt(dx * dx + dy * dy)

                if (distance < radius) {
                    handleX = event.x
                    handleY = event.y
                } else {
                    val ratio = radius / distance
                    handleX = centerX + dx * ratio
                    handleY = centerY + dy * ratio
                }

                invalidate() // Yeniden çizim
                onMove?.invoke((handleX - centerX) / radius, (handleY - centerY) / radius)
            }

            MotionEvent.ACTION_UP -> {
                resetHandle()
                invalidate()
                onMove?.invoke(0f, 0f)
            }
        }
        return true
    }

    private fun resetHandle() {
        handleX = centerX
        handleY = centerY
    }
}
