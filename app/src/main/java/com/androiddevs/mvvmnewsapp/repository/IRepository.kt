package com.androiddevs.mvvmnewsapp.repository

import androidx.lifecycle.LiveData
import com.androiddevs.mvvmnewsapp.api.RetrofitInstance
import com.androiddevs.mvvmnewsapp.model.Article
import com.androiddevs.mvvmnewsapp.model.NewsResponse
import retrofit2.Response

interface IRepository {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) : Response<NewsResponse>

    suspend fun searchNews(query: String, pageNumber: Int) : Response<NewsResponse>

    suspend fun saveArticle(article: Article)

    fun getSavedArticle() : LiveData<List<Article>>

    suspend fun deleteArticle (article: Article)
}