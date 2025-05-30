package com.example.jobfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.jobfinder.data.ThemePreference
import com.example.jobfinder.ui.AppNavigation
import com.example.jobfinder.ui.theme.JobFinderTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val themePref = ThemePreference(this)

        lifecycleScope.launch {
            val isDark = themePref.isDarkModeFlow.first()

            setContent {
                var darkTheme by remember { mutableStateOf(isDark) }
                val navController = rememberNavController()

                JobFinderTheme(darkTheme = darkTheme) {
                    AppNavigation(navController = navController, toggleTheme = {
                        darkTheme = !darkTheme
                        lifecycleScope.launch {
                            themePref.setDarkMode(darkTheme)
                        }
                    })
                }
            }
        }
    }
}
