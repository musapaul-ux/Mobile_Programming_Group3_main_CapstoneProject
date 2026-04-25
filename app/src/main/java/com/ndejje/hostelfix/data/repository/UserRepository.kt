package com.ndejje.hostelfix.data.repository

import com.ndejje.hostelfix.data.local.User
import com.ndejje.hostelfix.data.local.UserDao
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    val allUsers: Flow<List<User>> = userDao.getAllUsers()

    suspend fun insertUser(user: User) = userDao.insertUser(user)

    suspend fun getUserByEmail(email: String) = userDao.getUserByEmail(email)

    suspend fun deleteUser(user: User) = userDao.deleteUser(user)
    
    suspend fun createDefaultAdmin() {
        val admin = userDao.getUserByEmail("admin@hostelfix.com")
        if (admin == null) {
            userDao.insertUser(
                User(
                    name = "System Admin",
                    email = "admin@hostelfix.com",
                    password = "admin",
                    role = "Admin"
                )
            )
        }
    }
}