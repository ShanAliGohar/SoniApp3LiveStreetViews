package com.live.streetview.navigation.earthmap.compass.map.activities.utils

import android.location.Location

interface MyLocattionListener {
    fun onLocationChanged(location: Location)
}