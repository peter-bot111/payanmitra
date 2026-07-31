package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entities.LiveBusEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LiveBusDao {
    @Query("SELECT * FROM live_bus WHERE routeNumber = :routeNumber LIMIT 1")
    suspend fun getLiveBusForRoute(routeNumber: String): LiveBusEntity?

    @Query("SELECT * FROM live_bus WHERE routeNumber = :routeNumber LIMIT 1")
    fun observeLiveBusForRoute(routeNumber: String): Flow<LiveBusEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(bus: LiveBusEntity)

    @Query("SELECT * FROM live_bus WHERE routeNumber = :routeNumber")
    fun getBusesForRoute(routeNumber: String): Flow<List<LiveBusEntity>>

    @Query("SELECT * FROM live_bus WHERE busNumber = :busNumber")
    fun getLiveBusByNumber(busNumber: String): Flow<LiveBusEntity?>

    @Query("SELECT * FROM live_bus")
    fun getAllLiveBuses(): Flow<List<LiveBusEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLiveBuses(buses: List<LiveBusEntity>)

    @Update
    suspend fun updateLiveBus(bus: LiveBusEntity)
}
