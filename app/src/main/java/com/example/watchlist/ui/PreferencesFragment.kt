package com.example.watchlist.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.watchlist.R

class PreferencesFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }
}