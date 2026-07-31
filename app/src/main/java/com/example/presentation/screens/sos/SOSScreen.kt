package com.example.presentation.screens.sos

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.presentation.theme.ErrorRed
import com.example.util.SMSHelper
import kotlinx.coroutines.delay

@Composable
fun SOSScreen(
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    var countdown by remember { mutableIntStateOf(5) }
    var isCancelled by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (countdown > 0 && isCancelled == 0) {
            delay(1000)
            countdown -= 1
        }
        if (countdown == 0 && isCancelled == 0) {
            // Trigger SOS
            triggerEmergencyActions(context)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1B4B))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "🚨", fontSize = 64.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "EMERGENCY SOS ACTIVATED",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = ErrorRed
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (countdown > 0) {
                Text(
                    text = stringResource(R.string.sos_sending_in, countdown),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        isCancelled = 1
                        onCancel()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(
                        text = stringResource(R.string.cancel_sos),
                        color = ErrorRed,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            } else {
                Text(
                    text = "SOS Alert Sent! Calling 112 Emergency...",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Green
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Return to App", color = Color.White)
                }
            }
        }
    }
}

private fun triggerEmergencyActions(context: Context) {
    val sosMsg = "EMERGENCY! I am on Bus TN 57 N 2184 (Route 182). Location: https://maps.google.com/?q=10.3673,77.9803. Sent via PayanMitra App"
    SMSHelper.sendDirectSMS(context, "9842100112", sosMsg)

    // Trigger call 112 intent
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:112")
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
