package com.junkakeno.movieapp.data.network

import com.junkakeno.movieapp.data.model.MovieDetailResponse
import com.junkakeno.movieapp.data.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object{
        const val API_KEY = "ea6c6289"
    }

    @GET("?apikey=$API_KEY&type=movie")
    suspend fun getMovies(@Query("s") query: String?): SearchResponse

    @GET("?apikey=$API_KEY&type=movie&plot=short")
    suspend fun getMovie(@Query("i") query: String?): MovieDetailResponse

}