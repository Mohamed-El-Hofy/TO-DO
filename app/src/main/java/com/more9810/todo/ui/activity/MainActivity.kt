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
import androidx.fragment.app.Fragment
import com.more9810.todo.R
import com.more9810.todo.databinding.ActivityHomeBinding
import com.more9810.todo.ui.fragment.BottomSheetDialogFragment
import com.more9810.todo.ui.fragment.SettingsFragment
import com.more9810.todo.ui.fragment.TasksFragment
import com.more9810.todo.utils.Const
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var splashScreen: SplashScreen
    private var isSplashScreenViewed: Boolean = true

    private var tasksFragment: TasksFragment? = null
    private var settingsFragment: SettingsFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreen = installSplashScreen()
        setupSplashScreen()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        systemBars()

        tasksFragment = supportFragmentManager.findFragmentByTag("TaskFragment()") as? TasksFragment
            ?: TasksFragment()
        settingsFragment =
            supportFragmentManager.findFragmentByTag("SettingsFragment()") as? SettingsFragment
                ?: SettingsFragment()

        localizeLang()
        setNavigation()
        onFabClicked()
    }

    private fun localizeLang() {
        val sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val lang = sharedPref.getString("app_lang", Locale.getDefault().language)
        LocaleHelper.setLocale(this, lang ?: "en")
        LocaleHelper.loadLocale(this)
    }


    private fun setNavigation() {
        val menu = binding.bottomNavigation.menu
        menu.getItem(1).setEnabled(false)
        menu.getItem(2).setEnabled(false)

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.fragTasks -> {
                    showFragment(tasksFragment!!, "TaskFragment()")
                    binding.toolbar.tvAppBarTitle.text = "TO DO LIST"
                }

                R.id.fragSitings -> {
                    return@setOnItemSelectedListener false
                    showFragment(settingsFragment!!, "SettingsFragment()")
                    binding.toolbar.tvAppBarTitle.text = "Setteings"
                }

                else -> showFragment(TasksFragment(), "")
            }
            return@setOnItemSelectedListener true
        }
        binding.bottomNavigation.selectedItemId = R.id.fragTasks
    }

    private fun showFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction().replace(R.id.frameContainer, fragment, tag)
            .commit()
    }

    private fun onFabClicked() {
        binding.btnFab.setOnClickListener {
            val dialogAdd = BottomSheetDialogFragment()

            dialogAdd.show(supportFragmentManager, BottomSheetDialogFragment().tag)

            dialogAdd.onAddNewTask = BottomSheetDialogFragment.OnClickSaveTaskFromNew { task ->
                tasksFragment?.addNewTask(task)
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