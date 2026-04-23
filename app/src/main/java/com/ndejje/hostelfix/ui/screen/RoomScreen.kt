package com.ndejje.hostelfix.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ndejje.hostelfix.data.local.Room
import com.ndejje.hostelfix.ui.viewmodel.RoomViewModel

@Composable
fun RoomScreen(roomViewModel: RoomViewModel = viewModel()) {

    var roomNumber by remember { mutableStateOf("") }
    var hostelName by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }

    val rooms = roomViewModel.allRooms.collectAsState(initial = emptyList())

    Column(modifier = Modifier.padding(16.dp)) {

        OutlinedTextField(
            value = roomNumber,
            onValueChange = { roomNumber = it },
            label = { Text("Room Number") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = hostelName,
            onValueChange = { hostelName = it },
            label = { Text("Hostel Name") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = capacity,
            onValueChange = { capacity = it },
            label = { Text("Capacity") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if (roomNumber.isNotEmpty() && hostelName.isNotEmpty() && capacity.isNotEmpty()) {
                roomViewModel.insertRoom(
                    Room(
                        roomNumber = roomNumber,
                        hostelName = hostelName,
                        capacity = capacity.toInt()
                    )
                )
                roomNumber = ""
                hostelName = ""
                capacity = ""
            }
        }) {
            Text("Add Room")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(rooms.value) { room ->
                Text(
                    text = "Room ${room.roomNumber} - ${room.hostelName} (Capacity: ${room.capacity})"
                )
            }
        }
    }
}