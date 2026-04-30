package com.ndejje.hostelfix.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.window.Dialog
import com.ndejje.hostelfix.R
import com.ndejje.hostelfix.data.local.User
import com.ndejje.hostelfix.data.repository.UserRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsersScreen(
    userRepository: UserRepository,
    onNavigateBack: () -> Unit
) {
    val users by userRepository.allUsers.collectAsState(initial = emptyList())
    var showAddUserDialog by remember { mutableStateOf(false) }
    var userToEdit by remember { mutableStateOf<User?>(null) }
    var userToDelete by remember { mutableStateOf<User?>(null) }
    
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.user_management)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddUserDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add User")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            // Optimization: Added stable key to items
            items(users, key = { it.id }) { user ->
                UserItem(
                    user = user,
                    onEdit = { userToEdit = user },
                    onDelete = { userToDelete = user }
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            }
        }

        if (showAddUserDialog) {
            UserDialog(
                dialogTitle = "Add New User",
                onDismiss = { showAddUserDialog = false },
                onConfirm = { name, email, password, role ->
                    scope.launch {
                        userRepository.insertUser(
                            User(name = name, email = email, password = password, role = role)
                        )
                        showAddUserDialog = false
                    }
                }
            )
        }

        userToEdit?.let { user ->
            UserDialog(
                dialogTitle = "Edit User",
                initialUser = user,
                onDismiss = { userToEdit = null },
                onConfirm = { name, email, password, role ->
                    scope.launch {
                        userRepository.insertUser(
                            user.copy(name = name, email = email, password = password, role = role)
                        )
                        userToEdit = null
                    }
                }
            )
        }

        userToDelete?.let { user ->
            AlertDialog(
                onDismissRequest = { userToDelete = null },
                title = { Text("Delete User") },
                text = { Text("Are you sure you want to delete ${user.name}?") },
                confirmButton = {
                    Button(
                        onClick = {
                            scope.launch {
                                userRepository.deleteUser(user)
                                userToDelete = null
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { userToDelete = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun UserDialog(
    dialogTitle: String,
    initialUser: User? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(initialUser?.name ?: "") }
    var email by remember { mutableStateOf(initialUser?.email ?: "") }
    var password by remember { mutableStateOf(initialUser?.password ?: "") }
    var role by remember { mutableStateOf(initialUser?.role ?: "Student") }
    var showError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium)),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_medium)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = dialogTitle, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_medium)))
                
                OutlinedTextField(
                    value = name,
                    onValueChange = { 
                        name = it
                        showError = false
                    },
                    label = { Text(stringResource(R.string.name)) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = showError && name.isBlank()
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_medium)))
                OutlinedTextField(
                    value = email,
                    onValueChange = { 
                        email = it
                        showError = false
                    },
                    label = { Text(stringResource(R.string.email)) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = showError && email.isBlank()
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_medium)))
                OutlinedTextField(
                    value = password,
                    onValueChange = { 
                        password = it
                        showError = false
                    },
                    label = { Text(stringResource(R.string.password)) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    isError = showError && password.isBlank()
                )
                
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_medium)))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = role == "Student", onClick = { role = "Student" })
                    Text("Student")
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacer_medium)))
                    RadioButton(selected = role == "Admin", onClick = { role = "Admin" })
                    Text("Admin")
                }

                if (showError) {
                    Text(
                        text = "All fields are required",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_small))
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Button(onClick = {
                        if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                            onConfirm(name, email, password, role)
                        } else {
                            showError = true
                        }
                    }) {
                        Text(if (initialUser == null) "Add" else "Update")
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(
    user: User,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.padding_small))
    ) {
        Row(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium))
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = user.name, style = MaterialTheme.typography.titleMedium)
                Text(text = user.email, style = MaterialTheme.typography.bodySmall)
                Text(
                    text = stringResource(R.string.role) + ": " + user.role,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit User", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete User", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
