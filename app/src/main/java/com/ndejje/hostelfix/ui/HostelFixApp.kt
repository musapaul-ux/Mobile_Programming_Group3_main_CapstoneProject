package com.ndejje.hostelfix.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ndejje.hostelfix.HostelFixApplication
import com.ndejje.hostelfix.navigation.Screen
import com.ndejje.hostelfix.ui.screen.*
import com.ndejje.hostelfix.viewmodel.AppViewModelProvider
import com.ndejje.hostelfix.viewmodel.AuthViewModel
import com.ndejje.hostelfix.viewmodel.ComplaintViewModel

@Composable
fun HostelFixApp() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val complaintViewModel: ComplaintViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val currentUser by authViewModel.currentUser.collectAsState()
    val context = LocalContext.current
    val app = context.applicationContext as HostelFixApplication

    NavHost(navController = navController, startDestination = Screen.Login.route) {
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
                    authViewModel.logout() // Reset state before going to register
                    navController.navigate(Screen.Register.route) 
                }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = { _ ->
                    // After successful registration, reset state and go to login
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
            StudentHomeScreen(
                onNavigateToCreateComplaint = { navController.navigate(Screen.CreateComplaint.route) },
                onNavigateToMyComplaints = { navController.navigate(Screen.MyComplaints.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) }
            )
        }
        composable(Screen.AdminHome.route) {
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
        composable(Screen.CreateComplaint.route) {
            currentUser?.let { user ->
                CreateComplaintScreen(
                    userId = user.id,
                    viewModel = complaintViewModel,
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
