package com.example.moviesapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.moviesapp.databinding.ActivityMainBinding
import com.example.networksdk.Retrofit.ApiResponse
import com.example.networksdk.services.MovieService
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUi()
        lifecycleScope.launch {
            MovieService.getLatestMoviesList(1, this@MainActivity).collect {
                when (it) {
                    is ApiResponse.Success -> {
                        Log.d(TAG, it.data.toString())
                    }

                    is ApiResponse.Error -> {
                        Log.d(TAG, it.errorMessage)
                    }

                    is ApiResponse.Loading -> {
                        Log.d(TAG, "Loading")
                    }
                }

            }
        }

        setContentView(binding.root)
    }

    private fun initUi() {
        val adapter = TabsAdapter(this)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Latest"
                }

                1 -> {
                    tab.text = "Popular"
                }

                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }.attach()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}