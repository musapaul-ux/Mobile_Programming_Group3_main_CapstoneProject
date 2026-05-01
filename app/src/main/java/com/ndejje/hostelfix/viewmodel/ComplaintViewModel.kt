package com.ndejje.hostelfix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ndejje.hostelfix.data.model.Complaint
import com.ndejje.hostelfix.data.repository.ComplaintRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing maintenance complaints.
 * Provides data streams for the UI and handles user actions like submission and status updates.
 */
class ComplaintViewModel(private val repository: ComplaintRepository) : ViewModel() {

    /**
     * A StateFlow that provides a real-time list of all complaints.
     * SharingStarted.Lazily keeps the flow active as long as the ViewModel exists,
     * preventing "jumps" back to empty state when navigating away and back.
     */
    val allComplaints: StateFlow<List<Complaint>?> = repository.getAllComplaints()
        .map { it as List<Complaint>? }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val _userComplaints = MutableStateFlow<Map<Int, StateFlow<List<Complaint>?>>>(emptyMap())

    /**
     * Retrieves a stable StateFlow of complaints for a specific user.
     * Caches the flow to ensure the same object is returned, preventing recomposition loops.
     */
    fun getComplaintsState(userId: Int): StateFlow<List<Complaint>?> {
        val currentMap = _userComplaints.value
        return currentMap[userId] ?: run {
            val newFlow = repository.getComplaintsByUserId(userId)
                .map { it as List<Complaint>? }
                .stateIn(viewModelScope, SharingStarted.Lazily, null)
            _userComplaints.value = currentMap + (userId to newFlow)
            newFlow
        }
    }

    fun submitComplaint(complaint: Complaint) {
        viewModelScope.launch {
            repository.insertComplaint(complaint)
        }
    }

    fun updateStatus(id: Int, status: String) {
        viewModelScope.launch {
            repository.updateComplaintStatus(id, status)
        }
    }
}
