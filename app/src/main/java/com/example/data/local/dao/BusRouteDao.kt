package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entities.BusRouteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BusRouteDao {
    @Query("SELECT * FROM bus_routes WHERE sourceArea LIKE '%' || :areaName || '%' OR destinationArea LIKE '%' || :areaName || '%' OR viaStops LIKE '%' || :areaName || '%'")
    fun getRoutesForArea(areaName: String): Flow<List<BusRouteEntity>>

    @Query("SELECT * FROM bus_routes WHERE (sourceArea = :fromCode OR sourceArea LIKE '%' || :fromCode || '%') AND (destinationArea = :toCode OR destinationArea LIKE '%' || :toCode || '%') AND isBookable = 1")
    suspend fun getInterCityRoutes(fromCode: String, toCode: String): List<BusRouteEntity>

    @Query("SELECT * FROM bus_routes WHERE isBookable = 1")
    suspend fun getAllBookableRoutes(): List<BusRouteEntity>

    @Query("SELECT * FROM bus_routes WHERE routeNumber = :routeNumber")
    suspend fun getRouteByNumber(routeNumber: String): BusRouteEntity?

    @Query("SELECT * FROM bus_routes")
    fun getAllRoutes(): Flow<List<BusRouteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutes(routes: List<BusRouteEntity>)
}
