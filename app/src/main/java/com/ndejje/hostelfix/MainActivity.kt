package com.ndejje.hostelfix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ndejje.hostelfix.ui.HostelFixApp
import com.ndejje.hostelfix.ui.theme.HostelFixTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HostelFixTheme {
                HostelFixApp()
            }
        }
    }
}
