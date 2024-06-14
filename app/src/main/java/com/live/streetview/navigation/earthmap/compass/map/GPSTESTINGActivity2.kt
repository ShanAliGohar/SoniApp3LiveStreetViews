    package com.live.streetview.navigation.earthmap.compass.map

    import android.app.Activity
    import android.content.Context
    import android.content.Intent
    import android.content.IntentSender
    import android.graphics.Bitmap
    import android.graphics.BitmapFactory
    import android.graphics.Canvas
    import android.graphics.Color
    import android.graphics.drawable.BitmapDrawable
    import android.location.Address
    import android.location.Geocoder
    import android.location.Location
    import android.location.LocationManager
    import android.net.ConnectivityManager
    import android.net.NetworkCapabilities
    import android.os.Build
    import android.os.Bundle
    import android.preference.PreferenceManager
    import android.provider.Settings
    import android.speech.RecognizerIntent
    import android.util.Log
    import android.view.View
    import android.view.inputmethod.EditorInfo
    import android.view.inputmethod.InputMethodManager
    import android.widget.LinearLayout
    import android.widget.TextView
    import android.widget.Toast
    import androidx.activity.OnBackPressedCallback
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.appcompat.app.AlertDialog
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.view.isVisible
    import com.google.android.gms.ads.AdView
    import com.google.android.gms.common.api.ResolvableApiException
    import com.google.android.gms.location.LocationRequest
    import com.google.android.gms.location.LocationServices
    import com.google.android.gms.location.LocationSettingsRequest
    import com.google.android.gms.location.LocationSettingsResponse
    import com.google.android.gms.tasks.OnFailureListener
    import com.google.android.gms.tasks.OnSuccessListener
    import com.google.android.gms.tasks.Task
    import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
    import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
    import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds.getAdSize
    import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds
    import com.live.streetview.navigation.earthmap.compass.map.activities.DistanceCalculatorActivity
    import com.live.streetview.navigation.earthmap.compass.map.activities.NewAreaCalculatorActivity
    import com.live.streetview.navigation.earthmap.compass.map.activities.SearchPlacesActivity
    import com.live.streetview.navigation.earthmap.compass.map.activities.osmNavigation.OsmNavigation
    import com.live.streetview.navigation.earthmap.compass.map.activities.utils.LocationRepository
    import com.live.streetview.navigation.earthmap.compass.map.activities.utils.MyLocattionListener
    import com.live.streetview.navigation.earthmap.compass.map.activities.utils.OSMTileSourceFixed
    import com.live.streetview.navigation.earthmap.compass.map.activities.utils.UtlilityClassByKotlin
    import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityGpstestingactivity2Binding
    import com.live.streetview.navigation.earthmap.compass.map.databinding.MapLayerDialogBinding
    import com.streetview.liveearth.satellitemap.gpsnavigation.app.utilites.GeocoderCoroutineCloneTask
    import com.streetview.navigation.liveearth.satellite.hotelbooking.helper.OsmHelper
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.GlobalScope
    import kotlinx.coroutines.launch
    import kotlinx.coroutines.withContext
    import org.osmdroid.api.IMapController
    import org.osmdroid.bonuspack.routing.OSRMRoadManager
    import org.osmdroid.bonuspack.routing.Road
    import org.osmdroid.bonuspack.routing.RoadManager
    import org.osmdroid.config.Configuration
    import org.osmdroid.tileprovider.tilesource.MapBoxTileSource
    import org.osmdroid.tileprovider.tilesource.TileSourceFactory
    import org.osmdroid.util.GeoPoint
    import org.osmdroid.views.CustomZoomButtonsController
    import org.osmdroid.views.overlay.Marker
    import org.osmdroid.views.overlay.Polyline
    import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
    import java.io.IOException
    import java.text.DecimalFormat
    import kotlin.collections.ArrayList

    class GPSTESTINGActivity2 : AppCompatActivity() {
        var bindingGpsNavi: ActivityGpstestingactivity2Binding? = null
        private var startPoint: GeoPoint? = null

        lateinit var myRepository: LocationRepository
        private var latitude_global: Double = 0.0
        private var longitude_global: Double = 0.0
        private var tolatitude: Double = 0.0
        private lateinit var mOSMStyle: OSMTileSourceFixed
        private var tolongitude: Double = 0.0
        val carRoute = OSRMRoadManager.MEAN_BY_CAR
        private var mapLayerView: MapLayerDialogBinding? = null
        private var isLayerShowing: Boolean = true
        private var defaultMap: LinearLayout? = null
        private var satellite: LinearLayout? = null
        private var dark: LinearLayout? = null
        private var waypoints = ArrayList<GeoPoint>()
        private var endPoint: GeoPoint? = null
        val TAG = "GPS_NavigationActivity"
        private var road: Road? = null
        private var result: Double = 0.0

        private var duration : Double? = null

        /////////////////////////////////////////////////////////
        private var fromActivity: String = "GPS"
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            val ctx = applicationContext
            Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
            bindingGpsNavi = ActivityGpstestingactivity2Binding.inflate(layoutInflater)
            setContentView(bindingGpsNavi!!.root)
            this.onBackPressedDispatcher.addCallback(onBackPressedCallback)
            StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks(
                "StreetViewDisplayStreetGpsNavigationOnCreate",
                this@GPSTESTINGActivity2
            )

            imageClickStyleListener()
            bindingGpsNavi!!.destination.isSelected = true
            try {
                val data = intent.extras!!.getString("data")
                Log.d(TAG, "onCreate: $data")
                if (data == "voice") {
                    bindingGpsNavi!!.imageView37.visibility = View.VISIBLE
                } else {
                    bindingGpsNavi!!.imageView37.visibility = View.GONE
                }
            } catch (E: NullPointerException) {
            }
            bindingGpsNavi!!.destination.setOnClickListener {
                val intent = Intent(this, SearchPlacesActivity::class.java)
                startActivityForResult(intent, 100)
            }
            val speechRecognitionLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        // Handle the result here
                        val data: Intent? = result.data
                        // Retrieve the recognized text
                        val recognizedText =
                            data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                        // Display the recognized text in the TextView
                        recognizedText?.let {
                            bindingGpsNavi?.destination?.text = it[0] // Assuming you have a TextView named yourDestinationTextView
                            getlocation()

                        }
                    }
                }
            bindingGpsNavi!!.imageView37.setOnClickListener {

                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                speechRecognitionLauncher.launch(intent)
            }

            gpsONOFF(this)
            if (isNetworkAvailable(this)) {
                getYourCurrentLocation()
                carThemeFun()
                ///////////////////////////////////////


                fromActivity = intent.getStringExtra("fromActivity").toString()
                var strLocation: String = intent.getStringExtra("strLocation").toString()
                if (fromActivity == "liveEarth") {
                    tolatitude = intent.getDoubleExtra("fromLatitude", 0.0)
                    tolongitude = intent.getDoubleExtra("fromLongitude", 0.0)
                    var curlatitude: Double = intent.getDoubleExtra("curLatitude", 0.0)
                    var curlongitude: Double = intent.getDoubleExtra("curLongitude", 0.0)

                    Log.d("okg", "fromLatitude--fromLongitude: $tolatitude next $tolongitude")
                    Log.d("okg", "currnet: $curlatitude next $curlongitude")

    //                bindingGpsNavi!!.destination.text = strLocation


                    bindingGpsNavi!!.constraintLayoutbikecarwalk.visibility = View.VISIBLE
                    bindingGpsNavi!!.consraintgpstime.visibility = View.VISIBLE
                    bindingGpsNavi!!.consraintgpstime.animate().setDuration(1000).alpha(1f)


                    UtlilityClassByKotlin.liveErthdistination =
                        GeoPoint(tolatitude, tolongitude)
                    UtlilityClassByKotlin.liveErthOrigin = GeoPoint(curlatitude, curlongitude)
                }

            } else {
                Toast.makeText(this, "No Connection available", Toast.LENGTH_SHORT).show()
            }
            clickListeners()


            bindingGpsNavi!!.ivDistance.setOnClickListener {
                val intent = Intent(this, DistanceCalculatorActivity::class.java)
                StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation(
                    this@GPSTESTINGActivity2,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,

                    intent, bindingGpsNavi!!.view
                )
            }
            bindingGpsNavi!!.ivArea.setOnClickListener {
                val intent = Intent(this, NewAreaCalculatorActivity::class.java)
                StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation(
                    this@GPSTESTINGActivity2,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,

                    intent, bindingGpsNavi!!.view
                )
            }
            streetViewBannerAdsSmall()
            bindingGpsNavi!!.imageView38.setOnClickListener {
                StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks(
                    "StreetViewDisplayStreetGpsNavigationOnBtnClick",
                    this@GPSTESTINGActivity2
                )

                onBackPressedDispatcher.onBackPressed()
            }
        }


        private fun imageClickStyleListener() {
            bindingGpsNavi!!.mapLayerStyleImg.setOnClickListener {
                if (bindingGpsNavi!!.mapLayerLayout.isVisible) {
                    bindingGpsNavi!!.mapLayerLayout.visibility = View.GONE
                } else {
                    bindingGpsNavi!!.mapLayerLayout.visibility = View.VISIBLE
                }
            }
            bindingGpsNavi!!.trafficMap.setOnClickListener {

                OsmHelper.setStdTileProvider(this, bindingGpsNavi!!.mapView)
                bindingGpsNavi!!.mapView.setTileSource(TileSourceFactory.OpenTopo)

                bindingGpsNavi!!.mapLayerLayout.visibility = View.GONE
            }
            bindingGpsNavi!!.satelliteMap.setOnClickListener {
                OsmHelper.mapStyle(bindingGpsNavi!!.mapView)
                bindingGpsNavi!!.mapLayerLayout.visibility = View.GONE
            }
            bindingGpsNavi!!.normalMapStyle.setOnClickListener {
                OsmHelper.setStdTileProvider(this, bindingGpsNavi!!.mapView)
                bindingGpsNavi!!.mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)

                bindingGpsNavi!!.mapLayerLayout.visibility = View.GONE
            }
            bindingGpsNavi!!.darkMapStyle.setOnClickListener {
                setMapBoxMapStyleLayer()
                bindingGpsNavi!!.mapLayerLayout.visibility = View.GONE
            }

        }

        private fun setMapBoxMapStyleLayer() {
            val mapBoxTileSourceFixed = OSMTileSourceFixed("MapBoxSatelliteLabelled", 1, 19, 256)
            this.mOSMStyle = mapBoxTileSourceFixed
            mapBoxTileSourceFixed.retrieveAccessToken(this)
            (mOSMStyle as MapBoxTileSource?)!!.retrieveMapBoxMapId(this)
            bindingGpsNavi!!.mapView.setTileSource(this.mOSMStyle)

        }

        private fun streetViewBannerAdsSmall() {
            val billingHelper =
                StreetViewAppSoniBillingHelper(
                    this
                )
            val adContainer: LinearLayout = bindingGpsNavi!!.bannerAdPlace.adContainer
            val adView = AdView(this)
            adView.setAdSize(getAdSize(this))
            adView.adUnitId = StreetViewAppSoniMyAppAds.banner_admob_inApp
            if (billingHelper.isNotAdPurchased) {
                StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediation(
                    adContainer, adView, this
                )
            } else {
                bindingGpsNavi!!.bannerID.visibility = View.GONE
            }
        }

        private val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks(
                    "StreetViewDisplayStreetGpsNavigationOnClick",
                    this@GPSTESTINGActivity2
                )

                StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation(
                    this@GPSTESTINGActivity2,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,
                    bindingGpsNavi!!.whiteView
                )
            }
        }


        private fun clickListeners() {
            bindingGpsNavi!!.destination.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (bindingGpsNavi!!.destination.text.isEmpty()) {
                        Toast.makeText(this, "Please Enter Address", Toast.LENGTH_SHORT).show()
                    } else {
                        val view = this.currentFocus
                        if (view != null) {
                            val imm: InputMethodManager =
                                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(view.windowToken, 0)
                        }
                        getlocation()

                    }

                    return@OnEditorActionListener true
                }
                false
            })

            bindingGpsNavi!!.cardNavigation.setOnClickListener {

                //  bindingGpsNavi!!.consraintgpstime.visibility = View.GONE
                // Toast.makeText(this, "No destination found", Toast.LENGTH_SHORT).show()
                if (latitude_global == 0.0 && longitude_global == 0.0) {
                    Toast.makeText(this, "No destination found", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else {
                    StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks(
                        "StreetViewGpsNavigationStartClickONNavigation",
                        this
                    )

                    if (fromActivity == "liveEarth") {
                        // val intent = Intent(this, NewNavigationCloneActivity::class.java)

                        if (duration == 0.0){
                            Toast.makeText(this@GPSTESTINGActivity2, "No route found, Can not navigate", Toast.LENGTH_SHORT).show()
                        }else{
                            val intent = Intent(this, OsmNavigation::class.java)
                            intent.putExtra("lat", startPoint!!.latitude)
                            intent.putExtra("lng", startPoint!!.longitude)
                            intent.putExtra("d_lat", endPoint!!.latitude)
                            intent.putExtra("d_lng", endPoint!!.longitude)
    //                    startActivity(intent)
                            StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation(
                                this@GPSTESTINGActivity2,
                                StreetViewAppSoniMyAppAds.admobInterstitialAd,

                                intent, bindingGpsNavi!!.view
                            )
                        }

                    } else
                    /* if (fromActivity == "GPS")*/ {

                        val intent = Intent(this, OsmNavigation::class.java)
                        intent.putExtra("lat", startPoint!!.latitude)
                        intent.putExtra("lng", startPoint!!.longitude)
                        intent.putExtra("d_lat", endPoint!!.latitude)
                        intent.putExtra("d_lng", endPoint!!.longitude)
    //                    startActivity(intent)
                        StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation(
                            this@GPSTESTINGActivity2,
                            StreetViewAppSoniMyAppAds.admobInterstitialAd,

                            intent, bindingGpsNavi!!.view
                        )
                    }

                }

            }
            bindingGpsNavi!!.constraintWalk.setOnClickListener {


                bindingGpsNavi!!.bikeIdClick.setBackgroundDrawable(getDrawable(R.drawable.gpswhitebackground))
                bindingGpsNavi!!.carIdClick.setBackgroundDrawable(getDrawable(R.drawable.gpswhitebackground))
                bindingGpsNavi!!.walkIdClick.setBackgroundDrawable(getDrawable(R.drawable.gpsbluebackground))

                bindingGpsNavi!!.textView24.setTextColor(resources.getColor(R.color.black))
                bindingGpsNavi!!.textView23.setTextColor(resources.getColor(R.color.black))
                bindingGpsNavi!!.textView25.setTextColor(resources.getColor(R.color.white))

                bindingGpsNavi!!.imageView3.setImageDrawable(getDrawable(R.drawable.car_b))
                bindingGpsNavi!!.bikeImg.setImageDrawable(getDrawable(R.drawable.bike_b))
                bindingGpsNavi!!.walkImg.setImageDrawable(getDrawable(R.drawable.walk_w))

    //            }
            }

            bindingGpsNavi!!.constraintbike.setOnClickListener {
                bindingGpsNavi!!.walkIdClick.background =
                    resources.getDrawable(R.drawable.gpswhitebackground)
                bindingGpsNavi!!.carIdClick.background =
                    resources.getDrawable(R.drawable.gpswhitebackground)
                bindingGpsNavi!!.bikeIdClick.background =
                    resources.getDrawable(R.drawable.gpsbluebackground)

                bindingGpsNavi!!.walkImg.setImageDrawable(resources.getDrawable(R.drawable.walk_b))
                bindingGpsNavi!!.imageView3.setImageDrawable(resources.getDrawable(R.drawable.car_b))
                bindingGpsNavi!!.bikeImg.setImageDrawable(resources.getDrawable(R.drawable.bike_w))

                bindingGpsNavi!!.textView25.setTextColor(resources.getColor(R.color.black))
                bindingGpsNavi!!.textView24.setTextColor(resources.getColor(R.color.black))
                bindingGpsNavi!!.textView23.setTextColor(resources.getColor(R.color.white))
    //            }
            }

            bindingGpsNavi!!.constraintViewcar.setOnClickListener {

    //            if (routeCheck(drivingRoute, "Car")) {
    //                UtlilityClassByKotlin.routStyleValue = DirectionsCriteria.PROFILE_DRIVING
    //                lastSelectedDirectionsProfile = DirectionsCriteria.PROFILE_DRIVING
    //                showRouteLine()
    //                carThemeFun()
    //            }
            }
            bindingGpsNavi!!.currentviewgps.setOnClickListener {

                if (startPoint != null) {
                    bindingGpsNavi!!.mapView.controller.animateTo(startPoint, 16.0, 1600)
                } else {
                    getYourCurrentLocation()
                }

                // back on UI thread
    //            }
            }


        }

        private fun getlocation() {
            bindingGpsNavi!!.progressBar.visibility = View.VISIBLE
            val geocoder = Geocoder(this)
            val addressList: List<Address>

            try {
                addressList =
                    (geocoder.getFromLocationName(
                        bindingGpsNavi!!.destination.text.toString(),
                        1
                    ) as List<Address>)!!
                if (addressList.isEmpty()) {
                    Toast.makeText(this, "Please Enter valid address", Toast.LENGTH_SHORT).show()
                } else if (addressList.isNotEmpty()) {
                    endPoint = GeoPoint(addressList.get(0).latitude, addressList.get(0).longitude)
                    GlobalScope.launch(Dispatchers.IO) {
                        if (startPoint != null && endPoint != null) {
                            showroute(carRoute)
                        }

                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        private suspend fun showroute(type: String) {
            try {
                bindingGpsNavi!!.mapView.overlays.clear()
                val mRotationGestureOverlay =
                    RotationGestureOverlay(this, bindingGpsNavi!!.mapView)
                mRotationGestureOverlay.isEnabled = true
                bindingGpsNavi!!.mapView.overlays.add(mRotationGestureOverlay)
                val MY_USERAGENT = "com.beview.mygeoapp"
                val roadManager: RoadManager = OSRMRoadManager(this, MY_USERAGENT)
                (roadManager as OSRMRoadManager).setMean(type)
                waypoints = ArrayList<GeoPoint>()
                waypoints.add(startPoint!!)
                waypoints.add(endPoint!!)
                //marker
                val marker = Marker(bindingGpsNavi!!.mapView)
                marker.title = ("Current Location")
                marker.textLabelFontSize = 11
                //must set the icon to null last
                marker.icon = null
                marker.position = startPoint
                val icon = BitmapFactory.decodeResource(resources, R.drawable.currentlocarion)
                setMarkerIconAsPhoto(marker, icon!!)
                bindingGpsNavi!!.mapView.overlays.add(marker)
                //marker
                val markerend = Marker(bindingGpsNavi!!.mapView)
                markerend.title = ("destination Location")
                marker.textLabelFontSize = 11
                markerend.icon = null
                markerend.position = endPoint
                val iconend = BitmapFactory.decodeResource(resources, R.drawable.currentlocarion)
                setMarkerIconAsPhoto(markerend, iconend!!)
                bindingGpsNavi!!.mapView.overlays.add(markerend)
                var road: Road? = null
                road = roadManager.getRoad(waypoints)
                CalculateDistnce(roadManager, waypoints)
    //            CalculateDistnce(roadManager, waypoints)
                withContext(Dispatchers.Main)
                {
                    val line: Polyline = RoadManager.buildRoadOverlay(road)
                    line.color = Color.BLUE
                    line.width = 16.0F
                    line.outlinePaint.colorFilter
                    bindingGpsNavi!!.mapView.overlays.add(line)
                    bindingGpsNavi!!.mapView.invalidate()
                    bindingGpsNavi!!.mapView.controller.animateTo(startPoint, 12.0, 1400)
                    bindingGpsNavi!!.consraintgpstime.visibility = View.VISIBLE
                    bindingGpsNavi!!.progressBar.visibility = View.GONE
                }

            } catch (E: NullPointerException) {

            }
        }

        private suspend fun CalculateDistnce(
            roadManager: RoadManager,
            waypoints: ArrayList<GeoPoint>
        ) {
            road = roadManager.getRoad(waypoints)
            if (road!!.mLength >= 100.0) {
                result = road!!.mLength
            } else if (road!!.mLength >= 1.0) {
                result = Math.round(road!!.mLength * 10) / 10.0
            } else {
                result = road!!.mLength * 1000
            }
            withContext(Dispatchers.Main) {
                val res = DecimalFormat("#.#").format(result)
                bindingGpsNavi!!.textViewkm.text = res.toString() + "km"

               duration = road!!.mDuration
                if (duration == 0.0) {
                    Toast.makeText(applicationContext, "No Route found", Toast.LENGTH_SHORT).show()
                    bindingGpsNavi!!.textViewkm.text= "No Route found"
                } else {
                    bindingGpsNavi!!.textViewmin.text = calculateTime(duration!!.toInt())
                }
            }
        }

        private fun calculateTime(n: Int): String {
            var timeString = ""
            var d = n
            val day = d / (24 * 3600)
            d %= (24 * 3600)
            val hour = d / 3600
            d %= 3600
            val minutes = d / 60
            d %= 60
            val seconds = n
            if (day == 0) {
                Log.d("TAG", "convertSecondDay  poi:-----546------------- $minutes")
                timeString = "$hour hrs $minutes minutes"

                if (hour == 0) {
                    Log.d("TAG", "convertSecondDay poi1:-----549------------- $minutes")

                    timeString = "$minutes minutes"
                }
            } else {
                Log.d("TAG", "convertSecondDay poi2:-----554------------- $minutes")

                timeString = "$day d $hour hrs $minutes minutes"
            }
            return timeString
        }

        private fun carThemeFun() {
            bindingGpsNavi!!.walkIdClick.background =
                resources.getDrawable(R.drawable.gpswhitebackground)
            bindingGpsNavi!!.carIdClick.background = resources.getDrawable(R.drawable.gpsbluebackground)
            bindingGpsNavi!!.bikeIdClick.background =
                resources.getDrawable(R.drawable.gpswhitebackground)

            bindingGpsNavi!!.walkImg.setImageDrawable(resources.getDrawable(R.drawable.walk_b))
            bindingGpsNavi!!.imageView3.setImageDrawable(resources.getDrawable(R.drawable.car_w))
            bindingGpsNavi!!.bikeImg.setImageDrawable(resources.getDrawable(R.drawable.bike_b))

            bindingGpsNavi!!.textView25.setTextColor(resources.getColor(R.color.black))
            bindingGpsNavi!!.textView24.setTextColor(resources.getColor(R.color.white))
            bindingGpsNavi!!.textView23.setTextColor(resources.getColor(R.color.black))
        }


        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == Activity.RESULT_OK && requestCode == 1) {
                fromActivity = "GPS"

                bindingGpsNavi!!.consraintgpstime.visibility = View.VISIBLE
                bindingGpsNavi!!.consraintgpstime.animate().setDuration(1000).alpha(1f)

            } else if (requestCode == 301) {
                try {
                    val arrayList: ArrayList<String> =
                        data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
                    val voiceText: String = arrayList.get(0)
                    if (!voiceText.isNullOrEmpty()) {
                        bindingGpsNavi!!.destination.setText(voiceText, TextView.BufferType.EDITABLE);
                        getlocation()
                    }
                } catch (e: NullPointerException) {

                }
            } else if (resultCode == Activity.RESULT_OK && requestCode == 100) {
                val lng = data!!.getDoubleExtra("d_lng", 0.0)
                val lat = data!!.getDoubleExtra("d_lat", 0.0)
                if (lng != null) {
                    bindingGpsNavi!!.progressBar.visibility = View.VISIBLE
                    bindingGpsNavi!!.destination.text = data.getStringExtra("place")
                    endPoint = GeoPoint(lat, lng)
                    GlobalScope.launch(Dispatchers.IO) {
                        if (startPoint != null && endPoint != null) {
                            showroute(carRoute)
                        }

                    }
                }

                Log.d(TAG, "onActivityResult: lng${lng}")
            } else
                if (requestCode == 302) {
                    checkingGPS()
                    getYourCurrentLocation()
                }
        }

        private fun getYourCurrentLocation() {
            myRepository = LocationRepository(this, object : MyLocattionListener {
                override fun onLocationChanged(location: Location) {
                    if (location != null) {
                        latitude_global = location.latitude
                        longitude_global = location.longitude
                        val latLng = GeoPoint(location.latitude, location.longitude)
                        Thread {
                            runOnUiThread(Runnable {
                                // progressBar.visibility = View.GONE
                                getCurrentLocationInTextForm(latLng)
                                if (latitude_global != null) {
                                    startPoint = GeoPoint(location.latitude, location.longitude)
                                    initializeMap(latitude_global, longitude_global)
                                }

                                myRepository.stopLocation()
                            })

                        }.start()

                        UtlilityClassByKotlin.gpsOrigin =
                            GeoPoint(latitude_global, longitude_global)
                        myRepository.stopLocation()
                    } else {
                        myRepository.startLocation()
                    }
                }
            })

        }

        private fun initializeMap(lat: Double, lng: Double) {
            bindingGpsNavi!!.mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
            bindingGpsNavi!!.mapView.setBuiltInZoomControls(true)
            bindingGpsNavi!!.mapView.setMultiTouchControls(true)
            val mRotationGestureOverlay = RotationGestureOverlay(this, bindingGpsNavi!!.mapView)
            mRotationGestureOverlay.isEnabled = true
            bindingGpsNavi!!.mapView.overlays.add(mRotationGestureOverlay)
            val mapController: IMapController = bindingGpsNavi!!.mapView.controller
    //        startPoint = GeoPoint(lat, lng)
            mapController.animateTo(startPoint, 12.0, 1500)
            mapController.setCenter(startPoint)
            bindingGpsNavi!!.mapView.maxZoomLevel = 19.0
            bindingGpsNavi!!.mapView.getZoomController().setVisibility(
                CustomZoomButtonsController.Visibility.NEVER
            );
            //marker
            val marker = Marker(bindingGpsNavi!!.mapView)
            marker.title = ("Current Location")
            marker.textLabelFontSize = 11
            //must set the icon to null last
            marker.icon = null
            marker.position = startPoint
            val icon = BitmapFactory.decodeResource(resources, R.drawable.currentlocarion)
            setMarkerIconAsPhoto(marker, icon!!)
            bindingGpsNavi!!.mapView.overlays.add(marker)
            bindingGpsNavi!!.mapView.invalidate()
        }

        //load marker
        fun setMarkerIconAsPhoto(marker: Marker, thumbnail: Bitmap) {
            var thumbnail = thumbnail
            val borderSize = 2
            thumbnail = Bitmap.createScaledBitmap(thumbnail, 90, 90, true)
            val withBorder = Bitmap.createBitmap(
                thumbnail.width + borderSize * 2,
                thumbnail.height + borderSize * 2,
                thumbnail.config
            )
            val canvas = Canvas(withBorder)
            canvas.drawColor(Color.TRANSPARENT)
            canvas.drawBitmap(thumbnail, borderSize.toFloat(), borderSize.toFloat(), null)
            val icon = BitmapDrawable(resources, withBorder)
            marker.icon = icon
        }

        private fun getCurrentLocationInTextForm(latLng: GeoPoint) {
            val task = GeocoderCoroutineCloneTask(this@GPSTESTINGActivity2, latLng,
                object : GeocoderCoroutineCloneTask.GeoTaskCallback {
                    override fun onFailedLocationFetched() {

                    }

                    override fun onSuccessLocationFetched(fetchedAddress: String?) {
                        if (fetchedAddress != null && fetchedAddress.length > 0) {
                            var address = fetchedAddress
                            //binding!!.tvCurrentLoc.text = address + ""
    //                        setLocationMarker(latLng, mapbox_global!!)
                            bindingGpsNavi!!.currentlocGps.isSelected = true
                            bindingGpsNavi!!.currentlocGps.setText(address + "")
                        }
                    }
                })
            task.execute()


        }


        fun ConvertSectoDay(n: Int) {
            bindingGpsNavi!!.textViewmin.isSelected = true
            var n = n
            val day = n / (24 * 3600)
            n %= (24 * 3600)
            val hour = n / 3600
            n %= 3600
            val minutes = n / 60
            n %= 60
            val seconds = n
            if (day == 0) {

                bindingGpsNavi!!.textViewmin.text = "$hour Hr $minutes min"
                if (hour == 0) {
                    bindingGpsNavi!!.textViewmin.text = "$minutes min"
                }
            } else {
                bindingGpsNavi!!.textViewmin.text = "$day Day $hour Hr $minutes min"
            }

        }


        private fun checkingGPS() {
            //check if gps is enabled or not and then request user to enable it
            val locationRequest: LocationRequest = LocationRequest.create()
            locationRequest.interval = 10000
            locationRequest.fastestInterval = 5000
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            val settingsClient = LocationServices.getSettingsClient(this)
            val task: Task<LocationSettingsResponse> =
                settingsClient.checkLocationSettings(builder.build())
            task.addOnSuccessListener(this,
                OnSuccessListener<LocationSettingsResponse?> {

                })
            task.addOnFailureListener(this, OnFailureListener { e ->
                if (e is ResolvableApiException) {
                    try {
                        e.startResolutionForResult(this, 51)
                    } catch (e1: IntentSender.SendIntentException) {
                        e1.printStackTrace()
                    }
                }
            })
        }

        fun isNetworkAvailable(context: Context?): Boolean {
            if (context == null) return false
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    when {
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            return true
                        }

                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            return true
                        }

                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                            return true
                        }
                    }
                }
            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    return true
                }
            }
            return false
        }

        fun gpsONOFF(context: Context) {
            val lm = context.getSystemService(LOCATION_SERVICE) as LocationManager
            if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.setTitle("Enable GPS")
                alertDialog.setMessage("Your GPS setting is not enabled. Please enabled it in settings menu.")
                alertDialog.setPositiveButton(
                    "Settings"
                ) { dialog, which ->
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivityForResult(intent, 302)
                }
                alertDialog.setNegativeButton(
                    "Cancel"
                ) { dialog, which -> dialog.cancel() }
                val alert = alertDialog.create()
                alert.show()
            }
        }

    }