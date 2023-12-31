package com.androiddevs.mvvmnewsapp.ui.fragment

import android.os.Binder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.databinding.FragmentArticleBinding
import com.androiddevs.mvvmnewsapp.ui.NewViewModel
import com.androiddevs.mvvmnewsapp.ui.NewsActivity
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ArticleFragment: Fragment(R.layout.fragment_search_news) {
    private val viewModel: NewViewModel by sharedViewModel()
    lateinit var binding: FragmentArticleBinding
//    val args: ArticleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val article = args.article
        val article = viewModel.currentArticle

        article?.let {article->
            binding.webView.apply {
                webViewClient = object : WebViewClient(){
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        return false
                    }
                }
                loadUrl(article.url)
            }

            binding.fab.setOnClickListener {
                viewModel.saveArticle(article)
            }
        }

        viewModel.newsSaveState.observe(viewLifecycleOwner, Observer {
            if (it){
                Snackbar.make(view, "article saved", Snackbar.LENGTH_LONG).apply {
                    show()
                }
            }
        })
    }
}