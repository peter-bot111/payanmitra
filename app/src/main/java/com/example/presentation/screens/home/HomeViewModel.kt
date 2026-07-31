package com.example.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entities.BusRouteEntity
import com.example.data.local.entities.StateEntity
import com.example.data.repository.BusRouteRepository
import com.example.data.repository.StateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeViewModel(
    private val stateRepository: StateRepository,
    private val busRouteRepository: BusRouteRepository
) : ViewModel() {

    private val _currentGreeting = MutableStateFlow("")
    val currentGreeting: StateFlow<String> = _currentGreeting.asStateFlow()

    private val _selectedState = MutableStateFlow<StateEntity?>(null)
    val selectedState: StateFlow<StateEntity?> = _selectedState.asStateFlow()

    private val _selectedAreaName = MutableStateFlow("Dindigul Central")
    val selectedAreaName: StateFlow<String> = _selectedAreaName.asStateFlow()

    private val _nearbyBuses = MutableStateFlow<List<BusRouteEntity>>(emptyList())
    val nearbyBuses: StateFlow<List<BusRouteEntity>> = _nearbyBuses.asStateFlow()

    init {
        computeTimeBasedGreeting()
        loadInitialData()
    }

    private fun computeTimeBasedGreeting() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        _currentGreeting.value = when (hour) {
            in 4..11 -> "greeting_morning"
            in 12..16 -> "greeting_afternoon"
            else -> "greeting_evening"
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            val state = stateRepository.getStateByCode("TN")
            _selectedState.value = state

            busRouteRepository.getRoutesForArea(_selectedAreaName.value).collect { routes ->
                _nearbyBuses.value = routes
            }
        }
    }

    fun updateSelectedArea(areaName: String) {
        _selectedAreaName.value = areaName
        viewModelScope.launch {
            busRouteRepository.getRoutesForArea(areaName).collect { routes ->
                _nearbyBuses.value = routes
            }
        }
    }
}
