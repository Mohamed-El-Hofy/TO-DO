package com.more9810.todoapp.adapter

import androidx.recyclerview.widget.DiffUtil
import com.more9810.todoapp.model.local.entety.Task

class MyDiffer(
    private val oldList: List<Task>,
    private val newList: List<Task>,
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return true
    }
}