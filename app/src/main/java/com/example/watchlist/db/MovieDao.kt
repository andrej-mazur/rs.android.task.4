package com.example.watchlist.db

import androidx.lifecycle.LiveData

interface MovieDao {

    fun getAllMovies(): LiveData<List<Movie>>

    suspend fun insertMovie(movie: Movie)

    suspend fun updateMovie(movie: Movie)

    suspend fun deleteMovie(movie: Movie)
}