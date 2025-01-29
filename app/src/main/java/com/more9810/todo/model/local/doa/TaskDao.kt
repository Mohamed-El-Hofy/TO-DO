package com.more9810.todoapp.model.local.doa

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.more9810.todoapp.model.local.entety.Task

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Query("select * from Task order by id")
    fun getAllTask(): List<Task>

    @Query("select * from Task where time= :time order by id")
    fun getTaskByDate(time: Long): List<Task>
}