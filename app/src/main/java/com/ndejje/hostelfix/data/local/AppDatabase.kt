package com.ndejje.hostelfix.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ndejje.hostelfix.data.model.Complaint

@Database(entities = [Complaint::class, User::class, Room::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun complaintDao(): ComplaintDao
    abstract fun userDao(): UserDao
    abstract fun roomDao(): RoomDao
}
