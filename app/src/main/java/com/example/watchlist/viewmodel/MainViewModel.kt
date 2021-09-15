package com.example.watchlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.watchlist.*
import com.example.watchlist.db.Movie
import com.example.watchlist.db.MovieRepository
import com.example.watchlist.di.locateLazy
import com.example.watchlist.livedata.CombinedLiveData
import com.example.watchlist.livedata.SharedPreferenceLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val dbMode = SharedPreferenceLiveData(PREF_DB_MODE, DEFAULT_DB_MODE)

    private val sortBy = SharedPreferenceLiveData(PREF_SORT_BY, DEFAULT_SORT_BY)

    private val sortOrder = SharedPreferenceLiveData(PREF_SORT_ORDER, DEFAULT_SORT_ORDER)

    private val movieRepository: MovieRepository by locateLazy()

    fun insertMovie(movie: Movie) = viewModelScope.launch(Dispatchers.IO) {
        movieRepository.insertMovie(movie)
    }

    fun updateMovie(movie: Movie) = viewModelScope.launch(Dispatchers.IO) {
        movieRepository.updateMovie(movie)
    }

    fun deleteMovie(movie: Movie) = viewModelScope.launch(Dispatchers.IO) {
        movieRepository.deleteMovie(movie)
    }

    val movies = CombinedLiveData(dbMode, sortBy, sortOrder)
        .debounce(100L, viewModelScope)
        .switchMap { (_, sortBy, sortOrder) ->
            movieRepository.getAllMovies(sortBy!!, sortOrder!!)
        }
}