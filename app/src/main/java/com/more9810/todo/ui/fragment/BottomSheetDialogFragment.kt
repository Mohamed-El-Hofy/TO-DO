package com.more9810.todo.ui.fragment

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.more9810.todo.databinding.FragmentBottomShetDialogBinding
import com.more9810.todoapp.model.local.entety.Task
import java.util.Locale

class BottomSheetDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomShetDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBottomShetDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    private fun setup() {
        binding.tvDate.setOnClickListener {
            getDate()
        }
        binding.tvTime.setOnClickListener {
            getTime()
        }
        onClickSave()
    }

    var onClickItem: ((Task) -> Unit)? = null
    private fun onClickSave() {
        binding.btnSave.setOnClickListener {
            val taskContent = binding.etTask.text.toString()
            val data = binding.tvDate.text.toString()
            val time = binding.tvTime.text.toString()

            if (taskContent.isBlank()) {
                Toast.makeText(requireContext(), "!!!!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val task = Task(task = taskContent, time = "$data :: $time")

            onClickItem?.invoke(task)

            dismiss()
        }
    }

    private fun getDate() {
        val date = MaterialDatePicker.Builder.datePicker().build()
        date.show(getChildFragmentManager(), "")
        date.addOnPositiveButtonClickListener {
            val dateFormat =
                SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(date.selection)
            Log.d("more1010", "getDate: ${date.selection}")
            binding.tvDate.text = dateFormat
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
            var time = ""
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


}