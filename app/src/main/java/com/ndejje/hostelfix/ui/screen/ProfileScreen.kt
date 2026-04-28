package com.ndejje.hostelfix.ui.screen

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ndejje.hostelfix.R
import com.ndejje.hostelfix.data.local.User
import com.ndejje.hostelfix.data.repository.UserRepository
import com.ndejje.hostelfix.ui.components.UserDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * Screen displaying the user's profile information.
 * All details are centered under the profile picture for a balanced look.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: User,
    userRepository: UserRepository,
    onLogout: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            scope.launch {
                val internalPath = saveImageToInternalStorage(context, selectedUri, user.id)
                if (internalPath != null) {
                    userRepository.insertUser(
                        User(
                            id = user.id,
                            name = user.name,
                            email = user.email,
                            password = user.password,
                            role = user.role,
                            profilePictureUri = internalPath
                        )
                    )
                }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = dimensionResource(R.dimen.padding_large))
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Navigation Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.profile),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { showEditDialog = true }) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Profile Picture - Centered
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .clickable { imagePickerLauncher.launch("image/*") }
            ) {
                if (user.profilePictureUri != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(File(user.profilePictureUri))
                            .crossfade(true)
                            .build(),
                        contentDescription = "Profile Picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(0.7f).align(Alignment.Center),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape),
                    tonalElevation = 4.dp
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Upload Picture",
                        modifier = Modifier.padding(7.dp),
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // User Info - Strictly Centered
            Text(
                text = user.name, 
                style = MaterialTheme.typography.headlineMedium, 
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = user.email, 
                style = MaterialTheme.typography.titleLarge, 
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(R.string.role) + ": " + user.role,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.weight(1f).heightIn(min = 40.dp))

            // Balanced Logout Button
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.button_height)),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(stringResource(R.string.logout), style = MaterialTheme.typography.titleMedium)
            }
            
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))
        }

        if (showEditDialog) {
            UserDialog(
                dialogTitle = "Edit Profile",
                initialUser = user,
                showRoleSelection = false,
                onDismiss = { showEditDialog = false },
                onConfirm = { name, email, password, _ ->
                    scope.launch {
                        userRepository.insertUser(
                            User(
                                id = user.id,
                                name = name,
                                email = email,
                                password = password,
                                role = user.role,
                                profilePictureUri = user.profilePictureUri
                            )
                        )
                        showEditDialog = false
                    }
                }
            )
        }
    }
}

suspend fun saveImageToInternalStorage(context: Context, uri: Uri, userId: Int): String? = withContext(Dispatchers.IO) {
    try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.filesDir, "profile_pic_${userId}.jpg")
        val outputStream = FileOutputStream(file)
        
        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
