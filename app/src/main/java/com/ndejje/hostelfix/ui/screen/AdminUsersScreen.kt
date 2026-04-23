package com.ndejje.hostelfix.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.ndejje.hostelfix.R
import com.ndejje.hostelfix.data.local.User
import com.ndejje.hostelfix.data.repository.UserRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsersScreen(
    userRepository: UserRepository,
    onNavigateBack: () -> Unit
) {
    val users by userRepository.allUsers.collectAsState(initial = emptyList())

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
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            items(users) { user ->
                UserItem(user)
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            }
        }
    }
}

@Composable
fun UserItem(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.padding_small))
    ) {
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
            Text(text = user.name, style = MaterialTheme.typography.titleMedium)
            Text(text = user.email, style = MaterialTheme.typography.bodySmall)
            Text(
                text = stringResource(R.string.role) + ": " + user.role,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}
