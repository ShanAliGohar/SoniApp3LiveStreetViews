package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.services

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.TripLogActivity
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.trip.TripDao
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.trip.TripDatabase
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.trip.TripEntity
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.utils.SharedPrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

 const val STOP_SERVICE_ACTION = "com.yourapp.STOP_AUTO_TRIP_SERVICE"


class AutoTripServices : Service() {

    private val binder: IBinder = LocalBinder()
    private var locationListener: LocationListener? = null
    private var startLocation: Location? = null
    private var endLocation: Location? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var tripDao: TripDao
    private var totalDistance: Float = 0.0f



    interface LocationListener {
        fun onLocationChanged(location: Location)
    }

    inner class LocalBinder : Binder() {
        fun getService(): AutoTripServices = this@AutoTripServices
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        tripDao = TripDatabase.getDatabase(applicationContext).tripDao()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Initialize location listener
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                handleLocationChange(location)
            }
        }

        if (intent?.action == STOP_SERVICE_ACTION) {
            // Handle the custom action to stop the service
            stopForeground(true)
            stopSelf()
            return START_NOT_STICKY
        }

        // Start fetching location
        startLocationUpdates()
        startForeground(NOTIFICATION_ID, createNotification())
        return START_STICKY
    }

    override fun onDestroy() {
        // Stop location updates
        stopLocationUpdates()

        Log.d(TAG, "onDestroy: Service Stopped")
        // Store start and end locations
        storeLocationData(startLocation, endLocation)

        super.onDestroy()
    }

    private fun startLocationUpdates() {
        // Implement your location updates logic here
        val locationRequest = LocationRequest.create().apply {
            interval = 10000 // 10 seconds
            fastestInterval = 5000 // 5 seconds
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        // Request location updates
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        // Stop location updates when the service is destroyed
        locationListener?.let {
            // Remove location updates
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.locations.forEach { location ->
                locationListener?.onLocationChanged(location)
            }
        }
    }

    private fun handleLocationChange(location: Location) {
        // This method is called when the location changes
        Log.d(TAG, "Location changed: $location")

        if (startLocation == null) {
            // First location update, set it as the start location
            startLocation = location
        }

        endLocation?.let {
            totalDistance += startLocation?.distanceTo(it) ?: 0.0f
        }

        // Always update the end location to the latest
        endLocation = location
    }

    private fun storeLocationData(startLocation: Location?, endLocation: Location?) {
        // Check if both start and end locations are available


        Log.d(TAG, "storeLocationData: ${startLocation.toString() + endLocation.toString()}")


        if (startLocation != null && endLocation != null) {
            // Get the address strings using Geocoder
            val startAddress = translateLocation(startLocation)
            val endAddress = translateLocation(endLocation)

            // Get the current date and time
            val currentDate = getCurrentDate()
            val currentTime = getCurrentTime()

            val distanceInKm = totalDistance / 1000.0 // Convert to kilometers

            // Create a TripEntity object
            val tripEntity = TripEntity(
                title = "Your Trip Title",  // Provide a suitable title
                startPoint = startAddress,
                startOdoCounter = "00",
                startDate = currentDate,
                startTime = currentTime,
                endPoint = endAddress,
                finalOdoCounter = distanceInKm.toInt().toString(),
                endDate = currentDate,  // You may want to update this with the actual end date
                endTime = currentTime,  // You may want to update this with the actual end time
                costPerKm = 0,
                tripCost = 0,
                description = "Your trip description",
                vehicleId = SharedPrefManager(applicationContext).getActiveVehicleId()
            )

            // Insert the TripEntity into the TripDao
            CoroutineScope(Dispatchers.Default).launch {
                tripDao.insertTripLog(tripEntity)
            }
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH)
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    private fun getCurrentTime(): String {
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val currentTime = Date()
        return timeFormat.format(currentTime)
    }


    private fun translateLocations(startLocation: Location?, endLocation: Location?) {
        // Translate location points to addresses using Geocoder

        startLocation?.let { location ->
            translateLocation(location)
        }

        endLocation?.let { location ->
            translateLocation(location)
        }
    }

    private fun translateLocation(location: Location): String {
        var addressString = ""
        // Translate a single location to an address using Geocoder
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses: List<Address> = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
            )!!

            if (addresses.isNotEmpty()) {
                val address: Address = addresses[0]
                addressString = address.adminArea
                Log.d(TAG, "Translated Address: ${address.adminArea}")
            } else {
                Log.d(TAG, "No address found for the location.")
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error translating location to address", e)
        }

        return addressString
    }
    


    private fun createNotification(): Notification {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("auto_trip_service", "Auto Trip Calculating")
            } else {
                "" // If earlier than Oreo, the channelId is not used
            }

        val stopServiceIntent = Intent(this, StopServiceReceiver::class.java)
        val stopServicePendingIntent = PendingIntent.getBroadcast(
            this, 0, stopServiceIntent, PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Auto Trip Service")
            .setContentText("Location updates in progress")
            .setSmallIcon(androidx.core.R.drawable.notification_icon_background)
            .addAction(R.drawable.ic_close_icon, "Stop Service", stopServicePendingIntent)
            .build()
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        channelId: String,
        channelName: String
    ): String {
        val chan = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }


    companion object {
        private const val TAG = "LocationTrackingService"
        private const val NOTIFICATION_ID = 123
    }


}