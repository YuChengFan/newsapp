package com.androiddevs.mvvmnewsapp.repository

import com.androiddevs.mvvmnewsapp.api.NewsAPI
import com.androiddevs.mvvmnewsapp.api.RetrofitInstance
import com.androiddevs.mvvmnewsapp.db.ArticleDAO
import com.androiddevs.mvvmnewsapp.db.ArticleDatabase
import com.androiddevs.mvvmnewsapp.model.Article

class NewsRepository(
    val dao: ArticleDAO,
    val api: NewsAPI
): IRepository {

    override suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        api.getBreakingNews(countryCode, pageNumber)

    override suspend fun searchNews(query: String, pageNumber: Int) =
        api.searchForNews(query, pageNumber)

    override suspend fun saveArticle(article: Article) {
        dao.upsert(article)
    }

    override fun getSavedArticle() = dao.getAllArticles()

    override suspend fun deleteArticle (article: Article) = dao.delete(article)
}