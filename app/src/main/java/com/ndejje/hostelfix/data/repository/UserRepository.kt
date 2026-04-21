package com.ndejje.hostelfix.data.repository

import com.ndejje.hostelfix.data.local.UserDao
import com.ndejje.hostelfix.data.local.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    val allUsers: Flow<List<User>> = userDao.getAllUsers()

    suspend fun insert(user: User) {
        userDao.insertUser(user)
    }

    suspend fun delete(user: User) {
        userDao.deleteUser(user)
    }
}