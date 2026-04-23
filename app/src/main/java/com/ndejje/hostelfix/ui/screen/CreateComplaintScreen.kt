package com.ndejje.hostelfix.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.ndejje.hostelfix.R
import com.ndejje.hostelfix.data.model.Complaint
import com.ndejje.hostelfix.viewmodel.ComplaintViewModel

@Composable
fun CreateComplaintScreen(
    userId: Int,
    viewModel: ComplaintViewModel,
    onComplaintSubmitted: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(R.string.submit_complaint), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_large)))
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(stringResource(R.string.title)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_medium)))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(stringResource(R.string.description)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_large)))
        Button(
            onClick = {
                viewModel.submitComplaint(Complaint(title = title, description = description, userId = userId))
                onComplaintSubmitted()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.submit_complaint))
        }
    }
}