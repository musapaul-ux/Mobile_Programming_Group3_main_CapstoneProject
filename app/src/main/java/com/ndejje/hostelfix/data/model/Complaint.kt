package com.ndejje.hostelfix.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "complaints")
data class Complaint(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val hostelName: String,
    val roomNumber: String,
    val title: String,
    val description: String,
    val status: String = "Pending",
    val userId: Int,
    val timestamp: Long = System.currentTimeMillis()
)