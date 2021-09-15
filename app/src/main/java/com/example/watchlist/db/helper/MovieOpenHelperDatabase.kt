package com.example.watchlist.db.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.watchlist.db.MovieTestData
import com.example.watchlist.db.helper.MovieOpenHelperDatabase.Companion.COLUMN_NAME_ID
import com.example.watchlist.db.helper.MovieOpenHelperDatabase.Companion.COLUMN_NAME_TITLE
import com.example.watchlist.db.helper.MovieOpenHelperDatabase.Companion.COLUMN_NAME_YEAR
import com.example.watchlist.db.helper.MovieOpenHelperDatabase.Companion.TABLE_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

private val SQL_CREATE = """
    CREATE TABLE IF NOT EXISTS $TABLE_NAME (
        $COLUMN_NAME_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
        $COLUMN_NAME_TITLE TEXT NOT NULL,
        $COLUMN_NAME_YEAR INTEGER NOT NULL)
    """.trimMargin()

private val SQL_DROP = """
    DROP TABLE IF EXISTS $TABLE_NAME
    """.trimMargin()

class MovieOpenHelperDatabase private constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    val movieDao by lazy { MovieOpenHelperDao.create(this) }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE)

        scope.launch {
            MovieTestData.data.forEach { movie ->
                movieDao.insertMovie(movie)
            }
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion) {
            db.execSQL(SQL_DROP)
            onCreate(db)
        }
    }

    companion object {

        const val DATABASE_VERSION = 1

        const val DATABASE_NAME = "watchlist"

        const val TABLE_NAME = "movies"

        const val COLUMN_NAME_ID = "id"

        const val COLUMN_NAME_TITLE = "title"

        const val COLUMN_NAME_YEAR = "year"

        private var instance: MovieOpenHelperDatabase? = null

        fun create(context: Context): MovieOpenHelperDatabase {
            if (instance == null) {
                instance = MovieOpenHelperDatabase(context)
            }
            return instance!!
        }
    }
}