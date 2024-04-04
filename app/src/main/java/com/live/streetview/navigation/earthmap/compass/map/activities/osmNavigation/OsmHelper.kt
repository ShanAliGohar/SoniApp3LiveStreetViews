package com.live.streetview.navigation.earthmap.compass.map.activities.osmNavigation

import org.osmdroid.util.GeoPoint

class OsmHelper {
    companion object
    {
        fun computeAngleBetween(from: GeoPoint, to: GeoPoint): Double {
            return distanceRadians(
                Math.toRadians(from.latitude),
                Math.toRadians(from.longitude),
                Math.toRadians(to.latitude),
                Math.toRadians(to.longitude)
            )
        }

        fun computeDistanceBetween(from: GeoPoint, to: GeoPoint): Double {
            return computeAngleBetween(from, to) * 6371009.0
        }
        private fun distanceRadians(
            lat1: Double,
            lng1: Double,
            lat2: Double,
            lng2: Double
        ): Double {

            return arcHav(havDistance(lat1, lat2, lng1 - lng2))
        }
        fun hav(x: Double): Double {
            val sinHalf = Math.sin(x * 0.5)
            return sinHalf * sinHalf
        }

        fun arcHav(x: Double): Double {
            return 2.0 * Math.asin(Math.sqrt(x))
        }

        fun havDistance(lat1: Double, lat2: Double, dLng: Double): Double {
            return hav(lat1 - lat2) + hav(dLng) * Math.cos(lat1) * Math.cos(lat2)
        }
    }
}