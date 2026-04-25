package com.ndejje.hostelfix.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
                title = { Text("All Complaints") }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            items(complaints) { complaint ->
                AdminComplaintItem(complaint) { newStatus ->
                    viewModel.updateStatus(complaint.id, newStatus)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun AdminComplaintItem(complaint: Complaint, onUpdateStatus: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = complaint.title, style = MaterialTheme.typography.titleLarge)
            Text(text = "User ID: ${complaint.userId}", style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = complaint.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Status: ${complaint.status}")
                Row {
                    TextButton(onClick = { onUpdateStatus("In Progress") }) { Text("In Progress") }
                    TextButton(onClick = { onUpdateStatus("Resolved") }) { Text("Resolve") }
                }
            }
        }
    }
}
