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

    suspend fun getLiveBusForRoute(routeNumber: String): LiveBusEntity? {
        val bus = liveBusDao.getLiveBusForRoute(routeNumber)
        if (bus != null) return bus
        val totalSeats = 52
        val occupied = Random.nextInt(15, 35)
        val newBus = LiveBusEntity(
            busNumber = "TN ${Random.nextInt(10, 99)} N ${Random.nextInt(1000, 9999)}",
            routeNumber = routeNumber,
            currentLatitude = 10.3673 + (Random.nextDouble() - 0.5) * 0.1,
            currentLongitude = 77.9803 + (Random.nextDouble() - 0.5) * 0.1,
            currentSpeed = Random.nextDouble(30.0, 60.0),
            totalSeats = totalSeats,
            occupiedSeats = occupied,
            availableSeats = totalSeats - occupied,
            driverName = listOf("M. Selvam", "K. Raman", "R. Murugan", "S. Kumar").random(),
            driverPhone = "98${Random.nextInt(10000000, 99999999)}",
            busStatus = listOf("ON_TIME", "ON_TIME", "ON_TIME", "DELAYED").random(),
            delayMinutes = if (Random.nextInt(0, 4) == 0) Random.nextInt(5, 20) else 0,
            lastUpdated = System.currentTimeMillis(),
            hasAIS140 = true,
            isPanicActive = false
        )
        liveBusDao.insertOrUpdate(newBus)
        return newBus
    }

    fun observeLiveBusForRoute(routeNumber: String): Flow<LiveBusEntity?> =
        liveBusDao.observeLiveBusForRoute(routeNumber)

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
