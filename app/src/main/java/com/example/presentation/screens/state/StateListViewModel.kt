package com.example.presentation.screens.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entities.StateEntity
import com.example.data.repository.StateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StateListViewModel(
    private val stateRepository: StateRepository
) : ViewModel() {

    private val _states = MutableStateFlow<List<StateEntity>>(emptyList())
    val states: StateFlow<List<StateEntity>> = _states.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadStates()
    }

    private fun loadStates() {
        viewModelScope.launch {
            stateRepository.getAllStates().collect { list ->
                _states.value = list
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
