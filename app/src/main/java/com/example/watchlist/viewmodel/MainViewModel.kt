package com.example.watchlist.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.watchlist.*
import com.example.watchlist.db.Movie
import com.example.watchlist.db.MovieRepository
import com.example.watchlist.di.locateLazy
import com.example.watchlist.livedata.CombinedLiveData
import com.example.watchlist.livedata.SharedPreferenceLiveData
import com.example.watchlist.util.SharedPreferencesUtils
import kotlinx.coroutines.*

class MainViewModel : ViewModel() {

    private val sharedPreferencesUtils: SharedPreferencesUtils by locateLazy()

    private val sortBy = SharedPreferenceLiveData("pref_sort_by", DEFAULT_SORT_BY)

    private val sortOrder = SharedPreferenceLiveData("pref_sort_order", DEFAULT_SORT_ORDER)

    private val dbMode = SharedPreferenceLiveData("pref_db_mode", DEFAULT_DB_MODE)

    private val movieRepository: MovieRepository by locateLazy()

    private val movies = dbMode.switchMap {
        movieRepository.getAllMovies()
    }

    fun insertMovie(movie: Movie) = viewModelScope.launch(Dispatchers.IO) {
        movieRepository.insertMovie(movie)
    }

    fun updateMovie(movie: Movie) = viewModelScope.launch(Dispatchers.IO) {
        movieRepository.updateMovie(movie)
    }

    fun deleteMovie(movie: Movie) = viewModelScope.launch(Dispatchers.IO) {
        movieRepository.deleteMovie(movie)
    }

    val mediatorMovies = CombinedLiveData(movies, sortBy, sortOrder).debounce(100L, viewModelScope)

    val sortedMovies = Transformations.map(mediatorMovies) { (movies, sortBy, sortOrder) ->
        if (movies == null) {
            return@map movies
        }

        Log.i("FUUUUUUUUCK", "YEEEEEEEEEEEEEEEEEEAAAAAAAAAAAAAH")
        Log.i("FUUUUUUUUCK sortBy", sortBy.orEmpty())
        Log.i("FUUUUUUUUCK sortOrder", sortOrder.orEmpty())

        return@map if (sharedPreferencesUtils.isSortByTitle()) {
            if (sharedPreferencesUtils.isSortOrderAsc()) movies.sortedBy { it.title } else movies.sortedByDescending { it.title }
        } else if (sharedPreferencesUtils.isSortByYear()) {
            if (sharedPreferencesUtils.isSortOrderAsc()) movies.sortedBy { it.year } else movies.sortedByDescending { it.year }
        } else {
            movies
        }
    }
}