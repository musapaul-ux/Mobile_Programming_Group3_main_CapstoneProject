package com.ndejje.hostelfix.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ndejje.hostelfix.data.local.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: User,
    onLogout: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = user.name, style = MaterialTheme.typography.headlineLarge)
            Text(text = user.email, style = MaterialTheme.typography.bodyLarge)
            Text(text = "Role: ${user.role}", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
                Text("Logout")
            }
        }
    }
}
