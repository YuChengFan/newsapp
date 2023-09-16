package com.androiddevs.mvvmnewsapp.di

import androidx.lifecycle.ViewModelProvider
import com.androiddevs.mvvmnewsapp.api.RetrofitInstance
import com.androiddevs.mvvmnewsapp.db.ArticleDatabase
import com.androiddevs.mvvmnewsapp.repository.NewsRepository
import com.androiddevs.mvvmnewsapp.ui.NewViewModel
import com.androiddevs.mvvmnewsapp.ui.NewsViewModelProviderFactory
import com.androiddevs.mvvmnewsapp.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        provideRetrofit()
    }
    single{
        ArticleDatabase(androidApplication().applicationContext)
    }
    single{
        NewsRepository(get())
    }
    viewModel(
        NewViewModel(get())
    )
//    single {
//        NewsViewModelProviderFactory(get())
//    }
}

private fun provideRetrofit(): Retrofit{
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
    return Retrofit.Builder()
        .baseUrl(Constants.Base_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
}