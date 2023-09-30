package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.roundToInt


@SuppressLint("ClickableViewAccessibility")
class CropVideoView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    // second attrs
    private val numSeconds = 90
    private var startSecond = 0
    private var endSecond = 50
    private var minimumSecond = 5

    // stroke attrs
    private val secondBarWidth = 5f
    private val strokePaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = secondBarWidth
    }

    // frame attrs
    private val frameStrokeWidth = 5f
    private val draggableFrameHeight = 100f
    private val draggableFramePaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = frameStrokeWidth
    }
    private val draggableFrameRect by lazy {
        RectF().apply {
            left = interpolation(0f, startSecond.toFloat(), numSeconds.toFloat(), startPos, endPos)
            right = interpolation(0f, endSecond.toFloat(), numSeconds.toFloat(), startPos, endPos)
            top = heightF - draggableFrameHeight - frameStrokeWidth / 2
            bottom = heightF - frameStrokeWidth / 2
        }
    }

    // HandleBar attrs
    private val handleWidth = 30f
    private val leftHandleRect = RectF()
    private val rightHandleRect = RectF()
    private val handlePaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    // Measurements
    private var lastTouchX = 0f
    private val widthF get() = width.toFloat()
    private val heightF get() = height.toFloat()

    // Basic parameter
    private val startPos = 0.5f * secondBarWidth + handleWidth
    private val endPos get() = widthF - 0.5f * secondBarWidth - handleWidth
    private var touchStatus: TouchMode = TouchMode.IDLE
    private val minimumWidth get() = interpolation(0f, minimumSecond.toFloat(), numSeconds.toFloat(), startPos, endPos)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (leftHandleRect.contains(x, event.y)) {
                    touchStatus = TouchMode.LEFT_MOVE
                } else if (rightHandleRect.contains(x, event.y)) {
                    touchStatus = TouchMode.RIGHT_MOVE
                } else if (draggableFrameRect.contains(x, event.y)) {
                    touchStatus = TouchMode.DRAG
                }
                return if (touchStatus != TouchMode.IDLE) {
                    lastTouchX = x
                    true
                } else {
                    false
                }
            }

            MotionEvent.ACTION_MOVE -> {
                when (touchStatus) {
                    TouchMode.DRAG -> {
                        var deltaX = x - lastTouchX
                        if (draggableFrameRect.left + deltaX <= startPos) {
                            deltaX = startPos - draggableFrameRect.left
                        }
                        if (draggableFrameRect.right + deltaX >= endPos) {
                            deltaX = endPos - draggableFrameRect.right
                        }

                        if (deltaX != 0f) {
                            draggableFrameRect.offset(deltaX, 0f)
                            lastTouchX = x
                            invalidate()
                        }
                        return true
                    }

                    TouchMode.LEFT_MOVE -> {
                        var deltaX = x - lastTouchX
                        if (draggableFrameRect.left + deltaX < startPos) {
                            deltaX = startPos - draggableFrameRect.left
                        }
                        if (draggableFrameRect.right - (draggableFrameRect.left + deltaX) < minimumWidth) {
                            deltaX = draggableFrameRect.right - draggableFrameRect.left - minimumWidth
                        }
                        if (deltaX != 0f) {
                            draggableFrameRect.left += deltaX
                            lastTouchX = x
                            invalidate()
                        }
                        return true
                    }

                    TouchMode.RIGHT_MOVE -> {
                        var deltaX = x - lastTouchX
                        if (draggableFrameRect.right + deltaX >= endPos) {
                            deltaX = endPos - draggableFrameRect.right
                        }
                        if ((draggableFrameRect.right + deltaX) - draggableFrameRect.left < minimumWidth) {
                            deltaX = draggableFrameRect.left + minimumWidth - draggableFrameRect.right
                        }
                        if (deltaX != 0f) {
                            draggableFrameRect.right += deltaX
                            lastTouchX = x
                            invalidate()
                            return true
                        }
                    }
                    else -> {}
                }
            }
            //When action up, frame start & end should attach to the nearest stroke
            MotionEvent.ACTION_UP -> {
                when (touchStatus) {
                    TouchMode.DRAG -> {
                        val totalSecond = endSecond - startSecond
                        startSecond = interpolation(startPos, draggableFrameRect.left, endPos, 0f, numSeconds.toFloat()).roundToInt()
                        endSecond = startSecond + totalSecond
                    }

                    TouchMode.LEFT_MOVE -> {
                        startSecond = interpolation(startPos, draggableFrameRect.left, endPos, 0f, numSeconds.toFloat()).roundToInt()
                    }

                    TouchMode.RIGHT_MOVE -> {
                        endSecond = interpolation(startPos, draggableFrameRect.right, endPos, 0f, numSeconds.toFloat()).roundToInt()
                    }
                    else -> {}
                }
                if (touchStatus != TouchMode.IDLE) {
                    draggableFrameRect.left = interpolation(0f, startSecond.toFloat(), numSeconds.toFloat(), startPos, endPos)
                    draggableFrameRect.right = interpolation(0f, endSecond.toFloat(), numSeconds.toFloat(), startPos, endPos)
                    touchStatus = TouchMode.IDLE
                    invalidate()
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw vertical strokes with different lengths
        for (i in 0..numSeconds) {
            val startX = interpolation(0f, i.toFloat(), numSeconds.toFloat(), startPos, endPos)
            val endY = heightF - when {
                (i % 10 == 0) -> 60f
                (i % 5 == 0) -> 40f
                else -> 20f
            }
            canvas.drawLine(startX, heightF, startX, endY, strokePaint)
        }

        // Draw draggable frame
        canvas.drawRect(draggableFrameRect, draggableFramePaint)

        // Draw handle
        updateHandleRect(leftHandleRect, rightHandleRect, draggableFrameRect)
        canvas.drawRect(leftHandleRect, handlePaint)
        canvas.drawRect(rightHandleRect, handlePaint)
    }

    private fun interpolation(x0: Float, x: Float, x1: Float, y0: Float, y1: Float): Float {
        return y0 + (x - x0) * (y1 - y0) / (x1 - x0)
    }

    private fun updateHandleRect(leftRect: RectF, rightRect: RectF, frame: RectF) {
        leftRect.apply {
            left = frame.left - handleWidth
            right = frame.left
            top = frame.top - frameStrokeWidth / 2
            bottom = frame.bottom + frameStrokeWidth / 2
        }
        rightRect.apply {
            left = frame.right
            right = frame.right + handleWidth
            top = frame.top - frameStrokeWidth / 2
            bottom = frame.bottom + frameStrokeWidth / 2
        }
    }

    enum class TouchMode {
        IDLE,
        DRAG,
        LEFT_MOVE,
        RIGHT_MOVE
    }
}