package com.ndejje.hostelfix.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>
}