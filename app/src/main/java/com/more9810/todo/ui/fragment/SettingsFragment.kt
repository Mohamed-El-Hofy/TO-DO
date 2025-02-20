package com.more9810.todo.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import com.more9810.todo.R
import com.more9810.todo.databinding.FragmentSetnigsBinding
import com.more9810.todo.utils.Const

class SettingsFragment : Fragment() {
    private var _binding: FragmentSetnigsBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSetnigsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences(Const.SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE)
        setupListener()

    }


    override fun onStart() {
        super.onStart()
        initialUi()
        setupLanguageDropDownMenu()
        setupModeDropDownMenu()
    }


    private fun setupModeDropDownMenu(){
        val modes = resources.getStringArray(R.array.modeArr).toList()
        val adapter = ArrayAdapter(requireContext(), R.layout.item_tv_drop_down, modes)
        binding.modeAutoComp.setAdapter(adapter)
    }

    private fun setupLanguageDropDownMenu() {
        val language = resources.getStringArray(R.array.languageArr).toList()
        val adapter = ArrayAdapter(requireContext(), R.layout.item_tv_drop_down, language)
        binding.languageAutoComp.setAdapter(adapter)
    }

    private fun initialUi() {
        setupInitialModeState()
        setupInitialLanguageState()
    }

    private fun setupListener() {
        setupLanguageDropDownMenuListener()
        setupModeDropDownMenuListener()
    }

    private fun setupLanguageDropDownMenuListener() {
        binding.languageAutoComp.setOnItemClickListener { _, _, position, _ ->
            val selectedLanguage = binding.languageAutoComp.adapter.getItem(position).toString()
            binding.languageAutoComp.setText(selectedLanguage)
            val langCode = when (selectedLanguage) {
                getString(R.string.english) -> Const.ENGLISH_CODE
                getString(R.string.arabic) -> Const.ARABIC_CODE
                else -> Const.ENGLISH_CODE
            }
            applyLanguageChange(langCode)
        }
    }

    private fun applyLanguageChange(langCode: String) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(langCode))
    }

    private fun setupModeDropDownMenuListener() {
        binding.modeAutoComp.setOnItemClickListener { _, _, position, _ ->
            val selectedMode = binding.modeAutoComp.adapter.getItem(position).toString()
            binding.modeAutoComp.setText(selectedMode)
            val isDark = selectedMode == getString(R.string.night)
            applyModeChange(isDark)
            saveModeToSharedPreferences(isDark)
        }
    }

    private fun saveModeToSharedPreferences(isDark: Boolean) {
        with(sharedPreferences.edit()){
            putBoolean(Const.DARK_MODE_KEY, isDark)
            apply()
        }
    }

    private fun applyModeChange(isDark: Boolean) {
        if (isDark) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun setupInitialModeState() {
        val getCurrentDeviceMode = AppCompatDelegate.getDefaultNightMode()
        val mode = when (getCurrentDeviceMode) {
            AppCompatDelegate.MODE_NIGHT_YES -> getString(R.string.night)
            else -> getString(R.string.light)
        }
        binding.modeAutoComp.setText(mode)
    }

    private fun setupInitialLanguageState() {
        val getCurrentDeviceLanguageCode = AppCompatDelegate.getApplicationLocales()[0]?.language ?:resources.configuration.locales.get(0).language
        val languageCode = when (getCurrentDeviceLanguageCode) {
            Const.ENGLISH_CODE -> resources.getString(R.string.english)
            Const.ARABIC_CODE -> resources.getString(R.string.arabic)
            else -> resources.getString(R.string.english)
        }
        binding.languageAutoComp.setText(languageCode)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

