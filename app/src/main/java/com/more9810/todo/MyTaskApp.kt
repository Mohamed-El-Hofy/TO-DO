package com.more9810.todo

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.more9810.todo.model.local.TaskDatabase
import com.more9810.todo.utils.LocaleHelper

class MyTaskApp : Application() {
    override fun onCreate() {
        super.onCreate()
        TaskDatabase.initDatabase(applicationContext)

        val savedLanguage = LocaleHelper.getSavedLanguage(this)
        val localeList = LocaleListCompat.forLanguageTags(savedLanguage)
        AppCompatDelegate.setApplicationLocales(localeList)
    }


}