package com.live.streetview.navigation.earthmap.compass.map.location

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.live.streetview.navigation.earthmap.compass.map.activities.callack.MyLocationCallBack

class SpeedLocation(var mcontext: Context, var callBack: MyLocationCallBack) {
    var locationCallBack: LocationCallback? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null

    @get:SuppressLint("MissingPermission")
    val location: Unit
        get() {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mcontext)
            val locationRequest = LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)
                .setFastestInterval(1000)
                .setNumUpdates(100000)
            locationCallBack = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    callBack.onLocationListener(locationResult.lastLocation)
                }
            }
            fusedLocationProviderClient!!.requestLocationUpdates(
                locationRequest, locationCallBack!!,
                Looper.myLooper()!!
            )
        }

    fun onStop() {
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient!!.removeLocationUpdates(locationCallBack!!)
        }
    }
}
