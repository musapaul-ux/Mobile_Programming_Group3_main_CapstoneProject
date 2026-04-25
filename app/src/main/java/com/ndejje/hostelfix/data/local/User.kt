package com.ndejje.hostelfix.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class representing a User in the database.
 * This table stores information for both Students and Admins.
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,         // Unique identifier for the user
    val name: String,        // Full name of the user
    val email: String,       // Login email address
    val password: String,    // Plain text password (for simplicity in this capstone)
    val role: String         // Role determining access levels: "Student" or "Admin"
)