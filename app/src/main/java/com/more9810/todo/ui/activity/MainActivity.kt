package com.more9810.todo.ui.activity

import LocaleHelper
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsCompat.CONSUMED
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import com.more9810.todo.R
import com.more9810.todo.databinding.ActivityHomeBinding
import com.more9810.todo.model.local.TaskDatabase
import com.more9810.todo.ui.fragment.BottomSheetDialogFragment
import com.more9810.todo.ui.fragment.SettingsFragment
import com.more9810.todo.ui.fragment.TasksFragment
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var splashScreen: SplashScreen
    private var isSplashScreenViewed: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreen = installSplashScreen()
        setupSplashScreen()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        //enableEdgeToEdge()
        setContentView(binding.root)

        localizeLang()
        systemBars()
        setup()
    }

    private fun localizeLang() {
        val sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val lang = sharedPref.getString("app_lang", Locale.getDefault().language)
        LocaleHelper.setLocale(this, lang ?: "en")
        LocaleHelper.loadLocale(this)
    }

    private fun setup() {
        setNavigation()
        onFabClicked()
    }

    private fun setNavigation() {
        val menu = binding.bottomNavigation.menu
        menu.getItem(1).setEnabled(false)
        menu.getItem(2).setEnabled(false)
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->

            val fragment = when (menuItem.itemId) {
                R.id.fragTasks -> TasksFragment()
                R.id.fragSitings -> SettingsFragment()
                else -> TasksFragment()
            }
            supportFragmentManager.beginTransaction().replace(R.id.frameContainer, fragment)
                .commit()
            return@setOnItemSelectedListener true
        }
        binding.bottomNavigation.selectedItemId = R.id.fragSitings

    }

    private fun onFabClicked() {
        binding.btnFab.setOnClickListener {
            val dialog = BottomSheetDialogFragment()

            dialog.show(supportFragmentManager, BottomSheetDialogFragment().tag)
            dialog.onClickItem = { task ->
                TaskDatabase.getInstance().getDao().addTask(task)
                val fragment =
                    supportFragmentManager.findFragmentById(R.id.frameContainer) as? TasksFragment
                fragment?.refreshData()
            }
        }
    }

    private fun setupSplashScreen() {
        splashScreen.setKeepOnScreenCondition { isSplashScreenViewed }
        Handler(Looper.getMainLooper()).postDelayed({ isSplashScreenViewed = false }, 100)
    }

    private fun systemBars() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            v.updateLayoutParams {
                binding.main.updatePadding(bottom = systemBars.bottom)
            }
            return@setOnApplyWindowInsetsListener CONSUMED

        }

    }
}