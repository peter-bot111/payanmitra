package com.example.presentation.screens.buslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entities.BusRouteEntity
import com.example.data.repository.BusRouteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BusListViewModel(
    private val busRouteRepository: BusRouteRepository
) : ViewModel() {

    private val _areaName = MutableStateFlow("Dindigul Central")
    val areaName: StateFlow<String> = _areaName.asStateFlow()

    private val _busRoutes = MutableStateFlow<List<BusRouteEntity>>(emptyList())
    val busRoutes: StateFlow<List<BusRouteEntity>> = _busRoutes.asStateFlow()

    private val _filter = MutableStateFlow("ALL")
    val filter: StateFlow<String> = _filter.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    fun loadRoutesForArea(areaName: String) {
        _areaName.value = areaName
        viewModelScope.launch {
            busRouteRepository.getRoutesForArea(areaName).collect { routes ->
                _busRoutes.value = routes
            }
        }
    }

    fun setFilter(filterType: String) {
        _filter.value = filterType
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            loadRoutesForArea(_areaName.value)
            _isRefreshing.value = false
        }
    }
}
