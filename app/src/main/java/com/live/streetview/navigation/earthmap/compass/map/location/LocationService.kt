package com.live.streetview.navigation.earthmap.compass.map.location

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.live.streetview.navigation.earthmap.compass.map.activities.SpeedMeterActivity
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

class LocationService : Service(), LocationListener, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {
    var mLocationRequest: LocationRequest? = null
    var mGoogleApiClient: GoogleApiClient? = null
    var mCurrentLocation: Location? = null
    var lStart: Location? = null
    var lEnd: Location? = null
    var speed = 0f
    private val mBinder: IBinder = LocalBinder()
    override fun onBind(intent: Intent): IBinder? {
        createLocationRequest()
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()
        mGoogleApiClient!!.connect()
        return mBinder
    }

    protected fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = INTERVAL
        mLocationRequest!!.fastestInterval = FASTEST_INTERVAL
        mLocationRequest!!.priority =
            LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("MissingPermission")
    override fun onConnected(bundle: Bundle?) {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient!!, mLocationRequest!!, this
            )
        } catch (e: SecurityException) {
        }
    }

    protected fun stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
            mGoogleApiClient!!, this
        )
        distance = 0.0
    }

    override fun onConnectionSuspended(i: Int) {}
    override fun onLocationChanged(location: Location) {
        SpeedMeterActivity.dialog?.dismiss()
        mCurrentLocation = location
        if (lStart == null) {
            lStart = mCurrentLocation
            lEnd = mCurrentLocation
        } else lEnd = mCurrentLocation

        //Calling the method below updates the  live values of distance and speed to the TextViews.
        updateUI()
        //calculating the speed with getSpeed method it returns speed in m/s so we are converting it into kmph
        speed = location.speed * 18 / 5
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {}
    inner class LocalBinder : Binder() {
        val service: LocationService
            get() = this@LocationService
    }

    //The live feed of Distance and Speed are being set in the method below .
    private fun updateUI() {
        if (SpeedMeterActivity.p === 0) {
            distance = distance + lStart!!.distanceTo(
                lEnd!!
            ) / 1000.00
            SpeedMeterActivity.endTime = System.currentTimeMillis()
            var diff: Long = SpeedMeterActivity.endTime - SpeedMeterActivity.startTime
            diff = TimeUnit.MILLISECONDS.toMinutes(diff)
            SpeedMeterActivity.lastTravelTime!!.setText("$diff minutes")
            if (speed > 0.0) {
                SpeedMeterActivity.speed?.speedTo(speed)
            } else {
                SpeedMeterActivity.speed?.speedTo(0.0.toFloat())
            }
            SpeedMeterActivity.distance?.setText(DecimalFormat("#.##").format(distance))
            if (speed > maxSpeed) {
                maxSpeed = speed.toDouble()
                //  SpeedMeterActivity.maxSpeed.setText(new DecimalFormat("#.#").format(maxSpeed) + "km/s");
            }
            if (speed <= minspeed) {
                minspeed = speed.toDouble()
                //  SpeedMeterActivity.minSpeed.setText(new DecimalFormat("#.#").format(minspeed) + "km/s");
            }
            avgSpeed = (maxSpeed + minspeed) / 2
            // SpeedMeterActivity.avgSpeed.setText(new DecimalFormat("#.#").format(avgSpeed) + "km/s");
            lStart = lEnd
        }
    }

    override fun onUnbind(intent: Intent): Boolean {
        stopLocationUpdates()
        if (mGoogleApiClient!!.isConnected) mGoogleApiClient!!.disconnect()
        lStart = null
        lEnd = null
        distance = 0.0
        return super.onUnbind(intent)
    }

    companion object {
        private const val INTERVAL = (1000 * 2).toLong()
        private const val FASTEST_INTERVAL = (1000 * 1).toLong()
        var distance = 0.0
        var maxSpeed = 0.0
        var minspeed = 0.0
        var avgSpeed = 0.0
    }
}