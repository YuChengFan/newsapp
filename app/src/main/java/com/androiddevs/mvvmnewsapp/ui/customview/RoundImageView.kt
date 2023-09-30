package com.androiddevs.mvvmnewsapp.ui.customview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PaintFlagsDrawFilter
import android.graphics.Path
import android.graphics.Rect
import android.graphics.Region
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView
import com.androiddevs.mvvmnewsapp.R


class RoundImageView(context: Context?, attrs: AttributeSet?) :
    androidx.appcompat.widget.AppCompatImageView(context!!, attrs) {
    private var mBitmap: Bitmap? = null
    private var mBackgroundBitmap: Bitmap? = null
    private val mRect = Rect()
    private val pdf = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG)
    private val mPaint = Paint()
    private val mPath: Path = Path()

    init {
        init()
        mBitmap = BitmapFactory.decodeResource(resources, R.drawable.place_holder, null)
        mBackgroundBitmap = BitmapFactory.decodeResource(resources, R.mipmap.square_image, null)
//        mBackgroundBitmap = Bitmap.createScaledBitmap(mBackgroundBitmap!!, width, height, true)
    }

    //传入一个Bitmap对象
    fun setBitmap(bitmap: Bitmap?) {
        mBitmap = bitmap
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("hunter_test","onTouchEvent x :" + event?.x + ", y :" + event?.y)
        event?.let {
            if (bitmapRect.contains(it.x.toInt(), it.y.toInt())){
                Log.d("hunter_test","contains")
                bitmapRect.apply {
                    left = it.x.toInt() - width/4
                    right = it.x.toInt() + width/4
                    top = it.y.toInt() - height/4
                    bottom = it.y.toInt() + height/4
                }
                invalidate()
            }

        }
        return super.onTouchEvent(event)
    }

    private fun init() {
        mPaint.style = Paint.Style.STROKE
        mPaint.flags = Paint.ANTI_ALIAS_FLAG
        mPaint.color = Color.BLACK
        mPaint.isAntiAlias = true // 抗锯尺
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val start = 0.toFloat()
        mBackgroundBitmap?.let {

//            canvas.drawBitmap(Bitmap.createScaledBitmap(it, width, height/2, true), start, start, mPaint )
        } ?: Log.d("hunter_test","null bitmap")
        if (mBitmap == null) {
            return
        }
        mRect[0, 0, width] = height
        canvas.save()
//        canvas.drawFilter = pdf
        mPath.addCircle(width.toFloat() / 2, height.toFloat() / 4, width.toFloat() / 2, Path.Direction.CCW)
//        canvas.clipPath(mPath, Region.Op.UNION)
//        canvas.clipPath(mPath)
        canvas.drawBitmap(Bitmap.createScaledBitmap(mBitmap!!, width/2, height/2, true),null, bitmapRect, mPaint)
        canvas.restore()
//        mPaint.color = Color.BLUE
//        canvas.drawCircle(width.toFloat() / 2, height.toFloat() / 2, 200.toFloat(), mPaint)


//
//        mBackgroundBitmap?.let {
//
//            canvas.drawBitmap(Bitmap.createScaledBitmap(it, width, height/2, true), start, height.toFloat()/2, mPaint )
//        } ?: Log.d("hunter_test","null bitmap")
//        if (mBitmap == null) {
//            return
//        }
//        mRect[0, 0, width] = height
//        canvas.save()
////        canvas.drawFilter = pdf
//        mPath.addCircle(width.toFloat() / 2, height.toFloat() * 3  / 4, width.toFloat() / 2, Path.Direction.CCW)
////        canvas.clipPath(mPath, Region.Op.UNION)
//        canvas.clipPath(mPath)
//        canvas.drawBitmap(Bitmap.createScaledBitmap(mBitmap!!, width, height/2, true), start, height.toFloat() / 2, mPaint)
//        canvas.restore()
////        mPaint.color = Color.BLUE
////        canvas.drawCircle(width.toFloat() / 2, height.toFloat() / 2, 200.toFloat(), mPaint)
    }
    private val bitmapRect: Rect by lazy {
        Rect().apply {
            left = width/4
            right = width*3/4
            top = height/4
            bottom = height*3/4
        }
    }
}