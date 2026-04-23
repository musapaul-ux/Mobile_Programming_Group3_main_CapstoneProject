package com.ndejje.hostelfix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ndejje.hostelfix.data.model.Complaint
import com.ndejje.hostelfix.data.repository.ComplaintRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ComplaintViewModel(private val repository: ComplaintRepository) : ViewModel() {

    val allComplaints: StateFlow<List<Complaint>> = repository.getAllComplaints()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getComplaintsByUserId(userId: Int): StateFlow<List<Complaint>> = 
        repository.getComplaintsByUserId(userId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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