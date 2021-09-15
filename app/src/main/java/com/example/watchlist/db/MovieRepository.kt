package com.example.watchlist.db

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.watchlist.DB_MODE_ROOM
import com.example.watchlist.DEFAULT_DB_MODE
import com.example.watchlist.PREF_DB_MODE
import com.example.watchlist.db.helper.MovieOpenHelperDatabase
import com.example.watchlist.db.room.MovieRoomDatabase
import com.example.watchlist.di.locateLazy

class MovieRepository(
    private val roomDb: MovieRoomDatabase,
    private val openHelperDb: MovieOpenHelperDatabase,
) {

    private val sharedPreferences: SharedPreferences by locateLazy()

    private fun isRoomDbMode(): Boolean {
        return sharedPreferences.getString(PREF_DB_MODE, DEFAULT_DB_MODE) == DB_MODE_ROOM
    }

    private val dao
        get() = if (isRoomDbMode()) {
            Log.d(TAG, "*** Room database mode ***")
            roomDb.movieDao
        } else {
            Log.d(TAG, "*** SQLiteOpenHelper database mode ***")
            openHelperDb.movieDao
        }

    fun getAllMovies(sortBy: String, sortOrder: String): LiveData<List<Movie>> = dao.getAllMovies(sortBy, sortOrder)

    suspend fun insertMovie(movie: Movie) = dao.insertMovie(movie)

    suspend fun updateMovie(movie: Movie) = dao.updateMovie(movie)

    suspend fun deleteMovie(movie: Movie) = dao.deleteMovie(movie)

    companion object {
        private const val TAG = "MovieRepository"
    }
}