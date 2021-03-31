package com.junkakeno.movieapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.junkakeno.movieapp.data.repository.MoviesRepository
import com.junkakeno.movieapp.data.util.Resource
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class MovieDetailViewModel : ViewModel() {

    private val repository = MoviesRepository()

    fun getMovie(id:String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            if(repository.getMovie(id).response != "False") {
                emit(Resource.success(data = repository.getMovie(id)))
            }else{
                emit(Resource.error(data = null, message = repository.getMovie(id).error.toString()))
            }
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = "No movies found with id: $id"))
        }
    }

}