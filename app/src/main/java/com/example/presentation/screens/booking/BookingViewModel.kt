package com.example.presentation.screens.booking

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entities.TicketEntity
import com.example.data.repository.TicketRepository
import com.example.util.SMSHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class BookingViewModel(
    private val ticketRepository: TicketRepository
) : ViewModel() {

    private val _currentStep = MutableStateFlow(1) // 1: Info, 2: Payment, 3: Confirmation
    val currentStep: StateFlow<Int> = _currentStep.asStateFlow()

    private val _passengerName = MutableStateFlow("Naveen Kumar")
    val passengerName: StateFlow<String> = _passengerName.asStateFlow()

    private val _passengerPhone = MutableStateFlow("9876543210")
    val passengerPhone: StateFlow<String> = _passengerPhone.asStateFlow()

    private val _fromArea = MutableStateFlow("Dindigul Central")
    val fromArea: StateFlow<String> = _fromArea.asStateFlow()

    private val _toArea = MutableStateFlow("Palani")
    val toArea: StateFlow<String> = _toArea.asStateFlow()

    private val _passengerCount = MutableStateFlow(1)
    val passengerCount: StateFlow<Int> = _passengerCount.asStateFlow()

    private val _generatedTicket = MutableStateFlow<TicketEntity?>(null)
    val generatedTicket: StateFlow<TicketEntity?> = _generatedTicket.asStateFlow()

    fun updateName(name: String) { _passengerName.value = name }
    fun updatePhone(phone: String) { _passengerPhone.value = phone }
    fun updateToArea(to: String) { _toArea.value = to }

    fun incrementPassengers() { if (_passengerCount.value < 5) _passengerCount.value += 1 }
    fun decrementPassengers() { if (_passengerCount.value > 1) _passengerCount.value -= 1 }

    fun goToPayment() { _currentStep.value = 2 }

    fun confirmBookingAndPay(context: Context, routeNo: String, seatsStr: String) {
        viewModelScope.launch {
            val pnr = "PAYX-${SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())}-${Random.nextInt(10000, 99999)}"
            val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
            val fare = 45.0 * _passengerCount.value

            val qrData = "PNR:$pnr|ROUTE:$routeNo|FROM:${_fromArea.value}|TO:${_toArea.value}|SEATS:$seatsStr|FARE:$fare"

            val ticket = TicketEntity(
                pnr = pnr,
                routeNumber = routeNo,
                sourceArea = _fromArea.value,
                destinationArea = _toArea.value,
                journeyDate = dateStr,
                departureTime = "10:30 AM",
                busNumber = "TN 57 N 2184",
                busType = "EXPRESS",
                farePaid = fare,
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
            val smsMsg = "PayanMitra Ticket Confirmed!\nPNR: $pnr\nBus: TN 57 N 2184 (Route $routeNo)\nFrom ${_fromArea.value} to ${_toArea.value}\nSeats: $seatsStr\nFare: ₹$fare"
            SMSHelper.sendDirectSMS(context, _passengerPhone.value, smsMsg)
        }
    }
}
