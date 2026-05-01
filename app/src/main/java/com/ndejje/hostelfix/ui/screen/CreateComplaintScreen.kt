package com.ndejje.hostelfix.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ndejje.hostelfix.R
import com.ndejje.hostelfix.data.model.Complaint
import com.ndejje.hostelfix.viewmodel.ComplaintViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateComplaintScreen(
    userId: Int,
    viewModel: ComplaintViewModel,
    onNavigateBack: () -> Unit,
    onComplaintSubmitted: () -> Unit
) {
    var hostelName by remember { mutableStateOf("") }
    var roomNumber by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    
    var showError by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // Optimization: remember the gradient brush to prevent flickering during typing
    val primaryColor = MaterialTheme.colorScheme.primaryContainer
    val backgroundColor = MaterialTheme.colorScheme.background
    val gradientBrush = remember(primaryColor, backgroundColor) {
        Brush.verticalGradient(
            colors = listOf(
                primaryColor.copy(alpha = 0.4f),
                backgroundColor
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Header - Fixed at top for stability
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(R.string.submit_complaint),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                    tonalElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            value = hostelName,
                            onValueChange = { 
                                hostelName = it
                                showError = false
                            },
                            label = { Text("Hostel Name") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            isError = showError && hostelName.isBlank(),
                            singleLine = true
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OutlinedTextField(
                            value = roomNumber,
                            onValueChange = { 
                                roomNumber = it
                                showError = false
                            },
                            label = { Text("Room Number") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            isError = showError && roomNumber.isBlank(),
                            singleLine = true
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OutlinedTextField(
                            value = title,
                            onValueChange = { 
                                title = it
                                showError = false
                            },
                            label = { Text(stringResource(R.string.title)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            isError = showError && title.isBlank(),
                            singleLine = true
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OutlinedTextField(
                            value = description,
                            onValueChange = { 
                                description = it
                                showError = false
                            },
                            label = { Text(stringResource(R.string.description)) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 4,
                            shape = RoundedCornerShape(12.dp),
                            isError = showError && description.isBlank()
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                if (showError) {
                    Text(
                        text = "All fields are required",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        stringResource(R.string.submit_complaint),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}