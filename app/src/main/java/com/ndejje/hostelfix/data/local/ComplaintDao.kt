package com.ndejje.hostelfix.data.local

import androidx.room.*
import com.ndejje.hostelfix.data.model.Complaint
import kotlinx.coroutines.flow.Flow

@Dao
interface ComplaintDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertComplaint(complaint: Complaint)

    @Query("SELECT * FROM complaints ORDER BY timestamp DESC")
    fun getAllComplaints(): Flow<List<Complaint>>

    @Query("SELECT * FROM complaints WHERE id = :id")
    suspend fun getComplaintById(id: Int): Complaint?
}
