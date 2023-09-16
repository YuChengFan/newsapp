package com.androiddevs.mvvmnewsapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.adapter.NewsAdapter
import com.androiddevs.mvvmnewsapp.databinding.FragmentSearchNewsBinding
import com.androiddevs.mvvmnewsapp.ui.NewViewModel
import com.androiddevs.mvvmnewsapp.ui.NewsActivity
import com.androiddevs.mvvmnewsapp.util.Resource
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment: Fragment(R.layout.fragment_search_news) {
    lateinit var viewModel: NewViewModel
    private lateinit var searchAdapter: NewsAdapter
    private lateinit var binding: FragmentSearchNewsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        initRecycleView()

        var job: Job? = null
        binding.etSearch.addTextChangedListener {
            job?.cancel()
            job = viewLifecycleOwner.lifecycleScope.launch {
                delay(500L)
                it?.let {
                    Log.d("hunter_test","search list keyword =" + it.toString())
                    viewModel.searchNews(it.toString())
                }
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer {searchRespose->
            when(searchRespose){
                is Resource.Success ->{
                    updateProgressBar(false)
                    Log.d("hunter_test","response =" + searchRespose.data?.articles)
                    searchRespose.data?.let {
                        searchAdapter.differ.submitList(it.articles)
                    }
                }
                is Resource.Error ->{
                    updateProgressBar(false)
                }
                is Resource.Loading ->{
                    updateProgressBar(true)
                }
            }
        })
    }

    private fun initRecycleView(){
        searchAdapter = NewsAdapter()
        binding.rvSearchNews.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = searchAdapter
        }
        searchAdapter.setOnItemClickListener {
            Log.d("hunter_test","article = " + Gson().toJson(it).toString())
            val bundle = Bundle().apply {
                putString("url", it.url)
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment,
                bundle
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchNewsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private fun updateProgressBar(isShow: Boolean){
        binding.paginationProgressBar.visibility = if (isShow) View.VISIBLE else View.GONE
    }
}