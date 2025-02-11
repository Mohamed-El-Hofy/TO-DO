package com.more9810.todo.ui.activity

import android.content.Context
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.more9810.todo.R
import com.more9810.todo.databinding.ActivityTestBinding
import com.more9810.todo.utils.LocaleHelper
import java.util.Locale

class TestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



    }

    private fun lang() {
        val list = resources.getStringArray(R.array.languageArr)
        val arrAdapter = ArrayAdapter(this, R.layout.item_tv_setting, list)

        binding.autoCompLanguage.setAdapter(arrAdapter)

        binding.autoCompLanguage.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val item = parent.getItemAtPosition(position).toString()
                val langCode = when (item) {
                    resources.getString(R.string.arabic) -> "ar"
                    resources.getString(R.string.english) -> "en"
                    else -> Locale.getDefault().language
                }

            }
    }



}