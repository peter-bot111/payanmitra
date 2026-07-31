package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bus_stops")
data class BusStopEntity(
    @PrimaryKey val stopCode: String,
    val stopName: String,
    val stopNameTamil: String,
    val latitude: Double,
    val longitude: Double,
    val areaCode: String,
    val hasWaitingShed: Boolean,
    val hasWaterFacility: Boolean,
    val hasCCTV: Boolean
)
