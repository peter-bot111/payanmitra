package com.example.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirlineSeatReclineExtra
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.theme.AccentLavender
import com.example.presentation.theme.AccentViolet
import com.example.presentation.theme.ErrorRed
import com.example.presentation.theme.PrimaryBlue
import com.example.presentation.theme.PrimaryLightBlue
import com.example.presentation.theme.SuccessGreen
import com.example.presentation.theme.WarningAmber

enum class SeatType {
    AVAILABLE,
    OCCUPIED,
    WOMEN_RESERVED,
    SENIOR_RESERVED,
    SELECTED
}

data class Seat(
    val id: String,
    val row: Int,
    val column: Int,
    var type: SeatType
)

@Composable
fun SeatGrid(
    selectedSeats: List<String>,
    onSeatToggled: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Driver Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFFCBD5E1), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DirectionsBus,
                        contentDescription = "Driver",
                        tint = Color(0xFF334155),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "DRIVER",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF334155)
                    )
                }
            }
        }

        // 2+2 Seats Rows (Rows 1 to 10)
        for (row in 1..10) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left 2 seats
                Row {
                    val seatId1 = "S${(row - 1) * 4 + 1}"
                    val seatId2 = "S${(row - 1) * 4 + 2}"
                    SeatItem(
                        seatId = seatId1,
                        row = row,
                        isSelected = selectedSeats.contains(seatId1),
                        onSeatToggled = onSeatToggled
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    SeatItem(
                        seatId = seatId2,
                        row = row,
                        isSelected = selectedSeats.contains(seatId2),
                        onSeatToggled = onSeatToggled
                    )
                }

                // Aisle Space
                Spacer(modifier = Modifier.width(32.dp))

                // Right 2 seats
                Row {
                    val seatId3 = "S${(row - 1) * 4 + 3}"
                    val seatId4 = "S${(row - 1) * 4 + 4}"
                    SeatItem(
                        seatId = seatId3,
                        row = row,
                        isSelected = selectedSeats.contains(seatId3),
                        onSeatToggled = onSeatToggled
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    SeatItem(
                        seatId = seatId4,
                        row = row,
                        isSelected = selectedSeats.contains(seatId4),
                        onSeatToggled = onSeatToggled
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Legend
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Text(
                text = "Seat Color Legend",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color(0xFF334155)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                LegendBadge(color = SuccessGreen, label = "Available")
                LegendBadge(color = ErrorRed, label = "Occupied")
                LegendBadge(color = WarningAmber, label = "Women")
                LegendBadge(color = PrimaryBlue, label = "Senior")
            }
        }
    }
}

@Composable
private fun SeatItem(
    seatId: String,
    row: Int,
    isSelected: Boolean,
    onSeatToggled: (String) -> Unit
) {
    // Determine seat state
    val isWomen = row in 1..2
    val isSenior = row in 3..3
    val isOccupied = (seatId.hashCode() % 5 == 0)

    val bgColor = when {
        isSelected -> AccentViolet
        isOccupied -> ErrorRed
        isWomen -> WarningAmber
        isSenior -> PrimaryBlue
        else -> SuccessGreen
    }

    Box(
        modifier = Modifier
            .size(42.dp)
            .background(
                color = if (isSelected) AccentLavender else bgColor.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 2.dp,
                color = bgColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(enabled = !isOccupied) {
                onSeatToggled(seatId)
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.AirlineSeatReclineExtra,
                contentDescription = seatId,
                tint = bgColor,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = seatId,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = bgColor
            )
        }
    }
}

@Composable
private fun LegendBadge(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, RoundedCornerShape(3.dp))
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 11.sp, color = Color(0xFF475569))
    }
}
