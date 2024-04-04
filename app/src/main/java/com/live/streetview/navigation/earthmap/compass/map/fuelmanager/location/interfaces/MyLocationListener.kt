package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.location.interfaces

import android.location.Location

interface MyLocationListener {
    fun onLocationChange(location:Location)
}