package com.example.data.repository

import com.example.data.local.dao.TicketDao
import com.example.data.local.entities.TicketEntity
import kotlinx.coroutines.flow.Flow

class TicketRepository(
    private val ticketDao: TicketDao
) {
    fun getAllTickets(): Flow<List<TicketEntity>> = ticketDao.getAllTickets()

    suspend fun getTicketByPnr(pnr: String): TicketEntity? = ticketDao.getTicketByPnr(pnr)

    suspend fun bookTicket(ticket: TicketEntity) = ticketDao.insertTicket(ticket)

    suspend fun cancelTicket(pnr: String) = ticketDao.updateTicketStatus(pnr, "CANCELLED")
}
