package com.example.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entities.AreaEntity
import com.example.data.repository.StateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SearchBusViewModel(
    private val stateRepository: StateRepository
) : ViewModel() {

    private val _fromLocation = MutableStateFlow("Chennai Koyambedu")
    val fromLocation: StateFlow<String> = _fromLocation.asStateFlow()

    private val _toLocation = MutableStateFlow("Madurai Mattuthavani")
    val toLocation: StateFlow<String> = _toLocation.asStateFlow()

    private val _selectedDate = MutableStateFlow(
        SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(Date())
    )
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    private val _passengerCount = MutableStateFlow(1)
    val passengerCount: StateFlow<Int> = _passengerCount.asStateFlow()

    private val _allAreas = MutableStateFlow<List<AreaEntity>>(emptyList())
    val allAreas: StateFlow<List<AreaEntity>> = _allAreas.asStateFlow()

    init {
        loadAreas()
    }

    private fun loadAreas() {
        viewModelScope.launch {
            stateRepository.getAllAreas().collect { list ->
                _allAreas.value = list
            }
        }
    }

    fun setFromLocation(loc: String) { _fromLocation.value = loc }
    fun setToLocation(loc: String) { _toLocation.value = loc }
    fun setDate(date: String) { _selectedDate.value = date }

    fun swapLocations() {
        val temp = _fromLocation.value
        _fromLocation.value = _toLocation.value
        _toLocation.value = temp
    }

    fun incrementPassengers() { if (_passengerCount.value < 6) _passengerCount.value += 1 }
    fun decrementPassengers() { if (_passengerCount.value > 1) _passengerCount.value -= 1 }
}
