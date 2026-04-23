package com.ndejje.hostelfix.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.ndejje.hostelfix.R
import com.ndejje.hostelfix.data.model.Complaint
import com.ndejje.hostelfix.viewmodel.ComplaintViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminComplaintsScreen(
    viewModel: ComplaintViewModel,
    onNavigateBack: () -> Unit
) {
    val complaints by viewModel.allComplaints.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.all_complaints)) }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            items(complaints) { complaint ->
                AdminComplaintItem(complaint) { newStatus ->
                    viewModel.updateStatus(complaint.id, newStatus)
                }
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            }
        }
    }
}

@Composable
fun AdminComplaintItem(complaint: Complaint, onUpdateStatus: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.padding_small))
    ) {
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
            Text(text = complaint.title, style = MaterialTheme.typography.titleLarge)
            Text(
                text = stringResource(R.string.user_id_label, complaint.userId),
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            Text(text = complaint.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = stringResource(R.string.status_label, complaint.status))
                Row {
                    val inProgressStatus = stringResource(R.string.status_in_progress)
                    val resolvedStatus = stringResource(R.string.status_resolved)
                    
                    TextButton(onClick = { onUpdateStatus(inProgressStatus) }) { 
                        Text(stringResource(R.string.status_in_progress)) 
                    }
                    TextButton(onClick = { onUpdateStatus(resolvedStatus) }) { 
                        Text(stringResource(R.string.resolve_action)) 
                    }
                }
            }
        }
    }
}
