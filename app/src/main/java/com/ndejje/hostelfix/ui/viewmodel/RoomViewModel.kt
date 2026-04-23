package com.ndejje.hostelfix.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ndejje.hostelfix.data.local.DatabaseProvider
import com.ndejje.hostelfix.data.local.Room
import com.ndejje.hostelfix.data.repository.RoomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class RoomViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RoomRepository
    val allRooms: Flow<List<Room>>

    init {
        val dao = DatabaseProvider.getDatabase(application).roomDao()
        repository = RoomRepository(dao)
        allRooms = repository.getAllRooms()
    }

    fun insertRoom(room: Room) = viewModelScope.launch {
        repository.insertRoom(room)
    }
}