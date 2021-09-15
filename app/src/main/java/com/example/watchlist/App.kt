package com.example.watchlist

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import com.example.watchlist.db.MovieRepository
import com.example.watchlist.db.helper.MovieOpenHelperDatabase
import com.example.watchlist.db.room.MovieRoomDatabase
import com.example.watchlist.di.ServiceLocator

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        ServiceLocator.register<Context>(this)
        ServiceLocator.register(PreferenceManager.getDefaultSharedPreferences(ServiceLocator.locate()))
        ServiceLocator.register(MovieRoomDatabase.create(ServiceLocator.locate()))
        ServiceLocator.register(MovieOpenHelperDatabase.create(ServiceLocator.locate()))
        ServiceLocator.register(MovieRepository(ServiceLocator.locate(), ServiceLocator.locate()))
    }
}
