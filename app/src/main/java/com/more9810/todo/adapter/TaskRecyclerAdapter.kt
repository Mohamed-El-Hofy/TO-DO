//package com.more9810.todoapp.adapter
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import com.more9810.todoapp.databinding.ItemListTaskBinding
//import com.more9810.todo.model.local.TaskDatabase
//import com.more9810.todoapp.model.local.entety.Task
//
//class TaskRecyclerAdapter : RecyclerView.Adapter<TaskRecyclerAdapter.TaskViewHolder>() {
//
//    private var item: MutableList<Task> = mutableListOf()
//    private val db = TaskDatabase.getInstance().getDao()
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
//        return TaskViewHolder(
//            ItemListTaskBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//        )
//    }
//
//    fun setItem(newItem: List<Task>) {
//        val diffCallback = MyDiffer(item, newItem)
//        val diffCourses = DiffUtil.calculateDiff(diffCallback)
//        item.clear()
//        item.addAll(newItem)
//        diffCourses.dispatchUpdatesTo(this)
//    }
//
//
//    override fun getItemCount() = item.size
//
//    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
//        holder.bindView(item[position], position,onClickDelete)
//    }
//
//    class TaskViewHolder(private val binding: ItemListTaskBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bindView(task: Task, position: Int,onClickDelete:OnItemClickListener?) {
//            binding.content.tvTask.text = task.task
//            binding.leftView.setOnClickListener {
//                onClickDelete?.onDeleteItem(task, position)
//            }
//        }
//    }
//    var onClickDelete: OnItemClickListener? = null
//
//    fun interface OnItemClickListener{
//        fun onDeleteItem(task: Task,position: Int)
//    }
//}