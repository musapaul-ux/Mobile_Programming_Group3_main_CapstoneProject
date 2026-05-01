package com.ndejje.hostelfix.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
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
import com.ndejje.hostelfix.navigation.Screen
import com.ndejje.hostelfix.ui.screen.*
import com.ndejje.hostelfix.viewmodel.AppViewModelProvider
import com.ndejje.hostelfix.viewmodel.AuthViewModel
import com.ndejje.hostelfix.viewmodel.ComplaintViewModel
import kotlinx.coroutines.launch
import java.io.File

/**
 * Defines the items to be displayed in the Bottom Navigation Bar.
 */
sealed class BottomNavItem(val screen: Screen, val labelRes: Int, val icon: ImageVector) {
    // Universal items
    object WelcomeHome : BottomNavItem(Screen.Welcome, R.string.app_name, Icons.Default.Home)
    
    // Student items
    object StudentDashboard : BottomNavItem(Screen.StudentHome, R.string.welcome_title, Icons.Default.Dashboard)
    object AddComplaint : BottomNavItem(Screen.CreateComplaint, R.string.submit_complaint, Icons.Default.Add)
    object MyComplaints : BottomNavItem(Screen.MyComplaints, R.string.my_complaints, Icons.Default.List)
    object Profile : BottomNavItem(Screen.Profile, R.string.profile, Icons.Default.Person)

    // Admin items
    object AdminDashboard : BottomNavItem(Screen.AdminHome, R.string.admin_dashboard, Icons.Default.Dashboard)
    object AdminComplaints : BottomNavItem(Screen.AdminComplaints, R.string.all_complaints, Icons.Default.List)
    object AdminUsers : BottomNavItem(Screen.AdminUsers, R.string.user_management, Icons.Default.Person)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostelFixApp() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val complaintViewModel: ComplaintViewModel = viewModel(factory = AppViewModelProvider.Factory)
    
    val currentUser by authViewModel.currentUser.collectAsState()
    val context = LocalContext.current
    val app = context.applicationContext as HostelFixApplication

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Logic to show/hide Top and Bottom bars based on the current screen
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
                    // Admin Account Header - Tapping navigates to Profile
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .clickable {
                                scope.launch { drawerState.close() }
                                navController.navigate(Screen.Profile.route)
                            }
                            .padding(dimensionResource(R.dimen.padding_large))
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Box(contentAlignment = Alignment.BottomEnd) {
                                Surface(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                                    color = MaterialTheme.colorScheme.surfaceVariant
                                ) {
                                    if (currentUser?.profilePictureUri != null) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(app)
                                                .data(File(currentUser?.profilePictureUri!!))
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = "Admin Profile Picture",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        Icon(
                                            Icons.Default.Person,
                                            contentDescription = null,
                                            modifier = Modifier.padding(16.dp),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = currentUser?.name ?: "Admin",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = currentUser?.role ?: "Administrator",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Navigation", 
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), 
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                    
                    NavigationDrawerItem(
                        label = { Text("Home") },
                        selected = currentDestination?.route == Screen.AdminHome.route,
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(Screen.AdminHome.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Complaints") },
                        selected = currentDestination?.route == Screen.AdminComplaints.route,
                        icon = { Icon(Icons.Default.List, contentDescription = null) },
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(Screen.AdminComplaints.route)
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("User Management") },
                        selected = currentDestination?.route == Screen.AdminUsers.route,
                        icon = { Icon(Icons.Default.Person, contentDescription = null) },
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(Screen.AdminUsers.route)
                        }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    NavigationDrawerItem(
                        label = { Text("Logout") },
                        selected = false,
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
                                    Screen.AdminHome.route -> stringResource(R.string.admin_dashboard)
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
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            },
            bottomBar = {
                if (showBars && !isAdmin) {
                    val items = listOf(
                        BottomNavItem.WelcomeHome, 
                        BottomNavItem.StudentDashboard,
                        BottomNavItem.AddComplaint, 
                        BottomNavItem.MyComplaints, 
                        BottomNavItem.Profile
                    )

                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ) {
                        items.forEach { item ->
                            NavigationBarItem(
                                icon = { Icon(item.icon, contentDescription = null) },
                                label = { Text(if(item == BottomNavItem.WelcomeHome) "Home" else stringResource(item.labelRes)) },
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
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Welcome.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Welcome.route) {
                    WelcomeScreen(
                        onGetStarted = {
                            navController.navigate(Screen.Login.route)
                        }
                    )
                }
                composable(Screen.Login.route) {
                    LoginScreen(
                        viewModel = authViewModel,
                        onLoginSuccess = { role ->
                            if (role == "Admin") {
                                navController.navigate(Screen.AdminHome.route) {
                                    popUpTo(Screen.Welcome.route) { inclusive = true }
                                }
                            } else {
                                navController.navigate(Screen.StudentHome.route) {
                                    popUpTo(Screen.Welcome.route) { inclusive = true }
                                }
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
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0)
                            }
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
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0)
                            }
                        }
                    } else {
                        AdminHomeScreen()
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
                            userRepository = app.userRepository,
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
