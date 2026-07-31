package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tickets")
data class TicketEntity(
    @PrimaryKey val pnr: String,         // e.g., PAYX-20260731-9821A
    val routeNumber: String,
    val sourceArea: String,
    val destinationArea: String,
    val journeyDate: String,
    val departureTime: String,
    val busNumber: String,
    val busType: String,
    val farePaid: Double,
    val seatNumbers: String,            // e.g. "S12, S13"
    val passengerName: String,
    val passengerPhone: String,
    val ticketStatus: String,           // "CONFIRMED", "COMPLETED", "CANCELLED"
    val qrContent: String,
    val bookedTimestamp: Long
)
