package com.example.presentation.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.components.GlassCard
import com.example.presentation.theme.PrimaryBlue
import com.example.presentation.theme.SoftBlueBackground
import com.example.presentation.theme.TextPrimary
import com.example.presentation.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchBusScreen(
    viewModel: SearchBusViewModel,
    onSearchClick: (fromLoc: String, toLoc: String, dateStr: String) -> Unit,
    onBackClick: () -> Unit
) {
    val fromLoc by viewModel.fromLocation.collectAsState()
    val toLoc by viewModel.toLocation.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val passengers by viewModel.passengerCount.collectAsState()
    val allAreas by viewModel.allAreas.collectAsState()

    var showFromDropdown by remember { mutableStateOf(false) }
    var showToDropdown by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = SoftBlueBackground,
        topBar = {
            TopAppBar(
                title = { Text("Inter-City Bus Search", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = PrimaryBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SoftBlueBackground)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Main Search Box Card
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Reserve Your Seat Across Cities",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // From Location Input
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = fromLoc,
                                onValueChange = { viewModel.setFromLocation(it) },
                                label = { Text("From (Origin City / Area)") },
                                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = PrimaryBlue) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            DropdownMenu(
                                expanded = showFromDropdown,
                                onDismissRequest = { showFromDropdown = false },
                                modifier = Modifier.fillMaxWidth(0.85f)
                            ) {
                                allAreas.forEach { area ->
                                    DropdownMenuItem(
                                        text = { Text(area.areaName) },
                                        onClick = {
                                            viewModel.setFromLocation(area.areaName)
                                            showFromDropdown = false
                                        }
                                    )
                                }
                            }
                        }

                        // Swap Icon Button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = { viewModel.swapLocations() },
                                modifier = Modifier.background(Color(0xFFE0E7FF), RoundedCornerShape(20.dp))
                            ) {
                                Icon(Icons.Default.SwapVert, contentDescription = "Swap", tint = PrimaryBlue)
                            }
                        }

                        // To Location Input
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = toLoc,
                                onValueChange = { viewModel.setToLocation(it) },
                                label = { Text("To (Destination City / Area)") },
                                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = PrimaryBlue) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            DropdownMenu(
                                expanded = showToDropdown,
                                onDismissRequest = { showToDropdown = false },
                                modifier = Modifier.fillMaxWidth(0.85f)
                            ) {
                                allAreas.forEach { area ->
                                    DropdownMenuItem(
                                        text = { Text(area.areaName) },
                                        onClick = {
                                            viewModel.setToLocation(area.areaName)
                                            showToDropdown = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Date & Passenger Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Date
                            OutlinedTextField(
                                value = selectedDate,
                                onValueChange = { viewModel.setDate(it) },
                                label = { Text("Travel Date") },
                                leadingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = PrimaryBlue) },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )

                            // Passenger Counter
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(Color.White, RoundedCornerShape(12.dp))
                                    .padding(horizontal = 8.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                                Text("-", modifier = Modifier.clickable { viewModel.decrementPassengers() }.padding(8.dp), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Text("$passengers Pax", fontWeight = FontWeight.Bold)
                                Text("+", modifier = Modifier.clickable { viewModel.incrementPassengers() }.padding(8.dp), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Search Button
                        Button(
                            onClick = { onSearchClick(fromLoc, toLoc, selectedDate) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                        ) {
                            Icon(Icons.Default.DirectionsBus, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("SEARCH BUSES", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                        }
                    }
                }
            }

            // Popular Inter-city Routes
            item {
                Text("Popular Inter-City Routes", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))

                val popular = listOf(
                    Pair("Chennai Koyambedu", "Madurai Mattuthavani"),
                    Pair("Coimbatore Gandhipuram", "Madurai Mattuthavani"),
                    Pair("Bengaluru Majestic", "Chennai Koyambedu"),
                    Pair("Dindigul Central", "Palani")
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    popular.forEach { (src, dest) ->
                        GlassCard(
                            modifier = Modifier.clickable {
                                viewModel.setFromLocation(src)
                                viewModel.setToLocation(dest)
                            }
                        ) {
                            Text(
                                text = "$src ➔ $dest",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = PrimaryBlue,
                                modifier = Modifier.padding(2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
