package com.ndejje.hostelfix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ndejje.hostelfix.data.local.User
import com.ndejje.hostelfix.data.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    val allUsers: StateFlow<List<User>> = repository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun insertUser(user: User) {
        viewModelScope.launch {
            repository.insertUser(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            repository.deleteUser(user)
        }
    }
}