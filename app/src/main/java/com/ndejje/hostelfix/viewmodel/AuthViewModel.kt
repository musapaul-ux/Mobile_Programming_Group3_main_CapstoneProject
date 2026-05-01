package com.ndejje.hostelfix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ndejje.hostelfix.data.local.User
import com.ndejje.hostelfix.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for handling user authentication, including login and registration.
 * It also manages the current user session and profile updates.
 */
class AuthViewModel(private val repository: UserRepository) : ViewModel() {

    // Internal state for the currently logged-in user
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    // Internal state for tracking the overall authentication status (Success, Error, Loading)
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    init {
        // Automatically ensure a default admin account exists upon app startup
        viewModelScope.launch {
            repository.createDefaultAdmin()
        }
    }

    /**
     * Authenticates a user based on email and password.
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val user = repository.getUserByEmail(email)
            if (user != null && user.password == password) {
                _currentUser.value = user
                _authState.value = AuthState.Success(user.role)
            } else {
                _authState.value = AuthState.Error("Invalid email or password")
            }
        }
    }

    /**
     * Registers a new student in the system.
     */
    fun register(user: User) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            repository.insertUser(user)
            _authState.value = AuthState.Success(user.role)
        }
    }

    /**
     * Updates the user information in the database and the current session state.
     */
    fun updateUser(user: User) {
        viewModelScope.launch {
            repository.insertUser(user)
            _currentUser.value = user
        }
    }

    /**
     * Updates the profile picture for the currently logged-in user.
     */
    fun updateProfilePicture(uri: String) {
        viewModelScope.launch {
            _currentUser.value?.let { user ->
                val updatedUser = user.copy(profilePictureUri = uri)
                repository.insertUser(updatedUser)
                _currentUser.value = updatedUser
            }
        }
    }

    /**
     * Clears user data and resets the app state upon logout.
     */
    fun logout() {
        _currentUser.value = null
        _authState.value = AuthState.Idle
    }

    /**
     * Explicitly resets the authState to Idle, used to clear success/error triggers after navigation.
     */
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}

/**
 * Sealed class representing various states of the authentication process.
 */
sealed class AuthState {
    object Idle : AuthState()              // No active auth action
    object Loading : AuthState()           // Processing login/register
    data class Success(val role: String) : AuthState() // Successful auth with user role
    data class Error(val message: String) : AuthState() // Failed auth with error reason
}
