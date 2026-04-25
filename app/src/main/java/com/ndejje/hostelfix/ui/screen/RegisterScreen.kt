package com.ndejje.hostelfix.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.ndejje.hostelfix.R
import com.ndejje.hostelfix.data.local.User
import com.ndejje.hostelfix.viewmodel.AuthState
import com.ndejje.hostelfix.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: (String) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by viewModel.authState.collectAsState()
    
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(R.string.register), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_large)))
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
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_large)))
        
        if (showError) {
            Text(
                text = "All fields are required",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_small))
            )
        }

        Button(
            onClick = { 
                if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                    viewModel.register(User(name = name, email = email, password = password, role = "Student")) 
                } else {
                    showError = true
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = authState !is AuthState.Loading
        ) {
            Text(stringResource(R.string.register))
        }
        TextButton(onClick = onNavigateToLogin) {
            Text(stringResource(R.string.already_have_account))
        }

        LaunchedEffect(authState) {
            if (authState is AuthState.Success) {
                onRegisterSuccess((authState as AuthState.Success).role)
            }
        }
    }
}