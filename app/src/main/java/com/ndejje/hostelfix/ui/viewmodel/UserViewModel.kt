package com.ndejje.hostelfix.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ndejje.hostelfix.data.local.DatabaseProvider
import com.ndejje.hostelfix.data.repository.UserRepository
import com.ndejje.hostelfix.data.local.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository
    val allUsers: Flow<List<User>>

    init {
        val dao = DatabaseProvider.getDatabase(application).userDao()
        repository = UserRepository(dao)
        allUsers = repository.allUsers
    }

    fun insertUser(user: User) = viewModelScope.launch {
        repository.insertUser(user)
    }

    fun deleteUser(user: User) = viewModelScope.launch {
        repository.deleteUser(user)
    }
}
