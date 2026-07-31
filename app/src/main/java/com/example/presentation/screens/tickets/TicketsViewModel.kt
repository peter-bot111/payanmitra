package com.example.presentation.screens.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entities.TicketEntity
import com.example.data.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TicketsViewModel(
    private val ticketRepository: TicketRepository
) : ViewModel() {

    private val _allTickets = MutableStateFlow<List<TicketEntity>>(emptyList())
    val allTickets: StateFlow<List<TicketEntity>> = _allTickets.asStateFlow()

    init {
        loadTickets()
    }

    private fun loadTickets() {
        viewModelScope.launch {
            ticketRepository.getAllTickets().collect { list ->
                _allTickets.value = list
            }
        }
    }

    fun cancelTicket(pnr: String) {
        viewModelScope.launch {
            ticketRepository.cancelTicket(pnr)
        }
    }
}
