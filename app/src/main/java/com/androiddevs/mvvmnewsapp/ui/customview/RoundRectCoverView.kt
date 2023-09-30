package com.androiddevs.mvvmnewsapp.ui.customview

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import com.androiddevs.mvvmnewsapp.NewsAppApplication
import com.androiddevs.mvvmnewsapp.R

class RoundRectCoverView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val squareImage = ResourcesCompat.getDrawable(resources, R.drawable.place_holder, null)
    private val paint = Paint()
    private val bitmapPaint = Paint()
    private var mPadding = 0 // 间距，单位为像素
    private var mRoundCorner = 100 // 圆角矩形的角度，单位为像素
    private var mCoverColor = 0x99000000 // 遮罩的颜色，半透明的黑色

    private val porterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)

    init {
        // 开启View级别的离屏缓冲,并关闭硬件加速，使用软件绘制
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        // 将 dp 转换为像素
        mPadding = (mPadding * resources.displayMetrics.density).toInt()
        mRoundCorner = (mRoundCorner * resources.displayMetrics.density).toInt()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDraw(canvas: Canvas) {
        squareImage?.setBounds(0, 0, width/2, height/2)
        squareImage?.draw(canvas!!)
//        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.place_holder, null)
//        val x = 0.toFloat()
//        val top = Rect(0, 0, bitmap.width/10, bitmap.height/10)
//        val dest = Rect(0, 0, bitmap.width/10, bitmap.height/10)
//        canvas?.drawBitmap(bitmap,width.toFloat()/2, 0.toFloat()/2, bitmapPaint)

        // 先画一个圆角矩形,也就是透明区域(Destination image)
        canvas.drawRoundRect(
            0.toFloat(), 0.toFloat(),
            (width - mPadding).toFloat(), (width - mPadding).toFloat(),
            width.toFloat()/2, width.toFloat()/2, paint
        )

        // 设置遮罩的颜色
        paint.color = NewsAppApplication.appContext?.getColor(R.color.colorAccent)!!

        // 设置paint的 xfermode 为 PorterDuff.Mode.SRC_OUT
        paint.xfermode = porterDuffXfermode

        // 画遮罩的矩形(Source image)
        canvas.drawRect(0f, 0f, width.toFloat(), width.toFloat(), paint)

        // 清空paint 的 xfermode
        paint.xfermode = null
    }
}
