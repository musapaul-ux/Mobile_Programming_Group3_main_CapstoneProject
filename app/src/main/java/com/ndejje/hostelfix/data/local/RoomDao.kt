package com.ndejje.hostelfix.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {

    @Insert
    suspend fun insertRoom(room: Room)

    @Query("SELECT * FROM rooms")
    fun getAllRooms(): Flow<List<Room>>

    @Delete
    suspend fun deleteRoom(room: Room)
}