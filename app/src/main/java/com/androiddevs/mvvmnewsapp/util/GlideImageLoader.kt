package com.androiddevs.mvvmnewsapp.util

import android.widget.ImageView
import com.androiddevs.mvvmnewsapp.NewsAppApplication
import com.bumptech.glide.Glide

class GlideImageLoader: ImageLoader {
    override fun loadImage(url: String?, imageView: ImageView) {
        NewsAppApplication.appContext?.let {context ->
            url?.let {
                Glide.with(context).load(url).into(imageView)
            }
        }
    }
}