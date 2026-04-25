package com.ndejje.hostelfix.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class representing a maintenance complaint.
 * Stores details about hostel issues reported by students.
 */
@Entity(tableName = "complaints")
data class Complaint(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,                 // Unique ID for the complaint
    val hostelName: String,          // Name of the university hostel
    val roomNumber: String,          // Specific room number within the hostel
    val title: String,               // Brief title of the issue (e.g., "Leaking pipe")
    val description: String,         // Detailed description of the problem
    val status: String = "Pending",  // Lifecycle status: Pending, In Progress, Resolved
    val userId: Int,                 // ID of the student who submitted the report
    val timestamp: Long = System.currentTimeMillis() // Exact time of submission
)