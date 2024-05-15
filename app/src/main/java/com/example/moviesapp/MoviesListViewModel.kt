package com.example.moviesapp

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.networksdk.Retrofit.ApiResponse
import com.example.networksdk.data.Movie
import com.example.networksdk.data.MoviesResponse
import com.example.networksdk.services.MovieService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesListViewModel : ViewModel() {

    val latestMoviesList = MutableLiveData<ArrayList<Movie>>()
    val popularMoviesList = MutableLiveData<ArrayList<Movie>>()
    var latestPage: Int = 1
    var popularPage: Int = 1
    var showLoadingView = MutableLiveData<Boolean>(false)

    fun getMovies(type: String, context: Context) {
        if (type == Constants.LATEST) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    MovieService.getLatestMoviesList(latestPage+1, context).collect { response ->
                        withContext(Dispatchers.Main) {
                            handleResponse(response, type)
                        }
                    }
                }
            }
        }
        if (type == Constants.POPULAR) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    MovieService.getPopularMoviesList(popularPage+1, context).collect { response ->
                        withContext(Dispatchers.Main) {
                            handleResponse(response, type)
                        }
                    }
                }
            }
        }
    }

    private fun handleResponse(response: ApiResponse<MoviesResponse>, type: String) {
        when (response) {
            is ApiResponse.Success -> {
                if (type == Constants.LATEST) {
                    val newList = ArrayList<Movie>()
                    latestMoviesList.value?.let { newList.addAll(it) }
                    newList.addAll(response.data.results)
                    latestMoviesList.value = newList
                    latestPage = response.data.page
                }
                if(type == Constants.POPULAR) {
                    val newList = ArrayList<Movie>()
                    popularMoviesList.value?.let { newList.addAll(it) }
                    newList.addAll(response.data.results)
                    popularMoviesList.value = newList
                    popularPage = response.data.page
                }
                showLoadingView.value = false
            }

            is ApiResponse.Error -> {
                showLoadingView.value = false
                Log.d(TAG, response.errorMessage)
            }

            is ApiResponse.Loading -> {
                showLoadingView.value = true
            }
        }
    }

    companion object {
        private const val TAG = "LatestMoviesViewModel"
    }

}