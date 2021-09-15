package com.example.watchlist.db.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.watchlist.db.Movie
import com.example.watchlist.db.MovieDao

@Dao
interface MovieRoomDao : MovieDao {

    @Query("SELECT * FROM movies")
    override fun getAllMovies(): LiveData<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insertMovie(movie: Movie)

    @Update
    override suspend fun updateMovie(movie: Movie)

    @Delete
    override suspend fun deleteMovie(movie: Movie)

}