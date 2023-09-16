package com.androiddevs.mvvmnewsapp.util

object ImageLoaderFactory {
    private var instance: ImageLoader? = null
//    private var LOCK = Any()
    fun getInstance() = instance ?: createImageLoader()
        .also {
            instance = it
        }

    private fun createImageLoader() : ImageLoader{
        return GlideImageLoader()
    }
}