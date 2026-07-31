package com.example.presentation.screens.tracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entities.BusRouteEntity
import com.example.data.local.entities.LiveBusEntity
import com.example.data.repository.BusRouteRepository
import com.example.data.repository.LiveBusRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LiveTrackingViewModel(
    private val liveBusRepository: LiveBusRepository,
    private val busRouteRepository: BusRouteRepository
) : ViewModel() {

    private val _liveBus = MutableStateFlow<LiveBusEntity?>(null)
    val liveBus: StateFlow<LiveBusEntity?> = _liveBus.asStateFlow()

    private val _route = MutableStateFlow<BusRouteEntity?>(null)
    val route: StateFlow<BusRouteEntity?> = _route.asStateFlow()

    fun startTracking(routeNumber: String) {
        viewModelScope.launch {
            val r = busRouteRepository.getRouteByNumber(routeNumber)
            _route.value = r

            liveBusRepository.getBusesForRoute(routeNumber).collect { buses ->
                _liveBus.value = buses.firstOrNull()
            }
        }
    }
}
