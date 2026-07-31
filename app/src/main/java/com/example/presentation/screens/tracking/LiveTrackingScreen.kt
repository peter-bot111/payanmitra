package com.example.presentation.screens.tracking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.presentation.components.GlassCard
import com.example.presentation.components.SOSButton
import com.example.presentation.theme.ErrorRed
import com.example.presentation.theme.PrimaryBlue
import com.example.presentation.theme.SoftBlueBackground
import com.example.presentation.theme.SuccessGreen
import com.example.presentation.theme.TextPrimary
import com.example.presentation.theme.TextSecondary
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveTrackingScreen(
    routeNumber: String,
    viewModel: LiveTrackingViewModel,
    onBackClick: () -> Unit,
    onSOSClick: () -> Unit
) {
    val liveBus by viewModel.liveBus.collectAsState()
    val route by viewModel.route.collectAsState()

    LaunchedEffect(routeNumber) {
        viewModel.startTracking(routeNumber)
    }

    val defaultPos = LatLng(10.3673, 77.9803)
    val busPos = liveBus?.let { LatLng(it.currentLatitude, it.currentLongitude) } ?: defaultPos

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(busPos, 14f)
    }

    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 220.dp,
        sheetContainerColor = Color.White,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.upcoming_stops),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Stop 1 - Passed
                StopTimelineItem(
                    status = "PASSED",
                    stopName = "Dindigul Central Stand",
                    timeStr = "10:05 AM"
                )

                // Stop 2 - Current
                StopTimelineItem(
                    status = "CURRENT",
                    stopName = "Reddiyarchatram Four Road",
                    timeStr = "10:20 AM (Near National Highway)"
                )

                // Stop 3 - Upcoming
                StopTimelineItem(
                    status = "UPCOMING",
                    stopName = "Oddanchatram Bye-pass",
                    timeStr = "Est. 10:38 AM"
                )

                // Stop 4 - Final
                StopTimelineItem(
                    status = "UPCOMING",
                    stopName = "Palani Temple Gate",
                    timeStr = "Est. 11:05 AM"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // AIS-140 compliance banner
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFEFF6FF), RoundedCornerShape(10.dp))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.ais_140_notice),
                        fontSize = 12.sp,
                        color = PrimaryBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "Route $routeNumber Live Tracking",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            route?.let {
                                Text(
                                    text = "${it.sourceArea} → ${it.destinationArea}",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = PrimaryBlue
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = onSOSClick) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Report",
                                tint = ErrorRed
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = SoftBlueBackground)
                )
            },
            floatingActionButton = {
                SOSButton(onClick = onSOSClick)
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Map View
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    // Live Bus Marker
                    Marker(
                        state = MarkerState(position = busPos),
                        title = "🚌 Bus ${liveBus?.busNumber ?: ""}",
                        snippet = "Driver: ${liveBus?.driverName ?: ""}"
                    )

                    // Polyline for route
                    Polyline(
                        points = listOf(
                            LatLng(10.3673, 77.9803),
                            LatLng(10.4167, 77.9167),
                            LatLng(10.4833, 77.7500),
                            LatLng(10.4500, 77.5200)
                        ),
                        color = PrimaryBlue,
                        width = 8f
                    )
                }

                // Top Bus Info & Speedometer Card
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = liveBus?.busNumber ?: "TN 57 N 2184",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Driver: ${liveBus?.driverName ?: "M. Selvam"}",
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                        }

                        // Speedometer Badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(PrimaryBlue, RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${liveBus?.currentSpeed?.toInt() ?: 48} km/h",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StopTimelineItem(status: String, stopName: String, timeStr: String) {
    val (icon, color) = when (status) {
        "PASSED" -> Pair(Icons.Default.CheckCircle, SuccessGreen)
        "CURRENT" -> Pair(Icons.Default.DirectionsBus, PrimaryBlue)
        else -> Pair(Icons.Default.LocationOn, Color(0xFF94A3B8))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = stopName,
                fontWeight = if (status == "CURRENT") FontWeight.Bold else FontWeight.Medium,
                fontSize = 14.sp,
                color = if (status == "CURRENT") PrimaryBlue else TextPrimary
            )
            Text(
                text = timeStr,
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
    }
}
