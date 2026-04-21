package com.ndejje.hostelfix.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "complaints")
data class Complaint(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val category: String,
    val description: String,
    val roomNumber: String,
    val status: String = "Pending",
    val timestamp: Long = System.currentTimeMillis()
)