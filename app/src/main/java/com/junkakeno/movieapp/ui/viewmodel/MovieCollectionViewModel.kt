package com.junkakeno.movieapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.junkakeno.movieapp.data.repository.MoviesRepository
import com.junkakeno.movieapp.data.util.Resource
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class MovieCollectionViewModel : ViewModel() {

    private val repository = MoviesRepository()

    fun getMovies(query:String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            if(repository.getMovies(query).response != "False") {
                emit(Resource.success(data = repository.getMovies(query)))
            }else{
                emit(Resource.error(data = null, message = repository.getMovies(query).error.toString()))
            }
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = "No movies found for $query. \nTry a different title..."))
        }
    }

}