package com.live.streetview.navigation.earthmap.compass.map.activities

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognizerIntent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds
import com.live.streetview.navigation.earthmap.compass.map.activities.osmNavigation.OsmNavigation
import com.live.streetview.navigation.earthmap.compass.map.activities.utils.LocationRepository
import com.live.streetview.navigation.earthmap.compass.map.activities.utils.MyLocattionListener
import com.live.streetview.navigation.earthmap.compass.map.activities.utils.UtlilityClassByKotlin
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityVoiceNavigationBinding
import com.streetview.liveearth.satellitemap.gpsnavigation.app.utilites.GeocoderCoroutineCloneTask
import org.osmdroid.util.GeoPoint
import java.io.IOException

class VoiceNavigationActivity : AppCompatActivity() {
//    var binding: ActivityVoiceNavigationBinding? = null
//    lateinit var myRepository: LocationRepository
//    private var c_latitude_global: Double = 0.0
//    private var c_longitude_global: Double = 0.0
//    private var d_tolatitude: Double = 0.0
//    private var d_tolongitude: Double = 0.0
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityVoiceNavigationBinding.inflate(layoutInflater)
//        gpsONOFF(this)
//        getYourCurrentLocation()
//        setContentView(binding!!.getRoot())
//        if (isNetworkAvailable(this))
//            binding!!.button.setOnClickListener {
//
//                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
//                intent.putExtra(
//                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
//                )
//                startActivityForResult(intent, 301)
//            }
//
//        binding!!.button2.setOnClickListener {
//            if (d_tolatitude != null) {
//                val intent = Intent(this, OsmNavigation::class.java)
//                intent.putExtra("lat", c_latitude_global)
//                intent.putExtra("lng", c_longitude_global)
//                intent.putExtra("d_lat", d_tolatitude)
//                intent.putExtra("d_lng", d_tolongitude)
//
//                StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation(
//                    this@VoiceNavigationActivity,
//                    StreetViewAppSoniMyAppAds.admobInterstitialAd,
//                    
//                    intent
//                )
//            }
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == 302) {
//            checkingGPS()
//            getYourCurrentLocation()
//        } else {
//            val arrayList: ArrayList<String> =
//                data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
//            val voiceText: String = arrayList.get(0)
//            binding!!.txtDestination.text = voiceText
//            binding!!.progressBar3.visibility = View.VISIBLE
//            binding!!.button2.visibility = View.INVISIBLE
//            getlocation(voiceText)
//            Log.d("TAG", "onActivityResult: ${voiceText}")
//        }
//    }
//
//    private fun checkingGPS() {
//        //check if gps is enabled or not and then request user to enable it
//        val locationRequest: LocationRequest = LocationRequest.create()
//        locationRequest.interval = 10000
//        locationRequest.fastestInterval = 5000
//        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
//        val settingsClient = LocationServices.getSettingsClient(this)
//        val task: Task<LocationSettingsResponse> =
//            settingsClient.checkLocationSettings(builder.build())
//        task.addOnSuccessListener(this,
//            OnSuccessListener<LocationSettingsResponse?> {
//
//            })
//        task.addOnFailureListener(this, OnFailureListener { e ->
//            if (e is ResolvableApiException) {
//                try {
//                    e.startResolutionForResult(this, 51)
//                } catch (e1: IntentSender.SendIntentException) {
//                    e1.printStackTrace()
//                }
//            }
//        })
//    }
//
//    fun isNetworkAvailable(context: Context?): Boolean {
//        if (context == null) return false
//        val connectivityManager =
//            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            val capabilities =
//                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
//            if (capabilities != null) {
//                when {
//                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
//                        return true
//                    }
//                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
//                        return true
//                    }
//                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
//                        return true
//                    }
//                }
//            }
//        } else {
//            val activeNetworkInfo = connectivityManager.activeNetworkInfo
//            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
//                return true
//            }
//        }
//        return false
//    }
//
//    fun gpsONOFF(context: Context) {
//        val lm = context.getSystemService(LOCATION_SERVICE) as LocationManager
//        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            val alertDialog = AlertDialog.Builder(context)
//            alertDialog.setTitle("Enable GPS")
//            alertDialog.setMessage("Your GPS setting is not enabled. Please enabled it in settings menu.")
//            alertDialog.setPositiveButton(
//                "Settings"
//            ) { dialog, which ->
//                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                startActivityForResult(intent, 302)
//            }
//            alertDialog.setNegativeButton(
//                "Cancel"
//            ) { dialog, which -> dialog.cancel() }
//            val alert = alertDialog.create()
//            alert.show()
//        }
//    }
//
//    private fun getYourCurrentLocation() {
//        myRepository = LocationRepository(this, object : MyLocattionListener {
//            override fun onLocationChanged(location: Location) {
//                if (location != null) {
//                    c_latitude_global = location.latitude
//                    c_longitude_global = location.longitude
//                    val latLng = GeoPoint(location.latitude, location.longitude)
//                    Thread {
//                        runOnUiThread(Runnable {
//                            // progressBar.visibility = View.GONE
//                            getCurrentLocationInTextForm(latLng)
//                            if (c_latitude_global != null) {
////                                startPoint= GeoPoint(location.latitude,location.longitude)
////                                initializeMap(latitude_global, longitude_global)
//                            }
//
//                            myRepository.stopLocation()
//                        })
//
//                    }.start()
//
//                    UtlilityClassByKotlin.gpsOrigin =
//                        GeoPoint(c_latitude_global, c_longitude_global)
//                    myRepository.stopLocation()
//                } else {
//                    myRepository.startLocation()
//                }
//            }
//        })
//
//    }
//
//    private fun getCurrentLocationInTextForm(latLng: GeoPoint) {
//        val task = GeocoderCoroutineCloneTask(this@VoiceNavigationActivity, latLng,
//            object : GeocoderCoroutineCloneTask.GeoTaskCallback {
//                override fun onFailedLocationFetched() {
//
//                }
//
//                override fun onSuccessLocationFetched(fetchedAddress: String?) {
//                    if (fetchedAddress != null && fetchedAddress.length > 0) {
//                        var address = fetchedAddress
//                        binding!!.txtCurrentLocation.setText(address + "")
//                        binding!!.txtCurrentLocation.isSelected=true
//                    }
//                }
//            })
//        task.execute()
//
//
//    }
//
//    private fun getlocation(str: String) {
//
//        val geocoder = Geocoder(this)
//        val addressList: List<Address>
//        try {
//            addressList =
//                geocoder.getFromLocationName(str, 1) as List<Address>
//            if (addressList.isEmpty()) {
//                Toast.makeText(this, "Please Enter valid address", Toast.LENGTH_SHORT).show()
//            } else if (addressList.isNotEmpty()) {
//                binding!!.progressBar3.visibility = View.GONE
//                binding!!.button2.visibility = View.VISIBLE
//                d_tolatitude = addressList.get(0).latitude
//                d_tolongitude = addressList.get(0).longitude
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
//    }
//    override fun onBackPressed() {
////        super.onBackPressed()
//        StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation(
//            this,
//            StreetViewAppSoniMyAppAds.admobInterstitialAd,
//            StreetViewAppSoniMyAppAds.maxInterstitialAdStreetViewST
//        )
//    }
}