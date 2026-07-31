package com.example.presentation.screens.area

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entities.AreaEntity
import com.example.data.local.entities.DistrictEntity
import com.example.data.repository.StateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AreaSelectViewModel(
    private val stateRepository: StateRepository
) : ViewModel() {

    private val _districts = MutableStateFlow<List<DistrictEntity>>(emptyList())
    val districts: StateFlow<List<DistrictEntity>> = _districts.asStateFlow()

    private val _areas = MutableStateFlow<List<AreaEntity>>(emptyList())
    val areas: StateFlow<List<AreaEntity>> = _areas.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadDistricts("TN")
        loadAllAreas()
    }

    fun loadDistricts(stateCode: String) {
        viewModelScope.launch {
            stateRepository.getDistrictsForState(stateCode).collect { list ->
                _districts.value = list
            }
        }
    }

    private fun loadAllAreas() {
        viewModelScope.launch {
            stateRepository.getAllAreas().collect { list ->
                _areas.value = list
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
