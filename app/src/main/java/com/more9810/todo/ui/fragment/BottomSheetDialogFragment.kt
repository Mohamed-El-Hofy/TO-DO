package com.more9810.todo.ui.fragment

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.more9810.todo.R
import com.more9810.todo.databinding.FragmentBottomShetDialogBinding
import com.more9810.todo.model.local.entety.Task
import com.more9810.todoapp.utils.Const
import java.util.Locale


class BottomSheetDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomShetDialogBinding

    private var date: Long? = null
    private var time: String? = null
    private var editedTask: Task? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBottomShetDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arg = arguments ?: return
        val isAddNewTask = arg.getBoolean(Const.IS_COM_FROM_MAIN_ACTIVITY)

        editedTask = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arg.getParcelable(Const.EDITE_TASK_KEY, Task::class.java)
        } else {
            arg.getParcelable(Const.EDITE_TASK_KEY)
        }
        val position = arg.getInt(Const.EDITE_TASK_POSITION)


        checkItEditeOrAdd(isAddNewTask, position, arg)
    }

    private fun checkItEditeOrAdd(addNewTask: Boolean, position: Int, arg: Bundle) {
        if (addNewTask) {
            addOrUpdateTask(addNewTask, position)
        } else {
            if (arg.isEmpty) {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.cantEditeThisTask),
                    Toast.LENGTH_SHORT
                ).show()
                dismiss()
                return
            }
            date = editedTask?.date
            time = editedTask?.time
            binding.tvAddTaskLogo.text = resources.getString(R.string.edite_task)
            binding.etTask.setText(editedTask?.task)
            binding.tvDate.text = SimpleDateFormat(
                resources.getString(R.string.standerFormateDate), Locale.getDefault()
            ).format(editedTask?.date ?: 0)
            binding.tvTime.text = editedTask?.time ?: "0"
            addOrUpdateTask(addNewTask, position)
        }
    }

    private fun addOrUpdateTask(isAddNewTask: Boolean, position: Int) {
        binding.tvDate.setOnClickListener {
            getDate()
        }
        binding.tvTime.setOnClickListener {
            getTime()
        }
        onClickSave(isAddNewTask, position)
    }


    private fun onClickSave(isAddNewTask: Boolean, position: Int) {
        binding.btnSave.setOnClickListener {
            if (!validateInput()) return@setOnClickListener
            if (isAddNewTask) addNewTask()
            else editeTask(position)
        }
    }

    private fun addNewTask() {
        val taskText = binding.etTask.text.toString()
        val task = Task(task = taskText, date = date, time = time)
        if (onAddNewTask == null) return
        onAddNewTask?.onClickSave(task)
        dismiss()
    }

    private fun editeTask(position: Int) {
        val taskText = binding.etTask.text.toString()

        val task = Task(editedTask?.id, taskText, date, time, editedTask?.isComplete ?: false)
        if (onEditeTask == null) return
        onEditeTask?.onClickSave(task, position)
        dismiss()
    }

    private fun validateInput(): Boolean {
        var isValid = true

        if (binding.etTask.text.isNullOrBlank()) {
            binding.etTask.error = getString(R.string.requardFeild)
            isValid = false
        }

        if (date == null) {
            binding.tvDate.error = getString(R.string.requardFeild)
            isValid = false
        }

        if (time.isNullOrBlank()) {
            binding.tvTime.error = getString(R.string.requardFeild)
            isValid = false
        }

        return isValid
    }

    private fun getDate() {
        val constraintsBuilder =
            CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now())

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(constraintsBuilder.build())
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build()

        datePicker.show(getChildFragmentManager(), "")
        datePicker.addOnPositiveButtonClickListener {

            val dateFormat = SimpleDateFormat(
                resources.getString(R.string.standerFormateDate), Locale.getDefault()
            ).format(datePicker.selection)
            binding.tvDate.text = dateFormat
            date = datePicker.selection

        }

    }

    @SuppressLint("SetTextI18n")
    private fun getTime() {

        val isSystem24Hour = is24HourFormat(activity?.applicationContext)
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
        val picker =
            MaterialTimePicker.Builder().setTimeFormat(clockFormat).setHour(12).setMinute(0).build()
        picker.show(getChildFragmentManager(), "tag")

        picker.addOnPositiveButtonClickListener {
            var hour = picker.hour
            val minutes = picker.minute


            if (hour in 0..11) {
                time = if (hour == 0) {
                    "12:$minutes AM"
                } else {
                    "$hour:$minutes AM"
                }
            } else {
                if (hour == 12) {
                    time = "$hour:$minutes PM"
                } else {
                    hour -= 12
                    time = "$hour:$minutes PM"; }
            }


            binding.tvTime.text = time
        }
    }

    var onAddNewTask: OnClickSaveTaskFromNew? = null
    var onEditeTask: OnClickSaveTaskFromEdite? = null

    fun interface OnClickSaveTaskFromNew {
        fun onClickSave(task: Task)
    }

    fun interface OnClickSaveTaskFromEdite {
        fun onClickSave(task: Task, position: Int)
    }

}