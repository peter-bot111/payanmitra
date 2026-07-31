package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entities.BusStopEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BusStopDao {
    @Query("SELECT * FROM bus_stops WHERE areaCode = :areaCode")
    fun getStopsForArea(areaCode: String): Flow<List<BusStopEntity>>

    @Query("SELECT * FROM bus_stops")
    fun getAllStops(): Flow<List<BusStopEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStops(stops: List<BusStopEntity>)
}
