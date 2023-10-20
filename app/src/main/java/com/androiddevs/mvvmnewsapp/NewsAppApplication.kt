package com.androiddevs.mvvmnewsapp

import android.app.Application
import android.content.Context
import com.androiddevs.mvvmnewsapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NewsAppApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        ///testststwr
        startKoin {
            androidContext(this@NewsAppApplication)
            modules(appModule)
        }
        appContext = applicationContext
        /* If you has other classes that need context object to initialize when application is created,
         you can use the appContext here to process. */
    }

    companion object {
        var appContext: Context? = null
    }
}