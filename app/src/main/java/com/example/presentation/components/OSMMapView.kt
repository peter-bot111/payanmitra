package com.example.presentation.components

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

@Composable
fun OSMMapView(
    modifier: Modifier = Modifier,
    centerLat: Double = 10.3673,
    centerLng: Double = 77.9803,
    zoomLevel: Double = 14.0,
    busMarkerTitle: String = "Bus Position",
    routePoints: List<GeoPoint> = listOf(
        GeoPoint(10.3673, 77.9803),
        GeoPoint(10.4167, 77.9167),
        GeoPoint(10.4833, 77.7500),
        GeoPoint(10.4500, 77.5200)
    )
) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            Configuration.getInstance().userAgentValue = context.packageName
            MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(zoomLevel)
                val centerPoint = GeoPoint(centerLat, centerLng)
                controller.setCenter(centerPoint)

                // Add Bus Marker
                val marker = Marker(this).apply {
                    position = centerPoint
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    title = busMarkerTitle
                }
                overlays.add(marker)

                // Add Route Polyline
                if (routePoints.isNotEmpty()) {
                    val line = Polyline(this).apply {
                        setPoints(routePoints)
                        outlinePaint.color = android.graphics.Color.BLUE
                        outlinePaint.strokeWidth = 10f
                    }
                    overlays.add(line)
                }
                invalidate()
            }
        },
        update = { mapView ->
            mapView.controller.setCenter(GeoPoint(centerLat, centerLng))
            mapView.invalidate()
        }
    )
}
