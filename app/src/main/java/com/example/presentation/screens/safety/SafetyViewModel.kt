package com.example.presentation.screens.safety

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.util.SMSHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SafetyViewModel : ViewModel() {

    private val _isNightMode = MutableStateFlow(false)
    val isNightMode: StateFlow<Boolean> = _isNightMode.asStateFlow()

    private val _trustedContacts = MutableStateFlow(
        listOf("Mother: 9842100112", "Sister: 9443209811")
    )
    val trustedContacts: StateFlow<List<String>> = _trustedContacts.asStateFlow()

    fun toggleNightMode() {
        _isNightMode.value = !_isNightMode.value
    }

    fun shareJourney(context: Context) {
        SMSHelper.shareJourneyLink(
            context = context,
            route = "182",
            busNo = "TN 57 N 2184",
            lat = 10.3673,
            lng = 77.9803
        )
    }

    fun sendEmergencySOS(context: Context) {
        val sosMsg = "EMERGENCY SOS ALERT! I am in danger on Bus TN 57 N 2184 (Route 182). Location: https://maps.google.com/?q=10.3673,77.9803. Sent via PayanMitra"
        for (contact in _trustedContacts.value) {
            val phone = contact.substringAfter(": ").trim()
            SMSHelper.sendDirectSMS(context, phone, sosMsg)
        }
    }
}
