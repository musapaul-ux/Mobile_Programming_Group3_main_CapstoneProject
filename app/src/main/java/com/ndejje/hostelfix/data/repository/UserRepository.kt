package com.ndejje.hostelfix.data.repository

import android.content.Context
import com.ndejje.hostelfix.data.local.User
import com.ndejje.hostelfix.data.local.UserDao
import kotlinx.coroutines.flow.Flow

/**
 * Repository class that abstracts access to the User data source.
 * Acts as a mediator between the ViewModel and the Room database.
 */
class UserRepository(private val userDao: UserDao, context: Context) {
    
    private val sharedPreferences = context.getSharedPreferences("hostelfix_prefs", Context.MODE_PRIVATE)

    // Observable list of all users in the database
    val allUsers: Flow<List<User>> = userDao.getAllUsers()

    /**
     * Inserts a new user or updates an existing one.
     */
    suspend fun insertUser(user: User) = userDao.insertUser(user)

    /**
     * Finds a user by their email address.
     */
    suspend fun getUserByEmail(email: String) = userDao.getUserByEmail(email)

    /**
     * Finds a user by their ID.
     */
    suspend fun getUserById(id: Int) = userDao.getUserById(id)

    /**
     * Removes a user from the database.
     */
    suspend fun deleteUser(user: User) = userDao.deleteUser(user)

    /**
     * Persists the user ID to SharedPreferences.
     */
    fun saveSession(userId: Int) {
        sharedPreferences.edit().putInt("user_id", userId).apply()
    }

    /**
     * Retrieves the persisted user ID.
     */
    fun getSessionUserId(): Int {
        return sharedPreferences.getInt("user_id", -1)
    }

    /**
     * Clears the session from SharedPreferences.
     */
    fun clearSession() {
        sharedPreferences.edit().remove("user_id").apply()
    }
    
    /**
     * Checks for and creates a default system administrator if the database is empty.
     * This ensures the application always has at least one admin account.
     */
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
