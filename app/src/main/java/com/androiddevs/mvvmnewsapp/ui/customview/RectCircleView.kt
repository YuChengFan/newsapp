package com.androiddevs.mvvmnewsapp.ui.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class RectCircleView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint().apply {
//        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.RED // 红色
    }

    private val transparentPaint = Paint().apply {
//        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.TRANSPARENT // 完全透明
    }

    private val cornerRadius = dpToPx(0) // 将10dp转换为像素
    private val circleRadius = dpToPx(30) // 将3dp转换为像素

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()

        // 绘制正方形
        canvas?.drawRoundRect(RectF(0f, 0f, width, height), cornerRadius.toFloat(), cornerRadius.toFloat(), paint)

        // 计算圆心坐标
        val centerX = width / 2f
        val centerY = height / 2f

        // 绘制透明的圆形
        canvas?.drawCircle(centerX, centerY, circleRadius.toFloat(), transparentPaint)
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}
