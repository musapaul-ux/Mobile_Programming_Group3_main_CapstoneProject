package com.ndejje.hostelfix.data.local

import androidx.room.*
import com.ndejje.hostelfix.data.model.Complaint
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the Complaint entity.
 * Provides methods for interacting with the 'complaints' table.
 */
@Dao
interface ComplaintDao {

    /**
     * Submits a new complaint or replaces an existing one.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComplaint(complaint: Complaint)

    /**
     * Fetches all complaints in the system, newest first.
     */
    @Query("SELECT * FROM complaints ORDER BY timestamp DESC")
    fun getAllComplaints(): Flow<List<Complaint>>

    /**
     * Fetches complaints submitted by a specific student.
     */
    @Query("SELECT * FROM complaints WHERE userId = :userId ORDER BY timestamp DESC")
    fun getComplaintsByUserId(userId: Int): Flow<List<Complaint>>

    /**
     * Finds a single complaint by its unique ID.
     */
    @Query("SELECT * FROM complaints WHERE id = :id")
    suspend fun getComplaintById(id: Int): Complaint?

    /**
     * Updates all fields of an existing complaint.
     */
    @Update
    suspend fun updateComplaint(complaint: Complaint)

    /**
     * Updates only the status field of a specific complaint.
     * Used by admins to resolve issues.
     */
    @Query("UPDATE complaints SET status = :status WHERE id = :id")
    suspend fun updateComplaintStatus(id: Int, status: String)
}
