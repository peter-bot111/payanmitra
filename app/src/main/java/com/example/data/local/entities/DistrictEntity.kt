package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "districts")
data class DistrictEntity(
    @PrimaryKey val districtCode: String,
    val districtName: String,
    val districtNameTamil: String,
    val districtNameHindi: String,
    val stateCode: String,              // Foreign key reference
    val latitude: Double,
    val longitude: Double
)
