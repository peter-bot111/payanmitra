package com.example.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entities.BusRouteEntity
import com.example.data.repository.BusRouteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BusResultsViewModel(
    private val busRouteRepository: BusRouteRepository
) : ViewModel() {

    private val _busResults = MutableStateFlow<List<BusRouteEntity>>(emptyList())
    val busResults: StateFlow<List<BusRouteEntity>> = _busResults.asStateFlow()

    private val _selectedFilter = MutableStateFlow("ALL")
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()

    fun searchBuses(fromLoc: String, toLoc: String) {
        viewModelScope.launch {
            val results = busRouteRepository.getInterCityRoutes(fromLoc, toLoc)
            if (results.isNotEmpty()) {
                _busResults.value = results
            } else {
                // Fallback to all bookable routes if exact query is empty for demo richness
                val allBookable = busRouteRepository.getAllBookableRoutes()
                _busResults.value = allBookable
            }
        }
    }

    fun setFilter(filter: String) {
        _selectedFilter.value = filter
    }
}
