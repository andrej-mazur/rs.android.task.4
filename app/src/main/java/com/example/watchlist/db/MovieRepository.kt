package com.example.watchlist.db

import androidx.lifecycle.LiveData
import com.example.watchlist.db.helper.MovieOpenHelperDatabase
import com.example.watchlist.db.room.MovieRoomDatabase

class MovieRepository(
    private val roomDb: MovieRoomDatabase,
    private val openHelperDb: MovieOpenHelperDatabase,
) {

    private val dao get() = openHelperDb.movieDao

    fun getAllMovies(): LiveData<List<Movie>> = dao.getAllMovies()

    suspend fun insertMovie(movie: Movie) = dao.insertMovie(movie)

    suspend fun updateMovie(movie: Movie) = dao.updateMovie(movie)

    suspend fun deleteMovie(movie: Movie) = dao.deleteMovie(movie)
}