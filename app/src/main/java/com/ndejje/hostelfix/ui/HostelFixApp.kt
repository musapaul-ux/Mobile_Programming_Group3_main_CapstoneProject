package com.ndejje.hostelfix.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ndejje.hostelfix.HostelFixApplication
import com.ndejje.hostelfix.R
import com.ndejje.hostelfix.navigation.Screen
import com.ndejje.hostelfix.ui.screen.*
import com.ndejje.hostelfix.viewmodel.AppViewModelProvider
import com.ndejje.hostelfix.viewmodel.AuthViewModel
import com.ndejje.hostelfix.viewmodel.ComplaintViewModel

/**
 * Defines the items to be displayed in the Bottom Navigation Bar.
 */
sealed class BottomNavItem(val screen: Screen, val labelRes: Int, val icon: ImageVector) {
    object WelcomeHome : BottomNavItem(Screen.Welcome, R.string.app_name, Icons.Default.Home)
    object StudentDashboard : BottomNavItem(Screen.StudentHome, R.string.welcome_title, Icons.Default.Dashboard)
    object AddComplaint : BottomNavItem(Screen.CreateComplaint, R.string.submit_complaint, Icons.Default.Add)
    object MyComplaints : BottomNavItem(Screen.MyComplaints, R.string.my_complaints, Icons.Default.List)
    object Profile : BottomNavItem(Screen.Profile, R.string.profile, Icons.Default.Person)
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

    // Hide TopAppBar for redesigned screens to prevent shifts and double headers
    val hideTopBar = currentDestination?.route in listOf(
        Screen.Welcome.route, 
        Screen.Login.route, 
        Screen.Register.route,
        Screen.Profile.route,
        Screen.MyComplaints.route,
        Screen.CreateComplaint.route,
        Screen.AdminComplaints.route,
        Screen.StudentHome.route,
        Screen.AdminHome.route
    )

    // Only show BottomBar on authenticated screens
    val showBottomBar = currentDestination?.route !in listOf(Screen.Welcome.route, Screen.Login.route, Screen.Register.route)

    Scaffold(
        topBar = {
            if (!hideTopBar) {
                TopAppBar(
                    title = {
                        Text(
                            text = when (currentDestination?.route) {
                                Screen.AdminUsers.route -> stringResource(R.string.user_management)
                                else -> stringResource(R.string.app_name)
                            }
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                val items = if (currentUser?.role == "Admin") {
                    listOf(BottomNavItem.WelcomeHome, BottomNavItem.AdminDashboard, BottomNavItem.AdminComplaints, BottomNavItem.AdminUsers)
                } else {
                    listOf(BottomNavItem.WelcomeHome, BottomNavItem.StudentDashboard, BottomNavItem.AddComplaint, BottomNavItem.MyComplaints, BottomNavItem.Profile)
                }

                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    items.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = null) },
                            label = { Text(if(item == BottomNavItem.WelcomeHome) "Home" else stringResource(item.labelRes)) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
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
            // If we are hiding the top bar, we don't apply top padding from Scaffold to prevent empty gaps/shifts
            modifier = Modifier.padding(
                bottom = innerPadding.calculateBottomPadding(),
                top = if (hideTopBar) 0.dp else innerPadding.calculateTopPadding()
            )
        ) {
            composable(Screen.Welcome.route) {
                WelcomeScreen(onGetStarted = { navController.navigate(Screen.Login.route) })
            }
            composable(Screen.Login.route) {
                LoginScreen(
                    viewModel = authViewModel,
                    onLoginSuccess = { role ->
                        val startRoute = if (role == "Admin") Screen.AdminHome.route else Screen.StudentHome.route
                        navController.navigate(startRoute) { popUpTo(Screen.Welcome.route) { inclusive = true } }
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
                    onRegisterSuccess = { 
                        authViewModel.logout()
                        navController.navigate(Screen.Login.route) { popUpTo(Screen.Register.route) { inclusive = true } }
                    },
                    onNavigateToLogin = {
                        authViewModel.logout()
                        navController.popBackStack()
                    }
                )
            }
            composable(Screen.StudentHome.route) {
                if (currentUser == null || currentUser?.role != "Student") {
                    LaunchedEffect(Unit) { navController.navigate(Screen.Login.route) { popUpTo(0) } }
                } else {
                    StudentHomeScreen(
                        userName = currentUser?.name ?: "User",
                        onNavigateToCreateComplaint = { navController.navigate(Screen.CreateComplaint.route) },
                        onNavigateToMyComplaints = { navController.navigate(Screen.MyComplaints.route) },
                        onNavigateToProfile = { navController.navigate(Screen.Profile.route) }
                    )
                }
            }
            composable(Screen.AdminHome.route) {
                if (currentUser == null || currentUser?.role != "Admin") {
                    LaunchedEffect(Unit) { navController.navigate(Screen.Login.route) { popUpTo(0) } }
                } else {
                    AdminHomeScreen(
                        userName = currentUser?.name ?: "Admin",
                        onNavigateToComplaints = { navController.navigate(Screen.AdminComplaints.route) },
                        onNavigateToUsers = { navController.navigate(Screen.AdminUsers.route) },
                        onLogout = {
                            authViewModel.logout()
                            navController.navigate(Screen.Login.route) { popUpTo(0) }
                        }
                    )
                }
            }
            composable(Screen.CreateComplaint.route) {
                currentUser?.let { user ->
                    CreateComplaintScreen(
                        userId = user.id,
                        viewModel = complaintViewModel,
                        onNavigateBack = { navController.popBackStack() },
                        onComplaintSubmitted = { navController.popBackStack() }
                    )
                }
            }
            composable(Screen.MyComplaints.route) {
                currentUser?.let { user ->
                    MyComplaintsScreen(
                        userId = user.id,
                        viewModel = complaintViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
            composable(Screen.AdminComplaints.route) {
                AdminComplaintsScreen(
                    viewModel = complaintViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.AdminUsers.route) {
                AdminUsersScreen(
                    userRepository = app.userRepository,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Profile.route) {
                currentUser?.let { user ->
                    ProfileScreen(
                        user = user,
                        userRepository = app.userRepository,
                        onLogout = {
                            authViewModel.logout()
                            navController.navigate(Screen.Login.route) { popUpTo(0) }
                        },
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
