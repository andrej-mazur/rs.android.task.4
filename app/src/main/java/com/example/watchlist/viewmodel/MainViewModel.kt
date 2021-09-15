package com.example.watchlist.viewmodel

import androidx.lifecycle.*
import com.example.watchlist.*
import com.example.watchlist.db.Movie
import com.example.watchlist.db.MovieRepository
import com.example.watchlist.di.locateLazy
import com.example.watchlist.util.SharedPreferencesUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val sharedPreferencesUtils: SharedPreferencesUtils by locateLazy()

    val sortBy = MutableLiveData(DEFAULT_SORT_BY)

    val sortOrder = MutableLiveData(DEFAULT_SORT_ORDER)

    private val movieRepository: MovieRepository by locateLazy()

    private val movies = movieRepository.getAllMovies()

    fun insertMovie(movie: Movie) = viewModelScope.launch(Dispatchers.IO) {
        movieRepository.insertMovie(movie)
    }

    fun updateMovie(movie: Movie) = viewModelScope.launch(Dispatchers.IO) {
        movieRepository.updateMovie(movie)
    }

    fun deleteMovie(movie: Movie) = viewModelScope.launch(Dispatchers.IO) {
        movieRepository.deleteMovie(movie)
    }

    val mediatorMovies = MediatorLiveData<List<Movie>>().apply {
        addSource(movies) { setValue(movies.value) }
        addSource(sortBy) { setValue(movies.value) }
        addSource(sortOrder) { setValue(movies.value) }
    }

    val sortedMovies = Transformations.map(mediatorMovies) { movies ->
        if (movies == null) {
            return@map movies
        }
        return@map if (sharedPreferencesUtils.isSortByTitle()) {
            if (sharedPreferencesUtils.isSortOrderAsc()) movies.sortedBy { it.title } else movies.sortedByDescending { it.title }
        } else if (sharedPreferencesUtils.isSortByYear()) {
            if (sharedPreferencesUtils.isSortOrderAsc()) movies.sortedBy { it.year } else movies.sortedByDescending { it.year }
        } else {
            movies
        }
    }
}