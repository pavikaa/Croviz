package com.markopavicic.croviz.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.markopavicic.croviz.utils.Prefs

class Croviz : Application() {
    override fun onCreate() {
        super.onCreate()
        Prefs.init(this)
        if (Prefs.getThemeFromPrefs() != -1)
            AppCompatDelegate.setDefaultNightMode(Prefs.getThemeFromPrefs())
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}