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
import com.more9810.todo.utils.Const
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

        initRecycler()
        onDeleteTask()
        onEditeTask()
        updateTaskState()
        initCalenderView()
    }

    private fun initRecycler() {
        adapter.setItem(db.getAllTask())
        binding.rvTask.adapter = adapter
    }

    private fun onDeleteTask() {
        adapter.onClickDelete = TaskRecyclerAdapter.OnItemClickListener { task, position ->
            db.deleteTask(task)
            adapter.deleteTask(task, position)
            adapter.setItem(db.getAllTask())

            unDoDeleteTask(task, position)
        }
    }

    private fun unDoDeleteTask(task: Task, position: Int) {
        val snackBar = Snackbar.make(requireContext(), requireView(), "", Snackbar.LENGTH_LONG)
        snackBar.setText("Task Delete Successfully")
        snackBar.setAction("Undo") {
            db.addTask(task)
            adapter.addNewTask(task, position)
        }
        snackBar.show()
    }

    private fun onEditeTask() {
        adapter.onClickEdite = TaskRecyclerAdapter.OnItemClickListener { task, position ->
            val dialogEdite = BottomSheetDialogFragment()
            val arg = Bundle()
            arg.putBoolean(Const.COM_FROM_EDITE_TASK, true)
            arg.putParcelable(Const.EDITE_TASK_KEY, task)
            arg.putInt(Const.EDITE_TASK_POSITION, position)
            dialogEdite.arguments = arg

        dialogEdite.onEditeTask =
            BottomSheetDialogFragment.OnClickSaveTaskFromEdite { mTask, mPosition ->
                db.editeTask(mTask)
                adapter.updateTask(mTask, mPosition)
            }
        dialogEdite.show(childFragmentManager, "onEditeTask")
    }
    }

    fun addNewTask(task: Task) {
        db.addTask(task)
        adapter.setItem(db.getAllTask())
    }


    private fun updateTaskState() {
        adapter.onClickDone = TaskRecyclerAdapter.OnItemClickListener { task, position ->
            task.isComplete = !task.isComplete
            db.editeTask(task)
            adapter.updateTask(task, position)
        }
    }

    private fun initCalenderView() {
        binding.calendarView.selectedDate = CalendarDay.today()

        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, date.year)
            calendar.set(Calendar.MONTH, date.month + 1)
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
        adapter.onClickDone = null
        adapter.onClickDelete = null
        adapter.onClickEdite =null
    }
}