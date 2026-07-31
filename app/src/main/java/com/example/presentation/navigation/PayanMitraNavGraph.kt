package com.example.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.R
import com.example.presentation.screens.booking.BookingViewModel
import com.example.presentation.screens.booking.TicketBookingScreen
import com.example.presentation.screens.buslist.BusListScreen
import com.example.presentation.screens.buslist.BusListViewModel
import com.example.presentation.screens.explore.ExploreScreen
import com.example.presentation.screens.explore.ExploreViewModel
import com.example.presentation.screens.home.HomeScreen
import com.example.presentation.screens.home.HomeViewModel
import com.example.presentation.screens.language.LanguageScreen
import com.example.presentation.screens.language.LanguageViewModel
import com.example.presentation.screens.safety.SafetyViewModel
import com.example.presentation.screens.safety.WomenSafetyScreen
import com.example.presentation.screens.scanner.PayanLapScannerScreen
import com.example.presentation.screens.seats.SeatAvailabilityScreen
import com.example.presentation.screens.seats.SeatViewModel
import com.example.presentation.screens.settings.SettingsScreen
import com.example.presentation.screens.sos.SOSScreen
import com.example.presentation.screens.tickets.MyTicketsScreen
import com.example.presentation.screens.tickets.TicketsViewModel
import com.example.presentation.screens.tracking.LiveTrackingScreen
import com.example.presentation.screens.tracking.LiveTrackingViewModel
import com.example.presentation.theme.PrimaryBlue

sealed class Screen(val route: String, val titleRes: Int? = null, val icon: ImageVector? = null) {
    object Language : Screen("language")
    object Home : Screen("home", R.string.nav_home, Icons.Default.Home)
    object Explore : Screen("explore", R.string.nav_explore, Icons.Default.Map)
    object Tickets : Screen("tickets", R.string.nav_tickets, Icons.Default.ConfirmationNumber)
    object Safety : Screen("safety", R.string.nav_safety, Icons.Default.Security)
    object Settings : Screen("settings", R.string.nav_settings, Icons.Default.Settings)

    object BusList : Screen("bus_list/{areaName}") {
        fun createRoute(areaName: String) = "bus_list/$areaName"
    }
    object LiveTracking : Screen("live_tracking/{routeNumber}") {
        fun createRoute(routeNumber: String) = "live_tracking/$routeNumber"
    }
    object SeatAvailability : Screen("seats/{routeNumber}") {
        fun createRoute(routeNumber: String) = "seats/$routeNumber"
    }
    object TicketBooking : Screen("booking/{routeNumber}/{seats}") {
        fun createRoute(routeNumber: String, seats: String) = "booking/$routeNumber/$seats"
    }
    object SOS : Screen("sos")
    object PayanLapScanner : Screen("scanner")
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Explore,
    Screen.Tickets,
    Screen.Safety,
    Screen.Settings
)

@Composable
fun PayanMitraNavGraph(
    navController: NavHostController = rememberNavController(),
    homeViewModel: HomeViewModel,
    exploreViewModel: ExploreViewModel,
    busListViewModel: BusListViewModel,
    liveTrackingViewModel: LiveTrackingViewModel,
    seatViewModel: SeatViewModel,
    bookingViewModel: BookingViewModel,
    ticketsViewModel: TicketsViewModel,
    safetyViewModel: SafetyViewModel,
    languageViewModel: LanguageViewModel
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in bottomNavItems.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Color(0xEEFFFFFF),
                    contentColor = PrimaryBlue
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = currentRoute == item.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                item.icon?.let {
                                    Icon(
                                        imageVector = it,
                                        contentDescription = item.route,
                                        tint = if (selected) PrimaryBlue else Color(0xFF64748B)
                                    )
                                }
                            },
                            label = {
                                item.titleRes?.let {
                                    Text(
                                        text = stringResource(it),
                                        fontSize = 11.sp,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (selected) PrimaryBlue else Color(0xFF64748B)
                                    )
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color(0xFFE0E7FF)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Language.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Language.route) {
                LanguageScreen(
                    viewModel = languageViewModel,
                    onLanguageSelected = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Language.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = homeViewModel,
                    onNavigateToExplore = { navController.navigate(Screen.Explore.route) },
                    onNavigateToBusList = { area -> navController.navigate(Screen.BusList.createRoute(area)) },
                    onNavigateToTrack = { routeNo -> navController.navigate(Screen.LiveTracking.createRoute(routeNo)) },
                    onNavigateToBooking = { routeNo -> navController.navigate(Screen.SeatAvailability.createRoute(routeNo)) },
                    onNavigateToSOS = { navController.navigate(Screen.SOS.route) },
                    onNavigateToAreaSelect = { navController.navigate(Screen.Explore.route) }
                )
            }

            composable(Screen.Explore.route) {
                ExploreScreen(
                    viewModel = exploreViewModel,
                    onNavigateToTrack = { routeNo -> navController.navigate(Screen.LiveTracking.createRoute(routeNo)) },
                    onNavigateToSOS = { navController.navigate(Screen.SOS.route) },
                    onNavigateToAreaSelect = { }
                )
            }

            composable(Screen.Tickets.route) {
                MyTicketsScreen(
                    viewModel = ticketsViewModel,
                    onSOSClick = { navController.navigate(Screen.SOS.route) }
                )
            }

            composable(Screen.Safety.route) {
                WomenSafetyScreen(
                    viewModel = safetyViewModel,
                    onNavigateToSOS = { navController.navigate(Screen.SOS.route) }
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    onNavigateToLanguage = { navController.navigate(Screen.Language.route) },
                    onNavigateToPayanLapScanner = { navController.navigate(Screen.PayanLapScanner.route) },
                    onSOSClick = { navController.navigate(Screen.SOS.route) }
                )
            }

            composable(
                route = Screen.BusList.route,
                arguments = listOf(navArgument("areaName") { type = NavType.StringType })
            ) { backStackEntry ->
                val areaName = backStackEntry.arguments?.getString("areaName") ?: "Dindigul Central"
                BusListScreen(
                    areaName = areaName,
                    viewModel = busListViewModel,
                    onBackClick = { navController.popBackStack() },
                    onTrackClick = { routeNo -> navController.navigate(Screen.LiveTracking.createRoute(routeNo)) },
                    onBookClick = { routeNo -> navController.navigate(Screen.SeatAvailability.createRoute(routeNo)) },
                    onSOSClick = { navController.navigate(Screen.SOS.route) }
                )
            }

            composable(
                route = Screen.LiveTracking.route,
                arguments = listOf(navArgument("routeNumber") { type = NavType.StringType })
            ) { backStackEntry ->
                val routeNo = backStackEntry.arguments?.getString("routeNumber") ?: "182"
                LiveTrackingScreen(
                    routeNumber = routeNo,
                    viewModel = liveTrackingViewModel,
                    onBackClick = { navController.popBackStack() },
                    onSOSClick = { navController.navigate(Screen.SOS.route) }
                )
            }

            composable(
                route = Screen.SeatAvailability.route,
                arguments = listOf(navArgument("routeNumber") { type = NavType.StringType })
            ) { backStackEntry ->
                val routeNo = backStackEntry.arguments?.getString("routeNumber") ?: "182"
                SeatAvailabilityScreen(
                    routeNumber = routeNo,
                    viewModel = seatViewModel,
                    onBackClick = { navController.popBackStack() },
                    onProceedToBooking = { seats -> navController.navigate(Screen.TicketBooking.createRoute(routeNo, seats)) },
                    onSOSClick = { navController.navigate(Screen.SOS.route) }
                )
            }

            composable(
                route = Screen.TicketBooking.route,
                arguments = listOf(
                    navArgument("routeNumber") { type = NavType.StringType },
                    navArgument("seats") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val routeNo = backStackEntry.arguments?.getString("routeNumber") ?: "182"
                val seats = backStackEntry.arguments?.getString("seats") ?: "S12"
                TicketBookingScreen(
                    routeNumber = routeNo,
                    seatNumbers = seats,
                    viewModel = bookingViewModel,
                    onBackClick = { navController.popBackStack() },
                    onNavigateToTickets = { navController.navigate(Screen.Tickets.route) },
                    onSOSClick = { navController.navigate(Screen.SOS.route) }
                )
            }

            composable(Screen.SOS.route) {
                SOSScreen(
                    onCancel = { navController.popBackStack() }
                )
            }

            composable(Screen.PayanLapScanner.route) {
                PayanLapScannerScreen(
                    onBackClick = { navController.popBackStack() },
                    onScannedSuccess = { routeNo -> navController.navigate(Screen.SeatAvailability.createRoute(routeNo)) }
                )
            }
        }
    }
}
