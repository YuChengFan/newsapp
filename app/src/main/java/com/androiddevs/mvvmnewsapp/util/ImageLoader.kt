package com.androiddevs.mvvmnewsapp.util

import android.widget.ImageView

interface ImageLoader {
    fun loadImage(url: String?, imageView: ImageView)
}