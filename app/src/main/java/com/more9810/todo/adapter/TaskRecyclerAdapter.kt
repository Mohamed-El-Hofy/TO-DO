package com.more9810.todo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.more9810.todo.databinding.ItemTaskBinding
import com.more9810.todoapp.adapter.MyDiffer
import com.more9810.todoapp.model.local.entety.Task

class TaskRecyclerAdapter : RecyclerView.Adapter<TaskRecyclerAdapter.TaskViewHolder>() {

    private var item: MutableList<Task> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    fun setItem(newItem: List<Task>) {
        val diffCallback = MyDiffer(item, newItem)
        val diffCourses = DiffUtil.calculateDiff(diffCallback)
        item.clear()
        item.addAll(newItem)
        diffCourses.dispatchUpdatesTo(this)
    }


    override fun getItemCount() = item.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bindView(item[position], position, onClickDelete)
    }

    class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(task: Task, position: Int, onClickDelete: OnItemClickListener?) {
            binding.tvTask.text = task.task
            binding.tvDate.text = task.time
            onClickDelete?.onDeleteItem(task, position)
        }
    }

    var onClickDelete: OnItemClickListener? = null

    fun interface OnItemClickListener {
        fun onDeleteItem(task: Task, position: Int)
    }
}