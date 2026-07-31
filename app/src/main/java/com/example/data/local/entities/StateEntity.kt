package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "states")
data class StateEntity(
    @PrimaryKey val stateCode: String,  // e.g., "TN", "KA", "MH"
    val stateName: String,
    val stateNameTamil: String,
    val stateNameHindi: String,
    val busCorporation: String,         // e.g., "TNSTC", "KSRTC"
    val corporationWebsite: String,
    val helplineNumber: String
)
