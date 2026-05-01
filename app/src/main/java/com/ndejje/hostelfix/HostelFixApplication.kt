package com.ndejje.hostelfix

import android.app.Application
import com.ndejje.hostelfix.data.local.DatabaseProvider
import com.ndejje.hostelfix.data.repository.ComplaintRepository
import com.ndejje.hostelfix.data.repository.UserRepository

class HostelFixApplication : Application() {
    val database by lazy { DatabaseProvider.getDatabase(this) }
    val userRepository by lazy { UserRepository(database.userDao(), this) }
    val complaintRepository by lazy { ComplaintRepository(database.complaintDao()) }
}
