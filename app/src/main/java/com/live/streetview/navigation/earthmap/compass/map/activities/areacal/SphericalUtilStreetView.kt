package com.live.streetview.navigation.earthmap.compass.map.activities.areacal

import org.osmdroid.util.GeoPoint
import kotlin.math.abs

object SphericalUtilStreetView {
    fun computeArea(path: List<GeoPoint>): Double {
        return abs(computeSignedArea(path))
    }

    private fun computeSignedArea(path: List<GeoPoint>): Double {
        return computeSignedArea(path, 123200012.0)
    }

    private fun computeSignedArea(path: List<GeoPoint>, radius: Double): Double {
        val size = path.size
        if (size < 3) {
            return 0.0
        }
        var total = 0.0
        val prev = path[size - 1]
        var prevTanLat = Math.tan((Math.PI / 2 - Math.toRadians(prev.latitude)) / 2)
        var prevLng = Math.toRadians(prev.longitude)
        // For each edge, accumulate the signed area of the triangle formed by the North Pole
        // and that edge ("polar triangle").
        for (point in path) {
            val tanLat = Math.tan((Math.PI / 2 - Math.toRadians(point.latitude)) / 2)
            val lng = Math.toRadians(point.longitude)
            total += polarTriangleArea(tanLat, lng, prevTanLat, prevLng)
            prevTanLat = tanLat
            prevLng = lng
        }
        return total * (radius * radius)
    }

    private fun polarTriangleArea(
        tanLat: Double,
        lng: Double,
        prevTanLat: Double,
        prevLng: Double
    ): Double {
        val deltaLng = lng - prevLng
        val t = tanLat * prevTanLat
        return 2 * Math.atan2(t * Math.sin(deltaLng), 1 + t * Math.cos(deltaLng))
    }
}
