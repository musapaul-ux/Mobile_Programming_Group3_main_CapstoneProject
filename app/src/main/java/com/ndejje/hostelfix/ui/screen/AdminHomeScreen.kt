package com.ndejje.hostelfix.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.ndejje.hostelfix.R

@Composable
fun AdminHomeScreen(
    onNavigateToComplaints: () -> Unit,
    onNavigateToUsers: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(dimensionResource(R.dimen.padding_medium)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = stringResource(R.string.admin_dashboard), style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_large)))
            Button(
                onClick = onNavigateToComplaints,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.all_complaints))
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_medium)))
            Button(
                onClick = onNavigateToUsers,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.users))
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_large)))
            TextButton(onClick = onLogout) {
                Text(stringResource(R.string.logout))
            }
        }
    }
}