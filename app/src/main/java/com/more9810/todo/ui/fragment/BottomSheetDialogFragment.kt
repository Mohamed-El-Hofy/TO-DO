package com.more9810.todo.ui.fragment

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val task = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arg.getParcelable(Const.EDITE_TASK_KEY, Task::class.java)
        } else {
            arg.getParcelable(Const.EDITE_TASK_KEY)
        }


        if (isAddNewTask) {
            addOrUpdateTask(isAddNewTask)
        } else {

            binding.etTask.setText(task?.task)
            binding.tvDate.text = task?.date.toString()
            binding.tvTime.text = task?.time.toString()
            addOrUpdateTask(isAddNewTask)
        }


    }

    private fun addOrUpdateTask(isAddNewTask: Boolean) {
        binding.tvDate.setOnClickListener {
            getDate()
        }
        binding.tvTime.setOnClickListener {
            getTime()
        }
        onClickSave(isAddNewTask)
    }


    private fun onClickSave(isAddNewTask: Boolean) {
        binding.btnSave.setOnClickListener {
            if (!validateInput()) return@setOnClickListener
            setTaskData(isAddNewTask)
        }
    }

    private fun setTaskData(isAddNewTask: Boolean) {
        if (isAddNewTask) {
            val taskText = binding.etTask.text.toString()
            val task = Task(task = taskText, date = date, time = time)
            if (onAddNewTask == null) return
            onAddNewTask?.onClickSave(task, null)

        } else {
            val taskText = binding.etTask.text.toString()
            val task = Task(task = taskText, date = date, time = time)
            if (onEditeTask == null) return
            onEditeTask?.onClickSave(task, null)
        }





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

            val dateFormat =
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(datePicker.selection)
            binding.tvDate.text = dateFormat
        }
        date = datePicker.selection

        // Log.d("more1010", "getDate: ${date}")
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

    var onAddNewTask: OnClickSaveTask? = null
    var onEditeTask: OnClickSaveTask? = null

    fun interface OnClickSaveTask {
        fun onClickSave(task: Task, position: Int?)
    }

}