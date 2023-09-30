package com.androiddevs.mvvmnewsapp.ui.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.androiddevs.mvvmnewsapp.R

class CustomView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val squareImage = BitmapFactory.decodeResource(resources, R.mipmap.square_image)
    private val maskPaint = Paint().apply {
        isAntiAlias = true
        color = Color.TRANSPARENT
    }

    private val circleRadius = dpToPx(100) // 将3dp转换为像素

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // 创建一个透明的 Bitmap 作为遮罩层
        val maskBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val maskCanvas = Canvas(maskBitmap)

        // 在遮罩层上绘制一个黑色的圆
        maskCanvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), circleRadius.toFloat(), maskPaint)

        // 将遮罩层应用到正方形图片上
        val resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val resultCanvas = Canvas(resultBitmap)
        resultCanvas.drawBitmap(squareImage, null, Rect(0, 0, width, height), null)
        maskPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)
        resultCanvas.drawBitmap(maskBitmap, 0f, 0f, maskPaint)

        // 绘制最终结果
        canvas?.drawBitmap(resultBitmap, 0f, 0f, null)
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}
