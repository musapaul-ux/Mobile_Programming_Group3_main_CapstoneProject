package com.ndejje.hostelfix.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object StudentHome : Screen("student_home")
    object AdminHome : Screen("admin_home")
    object CreateComplaint : Screen("create_complaint")
    object MyComplaints : Screen("my_complaints")
    object AdminComplaints : Screen("admin_complaints")
    object AdminUsers : Screen("admin_users")
    object Profile : Screen("profile")
}