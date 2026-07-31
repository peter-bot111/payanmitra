package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "areas")
data class AreaEntity(
    @PrimaryKey val areaCode: String,
    val areaName: String,
    val areaNameTamil: String,
    val areaNameHindi: String,
    val districtCode: String,           // Foreign key reference
    val pinCode: String,
    val isRural: Boolean,
    val latitude: Double,
    val longitude: Double
)
