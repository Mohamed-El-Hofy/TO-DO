package com.more9810.todo.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.more9810.todo.R
import com.more9810.todo.databinding.FragmentSetnigsBinding
import java.util.Locale

class SettingsFragment : Fragment() {
    private var _binding: FragmentSetnigsBinding? = null
    private val binding get() = _binding!!
    companion object {
        fun getInstance(onSelectLanguage: OnSelectLanguage?): SettingsFragment {
            val fragment = SettingsFragment()
            fragment.onSelectLanguage = onSelectLanguage
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSetnigsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupLanguage()
        setupMoodNight()
    }


    private fun setupLanguage() {
        val list = resources.getStringArray(R.array.languageArr)
        val arrAdapter = ArrayAdapter(requireContext(), R.layout.item_tv_setting, list)

        binding.autoCompLanguage.setAdapter(arrAdapter)

        binding.autoCompLanguage.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val item = parent.getItemAtPosition(position).toString()
                val langCode = when (item) {
                    "Arabic" -> "ar"
                    "English" -> "en"
                    else -> Locale.getDefault().language
                }

                val sharedPref =
                    requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
                sharedPref.edit().putString("app_lang", langCode).apply()

                onSelectLanguage?.onLangSelected(langCode)
                requireActivity().recreate()
                Toast.makeText(requireContext(), item, Toast.LENGTH_SHORT).show()
            }
    }

    private var onSelectLanguage: OnSelectLanguage? = null

    fun interface OnSelectLanguage {
        fun onLangSelected(language: String)
    }

    private fun setupMoodNight() {

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}