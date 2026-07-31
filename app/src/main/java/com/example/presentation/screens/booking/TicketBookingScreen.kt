package com.example.presentation.screens.booking

import android.graphics.Bitmap
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.presentation.components.GlassCard
import com.example.presentation.components.SOSButton
import com.example.presentation.theme.PrimaryBlue
import com.example.presentation.theme.SoftBlueBackground
import com.example.presentation.theme.SuccessGreen
import com.example.presentation.theme.TextPrimary
import com.example.presentation.theme.TextSecondary
import com.example.util.QRCodeGenerator
import com.example.util.SMSHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketBookingScreen(
    routeNumber: String,
    seatNumbers: String,
    viewModel: BookingViewModel,
    onBackClick: () -> Unit,
    onNavigateToTickets: () -> Unit,
    onSOSClick: () -> Unit
) {
    val context = LocalContext.current
    val currentStep by viewModel.currentStep.collectAsState()
    val passengerName by viewModel.passengerName.collectAsState()
    val passengerPhone by viewModel.passengerPhone.collectAsState()
    val fromArea by viewModel.fromArea.collectAsState()
    val toArea by viewModel.toArea.collectAsState()
    val passengerCount by viewModel.passengerCount.collectAsState()
    val generatedTicket by viewModel.generatedTicket.collectAsState()

    var isSeniorCitizen by remember { mutableStateOf(false) }
    var isDivyang by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = SoftBlueBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Ticket Booking (ETMS)",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Step Progress Indicator
            StepProgressHeader(step = currentStep)

            Spacer(modifier = Modifier.height(20.dp))

            when (currentStep) {
                1 -> {
                    // STEP 1: Passenger Info
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 6.dp
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.passenger_info),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            OutlinedTextField(
                                value = passengerName,
                                onValueChange = { viewModel.updateName(it) },
                                label = { Text("Passenger Full Name") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = passengerPhone,
                                onValueChange = { viewModel.updatePhone(it) },
                                label = { Text("Mobile Number (for SMS Ticket)") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "From: $fromArea",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "To: $toArea",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = PrimaryBlue
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Passenger Stepper
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Number of Passengers", fontWeight = FontWeight.Medium)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Button(
                                        onClick = { viewModel.decrementPassengers() },
                                        shape = RoundedCornerShape(8.dp)
                                    ) { Text("-") }
                                    Text(
                                        text = "$passengerCount",
                                        modifier = Modifier.padding(horizontal = 12.dp),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Button(
                                        onClick = { viewModel.incrementPassengers() },
                                        shape = RoundedCornerShape(8.dp)
                                    ) { Text("+") }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Special needs
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = isSeniorCitizen,
                                    onCheckedChange = { isSeniorCitizen = it }
                                )
                                Text("Senior Citizen Concession", fontSize = 13.sp)
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = isDivyang,
                                    onCheckedChange = { isDivyang = it }
                                )
                                Text("Divyang Passenger Concession", fontSize = 13.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { viewModel.goToPayment() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) {
                        Text("Proceed to Payment →", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                    }
                }

                2 -> {
                    // STEP 2: Payment
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            Text(
                                text = "Payment Breakdown",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Route $routeNumber ($fromArea → $toArea)")
                                Text("₹${45 * passengerCount}")
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Selected Seat(s)")
                                Text(seatNumbers)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("GST & Service Tax")
                                Text("₹0.00 (Govt. Subsidized)")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total Fare", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("₹${45 * passengerCount}.00", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = PrimaryBlue)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // UPI QR Simulation Card
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Scan UPI QR Code to Pay", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            val qrBitmap = QRCodeGenerator.generateQRCode("upi://pay?pa=tnstc@sbi&pn=TNSTC&am=${45*passengerCount}&cu=INR")
                            qrBitmap?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = "UPI QR",
                                    modifier = Modifier.size(180.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = stringResource(R.string.ncmc_info),
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { viewModel.confirmBookingAndPay(context, routeNumber, seatNumbers) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                    ) {
                        Text("Simulate Payment Success & Book 🎫", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                    }
                }

                3 -> {
                    // STEP 3: Confirmation
                    generatedTicket?.let { ticket ->
                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = 10.dp
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Success",
                                    tint = SuccessGreen,
                                    modifier = Modifier.size(48.dp)
                                )
                                Text(
                                    text = "Ticket Confirmed!",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = SuccessGreen
                                )
                                Text(
                                    text = "PNR: ${ticket.pnr}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryBlue,
                                    modifier = Modifier.padding(top = 4.dp)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                val qrBitmap = QRCodeGenerator.generateQRCode(ticket.qrContent, 220, 220)
                                qrBitmap?.let {
                                    Image(
                                        bitmap = it.asImageBitmap(),
                                        contentDescription = "Ticket QR",
                                        modifier = Modifier.size(180.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "${ticket.sourceArea} → ${ticket.destinationArea}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "Bus: ${ticket.busNumber} | Seat: ${ticket.seatNumbers}",
                                    fontSize = 13.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    text = "Date: ${ticket.journeyDate} | Fare: ₹${ticket.farePaid.toInt()}",
                                    fontSize = 13.sp,
                                    color = TextSecondary
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            SMSHelper.openSmsApp(context, ticket.passengerPhone, "PayanMitra Ticket PNR: ${ticket.pnr}")
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(stringResource(R.string.share_sms), fontSize = 12.sp)
                                    }

                                    Button(
                                        onClick = onNavigateToTickets,
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                                    ) {
                                        Text("My Tickets", fontSize = 12.sp, color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun StepProgressHeader(step: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StepCircle(number = "1", label = "Info", active = step >= 1)
        Box(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(if (step >= 2) PrimaryBlue else Color.LightGray)
        )
        StepCircle(number = "2", label = "Payment", active = step >= 2)
        Box(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(if (step >= 3) PrimaryBlue else Color.LightGray)
        )
        StepCircle(number = "3", label = "Ticket", active = step >= 3)
    }
}

@Composable
private fun StepCircle(number: String, label: String, active: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = if (active) PrimaryBlue else Color.LightGray,
            modifier = Modifier.size(28.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = number, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
        Text(text = label, fontSize = 11.sp, color = if (active) PrimaryBlue else Color.Gray)
    }
}
