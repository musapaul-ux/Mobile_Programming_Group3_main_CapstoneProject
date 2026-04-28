package com.ndejje.hostelfix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ndejje.hostelfix.ui.HostelFixApp
import com.ndejje.hostelfix.ui.theme.HostelFixTheme
import com.ndejje.hostelfix.viewmodel.AppViewModelProvider
import com.ndejje.hostelfix.viewmodel.ThemeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = viewModel(factory = AppViewModelProvider.Factory)
            val isDarkMode by themeViewModel.isDarkMode.collectAsState()

            HostelFixTheme(darkTheme = isDarkMode) {
                HostelFixApp()
            }
        }
    }
}
