package com.example.data.repository

import com.example.data.local.dao.BusRouteDao
import com.example.data.local.dao.BusStopDao
import com.example.data.local.entities.BusRouteEntity
import com.example.data.local.entities.BusStopEntity
import kotlinx.coroutines.flow.Flow

class BusRouteRepository(
    private val busRouteDao: BusRouteDao,
    private val busStopDao: BusStopDao
) {
    fun getRoutesForArea(areaName: String): Flow<List<BusRouteEntity>> =
        busRouteDao.getRoutesForArea(areaName)

    fun getAllRoutes(): Flow<List<BusRouteEntity>> = busRouteDao.getAllRoutes()

    suspend fun getRouteByNumber(routeNumber: String): BusRouteEntity? =
        busRouteDao.getRouteByNumber(routeNumber)

    suspend fun getInterCityRoutes(fromCode: String, toCode: String): List<BusRouteEntity> =
        busRouteDao.getInterCityRoutes(fromCode, toCode)

    suspend fun getAllBookableRoutes(): List<BusRouteEntity> =
        busRouteDao.getAllBookableRoutes()

    fun getStopsForArea(areaCode: String): Flow<List<BusStopEntity>> =
        busStopDao.getStopsForArea(areaCode)

    fun getAllStops(): Flow<List<BusStopEntity>> = busStopDao.getAllStops()
}
