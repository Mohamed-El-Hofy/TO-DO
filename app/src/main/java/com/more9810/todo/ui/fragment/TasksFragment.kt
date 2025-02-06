package com.more9810.todo.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.more9810.todo.adapter.TaskRecyclerAdapter
import com.more9810.todo.databinding.FragmentTasksBinding
import com.more9810.todo.model.local.TaskDatabase
import com.more9810.todo.model.local.entety.Task
import com.more9810.todoapp.utils.Const
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.Calendar

class TasksFragment : Fragment() {


    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!
    private var adapter = TaskRecyclerAdapter()

    private val db = TaskDatabase.getInstance().getDao()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter.setItem(db.getAllTask())
        binding.rvTask.adapter = adapter


        onDeleteTask()
        onEditeTask()

        updateTask()
        initCalenderView()
    }

    private fun onDeleteTask() {
        adapter.onClickDelete = TaskRecyclerAdapter.OnItemClickListener { task, position ->
            db.deleteTask(task)
            adapter.notifyItemRemoved(position)
            adapter.setItem(db.getAllTask())
            val snackbar = Snackbar.make(requireContext(), requireView(), "", Snackbar.LENGTH_LONG)
            snackbar.setText("Task Delete Successfully")
            snackbar.setAction("Undo") {
                db.addTask(task)
                adapter.setItem(db.getAllTask())
            }
            snackbar.show()
        }
    }

    private fun onEditeTask() {
        adapter.onClickEdite = TaskRecyclerAdapter.OnItemClickListener { task, position ->
            val dialogEdite = BottomSheetDialogFragment()

            val arg = Bundle()
            arg.putParcelable(Const.EDITE_TASK_KEY, task)
            arg.putBoolean(Const.IS_COM_FROM_MAIN_ACTIVITY, false)
            dialogEdite.arguments = arg

            dialogEdite.show(childFragmentManager, BottomSheetDialogFragment().tag)
        }
    }

    fun addNewTask(task: Task) {
        db.addTask(task)
        adapter.setItem(db.getAllTask())
    }


    private fun updateTask() {
        adapter.onClickDone = TaskRecyclerAdapter.OnItemClickListener { task, _ ->
            if (task.isComplete) {
                db.editeTask(Task(task.id, task.task, task.date, task.time, isComplete = false))
            } else {
                db.editeTask(Task(task.id, task.task, task.date, task.time, isComplete = true))

            }

            adapter.setItem(db.getAllTask())
            adapter.notifyDataSetChanged()
        }
    }

    private fun initCalenderView() {
        binding.calendarView.selectedDate = CalendarDay.today()

        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, date.year)
            calendar.set(Calendar.MONTH, date.month - 1)
            calendar.set(Calendar.DAY_OF_MONTH, date.day)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            if (selected) {
                val tasks = db.getTaskByDate(calendar.timeInMillis)
                adapter.setItem(tasks)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}