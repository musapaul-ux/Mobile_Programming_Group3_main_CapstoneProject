package com.ndejje.hostelfix.data.local

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "hostelfix_database"
            )
            .fallbackToDestructiveMigration()
            .build()

            INSTANCE = instance
            instance
        }
    }
}