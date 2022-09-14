package com.markopavicic.croviz.utils

import android.content.Context
import android.content.SharedPreferences

object Prefs {
    private lateinit var preferences: SharedPreferences
    fun init(context: Context) {
        preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
    }

    fun setThemeToPrefs(theme: Int) {
        with(preferences.edit()) {
            putInt(Constants.THEME_KEY, theme)
            apply()
        }
    }

    fun getThemeFromPrefs(): Int {
        val defaultValue = -1
        val csrf = preferences.getInt(Constants.THEME_KEY, defaultValue)!!
        return csrf
    }
}