package com.ndejje.hostelfix.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.ndejje.hostelfix.R

@Composable
fun StudentHomeScreen(
    onNavigateToCreateComplaint: () -> Unit,
    onNavigateToMyComplaints: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
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
            Text(text = stringResource(R.string.welcome_title), style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_large)))
            Button(
                onClick = onNavigateToMyComplaints,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.my_complaints))
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_medium)))
            Button(
                onClick = onNavigateToProfile,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.profile))
            }
        }
    }
}