package com.example.presentation.screens.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entities.BusStopEntity
import com.example.data.local.entities.LiveBusEntity
import com.example.data.repository.BusRouteRepository
import com.example.data.repository.LiveBusRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExploreViewModel(
    private val busRouteRepository: BusRouteRepository,
    private val liveBusRepository: LiveBusRepository
) : ViewModel() {

    private val _busStops = MutableStateFlow<List<BusStopEntity>>(emptyList())
    val busStops: StateFlow<List<BusStopEntity>> = _busStops.asStateFlow()

    private val _liveBuses = MutableStateFlow<List<LiveBusEntity>>(emptyList())
    val liveBuses: StateFlow<List<LiveBusEntity>> = _liveBuses.asStateFlow()

    private val _selectedStop = MutableStateFlow<BusStopEntity?>(null)
    val selectedStop: StateFlow<BusStopEntity?> = _selectedStop.asStateFlow()

    private val _filterType = MutableStateFlow("ALL")
    val filterType: StateFlow<String> = _filterType.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            busRouteRepository.getAllStops().collect { stops ->
                _busStops.value = stops
            }
        }

        viewModelScope.launch {
            liveBusRepository.getAllLiveBuses().collect { buses ->
                _liveBuses.value = buses
            }
        }
    }

    fun selectStop(stop: BusStopEntity?) {
        _selectedStop.value = stop
    }

    fun setFilter(type: String) {
        _filterType.value = type
    }
}
