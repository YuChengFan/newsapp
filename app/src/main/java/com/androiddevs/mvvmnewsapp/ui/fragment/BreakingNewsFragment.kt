package com.androiddevs.mvvmnewsapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.adapter.NewsAdapter
import com.androiddevs.mvvmnewsapp.databinding.FragmentBreakingNewsBinding
import com.androiddevs.mvvmnewsapp.ui.NewViewModel
import com.androiddevs.mvvmnewsapp.util.Constants
import com.androiddevs.mvvmnewsapp.util.Resource
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class BreakingNewsFragment: BaseBindingFragment<FragmentBreakingNewsBinding>() {

    private val viewModel: NewViewModel by sharedViewModel()
    lateinit var newsAdapter: NewsAdapter

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBreakingNewsBinding = FragmentBreakingNewsBinding::inflate

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLastPage && !isLoading
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate){
                viewModel.getBreakingNews("us")
                isScrolling = false
            }
            val firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition()

            Log.d("hunter_test","firstCompletelyVisibleItemPosition = " + firstCompletelyVisibleItemPosition
                    + ", visibleItemCount = " + visibleItemCount + ", totalItemCount = " + totalItemCount)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            isScrolling = true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycleView()

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { newsResponse ->
            when(newsResponse){
                is Resource.Success ->{
                    updateProgressBar(false)
                    newsResponse.data?.let {
                        newsAdapter.differ.submitList(it.articles.toList())
                        val totalPages = it.totalResults/ (Constants.QUERY_PAGE_SIZE + 2)
                        isLastPage = viewModel.breakingNewsPage == totalPages
                    }
                }
                is Resource.Error ->{
                    updateProgressBar(false)
                    Log.d("hunter_test","error message :" + newsResponse.message)
                }
                is Resource.Loading ->{
                    updateProgressBar(true)
                }
            }
        })
    }

    private fun initRecycleView(){
        newsAdapter = NewsAdapter()
        binding?.rvBreakingNews?.adapter = newsAdapter
        binding?.rvBreakingNews?.layoutManager = LinearLayoutManager(activity)
        newsAdapter.setOnItemClickListener {
//            val bundle = Bundle().apply {
//                putSerializable("article", it)
//            }
//            viewModel.currentArticle = it
//            findNavController().navigate(
//                R.id.action_breakingNewsFragment_to_articleFragment,
//                bundle
//            )
            viewModel.currentArticle = it
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment
            )
        }
        binding?.rvBreakingNews?.addOnScrollListener(this@BreakingNewsFragment.scrollListener)
    }

    private fun updateProgressBar(isShow: Boolean){
        binding?.paginationProgressBar?.visibility = if (isShow) View.VISIBLE else View.GONE
        isLoading = isShow
    }
}