package com.more9810.todo.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.more9810.todo.R
import com.more9810.todo.databinding.ItemTaskBinding
import com.more9810.todo.model.local.entety.Task
import com.more9810.todoapp.adapter.MyDiffer
import com.zerobranch.layout.SwipeLayout

class TaskRecyclerAdapter : RecyclerView.Adapter<TaskRecyclerAdapter.TaskViewHolder>() {

    private var item: MutableList<Task> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun getItemCount() = item.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentItem = item[position]
        holder.bindView(currentItem, position)
        holder.onClickEditeOrDeleteTask(currentItem, position, onClickDelete, onClickEdite)
        holder.onClickDon(currentItem, position, onClickDone)
        holder.onClickRoot(currentItem, position, onClickRoot)

    }

    fun setItem(newItem: List<Task>) {
        val diffCallback = MyDiffer(item, newItem)
        val diffCourses = DiffUtil.calculateDiff(diffCallback)
        item.clear()
        item.addAll(newItem)
        diffCourses.dispatchUpdatesTo(this)
    }

    fun addNewTask(task: Task, position: Int) {
        item.add(position, task)
        notifyItemInserted(position)
        notifyItemRangeChanged(position, item.size - position)
    }

    fun updateTask(task: Task, position: Int) {
        if (position in item.indices) {
            item[position] = task
            notifyItemChanged(position)
        }
    }

    fun deleteTask(task: Task, position: Int) {
        if (position in item.indices) {
            item.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, item.size - position)

        }

    }
    class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(task: Task, position: Int) {
            binding.content.tvTask.text = task.task
            binding.content.tvTime.text = task.time
        }

        fun onClickEditeOrDeleteTask(
            task: Task,
            position: Int,
            onClickDelete: OnItemClickListener?,
            onClickEdite: OnItemClickListener?,
        ) {
            binding.leftView.isClickable = false
            binding.rightView.isClickable = false
            val swipeLayout = binding.swipeLayout
            swipeLayout.setOnActionsListener(object : SwipeLayout.SwipeActionsListener {
                override fun onOpen(direction: Int, isContinuous: Boolean) {
                    binding.leftView.isClickable = true
                    binding.rightView.isClickable = true
                    when (direction) {
                        SwipeLayout.RIGHT -> {
                            binding.leftView.setOnClickListener {
                                if (onClickDelete == null) return@setOnClickListener
                                onClickDelete.onItemClick(task, position)
                            }
                        }

                        SwipeLayout.LEFT -> {
                            binding.rightView.setOnClickListener {
                                if (onClickEdite == null) return@setOnClickListener
                                onClickEdite.onItemClick(task, position)
                            }
                        }

                    }
                }

                override fun onClose() {
                    binding.leftView.isClickable = false
                    binding.rightView.isClickable = false
                }
            })
        }

        fun onClickDon(task: Task, position: Int, onClickDone: OnItemClickListener?) {
            updateTaskStateUi(task, position)
            binding.content.btnDon.setOnClickListener {
                if (onClickDone == null) return@setOnClickListener
                onClickDone.onItemClick(task, position)
                updateTaskStateUi(task, position)
            }
        }

        private fun updateTaskStateUi(task: Task, position: Int) {

            val btn = binding.content.btnDon

            if (task.isComplete) {
                binding.content.tvTask.paintFlags =
                    binding.content.tvTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                btn.background = null
                btn.setTextColor(ContextCompat.getColor(btn.context, R.color.colorDon))
                btn.text = ContextCompat.getString(btn.context, R.string.done)
                binding.content.tvTask.setTextColor(
                    ContextCompat.getColor(
                        btn.context, R.color.colorDon
                    )
                )
                binding.content.lineLeft.background = ContextCompat.getDrawable(
                    btn.context, R.drawable.item_drow_shape_delete_secondry
                )
            } else {
                btn.background = ContextCompat.getDrawable(btn.context, R.drawable.bg_btn_not_done)
                btn.setTextColor(ContextCompat.getColor(btn.context, R.color.white))
                btn.text = ""
                binding.content.tvTask.setTextColor(
                    ContextCompat.getColor(
                        btn.context, R.color.colorPrimary
                    )
                )
                binding.content.lineLeft.background =
                    ContextCompat.getDrawable(btn.context, R.drawable.item_drow_shape_delete)
                binding.content.tvTask.paintFlags =
                    binding.content.tvTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()


            }
        }

        fun onClickRoot(task: Task, position: Int, onClickRoot: OnItemClickListener?) {
            binding.main.setOnClickListener {
                if (onClickRoot == null) return@setOnClickListener
                onClickRoot.onItemClick(task, position)
            }
        }


    }

    var onClickDelete: OnItemClickListener? = null
    var onClickEdite: OnItemClickListener? = null
    var onClickDone: OnItemClickListener? = null
    private var onClickRoot: OnItemClickListener? = null

    fun interface OnItemClickListener {
        fun onItemClick(task: Task, position: Int)
    }
}