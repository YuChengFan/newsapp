package com.androiddevs.mvvmnewsapp.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.NewsAppApplication
import com.androiddevs.mvvmnewsapp.model.Article
import com.androiddevs.mvvmnewsapp.model.NewsResponse
import com.androiddevs.mvvmnewsapp.repository.IRepository
import com.androiddevs.mvvmnewsapp.repository.NewsRepository
import com.androiddevs.mvvmnewsapp.util.Resource
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.inject
import retrofit2.Response

class NewViewModel(
    val newsRepository: IRepository
): ViewModel() {

    private val _breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val breakingNews : LiveData<Resource<NewsResponse>> = _breakingNews
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    private val _searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchNews: LiveData<Resource<NewsResponse>> = _searchNews

    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    private val _newsSaveState : MutableLiveData<Boolean> = MutableLiveData()
    val newsSaveState : LiveData<Boolean> = _newsSaveState

    var currentArticle : Article? = null

    init {
        getBreakingNews("us")
//        registerNetworkStateCallback()
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        if (countryCode.isEmpty()){
            _breakingNews.postValue(Resource.Error("incorrect country code"))
            return@launch
        }

        _breakingNews.postValue(Resource.Loading())
        val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
        _breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    fun searchNews(query: String) = viewModelScope.launch {
        if (query.isEmpty()){
            _searchNews.postValue(Resource.Error("invalid query string"))
            return@launch
        }

        _searchNews.postValue(Resource.Loading())
        val response = newsRepository.searchNews(query, searchNewsPage)
        _searchNews.postValue(handleSearchNewsResponse(response))
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.saveArticle(article)
        _newsSaveState.postValue(true)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    fun getSavedArticles() = newsRepository.getSavedArticle()


    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>{
        if (response.isSuccessful){
            breakingNewsPage++
            response.body()?.let { resultResponse->
                if (breakingNewsResponse == null){
                    breakingNewsResponse = resultResponse
                } else {
                    breakingNewsResponse?.articles?.addAll(resultResponse.articles)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>{
        if (response.isSuccessful){
            searchNewsPage++
            response.body()?.let { resultResponse->
                if (searchNewsResponse == null){
                    searchNewsResponse = resultResponse
                } else {
                    searchNewsResponse?.articles?.addAll(resultResponse.articles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun registerNetworkStateCallback() {
        NewsAppApplication.appContext?.let {
            val connectivityManager = it.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                connectivityManager.registerDefaultNetworkCallback(object :
                    ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
//                        Log.d("hunter_test", "onAvailable")
                    }
                    override fun onUnavailable() {
//                        Log.d("hunter_test", "onUnavailable")
                    }
                    override fun onCapabilitiesChanged(network : Network, networkCapabilities : NetworkCapabilities) {
//                        Log.e("hunter_test", "The default network changed capabilities: " + networkCapabilities)
                    }
                    override fun onLost(network : Network) {
//                        Log.e("hunter_test", "The application no longer has a default network. The last default network was " + network)
                    }
                })
            }
        } ?: Log.d("hunter_test","empty context")
    }
}