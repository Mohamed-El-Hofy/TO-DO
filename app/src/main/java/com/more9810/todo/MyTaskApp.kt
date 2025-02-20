package com.more9810.todo

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.more9810.todo.model.local.TaskDatabase
import com.more9810.todo.utils.Const

class MyTaskApp : Application() {
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        TaskDatabase.initDatabase(applicationContext)
        setNightMode()
    }

    private fun setNightMode() {
        sharedPreferences = getSharedPreferences(Const.SHARED_PREFERENCES_NAME, MODE_PRIVATE)
        val isDark = sharedPreferences.getBoolean(Const.DARK_MODE_KEY, getCurrentMode())
        applyModeChange(isDark)
    }

    private fun applyModeChange(isDark: Boolean) {
        if (isDark) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
    private fun getCurrentMode(): Boolean {
        val currentMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentMode == Configuration.UI_MODE_NIGHT_YES
    }


}