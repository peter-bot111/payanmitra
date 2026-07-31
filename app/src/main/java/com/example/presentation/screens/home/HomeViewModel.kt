package com.example.presentation.screens.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entities.BusRouteEntity
import com.example.data.local.entities.StateEntity
import com.example.data.repository.BusRouteRepository
import com.example.data.repository.StateRepository
import com.example.util.LocationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
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

    private val _isLocating = MutableStateFlow(false)
    val isLocating: StateFlow<Boolean> = _isLocating.asStateFlow()

    private val _locationStatus = MutableStateFlow<String?>(null)
    val locationStatus: StateFlow<String?> = _locationStatus.asStateFlow()

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

    fun detectLocationAndSelectArea(context: Context) {
        viewModelScope.launch {
            _isLocating.value = true
            _locationStatus.value = "Fetching GPS position..."
            val locInfo = LocationHelper.getCurrentLocation(context)
            if (locInfo != null) {
                val detectedArea = locInfo.areaName
                val detectedStateName = locInfo.stateName
                val detectedDistrict = locInfo.districtName

                val allStates = stateRepository.getAllStates().firstOrNull() ?: emptyList()
                val matchedState = allStates.find {
                    it.stateName.contains(detectedStateName, ignoreCase = true) ||
                            detectedStateName.contains(it.stateName, ignoreCase = true)
                } ?: StateEntity("LOC", detectedStateName, detectedStateName, detectedStateName, "$detectedStateName Transport", "", "108")

                _selectedState.value = matchedState

                val allAreas = stateRepository.getAllAreas().firstOrNull() ?: emptyList()
                val matchedAreaEntity = allAreas.find {
                    it.areaName.contains(detectedArea, ignoreCase = true) ||
                            it.areaName.contains(detectedDistrict, ignoreCase = true) ||
                            detectedArea.contains(it.areaName, ignoreCase = true)
                }

                val targetAreaName = matchedAreaEntity?.areaName ?: "$detectedArea $detectedDistrict"
                _selectedAreaName.value = targetAreaName
                _locationStatus.value = "GPS Location: $targetAreaName (${matchedState.stateName})"

                busRouteRepository.getRoutesForArea(targetAreaName).collect { routes ->
                    _nearbyBuses.value = if (routes.isNotEmpty()) {
                        routes
                    } else {
                        // If no specific routes match the auto-geocoded area name, fallback to all routes or state routes
                        busRouteRepository.getAllRoutes().firstOrNull()?.take(6) ?: emptyList()
                    }
                }
            } else {
                _locationStatus.value = "Could not obtain GPS lock. Using selected area."
            }
            _isLocating.value = false
        }
    }
}

