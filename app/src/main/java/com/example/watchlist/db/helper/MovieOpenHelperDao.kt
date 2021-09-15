package com.example.watchlist.db.helper

import android.content.ContentValues
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.watchlist.db.Movie
import com.example.watchlist.db.MovieDao
import com.example.watchlist.db.helper.MovieOpenHelperDatabase.Companion.COLUMN_NAME_ID
import com.example.watchlist.db.helper.MovieOpenHelperDatabase.Companion.COLUMN_NAME_TITLE
import com.example.watchlist.db.helper.MovieOpenHelperDatabase.Companion.COLUMN_NAME_YEAR
import com.example.watchlist.db.helper.MovieOpenHelperDatabase.Companion.TABLE_NAME

class MovieOpenHelperDao private constructor(db: MovieOpenHelperDatabase) : MovieDao {

    private val writableDatabase = db.writableDatabase

    private val readableDatabase = db.readableDatabase

    private val dbChangeListener = MutableLiveData(0)

    private fun changed() {
        dbChangeListener.postValue(0)
    }

    override fun getAllMovies(sortBy: String, sortOrder: String): LiveData<List<Movie>> {
        return dbChangeListener.map { getAllMoviesInner(sortBy, sortOrder) }
    }

    private fun getAllMoviesInner(sortBy: String, sortOrder: String): List<Movie> {
        val projection = arrayOf(COLUMN_NAME_ID, COLUMN_NAME_TITLE, COLUMN_NAME_YEAR)
        val sort = "$sortBy $sortOrder"
        val cursor = readableDatabase.query(TABLE_NAME, projection, null, null, null, null, sort)
        val movies = mutableListOf<Movie>()
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(COLUMN_NAME_ID))
                val title = getString(getColumnIndexOrThrow(COLUMN_NAME_TITLE))
                val year = getInt(getColumnIndexOrThrow(COLUMN_NAME_YEAR))
                movies.add(Movie(id, title, year))
            }
            close()
        }
        return movies
    }

    override suspend fun insertMovie(movie: Movie) {
        val values = ContentValues().apply {
            put(COLUMN_NAME_TITLE, movie.title)
            put(COLUMN_NAME_YEAR, movie.year)
        }
        writableDatabase.insert(TABLE_NAME, null, values)
        changed()
    }

    override suspend fun updateMovie(movie: Movie) {
        val values = ContentValues().apply {
            put(COLUMN_NAME_TITLE, movie.title)
            put(COLUMN_NAME_YEAR, movie.year)
        }
        val selection = "$COLUMN_NAME_ID = ?"
        val selectionArgs = arrayOf(movie.id.toString())
        writableDatabase.update(TABLE_NAME, values, selection, selectionArgs)
        changed()
    }

    override suspend fun deleteMovie(movie: Movie) {
        val selection = "$COLUMN_NAME_ID LIKE ?"
        val selectionArgs = arrayOf(movie.id.toString())
        writableDatabase.delete(TABLE_NAME, selection, selectionArgs)
        changed()
    }

    companion object {

        private var instance: MovieOpenHelperDao? = null

        fun create(db: MovieOpenHelperDatabase): MovieOpenHelperDao {
            if (instance == null) {
                instance = MovieOpenHelperDao(db)
            }
            return instance!!
        }
    }
}