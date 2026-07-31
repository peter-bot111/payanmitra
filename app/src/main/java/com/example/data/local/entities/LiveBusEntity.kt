package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "live_bus")
data class LiveBusEntity(
    @PrimaryKey val busNumber: String,
    val routeNumber: String,
    val currentLatitude: Double,
    val currentLongitude: Double,
    val currentSpeed: Double,
    val totalSeats: Int,
    val occupiedSeats: Int,
    val availableSeats: Int,
    val driverName: String,
    val driverPhone: String,
    val busStatus: String,              // "ON_TIME","DELAYED","CANCELLED"
    val delayMinutes: Int,
    val lastUpdated: Long,
    val hasAIS140: Boolean,             // AIS-140 compliant GPS tracker
    val isPanicActive: Boolean          // SOS activated on bus
)
