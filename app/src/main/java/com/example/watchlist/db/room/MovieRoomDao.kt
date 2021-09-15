package com.example.watchlist.db.room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.watchlist.db.Movie
import com.example.watchlist.db.MovieDao


@Dao
abstract class MovieRoomDao : MovieDao {

    @RawQuery(observedEntities = [Movie::class])
    protected abstract fun getAllMovies(query: SupportSQLiteQuery): LiveData<List<Movie>>

    override fun getAllMovies(sortBy: String, sortOrder: String): LiveData<List<Movie>> {
        val query = "SELECT * FROM movies ORDER BY $sortBy $sortOrder"
        return getAllMovies(SimpleSQLiteQuery(query))
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract override suspend fun insertMovie(movie: Movie)

    @Update
    abstract override suspend fun updateMovie(movie: Movie)

    @Delete
    abstract override suspend fun deleteMovie(movie: Movie)

}