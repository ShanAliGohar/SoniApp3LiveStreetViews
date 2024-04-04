package com.live.streetview.navigation.earthmap.compass.map.activities.osmNavigation

import android.location.Location

interface MyLocationListener {
    fun onLocationChanged(location: Location)
}