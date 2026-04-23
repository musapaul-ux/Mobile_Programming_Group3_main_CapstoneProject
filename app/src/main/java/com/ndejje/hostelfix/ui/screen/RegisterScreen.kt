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
    var role by remember { mutableStateOf("Student") }
    val authState by viewModel.authState.collectAsState()

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
            onValueChange = { name = it },
            label = { Text(stringResource(R.string.name)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_medium)))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.email)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_medium)))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_medium)))
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = role == stringResource(R.string.student), onClick = { role = "Student" })
            Text(stringResource(R.string.student))
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacer_medium)))
            RadioButton(selected = role == stringResource(R.string.admin), onClick = { role = "Admin" })
            Text(stringResource(R.string.admin))
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_large)))
        Button(
            onClick = { viewModel.register(User(name = name, email = email, password = password, role = role)) },
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