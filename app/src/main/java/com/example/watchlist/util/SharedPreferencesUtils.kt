package com.example.watchlist.util

import android.content.SharedPreferences
import com.example.watchlist.*

class SharedPreferencesUtils(private val sharedPreferences: SharedPreferences) {

    private fun getSortBy(): String? {
        return sharedPreferences.getString("pref_sort_by", DEFAULT_SORT_BY)
    }

    fun isSortByTitle(): Boolean {
        return getSortBy() == SORT_BY_TITLE
    }

    fun isSortByYear(): Boolean {
        return getSortBy() == SORT_BY_YEAR
    }

    private fun getSortOrder(): String? {
        return sharedPreferences.getString("pref_sort_order", DEFAULT_SORT_ORDER)
    }

    fun isSortOrderAsc(): Boolean {
        return getSortOrder() == SORT_ORDER_ASC
    }

    fun isSortOrderDesc(): Boolean {
        return getSortOrder() == SORT_ORDER_DESC
    }

    private fun getDbMode(): String? {
        return sharedPreferences.getString("pref_db_mode", DEFAULT_DB_MODE)
    }

    fun isDbModeRoom(): Boolean {
        return getDbMode() == DB_MODE_ROOM
    }

    fun isDbModeSQLiteOPenHelper(): Boolean {
        return getDbMode() == DB_MODE_SQLITE_OPEN_HELPER
    }
}