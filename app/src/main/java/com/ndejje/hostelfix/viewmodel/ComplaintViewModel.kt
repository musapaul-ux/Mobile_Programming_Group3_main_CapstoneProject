package com.ndejje.hostelfix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ndejje.hostelfix.data.model.Complaint
import com.ndejje.hostelfix.data.repository.ComplaintRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing maintenance complaints.
 * Provides data streams for the UI and handles user actions like submission and status updates.
 */
class ComplaintViewModel(private val repository: ComplaintRepository) : ViewModel() {

    /**
     * A StateFlow that provides a real-time list of all complaints in the system.
     * Used mainly by the Admin dashboard.
     */
    val allComplaints: StateFlow<List<Complaint>> = repository.getAllComplaints()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /**
     * Retrieves a stream of complaints submitted by a specific user.
     * @param userId The ID of the student.
     */
    fun getComplaintsByUserId(userId: Int): StateFlow<List<Complaint>> = 
        repository.getComplaintsByUserId(userId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /**
     * Submits a new complaint to the repository.
     */
    fun submitComplaint(complaint: Complaint) {
        viewModelScope.launch {
            repository.insertComplaint(complaint)
        }
    }

    /**
     * Updates the resolution status of a specific complaint.
     * @param id The complaint ID.
     * @param status The new status (e.g., "Resolved").
     */
    fun updateStatus(id: Int, status: String) {
        viewModelScope.launch {
            repository.updateComplaintStatus(id, status)
        }
    }
}