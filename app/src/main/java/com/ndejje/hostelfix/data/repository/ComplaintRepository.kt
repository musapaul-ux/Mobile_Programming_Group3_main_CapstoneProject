package com.ndejje.hostelfix.data.repository

import com.ndejje.hostelfix.data.local.ComplaintDao
import com.ndejje.hostelfix.data.model.Complaint
import kotlinx.coroutines.flow.Flow

/**
 * Repository class that manages the retrieval and storage of hostel complaints.
 */
class ComplaintRepository(private val complaintDao: ComplaintDao) {
    
    /**
     * Retrieves all complaints across all users.
     * Mainly used for the Admin view.
     */
    fun getAllComplaints(): Flow<List<Complaint>> = complaintDao.getAllComplaints()
    
    /**
     * Retrieves only the complaints submitted by a specific user.
     * Used for the Student's "My Complaints" view.
     */
    fun getComplaintsByUserId(userId: Int): Flow<List<Complaint>> = complaintDao.getComplaintsByUserId(userId)
    
    /**
     * Saves a new complaint to the database.
     */
    suspend fun insertComplaint(complaint: Complaint) = complaintDao.insertComplaint(complaint)
    
    /**
     * Updates the resolution status of an existing complaint.
     * e.g., changing status from "Pending" to "Resolved".
     */
    suspend fun updateComplaintStatus(id: Int, status: String) = complaintDao.updateComplaintStatus(id, status)
}