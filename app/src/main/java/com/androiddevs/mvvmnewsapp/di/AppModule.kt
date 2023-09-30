package com.androiddevs.mvvmnewsapp.di

import android.content.Context
import androidx.room.Room
import com.androiddevs.mvvmnewsapp.api.NewsAPI
import com.androiddevs.mvvmnewsapp.db.ArticleDAO
import com.androiddevs.mvvmnewsapp.db.ArticleDatabase
import com.androiddevs.mvvmnewsapp.repository.IRepository
import com.androiddevs.mvvmnewsapp.repository.NewsRepository
import com.androiddevs.mvvmnewsapp.ui.NewViewModel
import com.androiddevs.mvvmnewsapp.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

val appModule = module {
    single {
        provideNewsDatabase(androidApplication().applicationContext)
    }
    single {
        provideRetrofit()
    }
    single {
        provideNewsApi(get())
    }
    single {
        provideNewsDao(get())
    }
    single<IRepository>{
        provideNewsRepository(get(), get())
    }
    viewModel{
        NewViewModel(get())
    }
}

private fun provideNewsRepository(
    api: NewsAPI,
    dao: ArticleDAO
) = NewsRepository(dao, api)

private fun provideNewsDatabase(
    context: Context
) = Room.databaseBuilder(context, ArticleDatabase::class.java, "article_db.db"). build()

private fun provideNewsApi(
    retrofit: Retrofit
) = retrofit.create(NewsAPI::class.java)

fun provideNewsDao(
    database: ArticleDatabase
) = database.getArticleDao()

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