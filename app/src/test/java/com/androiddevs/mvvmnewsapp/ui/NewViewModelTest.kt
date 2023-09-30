package com.androiddevs.mvvmnewsapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.androiddevs.mvvmnewsapp.MainCoroutineRule
import com.androiddevs.mvvmnewsapp.getOrAwaitValueTest
import com.androiddevs.mvvmnewsapp.model.Article
import com.androiddevs.mvvmnewsapp.model.NewsResponse
import com.androiddevs.mvvmnewsapp.model.Source
import com.androiddevs.mvvmnewsapp.repository.TestNewsRepository
import com.androiddevs.mvvmnewsapp.util.Resource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NewViewModelTest{

    private val testArticle = Article(
        id = 1,
        author = "John Doe",
        description = "Sample description",
        publishedAt = "2023-09-19",
        source = Source(name = "Sample Source"),
        title = "Sample Title",
        url = "https://example.com",
        urlToImage = "https://example.com/image.jpg"
    )

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: NewViewModel

    @Before
    fun setup(){
        viewModel = NewViewModel(TestNewsRepository())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `input empty country code and return error`() = runTest {
        // 在 ViewModel 中调用 getBreakingNews 方法
        viewModel.getBreakingNews("")

        // 验证 breakingNews LiveData 的值是否为 Resource.Error
        val result = viewModel.breakingNews.getOrAwaitValueTest()

        assertThat(result.message).isEqualTo(Resource.Error("incorrect country code", null).message)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `input valid country code and return models`() = runTest {
        viewModel.getBreakingNews("us")
        val result = viewModel.breakingNews.getOrAwaitValueTest()
        assertThat(result.data).isInstanceOf(NewsResponse::class.java)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `input empty query and return error`() = runTest {
        // 在 ViewModel 中调用 getBreakingNews 方法
        viewModel.searchNews("")

        // 验证 breakingNews LiveData 的值是否为 Resource.Error
        val result = viewModel.searchNews.getOrAwaitValueTest()

        assertThat(result.message).isEqualTo(Resource.Error("invalid query string", null).message)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `input valid query and return models`() = runTest {
        viewModel.searchNews("htc")
        val result = viewModel.searchNews.getOrAwaitValueTest()
        assertThat(result.data).isInstanceOf(NewsResponse::class.java)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `insert an article and can get it from article list`() = runTest {
        viewModel.saveArticle(testArticle)
        val articleList = viewModel.getSavedArticles().getOrAwaitValueTest()
        assertThat(articleList).contains(testArticle)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `delete an article and it will disappear from article list`() = runTest {
        viewModel.saveArticle(testArticle)
        viewModel.deleteArticle(testArticle)
        val articleList = viewModel.getSavedArticles().getOrAwaitValueTest()
        assertThat(articleList).doesNotContain(testArticle)
    }

}