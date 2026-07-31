package com.example.presentation.screens.buslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.presentation.components.BusCard
import com.example.presentation.components.GlassCard
import com.example.presentation.components.SOSButton
import com.example.presentation.theme.PrimaryBlue
import com.example.presentation.theme.SoftBlueBackground
import com.example.presentation.theme.TextPrimary
import com.example.presentation.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusListScreen(
    areaName: String,
    viewModel: BusListViewModel,
    onBackClick: () -> Unit,
    onTrackClick: (String) -> Unit,
    onBookClick: (String) -> Unit,
    onSOSClick: () -> Unit
) {
    val busRoutes by viewModel.busRoutes.collectAsState()
    val filterType by viewModel.filter.collectAsState()

    LaunchedEffect(areaName) {
        viewModel.loadRoutesForArea(areaName)
    }

    val filteredList = busRoutes.filter { route ->
        when (filterType) {
            "EXPRESS" -> route.busType.contains("EXPRESS", ignoreCase = true)
            "ORDINARY" -> route.busType.contains("ORDINARY", ignoreCase = true)
            "LADIES" -> route.corporation.contains("Ladies", ignoreCase = true) || route.busType.contains("LADIES", ignoreCase = true)
            else -> true
        }
    }

    Scaffold(
        containerColor = SoftBlueBackground,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(R.string.buses_in, areaName),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${filteredList.size} buses available",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SoftBlueBackground)
            )
        },
        floatingActionButton = {
            SOSButton(onClick = onSOSClick)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Filter Chips Bar
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filterOptions = listOf("ALL", "EXPRESS", "ORDINARY", "LADIES")
                items(filterOptions) { option ->
                    val isSelected = filterType == option
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) PrimaryBlue else Color.White,
                        shadowElevation = 2.dp,
                        modifier = Modifier.clickable { viewModel.setFilter(option) }
                    ) {
                        Text(
                            text = when (option) {
                                "ALL" -> stringResource(R.string.all_buses)
                                "EXPRESS" -> stringResource(R.string.filter_express)
                                "ORDINARY" -> stringResource(R.string.filter_ordinary)
                                else -> stringResource(R.string.filter_ladies)
                            },
                            color = if (isSelected) Color.White else TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(text = "🚌", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No buses found for current filter",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Try changing your filter options above.",
                                fontSize = 13.sp,
                                color = TextSecondary,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredList) { route ->
                        BusCard(
                            route = route,
                            availableSeats = 28,
                            onTrackClick = { onTrackClick(route.routeNumber) },
                            onBookClick = { onBookClick(route.routeNumber) }
                        )
                    }
                }
            }
        }
    }
}
