package com.example.presentation.screens.busdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.components.AnimatedBusMap
import com.example.presentation.components.GlassCard
import com.example.presentation.components.InfoRow
import com.example.presentation.theme.PrimaryBlue
import com.example.presentation.theme.SoftBlueBackground
import com.example.presentation.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusDetailScreen(
    routeNumber: String,
    viewModel: BusDetailViewModel,
    onNavigateToSeats: (routeNumber: String) -> Unit,
    onNavigateToBooking: (routeNumber: String) -> Unit,
    onNavigateToSOS: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val route by viewModel.route.collectAsState()
    val liveBus by viewModel.liveBus.collectAsState()
    val availableSeats by viewModel.availableSeats.collectAsState()
    val busLat by viewModel.busLat.collectAsState()
    val busLon by viewModel.busLon.collectAsState()
    val busProgress by viewModel.routeProgress.collectAsState()
    val recentEvents by viewModel.recentEvents.collectAsState()
    val routeStops by viewModel.routeStops.collectAsState()
    val seatStates by viewModel.seatStates.collectAsState()

    LaunchedEffect(routeNumber) {
        viewModel.loadBusDetail(routeNumber)
    }

    Scaffold(
        containerColor = SoftBlueBackground,
        topBar = {
            TopAppBar(
                title = { Text("Route $routeNumber Detail", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = PrimaryBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SoftBlueBackground)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToSOS,
                containerColor = Color(0xFFDC2626)
            ) {
                Icon(Icons.Default.Emergency, contentDescription = "SOS", tint = Color.White)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding)
        ) {
            // SECTION 1: ANIMATED MAP
            item {
                Text(
                    text = "🗺️ Live Bus Location & Route Animation",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp),
                    color = TextPrimary
                )
                AnimatedBusMap(
                    busLat = busLat,
                    busLon = busLon,
                    routeWaypoints = emptyList(),
                    sourceCoord = Pair(10.3673, 77.9803),
                    destCoord = Pair(10.4500, 77.5200),
                    busProgress = busProgress,
                    busNumber = liveBus?.busNumber ?: routeNumber,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
            }

            // SECTION 2: LIVE SEAT AVAILABILITY
            item {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Available Seats (Live)",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "$availableSeats",
                                    style = MaterialTheme.typography.displaySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = when {
                                        availableSeats > 15 -> Color(0xFF059669)
                                        availableSeats in 5..15 -> Color(0xFFD97706)
                                        else -> Color(0xFFDC2626)
                                    }
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    "Total Capacity: ${liveBus?.totalSeats ?: 52}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    "Occupied: ${liveBus?.occupiedSeats ?: 22}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        val totalSeats = (liveBus?.totalSeats ?: 52).toFloat()
                        val occupiedSeats = (liveBus?.occupiedSeats ?: 22).toFloat()
                        val occupancy = (occupiedSeats / totalSeats).coerceIn(0f, 1f)

                        LinearProgressIndicator(
                            progress = { occupancy },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            color = when {
                                occupancy < 0.5f -> Color(0xFF059669)
                                occupancy < 0.85f -> Color(0xFFD97706)
                                else -> Color(0xFFDC2626)
                            },
                            trackColor = Color(0xFFE2E8F0)
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "${(occupancy * 100).toInt()}% Bus Full • ${liveBus?.busStatus ?: "On Time"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // SECTION 3: MINI SEAT GRID VISUALIZER
            item {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "Live Seat Map Overview",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(5),
                            modifier = Modifier.height(140.dp),
                            userScrollEnabled = false
                        ) {
                            items(52) { index ->
                                val state = seatStates[index] ?: "AVAILABLE"
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .padding(2.dp)
                                        .background(
                                            when (state) {
                                                "OCCUPIED" -> Color(0xFFDC2626)
                                                "LADIES" -> Color(0xFFEC4899)
                                                "SELECTED" -> Color(0xFF7C3AED)
                                                else -> Color(0xFF059669)
                                            },
                                            RoundedCornerShape(4.dp)
                                        )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Legend Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            listOf(
                                Pair("Available", Color(0xFF059669)),
                                Pair("Occupied", Color(0xFFDC2626)),
                                Pair("Ladies", Color(0xFFEC4899))
                            ).forEach { (label, color) ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .background(color, CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(label, style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { onNavigateToSeats(routeNumber) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("SELECT YOUR SEAT →", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            // SECTION 4: RECENT PASSENGER EVENTS FEED
            item {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "🔴 Real-Time Passenger Activity",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        if (recentEvents.isEmpty()) {
                            Text(
                                "Waiting for passenger entry/exit events...",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else {
                            recentEvents.take(5).forEach { event ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (event.isBoarding) Icons.AutoMirrored.Filled.Login else Icons.AutoMirrored.Filled.Logout,
                                        contentDescription = null,
                                        tint = if (event.isBoarding) Color(0xFF059669) else Color(0xFFDC2626),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "${if (event.isBoarding) "Boarded" else "Alighted"} at ${event.stopName}",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        event.timeAgo,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // SECTION 5: BUS INFO DETAILS
            item {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "Bus Specification & Timings",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        route?.let { r ->
                            InfoRow("Route Number", r.routeNumber)
                            InfoRow("Corporation", r.corporation)
                            InfoRow("Bus Type", r.busType.replace("_", " "))
                            InfoRow("Total Distance", "${r.totalDistance} km")
                            InfoRow("Journey Duration", "${r.journeyDuration / 60}h ${r.journeyDuration % 60}m")
                            InfoRow("First Departure", r.firstBusTime)
                            InfoRow("Last Departure", r.lastBusTime)
                            InfoRow("Service Frequency", r.frequency)
                            InfoRow("Operating Days", r.operatingDays)
                            InfoRow("Fare Amount", "₹${r.fareAmount.toInt()}")
                        }
                    }
                }
            }

            // SECTION 6: STOPS TIMELINE
            item {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "Route Timeline & ETAs",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        routeStops.forEachIndexed { index, stop ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.width(28.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .background(
                                                when {
                                                    index == 0 -> PrimaryBlue
                                                    index == routeStops.lastIndex -> Color(0xFFDC2626)
                                                    stop.isPassed -> Color(0xFF94A3B8)
                                                    stop.isCurrent -> Color(0xFF059669)
                                                    else -> Color(0xFFCBD5E1)
                                                },
                                                CircleShape
                                            )
                                    )
                                    if (index < routeStops.lastIndex) {
                                        Box(
                                            modifier = Modifier
                                                .width(2.dp)
                                                .height(30.dp)
                                                .background(Color(0xFFCBD5E1))
                                        )
                                    }
                                }

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 8.dp, bottom = 12.dp)
                                ) {
                                    Text(
                                        text = stop.stopName,
                                        fontWeight = if (stop.isCurrent) FontWeight.Bold else FontWeight.Normal,
                                        color = if (stop.isPassed) Color.Gray else TextPrimary
                                    )
                                    if (stop.eta.isNotEmpty()) {
                                        Text(
                                            text = "ETA: ${stop.eta}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = PrimaryBlue
                                        )
                                    }
                                }

                                if (stop.isCurrent) {
                                    Text(
                                        text = "● BUS HERE",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFF059669),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // SECTION 7: DRIVER INFO
            item {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(PrimaryBlue.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryBlue)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                liveBus?.driverName ?: "M. Selvam (Driver)",
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Text(
                                "Verified Government Driver • AIS-140 GPS Active",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.Call, contentDescription = "Call Driver", tint = Color(0xFF059669))
                        }
                    }
                }
            }

            // Bottom Actions
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { onNavigateToSeats(routeNumber) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Seat Grid")
                    }
                    Button(
                        onClick = { onNavigateToBooking(routeNumber) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Book Ticket →", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}
