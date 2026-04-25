package com.ndejje.hostelfix.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.ndejje.hostelfix.R
import com.ndejje.hostelfix.data.model.Complaint
import com.ndejje.hostelfix.viewmodel.ComplaintViewModel
import java.text.SimpleDateFormat
import java.util.*

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
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_medium)))
            }
        }
    }
}

@Composable
fun AdminComplaintItem(complaint: Complaint, onUpdateStatus: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.padding_small)),
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius))
    ) {
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = complaint.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = stringResource(R.string.user_id_label, complaint.userId),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                
                Surface(
                    color = when (complaint.status) {
                        stringResource(R.string.status_resolved) -> MaterialTheme.colorScheme.primary
                        stringResource(R.string.status_pending) -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.secondary
                    },
                    modifier = Modifier.clip(RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)))
                ) {
                    Text(
                        text = complaint.status,
                        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_small), vertical = dimensionResource(R.dimen.padding_small) / 2),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.hostel_label, complaint.hostelName),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stringResource(R.string.room_label, complaint.roomNumber),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_small)))

            Text(
                text = complaint.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(complaint.timestamp)),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                
                Row {
                    val inProgressStatus = stringResource(R.string.status_in_progress)
                    val resolvedStatus = stringResource(R.string.status_resolved)
                    
                    TextButton(onClick = { onUpdateStatus(inProgressStatus) }) { 
                        Text(stringResource(R.string.status_in_progress)) 
                    }
                    Button(
                        onClick = { onUpdateStatus(resolvedStatus) },
                        contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_medium))
                    ) { 
                        Text(stringResource(R.string.resolve_action)) 
                    }
                }
            }
        }
    }
}
