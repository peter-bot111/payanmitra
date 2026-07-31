package com.example.presentation.screens.area

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entities.AreaEntity
import com.example.data.repository.StateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AreaListViewModel(
    private val stateRepository: StateRepository
) : ViewModel() {

    private val _areas = MutableStateFlow<List<AreaEntity>>(emptyList())
    val areas: StateFlow<List<AreaEntity>> = _areas.asStateFlow()

    fun loadAreas(districtCode: String) {
        viewModelScope.launch {
            stateRepository.getAreasForDistrict(districtCode).collect { list ->
                _areas.value = list
            }
        }
    }
}
