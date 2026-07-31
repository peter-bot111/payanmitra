package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.data.local.PayanMitraDatabase
import com.example.data.prepopulate.DatabasePrepopulator
import com.example.data.repository.BusRouteRepository
import com.example.data.repository.LiveBusRepository
import com.example.data.repository.StateRepository
import com.example.data.repository.TicketRepository
import com.example.presentation.navigation.PayanMitraNavGraph
import com.example.presentation.screens.booking.BookingViewModel
import com.example.presentation.screens.buslist.BusListViewModel
import com.example.presentation.screens.explore.ExploreViewModel
import com.example.presentation.screens.home.HomeViewModel
import com.example.presentation.screens.language.LanguageViewModel
import com.example.presentation.screens.safety.SafetyViewModel
import com.example.presentation.screens.seats.SeatViewModel
import com.example.presentation.screens.tickets.TicketsViewModel
import com.example.presentation.screens.tracking.LiveTrackingViewModel
import com.example.presentation.theme.PayanMitraTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Database & Prepopulation
        val db = PayanMitraDatabase.getDatabase(this)

        lifecycleScope.launch {
            DatabasePrepopulator.prepopulate(db)
        }

        // 2. Repositories
        val stateRepository = StateRepository(db.stateDao(), db.districtDao(), db.areaDao())
        val busRouteRepository = BusRouteRepository(db.busRouteDao(), db.busStopDao())
        val liveBusRepository = LiveBusRepository(db.liveBusDao())
        val ticketRepository = TicketRepository(db.ticketDao())

        // 3. Start live bus tracking simulation
        liveBusRepository.startSimulation(lifecycleScope)

        // 4. ViewModels
        val homeViewModel = HomeViewModel(stateRepository, busRouteRepository)
        val exploreViewModel = ExploreViewModel(busRouteRepository, liveBusRepository)
        val busListViewModel = BusListViewModel(busRouteRepository)
        val liveTrackingViewModel = LiveTrackingViewModel(liveBusRepository, busRouteRepository)
        val seatViewModel = SeatViewModel()
        val bookingViewModel = BookingViewModel(ticketRepository, busRouteRepository)
        val ticketsViewModel = TicketsViewModel(ticketRepository)
        val safetyViewModel = SafetyViewModel()
        val languageViewModel = LanguageViewModel()
        val areaSelectViewModel = com.example.presentation.screens.area.AreaSelectViewModel(stateRepository)
        val searchBusViewModel = com.example.presentation.screens.search.SearchBusViewModel(stateRepository)
        val busResultsViewModel = com.example.presentation.screens.search.BusResultsViewModel(busRouteRepository)

        setContent {
            PayanMitraTheme {
                PayanMitraNavGraph(
                    homeViewModel = homeViewModel,
                    exploreViewModel = exploreViewModel,
                    busListViewModel = busListViewModel,
                    liveTrackingViewModel = liveTrackingViewModel,
                    seatViewModel = seatViewModel,
                    bookingViewModel = bookingViewModel,
                    ticketsViewModel = ticketsViewModel,
                    safetyViewModel = safetyViewModel,
                    languageViewModel = languageViewModel,
                    areaSelectViewModel = areaSelectViewModel,
                    searchBusViewModel = searchBusViewModel,
                    busResultsViewModel = busResultsViewModel
                )
            }
        }
    }
}
