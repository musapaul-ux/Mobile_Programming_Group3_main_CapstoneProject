package com.ndejje.hostelfix.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ndejje.hostelfix.data.local.User
import com.ndejje.hostelfix.ui.viewmodel.UserViewModel

@Composable
fun UserScreen(userViewModel: UserViewModel = viewModel()) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val users = userViewModel.allUsers.collectAsState(initial = emptyList())

    Column(modifier = Modifier.padding(16.dp)) {

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if (name.isNotEmpty() && email.isNotEmpty()) {
                userViewModel.insertUser(User(name = name, email = email, password = "password123", role = "Student"))
                name = ""
                email = ""
            }
        }) {
            Text("Add User")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(users.value) { user ->
                Text(text = "${user.name} - ${user.email}")
            }
        }
    }
}