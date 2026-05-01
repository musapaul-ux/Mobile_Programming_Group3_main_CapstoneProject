package com.ndejje.hostelfix.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.ndejje.hostelfix.R
import com.ndejje.hostelfix.data.model.Complaint
import com.ndejje.hostelfix.viewmodel.ComplaintViewModel

/**
 * Screen for students to submit a new hostel complaint.
 * Ensures data validation for required fields and numeric input for room numbers.
 */
@Composable
fun CreateComplaintScreen(
    userId: Int,
    viewModel: ComplaintViewModel,
    onComplaintSubmitted: () -> Unit
) {
    var hostelName by remember { mutableStateOf("") }
    var roomNumber by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        
        // Hostel Name Input
        OutlinedTextField(
            value = hostelName,
            onValueChange = { 
                hostelName = it
                showError = false
            },
            label = { Text("Hostel Name") },
            modifier = Modifier.fillMaxWidth(),
            isError = showError && hostelName.isBlank()
        )
        
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_medium)))
        
        // Room Number Input - Restricted to numbers only
        OutlinedTextField(
            value = roomNumber,
            onValueChange = { input ->
                // Only allow numeric input
                if (input.all { it.isDigit() }) {
                    roomNumber = input
                    showError = false
                }
            },
            label = { Text("Room Number") },
            modifier = Modifier.fillMaxWidth(),
            isError = showError && roomNumber.isBlank(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_medium)))
        
        // Complaint Title
        OutlinedTextField(
            value = title,
            onValueChange = { 
                title = it
                showError = false
            },
            label = { Text(stringResource(R.string.title)) },
            modifier = Modifier.fillMaxWidth(),
            isError = showError && title.isBlank()
        )
        
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_medium)))
        
        // Problem Description
        OutlinedTextField(
            value = description,
            onValueChange = { 
                description = it
                showError = false
            },
            label = { Text(stringResource(R.string.description)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            isError = showError && description.isBlank()
        )
        
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_large)))
        
        // Error Feedback
        if (showError) {
            Text(
                text = "All fields are required",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_small))
            )
        }
        
        // Submit Button
        Button(
            onClick = {
                if (hostelName.isNotBlank() && roomNumber.isNotBlank() && title.isNotBlank() && description.isNotBlank()) {
                    viewModel.submitComplaint(
                        Complaint(
                            hostelName = hostelName,
                            roomNumber = roomNumber,
                            title = title,
                            description = description,
                            userId = userId
                        )
                    )
                    onComplaintSubmitted()
                } else {
                    showError = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.submit_complaint))
        }
    }
}