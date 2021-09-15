package com.example.watchlist.db

import androidx.lifecycle.LiveData
import com.example.watchlist.db.helper.MovieOpenHelperDatabase
import com.example.watchlist.db.room.MovieRoomDatabase
import com.example.watchlist.di.locateLazy
import com.example.watchlist.util.SharedPreferencesUtils

class MovieRepository(
    private val roomDb: MovieRoomDatabase,
    private val openHelperDb: MovieOpenHelperDatabase,
) {

    private val sharedPreferencesUtils: SharedPreferencesUtils by locateLazy()

    private val dao get() = if (sharedPreferencesUtils.isDbModeRoom()) roomDb.movieDao else openHelperDb.movieDao

    fun getAllMovies(): LiveData<List<Movie>> = dao.getAllMovies()

    suspend fun insertMovie(movie: Movie) = dao.insertMovie(movie)

    suspend fun updateMovie(movie: Movie) = dao.updateMovie(movie)

    suspend fun deleteMovie(movie: Movie) = dao.deleteMovie(movie)
}