package com.ndejje.hostelfix.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.ndejje.hostelfix.R
import com.ndejje.hostelfix.data.local.User
import com.ndejje.hostelfix.data.repository.UserRepository
import com.ndejje.hostelfix.ui.components.UserDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: User,
    userRepository: UserRepository,
    onLogout: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.profile)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showEditDialog = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(dimensionResource(R.dimen.padding_large)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = user.name, style = MaterialTheme.typography.displayMedium, color = MaterialTheme.colorScheme.primary)
            Text(text = user.email, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_medium)))
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = stringResource(R.string.role) + ": " + user.role,
                    modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium), vertical = dimensionResource(R.dimen.padding_small)),
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_large)))
            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(stringResource(R.string.logout))
            }
        }

        if (showEditDialog) {
            UserDialog(
                dialogTitle = "Edit Profile",
                initialUser = user,
                showRoleSelection = false,
                onDismiss = { showEditDialog = false },
                onConfirm = { name, email, password, role ->
                    scope.launch {
                        userRepository.insertUser(
                            User(id = user.id, name = name, email = email, password = password, role = user.role)
                        )
                        showEditDialog = false
                    }
                }
            )
        }
    }
}
