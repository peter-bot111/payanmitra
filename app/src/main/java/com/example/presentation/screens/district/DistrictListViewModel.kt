package com.example.presentation.screens.district

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entities.DistrictEntity
import com.example.data.repository.StateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DistrictListViewModel(
    private val stateRepository: StateRepository
) : ViewModel() {

    private val _districts = MutableStateFlow<List<DistrictEntity>>(emptyList())
    val districts: StateFlow<List<DistrictEntity>> = _districts.asStateFlow()

    fun loadDistricts(stateCode: String) {
        viewModelScope.launch {
            stateRepository.getDistrictsForState(stateCode).collect { list ->
                _districts.value = list
            }
        }
    }
}
