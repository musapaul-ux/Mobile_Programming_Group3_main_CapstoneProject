package com.ndejje.hostelfix.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ndejje.hostelfix.data.model.Complaint

@Database(entities = [Complaint::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun complaintDao(): ComplaintDao
}
