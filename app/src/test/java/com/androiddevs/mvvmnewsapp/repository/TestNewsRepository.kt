package com.androiddevs.mvvmnewsapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.androiddevs.mvvmnewsapp.api.NewsAPI
import com.androiddevs.mvvmnewsapp.model.Article
import com.androiddevs.mvvmnewsapp.model.NewsResponse
import com.androiddevs.mvvmnewsapp.model.Source
import com.androiddevs.mvvmnewsapp.util.Resource
import org.mockito.Mockito
import retrofit2.Response

class TestNewsRepository: IRepository {
    val allSavedArticles : MutableLiveData<List<Article>> = MutableLiveData()
    private val saveArticles= mutableListOf<Article>()

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean){
        shouldReturnNetworkError = value
    }

    override suspend fun getBreakingNews(
        countryCode: String,
        pageNumber: Int
    ): Response<NewsResponse> {
        return Response.success(mockNewsResponse)
    }

    override suspend fun searchNews(query: String, pageNumber: Int): Response<NewsResponse> {
        return Response.success(mockNewsResponse)
    }

    override suspend fun saveArticle(article: Article) {
        saveArticles.add(article)
        allSavedArticles.postValue(saveArticles)
    }

    override fun getSavedArticle(): LiveData<List<Article>> {
        return allSavedArticles
    }

    override suspend fun deleteArticle(article: Article) {
        if (saveArticles.contains(article)){
            saveArticles.remove(article)
            allSavedArticles.postValue(saveArticles)
        }
    }

    // 创建一个模拟的 NewsResponse 对象
    private val mockNewsResponse = NewsResponse(
        articles = mutableListOf(
            Article(
                id = 1,
                author = "John Doe",
                description = "Sample description",
                publishedAt = "2023-09-19",
                source = Source(name = "Sample Source"),
                title = "Sample Title",
                url = "https://example.com",
                urlToImage = "https://example.com/image.jpg"
            ),
            Article(
                id = 2,
                author = "John Doe",
                description = "Sample description",
                publishedAt = "2023-09-19",
                source = Source(name = "Sample Source"),
                title = "Sample Title",
                url = "https://example.com",
                urlToImage = "https://example.com/image.jpg"
            ),
            Article(
                id = 3,
                author = "John Doe",
                description = "Sample description",
                publishedAt = "2023-09-19",
                source = Source(name = "Sample Source"),
                title = "Sample Title",
                url = "https://example.com",
                urlToImage = "https://example.com/image.jpg"
            )
        ),
        status = "ok",
        totalResults = 1
    )
}