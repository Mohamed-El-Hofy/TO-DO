package com.more9810.todo.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.more9810.todo.databinding.FragmentSetnigsBinding
import com.more9810.todo.utils.Const

class SettingsFragment : Fragment() {
    private var _binding: FragmentSetnigsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSetnigsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMoodNight()
    }


     fun setupMoodNight() {
        val sharedPreferences = activity?.getSharedPreferences("Mode", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        val nightMode = sharedPreferences?.getBoolean(Const.NIGHT_MODE, false)

        binding.switchMode.isChecked = nightMode ?: false
        if (nightMode == true){
            binding.switchMode.isChecked =true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        binding.switchMode.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor?.putBoolean(Const.NIGHT_MODE,true)
                editor?.apply()

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor?.putBoolean(Const.NIGHT_MODE, false)
                editor?.apply()

            }
        }

    }

    private var onSelectLanguage: OnSelectLanguage? = null

    fun interface OnSelectLanguage {
        fun onLangSelected(language: String)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


//    private fun setupLanguage() {
//        val list = resources.getStringArray(R.array.languageArr)
//        val arrAdapter = ArrayAdapter(requireContext(), R.layout.item_tv_setting, list)
//
//        binding.autoCompLanguage.setAdapter(arrAdapter)
//
//        binding.autoCompLanguage.onItemClickListener =
//            AdapterView.OnItemClickListener { parent, view, position, id ->
//                val item = parent.getItemAtPosition(position).toString()
//                val langCode = when (item) {
//                    "Arabic" -> "ar"
//                    "English" -> "en"
//                    else -> Locale.getDefault().language
//                }
//
//                val sharedPref =
//                    requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
//                sharedPref.edit().putString("app_lang", langCode).apply()
//
//                onSelectLanguage?.onLangSelected(langCode)
//                requireActivity().recreate()
//                Toast.makeText(requireContext(), item, Toast.LENGTH_SHORT).show()
//            }
//    }