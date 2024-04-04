package com.live.streetview.navigation.earthmap.compass.map.activities.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(private val context: Context) {

    private val PREF_NAME = "MyAppPreferences"

    private fun getSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // Save a string value
    fun saveString(key: String, value: String) {
        val editor = getSharedPreferences().edit()
        editor.putString(key, value)
        editor.apply()
    }

    // Get a string value
    fun getString(key: String, defaultValue: String): String? {
        return getSharedPreferences().getString(key, defaultValue)
    }

    // Remove a value
    fun removeValue(key: String) {
        val editor = getSharedPreferences().edit()
        editor.remove(key)
        editor.apply()
    }

    // Clear all values
    fun clearSharedPreferences() {
        val editor = getSharedPreferences().edit()
        editor.clear()
        editor.apply()
    }
}
