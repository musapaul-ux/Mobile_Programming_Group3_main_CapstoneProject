package com.ndejje.hostelfix.data.repository

import com.ndejje.hostelfix.data.local.User
import com.ndejje.hostelfix.data.local.UserDao
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    val allUsers: Flow<List<User>> = userDao.getAllUsers()

    suspend fun insertUser(user: User) = userDao.insertUser(user)

    suspend fun getUserByEmail(email: String) = userDao.getUserByEmail(email)

    suspend fun deleteUser(user: User) = userDao.deleteUser(user)
}