package com.live.streetview.navigation.earthmap.compass.map.fuelmanager

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.location.Location
import android.net.ConnectivityManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityFuelStationMapBinding
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.fuelprices.dataClasses.FuelDataList
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.fuelprices.dataClasses.fuelsModel.MainFuel
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.fuelprices.retrofit.ApiClientFuel
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.helper.OSMTileSourceFixed
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.helper.OsmHelper
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.location.RepoLocation
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.location.interfaces.MyLocationListener
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.nearbyplaces.NearbyPlacesRepository
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.nearbyplaces.NearbyPlacesViewModel
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.nearbyplaces.NearbyPlacesViewModelFactory
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.nearbyplaces.api.ApiClientForNearbyPlaces
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.nearbyplaces.api.NearByPlacesApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class FuelStationMapActivity : AppCompatActivity() {

    private lateinit var viewModel: NearbyPlacesViewModel
    private lateinit var binding: ActivityFuelStationMapBinding
    lateinit var repo: RepoLocation
    private val locationPermissionCode = 103
    private var map: MapView? = null
    private lateinit var mapController: IMapController
    private lateinit var mOSMStyle: OSMTileSourceFixed
    private var fuelDataList: ArrayList<FuelDataList> = ArrayList()
    private var electDataList: ArrayList<FuelDataList> = ArrayList()
    private var gasDataList: ArrayList<FuelDataList> = ArrayList()
    lateinit var currentPosition: GeoPoint
    private var currencyType: String? = null
    lateinit var startMarker: Marker
    private var marker: Marker? = null
    private var endPoint: GeoPoint? = null


    companion object {
        const val RESULT_KEY = "result_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )
        binding = ActivityFuelStationMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Location permission is not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }



        getLocationUser()


        map = binding.map


        // Initialize ViewModel and Repository
        val apiClientForNearbyPlaces = ApiClientForNearbyPlaces.retrofitInstance.create(
            NearByPlacesApi::class.java
        )
        val repository = NearbyPlacesRepository(apiClientForNearbyPlaces)
        val viewModelFactory = NearbyPlacesViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[NearbyPlacesViewModel::class.java]

//        loadData("China")

        // Observe LiveData
        viewModel.nearbyPlaces.observe(this, Observer { resultList ->
            // Handle the list of nearby places
            // You can update your UI here



            for (result in resultList) {

                val latitudeDest = result.geocodes.main.latitude
                val longitudeDest = result.geocodes.main.longitude

                marker = Marker(map)
                endPoint = GeoPoint(latitudeDest, longitudeDest)
                Log.d("TAG", "onResponse:endpoint " + endPoint)
                marker!!.position = endPoint

                marker!!.title = result.name

                // Set the marker!! icon (optional)
                val icon =
                    BitmapFactory.decodeResource(resources, R.drawable.marker)
                OsmHelper.setMarkerIconAsPhoto(this, marker!!, icon)

                // Add the marker as an overlay on the OSM map
                map!!.overlays.add(marker)
                marker!!.setOnMarkerClickListener(object : Marker.OnMarkerClickListener {
                    override fun onMarkerClick(marker: Marker?, mapView: MapView?): Boolean {
//                        marker!!.title= iconClickCustomDialog().toString()
                        iconClickCustomDialog(result.name)
                    return true
                    }

                })
            }
        })

        viewModel.error.observe(this, Observer { errorMessage ->
            // Handle the error message
        })
    }




    private fun iconClickCustomDialog(name:String) {
        val dialog = Dialog(this@FuelStationMapActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.custome_dialog)

        val Gasoline: TextView = dialog.findViewById(R.id.infoWindowTitleGasoline)
        val priceTextView: TextView = dialog.findViewById(R.id.infoWindowSnippetGasoline)
        val diesel: TextView = dialog.findViewById(R.id.infoWindowTitleDiesel)
        val dieselprice: TextView = dialog.findViewById(R.id.infoWindowSnippetDiesel)
        val pumpName: TextView = dialog.findViewById(R.id.pumpName)
        val selectStation : Button = dialog.findViewById(R.id.selectFuelStation)

        dialog.setCancelable(true)

        Log.d("TAG", "iconClickCustomDialog: ${fuelDataList.size}")
        pumpName.text=name
            //descriptionTvDetails.text = fuelDataList1.price
        for (fuelDataList1 in fuelDataList) {
            if (fuelDataList1.name == "Diesel"){
                diesel.text=fuelDataList1.name
                dieselprice.text=fuelDataList1.price

            } else if (fuelDataList1.name == "Gasoline"){
                Gasoline.text = fuelDataList1.name
                priceTextView.text = fuelDataList1.price
            }

            Log.d("TAG", "iconClickCustomDialog: $fuelDataList1")
        }


        selectStation.setOnClickListener {
            // Set the result and finish the activity
            val intent = Intent().apply {
                putExtra(RESULT_KEY, name)
            }
            setResult(Activity.RESULT_OK, intent)
            dialog.dismiss()
            finish()
        }

        dialog.show()
    }
    private fun getLocationUser() {
        repo = RepoLocation(this, object : MyLocationListener {
            override fun onLocationChange(location: Location) {
                if (location != null) {
                    repo.stopLocation()

                    try {
                        val geocoder = Geocoder(this@FuelStationMapActivity, Locale.getDefault())
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                        Log.e("CountryName", "$addresses")

                        if (addresses != null) {
                            if (addresses.isNotEmpty()) {
                                val country = addresses[0].countryName
                                // Use the detected country to load data
                                loadData(country)
                            } else {
                                Log.e("CountryName", "No address found for the location")
                            }
                        }

                        viewModel.getNearbyPlaces(location.latitude, location.longitude)
                        osmMapLoad(location.latitude, location.longitude)
                    } catch (e: IOException) {
                        Log.e("CountryName", "Error getting address: ${e.message}")
                    }
                } else {
                    repo.startLocation()
                }
            }
        })
    }

//    private fun getLocationUser() {
//        repo = RepoLocation(this, object : MyLocationListener {
//            override fun onLocationChange(location: Location) {
//                if (location != null) {
//
//                    repo.stopLocation()
//
//                    viewModel.getNearbyPlaces(location.latitude, location.longitude)
//                    osmMapLoad(location.latitude, location.longitude)
//                    repo.stopLocation()
//                } else {
//                    repo.startLocation()
//                }
//            }
//        })
//    }

    private fun osmMapLoad(lat: Double, long: Double) {
        //Map Load
        currentPosition = GeoPoint(lat, long)

        map!!.setTileSource(TileSourceFactory.MAPNIK)
        map!!.setBuiltInZoomControls(true)
        map!!.setMultiTouchControls(true)
        mapController = map!!.controller
        mapController.animateTo(currentPosition)
        mapController.setCenter(GeoPoint(lat, long))
        mapController.setZoom(13)
        map!!.zoomController?.onDetach()

        //rotation
        val mRotationGestureOverlay = RotationGestureOverlay(this, map)
        mRotationGestureOverlay.isEnabled = true
        map!!.overlays.add(mRotationGestureOverlay)

        startMarker = Marker(map)
        startMarker.position = currentPosition
        startMarker.title = "current location"
        startMarker.icon = null
        val icon =
            BitmapFactory.decodeResource(resources, org.osmdroid.library.R.drawable.marker_default)
        OsmHelper.setMarkerIconAsPhoto(this, startMarker, icon)

        map!!.overlays.add(startMarker)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted, you can now access the user's location
                // Call the necessary location-related methods here
            } else {
                // Location permission denied, handle it as needed
                Toast.makeText(this, "Need Location Permission", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isNetworkConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }

    private fun loadData(country: String) {
        val apiClient = ApiClientFuel.apiService
        val call = apiClient.getPlaces2(country)

        call.enqueue(object : Callback<MainFuel> {
            @SuppressLint("UseCompatLoadingForColorStateLists")
            override fun onResponse(call: Call<MainFuel>, response: Response<MainFuel>) {
                Log.e("TAG", "onResponse: ${response.body()}")
                if (response.isSuccessful) {
                    CoroutineScope(Dispatchers.IO).launch {
                            with(fuelDataList) {
                                add(
                                    FuelDataList(
                                        "Diesel",
                                        response.body()?.data?.dieselPrices?.Currency
                                            ?: "no data found"
                                    )
                                )
                                add(
                                    FuelDataList(
                                        "Gasoline",
                                        response.body()?.data?.gasolinePrices?.Currency
                                            ?: "no data found"
                                    )
                                )
                                add(
                                    FuelDataList(
                                        "Methane",
                                        response.body()?.data?.methanePrices?.Currency
                                            ?: "no data found"
                                    )
                                )
                                add(
                                    FuelDataList(
                                        "Ethanol",
                                        response.body()?.data?.ethanolPrices?.Currency
                                            ?: "no data found"
                                    )
                                )
                                add(
                                    FuelDataList(
                                        "Kerosene",
                                        response.body()?.data?.kerosenePrices?.Currency
                                            ?: "no data found"
                                    )
                                )
                                add(
                                    FuelDataList(
                                        "Heating Oil",
                                        response.body()?.data?.heatingOilPrices?.Currency
                                            ?: "no data found"
                                    )
                                )
                                add(FuelDataList("LPG", response.body()?.data?.lpgPrices?.Currency))
                            }
                            Log.e("TAGdtataat", "onDataAdded: ${fuelDataList.size}")
                            /*setting fuel prices adapter...*/
                            withContext(Dispatchers.Main) {
                            }
                        }
                }
            }
            override fun onFailure(call: Call<MainFuel>, t: Throwable) {
                Log.e("TAG", "onFailure: ${t.localizedMessage} ")
            }
        })
    }
}
