package com.example.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

@Composable
fun AnimatedBusMap(
    busLat: Double,
    busLon: Double,
    routeWaypoints: List<String> = emptyList(),
    sourceCoord: Pair<Double, Double>? = Pair(10.3673, 77.9803),
    destCoord: Pair<Double, Double>? = Pair(10.4500, 77.5200),
    busProgress: Float = 0.3f,
    busNumber: String = "182",
    modifier: Modifier = Modifier
) {
    val animatedLat by animateFloatAsState(
        targetValue = busLat.toFloat(),
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "busLat"
    )
    val animatedLon by animateFloatAsState(
        targetValue = busLon.toFloat(),
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "busLon"
    )

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            Configuration.getInstance().userAgentValue = context.packageName
            MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(13.0)
                controller.setCenter(GeoPoint(busLat, busLon))

                // Full Route Polyline
                if (sourceCoord != null && destCoord != null) {
                    val fullRoute = Polyline(this).apply {
                        outlinePaint.color = android.graphics.Color.GRAY
                        outlinePaint.strokeWidth = 6f
                        setPoints(
                            listOf(
                                GeoPoint(sourceCoord.first, sourceCoord.second),
                                GeoPoint(busLat, busLon),
                                GeoPoint(destCoord.first, destCoord.second)
                            )
                        )
                    }
                    overlays.add(fullRoute)
                }

                // Completed Route Polyline
                if (sourceCoord != null) {
                    val completedRoute = Polyline(this).apply {
                        outlinePaint.color = android.graphics.Color.BLUE
                        outlinePaint.strokeWidth = 10f
                        setPoints(
                            listOf(
                                GeoPoint(sourceCoord.first, sourceCoord.second),
                                GeoPoint(busLat, busLon)
                            )
                        )
                    }
                    overlays.add(completedRoute)
                }

                // Source Marker
                sourceCoord?.let {
                    val marker = Marker(this).apply {
                        position = GeoPoint(it.first, it.second)
                        title = "Start Point"
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    }
                    overlays.add(marker)
                }

                // Dest Marker
                destCoord?.let {
                    val marker = Marker(this).apply {
                        position = GeoPoint(it.first, it.second)
                        title = "Destination"
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    }
                    overlays.add(marker)
                }

                // Bus Marker
                val busMarker = Marker(this).apply {
                    position = GeoPoint(busLat, busLon)
                    title = "Bus $busNumber"
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                }
                overlays.add(busMarker)
            }
        },
        update = { mapView ->
            val busMarker = mapView.overlays.filterIsInstance<Marker>()
                .find { it.title?.startsWith("Bus") == true }
            busMarker?.let { marker ->
                val newPos = GeoPoint(animatedLat.toDouble(), animatedLon.toDouble())
                marker.position = newPos
                mapView.controller.animateTo(newPos)
            }
            mapView.invalidate()
        }
    )
}
