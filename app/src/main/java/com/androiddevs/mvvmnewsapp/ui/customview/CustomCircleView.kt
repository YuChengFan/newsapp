package com.androiddevs.mvvmnewsapp.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.androiddevs.mvvmnewsapp.R

class CustomCircleView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val squareImage = ResourcesCompat.getDrawable(resources, R.drawable.place_holder, null)
    private val maskPaint = Paint().apply {
        color = ContextCompat.getColor(context, android.R.color.holo_red_light)
    }

//    private val paint = Paint().apply {
////        isAntiAlias = true
////        style = Paint.Style.FILL
//        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
//        color = 0xFFFF0000.toInt() // 红色
//    }

    private val transparentPaint = Paint().apply {
//        isAntiAlias = true
//        style = Paint.Style.FILL
//        color = 0x00FFFFFF // 透明
        color = Color.BLUE // 完全透明
    }

    private val circleRadius = resources.getDimensionPixelSize(R.dimen.circle_radius).toFloat()
    private val xpaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        Log.d("CustomCircleView", "Drawing square image")

        squareImage?.setBounds(0, 0, width, height)
        squareImage?.draw(canvas!!)

        Log.d("CustomCircleView", "Drawing circular mask")

        val width = width.toFloat()
        val height = height.toFloat()

        // 绘制红色矩形
//        val mPadding = resources.getDimensionPixelOffset(R.dimen.circle_radius_2).toFloat()
        val mPadding = 0.toFloat()
        val mRoundCorner = 0
        canvas?.drawRoundRect(mPadding, mPadding, width - mPadding, height - mPadding, mRoundCorner.toFloat(), mRoundCorner.toFloat(), xpaint)
        //设置遮罩的颜色
        xpaint.color = Color.BLUE
        //设置paint的 xfermode 为PorterDuff.Mode.SRC_OUT
        xpaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
        //画遮罩的矩形(Source image)
        canvas?.drawRect(0f, 0f, width, height, xpaint)
        //清空paint 的 xfermode
        xpaint.xfermode = null


        // 绘制透明圆
//        val centerX = width / 2f
//        val centerY = height / 2f
//        canvas?.drawCircle(centerX, centerY, circleRadius, transparentPaint)
    }
}
