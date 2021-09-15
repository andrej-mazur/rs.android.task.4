package com.example.watchlist.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.watchlist.db.Movie
import com.example.watchlist.db.MovieTestData
import com.example.watchlist.di.locateLazy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Database(entities = [Movie::class], version = 1)
abstract class MovieRoomDatabase : RoomDatabase() {

    abstract val movieDao: MovieRoomDao

    companion object {
        fun create(context: Context) = Room
            .databaseBuilder(context, MovieRoomDatabase::class.java, "watchlist")
            .fallbackToDestructiveMigration()
            .addCallback(MovieRoomDatabaseCallback())
            .build()
    }
}

class MovieRoomDatabaseCallback : RoomDatabase.Callback() {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val db: MovieRoomDatabase by locateLazy()

    override fun onCreate(supportSQLiteDatabase: SupportSQLiteDatabase) {
        scope.launch {
            MovieTestData.data.forEach { movie ->
                db.movieDao.insertMovie(movie)
            }
        }
    }
}