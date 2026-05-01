package com.ndejje.hostelfix.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.ndejje.hostelfix.R
import com.ndejje.hostelfix.viewmodel.AuthViewModel

/**
 * The home dashboard for students.
 * Displays a personalized welcome message and quick access buttons.
 */
@Composable
fun StudentHomeScreen(
    authViewModel: AuthViewModel,
    onNavigateToCreateComplaint: () -> Unit,
    onNavigateToMyComplaints: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val currentUser by authViewModel.currentUser.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToCreateComplaint) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.submit_complaint))
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(dimensionResource(R.dimen.padding_medium)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Personalized welcome message using the user's name
            Text(
                text = stringResource(
                    R.string.personalized_welcome, 
                    currentUser?.name ?: "Student"
                ), 
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_large)))

        }
    }
}
