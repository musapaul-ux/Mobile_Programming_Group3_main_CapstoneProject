package com.ndejje.hostelfix.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rooms")
data class Room(
    @PrimaryKey(autoGenerate = true)
    val roomId: Int = 0,
    val roomNumber: String,
    val hostelName: String,
    val capacity: Int
)