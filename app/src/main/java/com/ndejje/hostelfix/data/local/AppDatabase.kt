package com.ndejje.hostelfix.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ndejje.hostelfix.data.model.Complaint

/**
 * The Room Database for the application.
 * Incremented to version 4 to support profile picture storage.
 */
@Database(entities = [Complaint::class, User::class, Room::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun complaintDao(): ComplaintDao
    abstract fun userDao(): UserDao
    abstract fun roomDao(): RoomDao
}
