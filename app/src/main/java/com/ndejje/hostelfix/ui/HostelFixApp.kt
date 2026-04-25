package com.ndejje.hostelfix.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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

sealed class BottomNavItem(val screen: Screen, val labelRes: Int, val icon: ImageVector) {
    // Student items
    object Home : BottomNavItem(Screen.StudentHome, R.string.welcome_title, Icons.Default.Home)
    object MyComplaints : BottomNavItem(Screen.MyComplaints, R.string.my_complaints, Icons.Default.List)
    object Profile : BottomNavItem(Screen.Profile, R.string.profile, Icons.Default.Person)

    // Admin items
    object AdminDashboard : BottomNavItem(Screen.AdminHome, R.string.admin_dashboard, Icons.Default.Home)
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

    val showBars = currentDestination?.route !in listOf(Screen.Login.route, Screen.Register.route)

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
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        },
        bottomBar = {
            if (showBars) {
                val items = if (currentUser?.role == "Admin") {
                    listOf(BottomNavItem.AdminDashboard, BottomNavItem.AdminComplaints, BottomNavItem.AdminUsers)
                } else {
                    listOf(BottomNavItem.Home, BottomNavItem.MyComplaints, BottomNavItem.Profile)
                }

                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    items.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = null) },
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
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    viewModel = authViewModel,
                    onLoginSuccess = { role ->
                        if (role == "Admin") {
                            navController.navigate(Screen.AdminHome.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        } else {
                            navController.navigate(Screen.StudentHome.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
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
                    AdminHomeScreen(
                        onNavigateToComplaints = { navController.navigate(Screen.AdminComplaints.route) },
                        onNavigateToUsers = { navController.navigate(Screen.AdminUsers.route) },
                        onLogout = {
                            authViewModel.logout()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0)
                            }
                        }
                    )
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
