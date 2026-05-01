package com.ndejje.hostelfix.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for User entity.
 * Defines the SQL queries and database interactions for users.
 */
@Dao
interface UserDao {
    /**
     * Inserts or updates a user in the database.
     * REPLACE strategy is used to handle editing existing users via their ID.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    /**
     * Retrieves a single user by their email address.
     * Used for authentication/login.
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    /**
     * Retrieves a single user by their ID.
     */
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Int): User?

    /**
     * Observes all users in the database.
     * Returns a Flow that emits updates whenever the table changes.
     */
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>

    /**
     * Deletes a specific user from the database.
     */
    @Delete
    suspend fun deleteUser(user: User)
}
