package com.more9810.todo

import android.app.Application
import android.content.Context
import com.more9810.todo.model.local.TaskDatabase
import com.more9810.todo.utils.LocaleHelper

class MyTaskApp : Application() {
    override fun onCreate() {
        super.onCreate()
        TaskDatabase.initDatabase(applicationContext)
    }
    class MyApp : Application() {
        override fun attachBaseContext(base: Context) {
            super.attachBaseContext(LocaleHelper.updateLocale(base))
        }
    }
}