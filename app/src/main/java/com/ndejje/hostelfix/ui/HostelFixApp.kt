package com.ndejje.hostelfix.ui

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ndejje.hostelfix.HostelFixApplication
import com.ndejje.hostelfix.R
import com.ndejje.hostelfix.data.local.User
import com.ndejje.hostelfix.navigation.Screen
import com.ndejje.hostelfix.ui.screen.*
import com.ndejje.hostelfix.viewmodel.AppViewModelProvider
import com.ndejje.hostelfix.viewmodel.AuthViewModel
import com.ndejje.hostelfix.viewmodel.ComplaintViewModel
import com.ndejje.hostelfix.viewmodel.ThemeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * Navigation items for the Bottom Navigation Bar.
 */
sealed class BottomNavItem(val screen: Screen, val labelRes: Int, val icon: ImageVector) {
    object Home : BottomNavItem(Screen.StudentHome, R.string.dashboard, Icons.Default.Home)
    object AdminHome : BottomNavItem(Screen.AdminHome, R.string.dashboard, Icons.Default.Home)
    object AddComplaint : BottomNavItem(Screen.CreateComplaint, R.string.submit_complaint, Icons.Default.Add)
    object MyComplaints : BottomNavItem(Screen.MyComplaints, R.string.my_complaints, Icons.AutoMirrored.Filled.List)
    object Profile : BottomNavItem(Screen.Profile, R.string.profile, Icons.Default.Person)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostelFixApp() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val complaintViewModel: ComplaintViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val themeViewModel: ThemeViewModel = viewModel(factory = AppViewModelProvider.Factory)
    
    val currentUser by authViewModel.currentUser.collectAsState()
    val isDarkMode by themeViewModel.isDarkMode.collectAsState()
    val context = LocalContext.current
    val app = context.applicationContext as HostelFixApplication

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Top and Bottom bars are shown only for authenticated users
    val showBars = currentDestination?.route !in listOf(Screen.Welcome.route, Screen.Login.route, Screen.Register.route)
    val isAdmin = currentUser?.role == "Admin"

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = isAdmin && showBars,
        drawerContent = {
            if (isAdmin && showBars) {
                ModalDrawerSheet {
                    // Admin Account Header - Clickable to view profile
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .clickable {
                                scope.launch { drawerState.close() }
                                navController.navigate(Screen.Profile.route)
                            }
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                modifier = Modifier.size(64.dp),
                                shape = CircleShape,
                                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                if (currentUser?.profilePictureUri != null) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context)
                                            .data(File(currentUser?.profilePictureUri!!))
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "Admin Avatar",
                                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(
                                        Icons.Default.Person,
                                        contentDescription = null,
                                        modifier = Modifier.padding(16.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = currentUser?.name ?: "Admin", 
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "Administrator", 
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = "Tap to view profile", 
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            
                            Icon(
                                Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)
                            )
                        }
                    }
                    HorizontalDivider()
                    
                    NavigationDrawerItem(
                        label = { Text("Dashboard") },
                        selected = currentDestination?.route == Screen.AdminHome.route,
                        icon = { Icon(Icons.Default.Dashboard, null) },
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(Screen.AdminHome.route)
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Complaints") },
                        selected = currentDestination?.route == Screen.AdminComplaints.route,
                        icon = { Icon(Icons.AutoMirrored.Filled.List, null) },
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(Screen.AdminComplaints.route)
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("User Management") },
                        selected = currentDestination?.route == Screen.AdminUsers.route,
                        icon = { Icon(Icons.Default.Group, null) },
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(Screen.AdminUsers.route)
                        }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    NavigationDrawerItem(
                        label = { Text("Logout") },
                        selected = false,
                        icon = { Icon(Icons.AutoMirrored.Filled.Logout, null) },
                        onClick = {
                            scope.launch { drawerState.close() }
                            authViewModel.logout()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0)
                            }
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                if (showBars) {
                    TopAppBar(
                        title = {
                            Text(
                                text = when (currentDestination?.route) {
                                    Screen.StudentHome.route -> stringResource(R.string.welcome_title)
                                    Screen.AdminHome.route -> "Admin Dashboard"
                                    Screen.CreateComplaint.route -> stringResource(R.string.submit_complaint)
                                    Screen.MyComplaints.route -> stringResource(R.string.my_complaints)
                                    Screen.AdminComplaints.route -> stringResource(R.string.all_complaints)
                                    Screen.AdminUsers.route -> stringResource(R.string.user_management)
                                    Screen.Profile.route -> stringResource(R.string.profile)
                                    else -> stringResource(R.string.app_name)
                                }
                            )
                        },
                        navigationIcon = {
                            if (isAdmin) {
                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                                }
                            }
                        },
                        actions = {
                            // Dark/Light Mode Toggle
                            IconButton(onClick = { themeViewModel.toggleDarkMode(!isDarkMode) }) {
                                Icon(
                                    imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                    contentDescription = "Toggle Theme",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            },
            bottomBar = {
                if (showBars && !isAdmin) {
                    val items = listOf(
                        BottomNavItem.Home, 
                        BottomNavItem.AddComplaint, 
                        BottomNavItem.MyComplaints, 
                        BottomNavItem.Profile
                    )
                    NavigationBar {
                        items.forEach { item ->
                            NavigationBarItem(
                                icon = { Icon(item.icon, null) },
                                label = { Text(stringResource(item.labelRes)) },
                                selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true,
                                onClick = {
                                    navController.navigate(item.screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        ) { scaffoldPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Welcome.route,
                modifier = Modifier.padding(scaffoldPadding)
            ) {
                composable(Screen.Welcome.route) {
                    WelcomeScreen(onGetStarted = { navController.navigate(Screen.Login.route) })
                }
                composable(Screen.Login.route) {
                    LoginScreen(
                        viewModel = authViewModel,
                        onLoginSuccess = { role ->
                            val startRoute = if (role == "Admin") Screen.AdminHome.route else Screen.StudentHome.route
                            navController.navigate(startRoute) {
                                popUpTo(Screen.Welcome.route) { inclusive = true }
                            }
                        },
                        onNavigateToRegister = {
                            authViewModel.logout()
                            navController.navigate(Screen.Register.route)
                        }
                    )
                }
                composable(Screen.Register.route) {
                    RegisterScreen(
                        viewModel = authViewModel,
                        onRegisterSuccess = { _ ->
                            authViewModel.logout()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Register.route) { inclusive = true }
                            }
                        },
                        onNavigateToLogin = {
                            authViewModel.logout()
                            navController.popBackStack()
                        }
                    )
                }
                composable(Screen.StudentHome.route) {
                    if (currentUser == null || currentUser?.role != "Student") {
                        LaunchedEffect(Unit) {
                            navController.navigate(Screen.Login.route) { popUpTo(0) }
                        }
                    } else {
                        StudentHomeScreen(
                            authViewModel = authViewModel,
                            onNavigateToCreateComplaint = { navController.navigate(Screen.CreateComplaint.route) },
                            onNavigateToMyComplaints = { navController.navigate(Screen.MyComplaints.route) },
                            onNavigateToProfile = { navController.navigate(Screen.Profile.route) }
                        )
                    }
                }
                composable(Screen.AdminHome.route) {
                    if (currentUser == null || currentUser?.role != "Admin") {
                        LaunchedEffect(Unit) {
                            navController.navigate(Screen.Login.route) { popUpTo(0) }
                        }
                    } else {
                        AdminHomeScreen(authViewModel = authViewModel)
                    }
                }
                composable(Screen.CreateComplaint.route) {
                    currentUser?.let { user ->
                        if (user.role == "Student") {
                            CreateComplaintScreen(
                                userId = user.id,
                                viewModel = complaintViewModel,
                                onComplaintSubmitted = { navController.popBackStack() }
                            )
                        }
                    }
                }
                composable(Screen.MyComplaints.route) {
                    currentUser?.let { user ->
                        if (user.role == "Student") {
                            MyComplaintsScreen(
                                userId = user.id,
                                viewModel = complaintViewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
                composable(Screen.AdminComplaints.route) {
                    currentUser?.let { user ->
                        if (user.role == "Admin") {
                            AdminComplaintsScreen(
                                viewModel = complaintViewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
                composable(Screen.AdminUsers.route) {
                    currentUser?.let { user ->
                        if (user.role == "Admin") {
                            AdminUsersScreen(
                                userRepository = app.userRepository,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
                composable(Screen.Profile.route) {
                    currentUser?.let { user ->
                        ProfileScreen(
                            user = user,
                            onUpdateUser = { updatedUser -> authViewModel.updateUser(updatedUser) },
                            onLogout = {
                                authViewModel.logout()
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(0)
                                }
                            },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

private suspend fun saveImageToInternalStorage(context: Context, uri: Uri, userId: Int): String? = withContext(Dispatchers.IO) {
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
