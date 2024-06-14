package com.live.streetview.navigation.earthmap.compass.map.activities

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.gms.ads.AdView
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.activities.osmNavigation.MyLocationListener
import com.live.streetview.navigation.earthmap.compass.map.activities.osmNavigation.NavigationActivity
import com.live.streetview.navigation.earthmap.compass.map.activities.utils.LiveEarthAddressFromLatLng
import com.live.streetview.navigation.earthmap.compass.map.activities.utils.LocationHelper
import com.live.streetview.navigation.earthmap.compass.map.activities.utils.OSMTileSourceFixed
import com.live.streetview.navigation.earthmap.compass.map.activities.utils.RepoLocation
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityEarthMap2DactivityBinding
import com.streetview.navigation.liveearth.satellite.hotelbooking.helper.OsmHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.api.IMapController
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.MapBoxTileSource
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.infowindow.InfoWindow
import java.io.IOException
import java.util.*


class EarthMap2DActivity : AppCompatActivity() {
    var binding: ActivityEarthMap2DactivityBinding? = null
    private lateinit var map: MapView
    private var mapController: IMapController? = null
    private lateinit var mOSMStyle: OSMTileSourceFixed
    lateinit var Repo: RepoLocation
    val TAG = "MapBoxActivity"
    private lateinit var currentPosition: GeoPoint
    private var selectedMarker: Marker? = null
    private var infoWindowVisible = false
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var latitudeDest: Double = 0.0
    private var longitudeDest: Double = 0.0
    private var markerLat: Double = 0.0
    private var markerLng: Double = 0.0
    private var startPoint: GeoPoint? = null
    private var endPoint: GeoPoint = GeoPoint(0.0, 0.0)
    private var endPointMarker: GeoPoint? = null
    lateinit var startMarker: Marker
    lateinit var marker: Marker
    private var searchedPlaceMarker: Marker? = null

    var currentLocationMarker : Marker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(
            applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )
        binding = ActivityEarthMap2DactivityBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks(
            "EarthMap2DActivityOnCreate",
            this@EarthMap2DActivity
        )

        streetViewBannerAdsSmall()

        //on back press dispatcher
        this.onBackPressedDispatcher.addCallback(onBackPressedCallback)


        getLocationUser()
        imageClickStyleListener()

        binding!!.sercheditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (LocationHelper.isInternetAvailable(this)) {

                    val searchQuery = binding!!.sercheditText.text.toString()
                    if (searchQuery.isNotEmpty()) {
                        performSearch(searchQuery)
                        drawRoute()
                    } else {
                        Toast.makeText(this, "Enter Destination First", Toast.LENGTH_SHORT).show()
                    }
                } else Toast.makeText(this, "Connect to Internet", Toast.LENGTH_SHORT).show()
            }
            true
        }

        binding!!.ImgCurrentLocation.setOnClickListener {
//            OsmHelper.zoomWithAnimate(mapController,currentPosition,20)

            mapController?.animateTo(currentPosition)

        }
        binding!!.ImgZoomIn.setOnClickListener {
            binding!!.map.controller.zoomIn()
        }

        binding!!.ImgZoomOut.setOnClickListener {
            binding!!.map.controller.zoomOut()
        }
//        imageClickStyleListener()

        binding!!.backArrow.setOnClickListener {
            StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks(
                "EarthMap2DActivityOnBtnExit",
                this@EarthMap2DActivity
            )
            StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation(
                this,
                StreetViewAppSoniMyAppAds.admobInterstitialAd,
                 binding!!.whiteView
            )
        }

        binding!!.SearchBtn.setOnClickListener {

            if (isInternetAvailable(this)) {
                val searchQuery = binding!!.sercheditText.text.toString()
                if (searchQuery.isNotEmpty()) {
                    performSearch(searchQuery)
                    drawRoute()
                } else {
                    Toast.makeText(this, "Enter Destination First", Toast.LENGTH_SHORT).show()
                }
                // Hide the keyboard
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding!!.sercheditText.windowToken, 0)

            } else Toast.makeText(this, "Connect to Internet", Toast.LENGTH_SHORT).show()
        }

        binding!!.navigateBtn.setOnClickListener {
            val intent = Intent(this@EarthMap2DActivity, NavigationActivity::class.java)
            val bundle = Bundle()
            bundle.putDouble("originLat", latitude)
            bundle.putDouble("originLng", longitude)
            bundle.putDouble("destLat", markerLat)
            bundle.putDouble("destLng", markerLng)
            intent.putExtras(bundle)
//            startActivity(intent)
            StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation(
                this@EarthMap2DActivity,
                StreetViewAppSoniMyAppAds.admobInterstitialAd,
                
                intent, binding!!.whiteView
            )
        }
    }

    private fun getLocationUser() {
        Repo = RepoLocation(this, object : MyLocationListener {

            override fun onLocationChanged(location: Location) {
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                    startPoint = GeoPoint(latitude, longitude)

                    Log.e(TAG, "onLocationChanged: kkk ${latitude}")
                    Repo.stopLocation()
                    osmMapLoad(latitude, longitude)

                    LiveEarthAddressFromLatLng(
                        this@EarthMap2DActivity,
                        location,
                        object : LiveEarthAddressFromLatLng.GeoTaskCallback {
                            override fun onSuccessLocationFetched(fetchedAddress: String?) {

                            }

                            override fun onFailedLocationFetched() {

                            }

                        }).execute()
                    Repo.stopLocation()

                } else {
                    Repo.startLocation()
                }
            }
        })
    }


    private fun imageClickStyleListener() {
        binding!!.mapLayerStyleImg.setOnClickListener {
            if (binding!!.mapLayerLayout.isVisible) {
                binding!!.mapLayerLayout.visibility = View.GONE
            } else {
                binding!!.mapLayerLayout.visibility = View.VISIBLE
            }
        }
        binding!!.trafficMap.setOnClickListener {

            OsmHelper.setStdTileProvider(this, binding!!.map)
            binding!!.map.setTileSource(TileSourceFactory.OpenTopo)

            binding!!.mapLayerLayout.visibility = View.GONE
        }
        binding!!.satelliteMap.setOnClickListener {
            OsmHelper.mapStyle(binding!!.map)
            binding!!.mapLayerLayout.visibility = View.GONE
        }
        binding!!.normalMapStyle.setOnClickListener {
            OsmHelper.setStdTileProvider(this, binding!!.map)
            binding!!.map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)

            binding!!.mapLayerLayout.visibility = View.GONE
        }
        binding!!.darkMapStyle.setOnClickListener {
            setMapBoxMapStyleLayer()
            binding!!.mapLayerLayout.visibility = View.GONE
        }

    }

    private fun setMapBoxMapStyleLayer() {
        val mapBoxTileSourceFixed = OSMTileSourceFixed("MapBoxSatelliteLabelled", 1, 19, 256)
        this.mOSMStyle = mapBoxTileSourceFixed
        mapBoxTileSourceFixed.retrieveAccessToken(this)
        (mOSMStyle as MapBoxTileSource?)!!.retrieveMapBoxMapId(this)
        binding!!.map.setTileSource(this.mOSMStyle)

    }

    private fun osmMapLoad(lat: Double, Long: Double) {
        // Map Load
        currentPosition = GeoPoint(lat, Long)

        map = binding!!.map
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setBuiltInZoomControls(true)
        map.setMultiTouchControls(true)
        mapController = map.controller
        mapController?.animateTo(currentPosition)
        mapController?.setCenter(GeoPoint(latitude, longitude))
        mapController?.setZoom(17.44)
        map.zoomController?.onDetach()
        map.maxZoomLevel = 19.0

        // Rotation
        val mRotationGestureOverlay = RotationGestureOverlay(this, map)
        mRotationGestureOverlay.isEnabled = true
        map.overlays.add(mRotationGestureOverlay)

//        OsmHelper.zoomWithAnimate(mapController, currentPosition, 15)

        // Marker
        startMarker = Marker(map)
        startMarker.position = currentPosition

        // Create custom info window view
        val infoWindowView = layoutInflater.inflate(R.layout.custom_marker_layout, null)

        // Set custom address
        val addressTextView = infoWindowView.findViewById<TextView>(R.id.addressTextView)

        if (isInternetAvailable(this)) {

          CoroutineScope(Dispatchers.Main).launch {
                val address = getAddressFromLocation(lat, Long)
                addressTextView.text = address
            }
        } else Toast.makeText(this, "Connect to Internet", Toast.LENGTH_SHORT).show()

        // Create custom info window
        val infoWindow = object : InfoWindow(infoWindowView, map) {
            override fun onOpen(item: Any?) {
                // Customization logic when the info window is opened
            }

            override fun onClose() {
                // Customization logic when the info window is closed
            }
        }

        // Set custom info window to the marker
        startMarker.infoWindow = infoWindow

        // Set custom marker icon
        val icon = BitmapFactory.decodeResource(resources, R.drawable.currentlocarion)
        com.streetview.navigation.liveearth.satellite.hotelbooking.helper.OsmHelper.setMarkerIconAsPhoto(
            this,
            startMarker,
            icon!!
        )
        startMarker.setOnMarkerClickListener { marker, mapView ->
            if (marker == selectedMarker) {
                if (infoWindowVisible) {
                    hideInfoWindow(marker)
                    infoWindowVisible = false
                } else {
                    showInfoWindow(marker)
                    infoWindowVisible = true
                }
            } else {
                showInfoWindow(marker)
                hideInfoWindow(selectedMarker)
                selectedMarker = marker
                infoWindowVisible = true
            }
            true
        }
        map.overlays.add(startMarker)
    }

    private fun showInfoWindow(marker: Marker) {
        marker.showInfoWindow()

    }

    private fun hideInfoWindow(marker: Marker?) {
        if (infoWindowVisible) {
            val infoWindow = marker?.infoWindow
            if (infoWindow != null) {
                infoWindow.close()
            }
        }
    }

    private suspend fun getAddressFromLocation(lat: Double, lon: Double): String = withContext(Dispatchers.IO) {
        val geocoder = Geocoder(this@EarthMap2DActivity, Locale.getDefault())
        try {
            val addressList = geocoder.getFromLocation(lat, lon, 1)
            if (addressList != null && addressList.isNotEmpty()) {
                val address = addressList[0]
                return@withContext address.getAddressLine(0) ?: "Unknown Address"
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return@withContext "Unknown Address"
    }


    private fun performSearch(query: String) {
        if (isInternetAvailable(this)) {
            val geocoder = Geocoder(this, Locale.getDefault())
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val addressList = withContext(Dispatchers.IO) { geocoder.getFromLocationName(query, 1) }
                    if (addressList != null && addressList.isNotEmpty()) {
                        val address = addressList[0]
                        val latitudeDest = address.latitude
                        val longitudeDest = address.longitude
                        val endPoint = GeoPoint(latitudeDest, longitudeDest)

                        // Remove the previous search marker if it exists
                        searchedPlaceMarker?.let {
                            binding!!.map.overlays.remove(it)
                        }

                        // Add a marker for the searched place
                        searchedPlaceMarker = Marker(binding!!.map).apply {
                            position = GeoPoint(latitudeDest, longitudeDest)
                            val icon = BitmapFactory.decodeResource(resources, R.drawable.marker_default)
                            com.streetview.navigation.liveearth.satellite.hotelbooking.helper.OsmHelper.setMarkerIconAsPhoto(
                                this@EarthMap2DActivity, this, icon
                            )
                            markerLat = position.latitude
                            markerLng = position.longitude
                            endPointMarker = GeoPoint(markerLat, markerLng)
                            binding!!.map.overlays.add(this)

                            val infoWindowView = layoutInflater.inflate(R.layout.custom_marker_layout, null)
                            val addressTextView = infoWindowView.findViewById<TextView>(R.id.addressTextView)

                            addressTextView.text = withContext(Dispatchers.IO) {
                                getAddressFromLocation(latitudeDest, longitudeDest)
                            }

                            val infoWindow = object : InfoWindow(infoWindowView, binding!!.map) {
                                override fun onOpen(item: Any?) {
                                    // Customization logic when the info window is opened
                                }

                                override fun onClose() {
                                    // Customization logic when the info window is closed
                                }
                            }
                            this.infoWindow = infoWindow  // Assign the infoWindow to the marker
                            setOnMarkerClickListener { marker, mapView ->
                                if (marker == selectedMarker) {
                                    if (infoWindowVisible) {
                                        hideInfoWindow(marker)
                                        infoWindowVisible = false
                                    } else {
                                        showInfoWindow(marker)
                                        infoWindowVisible = true
                                    }
                                } else {
                                    showInfoWindow(marker)
                                    hideInfoWindow(selectedMarker)
                                    selectedMarker = marker
                                    infoWindowVisible = true
                                }
                                true
                            }
                        }

                        binding!!.map.controller.animateTo(GeoPoint(latitudeDest, longitudeDest))
                        binding!!.map.invalidate()
                    } else {
                        // Handle when no address is found
                        Toast.makeText(this@EarthMap2DActivity, "Address not found", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@EarthMap2DActivity, "Geocoder service not available", Toast.LENGTH_SHORT).show()
                }

                // Hide the keyboard
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding!!.sercheditText.windowToken, 0)
            }
        } else {
            Toast.makeText(this, "No internet connection available", Toast.LENGTH_SHORT).show()
        }
    }


    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val activeNetwork =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

            return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }


    private fun drawRoute() {
        binding!!.map.overlays.clear()
        CoroutineScope(Dispatchers.IO).launch {
            val roadManager: RoadManager = OSRMRoadManager(this@EarthMap2DActivity, "")
            val waypoints = ArrayList<GeoPoint>()

            if (startPoint != null && endPointMarker != null) {
                val startGeoPoint = GeoPoint(startPoint!!.latitude, startPoint!!.longitude)
                val endGeoPoint = GeoPoint(endPointMarker!!.latitude, endPointMarker!!.longitude)

                waypoints.add(startGeoPoint)
                waypoints.add(endGeoPoint)

                val road = roadManager.getRoad(waypoints)

                if (road != null) {
                    Log.d("TAG", "drawRoute: $road")

                    val roadOverlay: Polyline = RoadManager.buildRoadOverlay(road)
                    roadOverlay.color = ContextCompat.getColor(this@EarthMap2DActivity, R.color.bg)
                    roadOverlay.width = 16.0F

                    withContext(Dispatchers.Main) {
                        val mRotationGestureOverlay =
                            RotationGestureOverlay(this@EarthMap2DActivity, binding!!.map)
                        mRotationGestureOverlay.isEnabled = true
                        binding!!.map.overlays.add(mRotationGestureOverlay)
                        binding!!.map.overlays.add(roadOverlay)

                        // Add marker for the current location
                        if (binding?.map != null) {
                             currentLocationMarker = Marker(binding!!.map)
                            currentLocationMarker?.position = GeoPoint(latitude, longitude)
                        } else {
                            Log.e("MapError", "Binding or map is null. Ensure binding and map are properly initialized.")
                        }

                        val currentInfoWindowView =
                            layoutInflater.inflate(R.layout.custom_marker_layout, null)
                        val currentAddressTextView =
                            currentInfoWindowView.findViewById<TextView>(R.id.addressTextView)
                        currentAddressTextView.text = getAddressFromLocation(latitude, longitude)

                        val currentInfoWindow =
                            object : InfoWindow(currentInfoWindowView, binding!!.map) {
                                override fun onOpen(item: Any?) {
                                    // Customization logic when the info window is opened
                                }

                                override fun onClose() {
                                    // Customization logic when the info window is closed
                                }
                            }

                        currentLocationMarker?.infoWindow = currentInfoWindow
                        val currentMarkerIcon =
                            BitmapFactory.decodeResource(resources, R.drawable.currentlocarion)
                        com.streetview.navigation.liveearth.satellite.hotelbooking.helper.OsmHelper.setMarkerIconAsPhoto(
                            this@EarthMap2DActivity,
                            currentLocationMarker!!,
                            currentMarkerIcon!!
                        )
                        binding!!.map.overlays.add(currentLocationMarker)

                        // Add marker for the search location
                        val searchLocationMarker = Marker(binding!!.map)
                        searchLocationMarker.position = GeoPoint(latitudeDest, longitudeDest)

                        val searchInfoWindowView =
                            layoutInflater.inflate(R.layout.custom_marker_layout, null)
                        val searchAddressTextView =
                            searchInfoWindowView.findViewById<TextView>(R.id.addressTextView)
                        searchAddressTextView.text =
                            getAddressFromLocation(latitudeDest, longitudeDest)

                        val searchInfoWindow =
                            object : InfoWindow(searchInfoWindowView, binding!!.map) {
                                override fun onOpen(item: Any?) {
                                    // Customization logic when the info window is opened
                                }

                                override fun onClose() {
                                    // Customization logic when the info window is closed
                                }
                            }

                        searchLocationMarker.infoWindow = searchInfoWindow
                        val searchMarkerIcon =
                            BitmapFactory.decodeResource(resources, R.drawable.currentlocarion)
                        com.streetview.navigation.liveearth.satellite.hotelbooking.helper.OsmHelper.setMarkerIconAsPhoto(
                            this@EarthMap2DActivity,
                            searchLocationMarker,
                            searchMarkerIcon!!
                        )
                        binding!!.navigateBtn.visibility = View.VISIBLE

                        binding!!.map.overlays.add(searchLocationMarker)

                        binding!!.map.invalidate()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        // Handle the case when the road is null, for example, display an error message.
                        // For now, we'll just print a log message.
                        Log.e("TAG", "drawRoute: Road is null.")
                        // Hide the navigation button or display a message to the user.
                        binding!!.navigateBtn.visibility = View.GONE
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    // Handle the case when either startPoint or endPointMarker is null.
                    // For example, you can display an error message or log the issue.
                    Log.e("TAG", "drawRoute: One or both GeoPoints are null.")
                    // Hide the navigation button or display a message to the user.
                    binding!!.navigateBtn.visibility = View.GONE
                }
            }
        }
    }

    private fun streetViewBannerAdsSmall() {
        val billingHelper = StreetViewAppSoniBillingHelper(this)
        val adContainer: LinearLayout = binding!!.bannerAd.adContainer
        val adView = AdView(this)
        adView.setAdSize(StreetViewAppSoniMyAppAds.getAdSize(this))
        adView.adUnitId = StreetViewAppSoniMyAppAds.banner_admob_inApp
        if (billingHelper.isNotAdPurchased) {
            StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediation(
                adContainer, adView, this
            )
        } else {
            binding!!!!.bannerID.visibility = View.GONE
        }
    }


    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks(
                "EarthMap2DActivityOnExit",
                this@EarthMap2DActivity
            )

            StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation(
                this@EarthMap2DActivity,
                StreetViewAppSoniMyAppAds.admobInterstitialAd,
                 binding!!.whiteView
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback.remove()
    }
}
