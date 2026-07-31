package com.example.presentation.screens.busdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entities.BusRouteEntity
import com.example.data.local.entities.LiveBusEntity
import com.example.data.repository.BusRouteRepository
import com.example.data.repository.LiveBusRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

data class PassengerEvent(
    val isBoarding: Boolean,
    val stopName: String,
    val timeAgo: String
)

data class RouteStop(
    val stopName: String,
    val eta: String,
    val isPassed: Boolean,
    val isCurrent: Boolean,
    val latitude: Double,
    val longitude: Double
)

class BusDetailViewModel(
    private val busRouteRepository: BusRouteRepository,
    private val liveBusRepository: LiveBusRepository
) : ViewModel() {

    val route = MutableStateFlow<BusRouteEntity?>(null)
    val liveBus = MutableStateFlow<LiveBusEntity?>(null)
    val availableSeats = MutableStateFlow(30)
    val busLat = MutableStateFlow(10.3673)
    val busLon = MutableStateFlow(77.9803)
    val routeProgress = MutableStateFlow(0.3f)
    val recentEvents = MutableStateFlow<List<PassengerEvent>>(emptyList())
    val routeStops = MutableStateFlow<List<RouteStop>>(emptyList())
    val seatStates = MutableStateFlow<Map<Int, String>>(emptyMap())

    private val routeWaypoints = mutableListOf<Pair<Double, Double>>()
    private var currentWaypointIndex = 0

    fun loadBusDetail(routeNumber: String) {
        viewModelScope.launch {
            val busRoute = busRouteRepository.getRouteByNumber(routeNumber)
            route.value = busRoute

            val bus = liveBusRepository.getLiveBusForRoute(routeNumber)
            liveBus.value = bus
            availableSeats.value = bus?.availableSeats ?: 30

            initializeRouteWaypoints(busRoute)

            val seats = mutableMapOf<Int, String>()
            val totalSeats = bus?.totalSeats ?: 52
            val occupiedCount = bus?.occupiedSeats ?: 22
            repeat(totalSeats) { index ->
                seats[index] = when {
                    index < 6 -> "LADIES"
                    index < occupiedCount + 6 -> "OCCUPIED"
                    else -> "AVAILABLE"
                }
            }
            seatStates.value = seats

            startBusMovementSimulation()
        }
    }

    private fun initializeRouteWaypoints(busRoute: BusRouteEntity?) {
        val sourceLat = 10.3673
        val sourceLon = 77.9803
        val destLat = 10.4500
        val destLon = 77.5200

        val numPoints = 10
        routeWaypoints.clear()
        repeat(numPoints + 1) { i ->
            val fraction = i.toFloat() / numPoints
            val lat = sourceLat + (destLat - sourceLat) * fraction
            val lon = sourceLon + (destLon - sourceLon) * fraction
            val jitter = (Random.nextDouble() - 0.5) * 0.005
            routeWaypoints.add(Pair(lat + jitter, lon + jitter))
        }

        currentWaypointIndex = (routeWaypoints.size * 0.3f).toInt()
        val current = routeWaypoints[currentWaypointIndex]
        busLat.value = current.first
        busLon.value = current.second

        val stopNames = listOf(
            busRoute?.sourceArea ?: "Starting Hub",
            "Reddiyarchatram",
            "Oddanchatram Central",
            "Sathirappatti",
            busRoute?.destinationArea ?: "Destination Terminus"
        )

        routeStops.value = stopNames.mapIndexed { i, name ->
            RouteStop(
                stopName = name,
                eta = if (i < 2) "Departed" else "+${(i - 1) * 12} min",
                isPassed = i < 2,
                isCurrent = i == 2,
                latitude = routeWaypoints.getOrElse(i * 2) { routeWaypoints.last() }.first,
                longitude = routeWaypoints.getOrElse(i * 2) { routeWaypoints.last() }.second
            )
        }
    }

    private fun startBusMovementSimulation() {
        viewModelScope.launch {
            var simSpeed = 50.0
            var trafficFactor = 1.0

            while (true) {
                delay(2500L)

                if (Random.nextDouble() < 0.2) {
                    trafficFactor = if (Random.nextDouble() > 0.7) 1.0 else if (Random.nextDouble() > 0.4) 0.6 else 0.3
                }

                val targetSpeed = 65.0 * trafficFactor
                simSpeed += (targetSpeed - simSpeed) * 0.2
                liveBus.value = liveBus.value?.copy(
                    currentSpeed = simSpeed,
                    busStatus = when {
                        simSpeed > 50 -> "Traffic: Clear (On Time)"
                        simSpeed > 25 -> "Traffic: Moderate"
                        else -> "Traffic: Heavy Congestion"
                    }
                )

                if (currentWaypointIndex < routeWaypoints.size - 1) {
                    val current = routeWaypoints[currentWaypointIndex]
                    val next = routeWaypoints[currentWaypointIndex + 1]

                    val progressInc = (simSpeed / 3600.0) * 0.8
                    val currentProgress = (routeProgress.value + progressInc).coerceIn(0.0, 1.0).toFloat()

                    if (currentProgress >= 1f) {
                        currentWaypointIndex++
                        routeProgress.value = 0f

                        val maxSeats = liveBus.value?.totalSeats ?: 52
                        val currentAvail = availableSeats.value

                        val seatChange = Random.nextInt(-3, 4)
                        val newAvail = (currentAvail + seatChange).coerceIn(2, maxSeats - 5)

                        availableSeats.value = newAvail
                        liveBus.value = liveBus.value?.copy(
                            occupiedSeats = maxSeats - newAvail,
                            availableSeats = newAvail
                        )

                        val stopName = routeStops.value.getOrNull(currentWaypointIndex / 2)?.stopName ?: "Intermediate Stop"
                        val event = PassengerEvent(
                            isBoarding = seatChange < 0,
                            stopName = stopName,
                            timeAgo = "Just now"
                        )
                        recentEvents.value = (listOf(event) + recentEvents.value).take(8)

                        val updatedStops = routeStops.value.mapIndexed { i, stop ->
                            stop.copy(
                                isPassed = i < currentWaypointIndex / 2,
                                isCurrent = i == currentWaypointIndex / 2
                            )
                        }
                        routeStops.value = updatedStops
                    } else {
                        routeProgress.value = currentProgress
                        val newLat = current.first + (next.first - current.first) * currentProgress
                        val newLon = current.second + (next.second - current.second) * currentProgress
                        busLat.value = newLat
                        busLon.value = newLon
                    }
                } else {
                    currentWaypointIndex = 0
                    routeProgress.value = 0f
                }
            }
        }
    }
}
