package com.ndejje.hostelfix.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ndejje.hostelfix.HostelFixApplication
import com.ndejje.hostelfix.data.repository.ComplaintRepository
import com.ndejje.hostelfix.data.repository.UserRepository

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            AuthViewModel(hostelFixApplication().userRepository)
        }
        initializer {
            ComplaintViewModel(hostelFixApplication().complaintRepository)
        }
    }
}

fun CreationExtras.hostelFixApplication(): HostelFixApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HostelFixApplication)
