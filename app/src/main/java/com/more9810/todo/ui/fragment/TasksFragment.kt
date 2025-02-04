package com.more9810.todo.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.more9810.todo.adapter.TaskRecyclerAdapter
import com.more9810.todo.databinding.FragmentTasksBinding
import com.more9810.todo.model.local.TaskDatabase
import com.more9810.todo.model.local.entety.Task

class TasksFragment : Fragment() {


    private lateinit var binding: FragmentTasksBinding
    private var adapter = TaskRecyclerAdapter()

    private val db = TaskDatabase.getInstance().getDao()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter.setItem(db.getAllTask())
        binding.rvTask.adapter = adapter


        adapter.onClickDelete = TaskRecyclerAdapter.OnItemClickListener { task, position ->
            db.deleteTask(task)
            adapter.notifyItemRemoved(position)
            refreshData()
        }

        updateTask()

    }

    fun refreshData() {
        adapter.setItem(db.getAllTask())
    }


    private fun updateTask() {
        adapter.onClickDone = TaskRecyclerAdapter.OnItemClickListener { task, _ ->
            if (task.isComplete) {
                db.editeTask(Task(task.id, task.task, task.date, task.time, isComplete = false))
            } else {
                db.editeTask(Task(task.id, task.task, task.date, task.time, isComplete = true))

            }

            refreshData()
            adapter.notifyDataSetChanged()
        }
    }

}