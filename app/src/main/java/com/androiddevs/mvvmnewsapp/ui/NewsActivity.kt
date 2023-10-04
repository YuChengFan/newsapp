package com.androiddevs.mvvmnewsapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.databinding.ActivityNewsBinding
import kotlinx.coroutines.CoroutineExceptionHandler
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsActivity : BaseBindingActivity<ActivityNewsBinding>() {

    val viewModel: NewViewModel? by viewModel()

    override val bindingInflater: (LayoutInflater) -> ActivityNewsBinding = ActivityNewsBinding::inflate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("hunter_test","add diff")
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.newsHostFragment) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHostFragment.findNavController())
    }
}
