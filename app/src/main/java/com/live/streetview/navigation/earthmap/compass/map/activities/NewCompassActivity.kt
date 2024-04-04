package com.live.streetview.navigation.earthmap.compass.map.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.google.android.gms.ads.AdView
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.activities.callack.MyLocationCallBack
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityNewCompassBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale

class NewCompassActivity : AppCompatActivity(), SensorEventListener, LocationListener {

    private var imagedile: ImageView? = null
    private var textDegree: TextView? = null
    private var txtcompasscurrentloc: TextView? = null
    private var progressBar: ProgressBar? = null
    private var constraintLayout: ConstraintLayout? = null
    private var bindingCompass: ActivityNewCompassBinding? = null
    private val gravity = FloatArray(3)
    private var azimuth = 0f
    private var azimuthCurrent = 0f
    private var sensorManager: SensorManager? = null
    private var pressure = 0f
    private val geomgnetic = FloatArray(3)
    var comlat = 0.0
    var comlng = 0.0
    var location: MyLocation? = null

    companion object {
        private const val MIN_TIME_BW_UPDATES: Long = 1000 * 60 * 1 // 1 minute
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 10f // 10 meters
        private const val LOCATION_PERMISSION_REQUEST_CODE = 123

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingCompass = ActivityNewCompassBinding.inflate(layoutInflater)
        setContentView(bindingCompass!!.root)

        StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks("NewCompassActivityOnCreate", this@NewCompassActivity)

        imagedile = findViewById(R.id.imagedile)
        textDegree = findViewById(R.id.textfind)
        txtcompasscurrentloc = findViewById(R.id.currentcompassplace)
        progressBar = findViewById(R.id.progressbarcom)
        constraintLayout = findViewById(R.id.constraintLayout18)

        this.onBackPressedDispatcher.addCallback(onBackPressedCallback)

        bindingCompass!!.imagecompassarrow.setOnClickListener {
            StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks("NewCompassActivityOnBtnExit", this@NewCompassActivity)

            onBackPressedDispatcher.onBackPressed()
        }

        val PM = packageManager
        val acc = PM.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER)
        val mag = PM.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS)

        if (!acc || !mag) {
            Toast.makeText(this, "No Compass supported", Toast.LENGTH_SHORT).show()
        }

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        if (checkLocationPermission()) {
            // Permissions are granted, proceed with location-related functionality
            requestLocationUpdates()
        } else {
            // Request location permissions
            requestLocationPermission()
        }

        location = MyLocation(this, object : MyLocationCallBack {
            override fun onLocationListener(location: Location?) {
                progressBar!!.visibility = View.GONE
                constraintLayout!!.visibility = View.VISIBLE
                bindingCompass!!.txtaltilat.text = location?.latitude.toString()
                bindingCompass!!.textaltilng.text = location?.longitude.toString()
                if (location != null) {
                    onLocationChanged(location)
                }
            }

            override fun MyLocationListener(location: Location?) {}
        })
        location!!.location


        streetViewBannerAdsSmall()
    }

    private fun streetViewBannerAdsSmall() {
        val billingHelper = StreetViewAppSoniBillingHelper(this)
        val adContainer: LinearLayout = bindingCompass!!.bannerAdPlace.adContainer
        val adView = AdView(this)
        adView.setAdSize(StreetViewAppSoniMyAppAds.getAdSize(this))
        adView.adUnitId = StreetViewAppSoniMyAppAds.banner_admob_inApp
        if (billingHelper.isNotAdPurchased) {
            StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediation(
                adContainer, adView, this
            )
        } else {
            bindingCompass!!.bannerID.visibility = View.GONE
        }
    }
    private fun checkLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks("NewCompassActivityOnExit", this@NewCompassActivity)
            StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation(
                this@NewCompassActivity,
                StreetViewAppSoniMyAppAds.admobInterstitialAd,
                bindingCompass!!.whiteView
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback.remove()
    }

    override fun onResume() {
        super.onResume()
        sensorManager?.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
            SensorManager.SENSOR_DELAY_GAME
        )
        sensorManager?.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    private fun requestLocationUpdates() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            MIN_TIME_BW_UPDATES,
            MIN_DISTANCE_CHANGE_FOR_UPDATES,
            this
        )

        val lastKnownLocation =
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        lastKnownLocation?.let {
            onLocationChanged(it)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted, proceed with location-related functionality
                requestLocationUpdates()
            } else {
                // Location permission denied, handle accordingly (e.g., show a message)
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        val alpha = 0.97f
        synchronized(this) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                gravity[0] = alpha * gravity.get(0) + (1 - alpha) * event.values[0]
                gravity[1] = alpha * gravity.get(1) + (1 - alpha) * event.values[1]
                gravity[2] = alpha * gravity.get(2) + (1 - alpha) * event.values[2]
            }
            if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                geomgnetic[0] = alpha * geomgnetic.get(0) + (1 - alpha) * event.values[0]
                geomgnetic[1] = alpha * geomgnetic.get(1) + (1 - alpha) * event.values[1]
                geomgnetic[2] = alpha * geomgnetic.get(2) + (1 - alpha) * event.values[2]
            }
            val R = FloatArray(9)
            val I = FloatArray(9)
            val success = SensorManager.getRotationMatrix(R, I, gravity, geomgnetic)
            if (success) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(R, orientation)
                azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                azimuth = (azimuth + 360) % 360

                //
                val anim: Animation = RotateAnimation(
                    -azimuthCurrent, -azimuth, Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f
                )
                azimuthCurrent = azimuth
                anim.duration = 500
                anim.repeatCount = 0
                anim.fillAfter = true
                imagedile!!.startAnimation(anim)
                val a = Math.round(azimuthCurrent).toInt()
                textDegree!!.text = "$a°"
                // binding.txt1Compassnorth.setText(String.valueOf(a));
//                int str_south=a+180;
//                binding.txt1Compasssouth.setText(String.valueOf(str_south));
            }
        }
        pressure = event.values[0]
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        var sensor = sensor
        val sensors: List<Sensor> = sensorManager!!.getSensorList(Sensor.TYPE_PRESSURE)
        if (sensors.size > 0) {
            sensor = sensors[0]
            sensorManager!!.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
        /* float altitude = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, pressure);*/

//        binding.txt1CompassDistance.setText(String.valueOf(altitude) + " feet");
    }

    override fun onLocationChanged(location: Location) {
        // Location change handling logic
              bindingCompass!!.progressbar2.visibility = View.GONE
        txtcompasscurrentloc!!.visibility = View.VISIBLE
        comlat = location.latitude
        comlng = location.longitude

        try {
            CoroutineScope(Dispatchers.IO).launch {
                val geo = Geocoder(
                    this@NewCompassActivity.applicationContext,
                    Locale.getDefault()
                )
                val addresses = geo.getFromLocation(comlat, comlng, 1)

                withContext(Dispatchers.Main) {
                    if (addresses != null) {
                        if (addresses.isNotEmpty()) {
                            val locality = addresses[0].locality
                            txtcompasscurrentloc?.text = locality
                        }
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}
