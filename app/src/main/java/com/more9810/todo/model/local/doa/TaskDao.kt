package com.more9810.todo.model.local.doa

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.more9810.todo.model.local.entety.Task

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTask(task: Task)

    @Update()
    fun editeTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Query("select * from Task order by id")
    fun getAllTask(): List<Task>

    @Query("select * from Task where date= :date order by id")
    fun getTaskByDate(date: Long): List<Task>
}