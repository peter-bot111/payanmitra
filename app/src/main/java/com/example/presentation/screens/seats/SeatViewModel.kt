package com.example.presentation.screens.seats

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SeatViewModel : ViewModel() {
    private val _selectedSeats = MutableStateFlow<List<String>>(emptyList())
    val selectedSeats: StateFlow<List<String>> = _selectedSeats.asStateFlow()

    fun toggleSeat(seatId: String) {
        val current = _selectedSeats.value.toMutableList()
        if (current.contains(seatId)) {
            current.remove(seatId)
        } else {
            current.add(seatId)
        }
        _selectedSeats.value = current
    }
}
