package com.example.networksdk.Retrofit

import com.example.networksdk.BuildConfig
import com.example.networksdk.data.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") page: Int, @Query("api_key") apiKey: String =
            BuildConfig.API_KEY): Response<MoviesResponse>

    @GET("movie/now_playing")
    suspend fun getLatestMovies(@Query("page") page: Int, @Query("api_key") apiKey: String =
            BuildConfig.API_KEY): Response<MoviesResponse>
}