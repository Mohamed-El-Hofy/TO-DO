package com.more9810.todo

import android.app.Application
import com.more9810.todo.model.local.TaskDatabase

class MyTaskApp : Application() {
    override fun onCreate() {
        super.onCreate()
        TaskDatabase.initDatabase(applicationContext)
    }
}