package com.more9810.todo.model.local.entety

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity()
@Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val task: String,
    val date: Long? = null,
    val time: String? = null,
    val isComplete: Boolean = false,
) : Parcelable

