package com.ndejje.hostelfix.data.repository

import com.ndejje.hostelfix.data.local.ComplaintDao
import com.ndejje.hostelfix.data.model.Complaint
import kotlinx.coroutines.flow.Flow

class ComplaintRepository(private val complaintDao: ComplaintDao) {
    fun getAllComplaints(): Flow<List<Complaint>> = complaintDao.getAllComplaints()
    fun getComplaintsByUserId(userId: Int): Flow<List<Complaint>> = complaintDao.getComplaintsByUserId(userId)
    suspend fun insertComplaint(complaint: Complaint) = complaintDao.insertComplaint(complaint)
    suspend fun updateComplaintStatus(id: Int, status: String) = complaintDao.updateComplaintStatus(id, status)
}