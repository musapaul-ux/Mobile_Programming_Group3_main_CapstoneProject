package com.ndejje.hostelfix.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.ndejje.hostelfix.R
import com.ndejje.hostelfix.data.model.Complaint
import com.ndejje.hostelfix.viewmodel.ComplaintViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyComplaintsScreen(
    userId: Int,
    viewModel: ComplaintViewModel,
    onNavigateBack: () -> Unit
) {
    val complaints by viewModel.getComplaintsByUserId(userId).collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.my_complaints)) }
            )
        }
    ) { padding ->
        if (complaints.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.no_complaints))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(dimensionResource(R.dimen.padding_medium))
            ) {
                items(complaints) { complaint ->
                    ComplaintItem(complaint)
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                }
            }
        }
    }
}

@Composable
fun ComplaintItem(complaint: Complaint) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.padding_small))
    ) {
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
            Text(text = complaint.title, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            Text(text = complaint.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.status_label, complaint.status),
                    color = when (complaint.status) {
                        stringResource(R.string.status_resolved) -> MaterialTheme.colorScheme.primary
                        stringResource(R.string.status_pending) -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.secondary
                    }
                )
                Text(
                    text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(complaint.timestamp)),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}
