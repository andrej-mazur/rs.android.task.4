package com.example.watchlist.livedata

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.example.watchlist.di.locateLazy

class SharedPreferenceLiveData(
    private val key: String,
    private val defaultValue: String = ""
) : LiveData<String>() {

    private val sharedPreferences: SharedPreferences by locateLazy()

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences: SharedPreferences, key: String ->
        when (key) {
            key -> postValue(sharedPreferences.getString(key, defaultValue))
        }
    }

    override fun onActive() {
        super.onActive()
        postValue(sharedPreferences.getString(key, defaultValue))
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onInactive() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        super.onInactive()
    }
}
