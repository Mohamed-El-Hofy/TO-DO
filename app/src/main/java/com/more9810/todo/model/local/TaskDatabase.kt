package com.more9810.todo.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.more9810.todoapp.model.local.doa.TaskDao
import com.more9810.todoapp.model.local.entety.Task

@Database(entities = [Task::class], version = 2)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun getDao(): TaskDao

    companion object {
        private const val DB_NAME = "Task"
        private var instance: TaskDatabase? = null

        fun initDatabase(applicationContext: Context): TaskDatabase {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(applicationContext, TaskDatabase::class.java, DB_NAME)
                        .allowMainThreadQueries().fallbackToDestructiveMigration().build()
            }
            return instance!!
        }

        fun getInstance(): TaskDatabase {
            return instance!!
        }
    }

}