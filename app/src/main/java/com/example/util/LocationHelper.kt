package com.example.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume

data class LocationInfo(
    val stateName: String,
    val districtName: String,
    val areaName: String,
    val latitude: Double,
    val longitude: Double
)

object LocationHelper {

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(context: Context): LocationInfo? = withContext(Dispatchers.IO) {
        try {
            val fusedClient = LocationServices.getFusedLocationProviderClient(context)
            val cancellationTokenSource = CancellationTokenSource()

            val location = suspendCancellableCoroutine { continuation ->
                fusedClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    cancellationTokenSource.token
                ).addOnSuccessListener { loc ->
                    if (continuation.isActive) continuation.resume(loc)
                }.addOnFailureListener {
                    if (continuation.isActive) continuation.resume(null)
                }
            } ?: suspendCancellableCoroutine { continuation ->
                fusedClient.lastLocation.addOnSuccessListener { loc ->
                    if (continuation.isActive) continuation.resume(loc)
                }.addOnFailureListener {
                    if (continuation.isActive) continuation.resume(null)
                }
            }

            if (location == null) return@withContext null

            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses: List<Address>? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                suspendCancellableCoroutine { continuation ->
                    try {
                        geocoder.getFromLocation(location.latitude, location.longitude, 1) { list ->
                            if (continuation.isActive) continuation.resume(list)
                        }
                    } catch (e: Exception) {
                        if (continuation.isActive) continuation.resume(null)
                    }
                }
            } else {
                @Suppress("DEPRECATION")
                geocoder.getFromLocation(location.latitude, location.longitude, 1)
            }

            val address = addresses?.firstOrNull()
            val stateName = address?.adminArea ?: "Tamil Nadu"
            val districtName = address?.subAdminArea ?: address?.locality ?: "Dindigul"
            val areaName = address?.locality ?: address?.subLocality ?: districtName

            LocationInfo(
                stateName = stateName,
                districtName = districtName,
                areaName = areaName,
                latitude = location.latitude,
                longitude = location.longitude
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
