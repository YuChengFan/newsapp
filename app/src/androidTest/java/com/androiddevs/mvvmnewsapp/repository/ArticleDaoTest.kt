package com.androiddevs.mvvmnewsapp.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.androiddevs.mvvmnewsapp.MainDispatcherRule
import com.androiddevs.mvvmnewsapp.db.ArticleDAO
import com.androiddevs.mvvmnewsapp.db.ArticleDatabase
import com.androiddevs.mvvmnewsapp.getOrAwaitValueTest
import com.androiddevs.mvvmnewsapp.model.Article
import com.androiddevs.mvvmnewsapp.model.Source
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ArticleDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainDispatcherRule()

    private lateinit var database: ArticleDatabase
    private lateinit var dao: ArticleDAO


    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ArticleDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.getArticleDao()
    }

    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun insertAArticleAndCanFindItFromList() = runTest{
        val article = Article(
            id = 1,
            author = "John Doe",
            description = "Sample description",
            publishedAt = "2023-09-19",
            source = Source(name = "Sample Source"),
            title = "Sample Title",
            url = "https://example.com",
            urlToImage = "https://example.com/image.jpg"
        )
        dao.upsert(article)
        val articles = dao.getAllArticles().getOrAwaitValueTest()
        assertThat(articles).contains(article)
    }

    @Test
    fun deleteAArticleAndCannotFindItFromList() = runTest{
        val article = Article(
            id = 1,
            author = "John Doe",
            description = "Sample description",
            publishedAt = "2023-09-19",
            source = Source(name = "Sample Source"),
            title = "Sample Title",
            url = "https://example.com",
            urlToImage = "https://example.com/image.jpg"
        )
        dao.upsert(article)
        dao.delete(article)
        val articles = dao.getAllArticles().getOrAwaitValueTest()
        assertThat(articles).doesNotContain(article)
    }
}