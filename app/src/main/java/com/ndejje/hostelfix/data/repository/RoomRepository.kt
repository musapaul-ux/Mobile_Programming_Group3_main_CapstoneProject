package com.ndejje.hostelfix.data.repository

import com.ndejje.hostelfix.data.local.Room
import com.ndejje.hostelfix.data.local.RoomDao
import kotlinx.coroutines.flow.Flow

class RoomRepository(private val roomDao: RoomDao) {

    fun getAllRooms(): Flow<List<Room>> = roomDao.getAllRooms()

    suspend fun insertRoom(room: Room) = roomDao.insertRoom(room)

    suspend fun deleteRoom(room: Room) = roomDao.deleteRoom(room)
}