package com.example.presentation.screens.booking

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entities.BusRouteEntity
import com.example.data.local.entities.TicketEntity
import com.example.data.repository.BusRouteRepository
import com.example.data.repository.TicketRepository
import com.example.util.SMSHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class BookingViewModel(
    private val ticketRepository: TicketRepository,
    private val busRouteRepository: BusRouteRepository
) : ViewModel() {

    private val _currentStep = MutableStateFlow(1) // 1: Info, 2: Payment, 3: Confirmation
    val currentStep: StateFlow<Int> = _currentStep.asStateFlow()

    private val _passengerName = MutableStateFlow("Naveen Kumar")
    val passengerName: StateFlow<String> = _passengerName.asStateFlow()

    private val _passengerPhone = MutableStateFlow("9876543210")
    val passengerPhone: StateFlow<String> = _passengerPhone.asStateFlow()

    private val _selectedRoute = MutableStateFlow<BusRouteEntity?>(null)
    val selectedRoute: StateFlow<BusRouteEntity?> = _selectedRoute.asStateFlow()

    private val _passengerCount = MutableStateFlow(1)
    val passengerCount: StateFlow<Int> = _passengerCount.asStateFlow()

    private val _isSeniorCitizen = MutableStateFlow(false)
    val isSeniorCitizen: StateFlow<Boolean> = _isSeniorCitizen.asStateFlow()

    private val _isDivyang = MutableStateFlow(false)
    val isDivyang: StateFlow<Boolean> = _isDivyang.asStateFlow()

    private val _selectedDate = MutableStateFlow(
        SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(Date())
    )
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    private val _generatedTicket = MutableStateFlow<TicketEntity?>(null)
    val generatedTicket: StateFlow<TicketEntity?> = _generatedTicket.asStateFlow()

    val calculatedFare: StateFlow<Double> = combine(
        _selectedRoute, _passengerCount, _isSeniorCitizen, _isDivyang
    ) { route, count, senior, divyang ->
        val base = route?.fareAmount ?: 45.0
        val discount = when {
            divyang -> 0.50
            senior -> 0.25
            else -> 0.0
        }
        base * count * (1.0 - discount)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 45.0)

    fun loadRoute(routeNumber: String) {
        viewModelScope.launch {
            val route = busRouteRepository.getRouteByNumber(routeNumber)
            _selectedRoute.value = route
        }
    }

    fun setSeniorCitizen(value: Boolean) { _isSeniorCitizen.value = value }
    fun setDivyang(value: Boolean) { _isDivyang.value = value }
    fun setDate(date: String) { _selectedDate.value = date }
    fun updateName(name: String) { _passengerName.value = name }
    fun updatePhone(phone: String) { _passengerPhone.value = phone }

    fun incrementPassengers() { if (_passengerCount.value < 5) _passengerCount.value += 1 }
    fun decrementPassengers() { if (_passengerCount.value > 1) _passengerCount.value -= 1 }

    fun goToPayment() { _currentStep.value = 2 }

    fun confirmBookingAndPay(context: Context, routeNo: String, seatsStr: String) {
        viewModelScope.launch {
            val route = _selectedRoute.value
            val source = route?.sourceArea ?: "Dindigul Central"
            val dest = route?.destinationArea ?: "Palani"
            val depTime = route?.firstBusTime ?: "10:30 AM"
            val busNo = route?.let { "${it.corporation} $routeNo" } ?: "TN 57 N 2184"
            val busType = route?.busType ?: "EXPRESS"
            val totalFare = calculatedFare.value

            val pnr = "PAYX-${SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())}-${Random.nextInt(10000, 99999)}"
            val dateStr = _selectedDate.value

            val qrData = "PNR:$pnr|ROUTE:$routeNo|FROM:$source|TO:$dest|SEATS:$seatsStr|FARE:$totalFare"

            val ticket = TicketEntity(
                pnr = pnr,
                routeNumber = routeNo,
                sourceArea = source,
                destinationArea = dest,
                journeyDate = dateStr,
                departureTime = depTime,
                busNumber = busNo,
                busType = busType,
                farePaid = totalFare,
                seatNumbers = seatsStr,
                passengerName = _passengerName.value,
                passengerPhone = _passengerPhone.value,
                ticketStatus = "CONFIRMED",
                qrContent = qrData,
                bookedTimestamp = System.currentTimeMillis()
            )

            ticketRepository.bookTicket(ticket)
            _generatedTicket.value = ticket
            _currentStep.value = 3

            // Auto-SMS confirmation to registered phone
            val smsMsg = "PayanMitra Ticket Confirmed!\nPNR: $pnr\nBus: $busNo (Route $routeNo)\nFrom $source to $dest\nDate: $dateStr | Seats: $seatsStr\nFare: ₹$totalFare"
            SMSHelper.sendDirectSMS(context, _passengerPhone.value, smsMsg)
        }
    }
}
