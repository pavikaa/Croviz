package com.markopavicic.croviz.utils

import android.content.Context
import android.content.SharedPreferences

object Prefs {
    private lateinit var preferences: SharedPreferences
    fun init(context: Context) {
        preferences =
            context.getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
    }

    fun setThemeToPrefs(theme: Int) {
        with(preferences.edit()) {
            putInt(Constants.THEME_KEY, theme)
            apply()
        }
    }

    fun getThemeFromPrefs(): Int {
        val defaultValue = -1
        return preferences.getInt(Constants.THEME_KEY, defaultValue)
    }

    fun firstRun() {
        with(preferences.edit()) {
            putBoolean(Constants.FIRST_RUN_KEY, false)
            apply()
        }
    }

    fun checkIfFirstRun(): Boolean {
        val defaultValue = true
        return preferences.getBoolean(Constants.FIRST_RUN_KEY, defaultValue)
    }
}