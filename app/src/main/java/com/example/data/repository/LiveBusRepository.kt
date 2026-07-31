package com.example.data.repository

import com.example.data.local.dao.LiveBusDao
import com.example.data.local.entities.LiveBusEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlin.random.Random

class LiveBusRepository(
    private val liveBusDao: LiveBusDao
) {
    fun getBusesForRoute(routeNumber: String): Flow<List<LiveBusEntity>> =
        liveBusDao.getBusesForRoute(routeNumber)

    fun getLiveBus(busNumber: String): Flow<LiveBusEntity?> =
        liveBusDao.getLiveBusByNumber(busNumber)

    fun getAllLiveBuses(): Flow<List<LiveBusEntity>> =
        liveBusDao.getAllLiveBuses()

    suspend fun updateBus(bus: LiveBusEntity) = liveBusDao.updateLiveBus(bus)

    // Starts live location simulation loop (moves bus coordinates slightly every 10s)
    fun startSimulation(scope: CoroutineScope) {
        scope.launch(Dispatchers.IO) {
            while (true) {
                delay(10_000)
                val currentBuses = liveBusDao.getAllLiveBuses().firstOrNull() ?: emptyList()
                for (bus in currentBuses) {
                    val latDelta = (Random.nextDouble() - 0.48) * 0.002
                    val lngDelta = (Random.nextDouble() - 0.48) * 0.002
                    val newSpeed = (35.0 + Random.nextDouble() * 20.0).coerceIn(20.0, 60.0)
                    val updatedBus = bus.copy(
                        currentLatitude = bus.currentLatitude + latDelta,
                        currentLongitude = bus.currentLongitude + lngDelta,
                        currentSpeed = newSpeed,
                        lastUpdated = System.currentTimeMillis()
                    )
                    liveBusDao.updateLiveBus(updatedBus)
                }
            }
        }
    }
}
