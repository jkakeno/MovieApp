package com.junkakeno.movieapp.data.repository

import com.junkakeno.movieapp.data.network.Retrofit
import timber.log.Timber

class MoviesRepository {

    private val service = Retrofit.getMovieService()

    suspend fun getMovies(query:String) = service.getMovies(query)

    suspend fun getMovie(id:String) = service.getMovie(id)

}