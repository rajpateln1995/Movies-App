package com.example.networksdk.services

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.networksdk.Retrofit.ApiClient
import com.example.networksdk.Retrofit.ApiResponse
import com.example.networksdk.Retrofit.ApiService
import com.example.networksdk.data.MoviesResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject

object MovieService {

    private val apiService = ApiClient.getClient().create(ApiService::class.java)

    private const val TAG = "MovieService"

    fun getPopularMoviesList(page: Int, context: Context): Flow<ApiResponse<MoviesResponse>> = flow {
        if (!isInternetAvailable(context)) {
            emit(ApiResponse.Error("No Internet Connection"))
            return@flow
        }
        emit(ApiResponse.Loading)
        val response = apiService.getPopularMovies(page)
        if (response.isSuccessful) {
            val data = response.body()
            if (data != null) {
                emit(ApiResponse.Success(data))
            } else {
                emit(ApiResponse.Error("Server Error"))
            }
        } else {
            val errorBody = response.errorBody()?.string()
            val errorJson = JSONObject(errorBody)
            val errorMessage = errorJson.getString("status_message")

            emit(ApiResponse.Error(errorMessage))
        }
    }

    fun getLatestMoviesList(page: Int, context: Context): Flow<ApiResponse<MoviesResponse>> = flow {
        emit(ApiResponse.Loading)
        if (!isInternetAvailable(context)) {
            emit(ApiResponse.Error("No Internet Connection"))
            return@flow
        }
        val response = apiService.getLatestMovies(page)
        if (response.isSuccessful) {
            val data = response.body()
            if (data != null) {
                emit(ApiResponse.Success(data))
            } else {
                emit(ApiResponse.Error("Server Error"))
            }
        } else {
            val errorBody = response.errorBody()?.string()
            val errorJson = JSONObject(errorBody)
            val errorMessage = errorJson.getString("status_message")
            emit(ApiResponse.Error(errorMessage))
        }
    }

    fun getImageUrl(path: String): String {
        return "https://image.tmdb.org/t/p/original/$path"
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

        // Emit error state if internet is not available
        return networkCapabilities == null || networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

}