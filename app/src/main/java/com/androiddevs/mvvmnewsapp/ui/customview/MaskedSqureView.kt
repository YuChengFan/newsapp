package com.androiddevs.mvvmnewsapp.ui.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.androiddevs.mvvmnewsapp.R

class MaskedSquareView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private lateinit var bitmap: Bitmap
    init {
        // 初始化 bitmap，這裡假設你已經有了正方形的 bitmap
        // 如果還沒有，可以在後面設定它的方法(setBitmap)
    }
    fun setBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
        invalidate() // 當 bitmap 設定好後，請重新繪製
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.place_holder, null)

        val centerX = width / 2
        val centerY = height / 2
        val bitmapSize = minOf(bitmap.width, bitmap.height)
        val halfSize = bitmapSize / 2
        val srcRect = Rect(0, 0, bitmapSize, bitmapSize)
        val dstRect = Rect(centerX - halfSize, centerY - halfSize, centerX + halfSize, centerY + halfSize)
        val paint = Paint()
        paint.color = Color.RED
        paint.style = Paint.Style.FILL
        // 先畫紅色遮罩
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        // 用畫筆的 PorterDuff 模式來繪製 bitmap
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, srcRect, dstRect, paint)
    }
}




