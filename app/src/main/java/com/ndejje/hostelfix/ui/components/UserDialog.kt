package com.ndejje.hostelfix.ui.components

import androidx.compose.foundation.layout.*
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

/**
 * A reusable dialog component for Adding or Editing user information.
 * Used by both Admin Management and individual User Profile editing.
 */
@Composable
fun UserDialog(
    dialogTitle: String,                 // Title shown at the top of the dialog
    initialUser: User? = null,           // Existing user data (if editing)
    showRoleSelection: Boolean = true,   // Whether to show role radio buttons (hidden for user self-edit)
    onDismiss: () -> Unit,               // Callback to close the dialog
    onConfirm: (String, String, String, String) -> Unit // Callback with updated Name, Email, Password, Role
) {
    // Local state for tracking form inputs
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
                
                // Name Input Field
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
                
                // Email Input Field
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
                
                // Password Input Field with Masking
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
                
                // Optional Role Selection (Only for Admin Management)
                if (showRoleSelection) {
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_medium)))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = role == "Student", onClick = { role = "Student" })
                        Text("Student")
                        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacer_medium)))
                        RadioButton(selected = role == "Admin", onClick = { role = "Admin" })
                        Text("Admin")
                    }
                }

                // Error message for empty fields
                if (showError) {
                    Text(
                        text = "All fields are required",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_small))
                    )
                }

                // Action Buttons: Cancel and Confirm
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
