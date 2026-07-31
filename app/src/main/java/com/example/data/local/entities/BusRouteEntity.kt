package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bus_routes")
data class BusRouteEntity(
    @PrimaryKey val routeNumber: String,
    val sourceArea: String,
    val destinationArea: String,
    val stateCode: String,
    val corporation: String,            // "TNSTC", "KSRTC" etc.
    val totalDistance: Int,             // km
    val journeyDuration: Int,           // minutes
    val fareAmount: Double,
    val busType: String,                // "ORDINARY","EXPRESS","DELUXE","SUPERDELUXE","AC","VOLVO"
    val frequency: String,              // "Every 30 min", "Every 1 hr"
    val operatingDays: String,          // "MON-SUN", "MON-SAT"
    val firstBusTime: String,           // "05:30"
    val lastBusTime: String,            // "22:45"
    val viaStops: String,               // Comma-separated or JSON list of stop names
    val isBookable: Boolean = false,    // true = inter-city reserved, false = local
    val isACBus: Boolean = false,
    val isSleeper: Boolean = false,
    val totalSeats: Int = 36
)
