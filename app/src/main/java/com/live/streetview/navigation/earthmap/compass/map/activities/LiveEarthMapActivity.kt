package com.live.streetview.navigation.earthmap.compass.map.activities


import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.google.android.gms.ads.AdView
import com.google.android.gms.location.FusedLocationProviderClient
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.activities.osmNavigation.OsmNavigation
import com.live.streetview.navigation.earthmap.compass.map.activities.utils.OSMTileSourceFixed
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityLiveEarthMapBinding
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
import java.util.Locale

class LiveEarthMapActivity : AppCompatActivity() {

    private var fusedLocationClientLiveEarth: FusedLocationProviderClient? = null
    private lateinit var mOSMStyle: OSMTileSourceFixed
    var locationLiveEarthMap: Location? = null
    var constraintLayout: ConstraintLayout? = null
    var lat = 0.0
    var lng = 0.0
    private var textCurrentLiveLocLiveEarth: TextView? = null
    private var textSearchLiveEarthMap: EditText? = null
    private var endPoint: GeoPoint? = null
    val carRoute = OSRMRoadManager.MEAN_BY_CAR

    var text1: String? = null
    private var waypoints = ArrayList<GeoPoint>()

    //    var mapboxNavigation: MapboxNavigation? = null
    var view: View? = null

    //    var StyleView: View? = null
    var viewGoToStreet: View? = null
    private var NaviView: View? = null
    var bindingLiveEarth: ActivityLiveEarthMapBinding? = null
    private var startPoint: GeoPoint? = null
    var imagesetalite: ImageView? = null
    var imageViewSetaliteStreet: ImageView? = null
    var imageViewded: ImageView? = null
    var imageArrowLiveEarth: ImageView? = null
    var imageLiveNight: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        bindingLiveEarth = ActivityLiveEarthMapBinding.inflate(layoutInflater)
        setContentView(bindingLiveEarth!!.root)

        this.onBackPressedDispatcher.addCallback(onBackPressedCallback)

        StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks("StreetViewLiveEarthMapOnCreate", this)

        imageClickStyleListener()
        getliveearthcurrentloc()
        imageArrowLiveEarth = findViewById(R.id.imageArrowBackLiveEarth)


        imageArrowLiveEarth!!.setOnClickListener(View.OnClickListener {
            StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks("StreetViewLiveEarthMapOnExitBtn", this)

            onBackPressedDispatcher.onBackPressed()
        })

        gpsONOFF(this)
        //initBannerAdLiveEarth();
        textCurrentLiveLocLiveEarth = findViewById(R.id.textliveloc)
        textCurrentLiveLocLiveEarth!!.setSelected(true)
        textSearchLiveEarthMap = findViewById(R.id.LiveEarthSearch)
        view = findViewById(R.id.currentviewlocearthmap)
//        constraintLayout = findViewById(R.id.styleviewmap)
        viewGoToStreet = findViewById(R.id.gotoStreet)

        viewGoToStreet!!.setOnClickListener(View.OnClickListener {
            StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks("StreetViewLiveEarthMapToStreetView", this)

            val intent = Intent(this@LiveEarthMapActivity, CountryActivities::class.java)
            intent.putExtra("status", "liveEarth")
            StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation(
                this@LiveEarthMapActivity,
                StreetViewAppSoniMyAppAds.admobInterstitialAd,
                
                intent,bindingLiveEarth!!.whiteView
            )
//            startActivity(intent)
        })
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
//

        textSearchLiveEarthMap!!.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (textSearchLiveEarthMap!!.text.isEmpty()) {
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


        NaviView = findViewById(R.id.naviview)
        NaviView!!.setOnClickListener(View.OnClickListener {
            if (endPoint != null && startPoint != null) {
                StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks("StreetViewLiveEarthMapToOsmNavigation", this)
                val intent = Intent(this, OsmNavigation::class.java)
                intent.putExtra("lat", startPoint!!.latitude)
                intent.putExtra("lng", startPoint!!.longitude)
                intent.putExtra("d_lat", endPoint!!.latitude)
                intent.putExtra("d_lng", endPoint!!.longitude)
//                startActivity(intent)
                StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation(
                    this@LiveEarthMapActivity,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,
                    
                    intent,bindingLiveEarth!!.whiteView
                )

            } else {
                Toast.makeText(
                    this@LiveEarthMapActivity,
                    "No destination found",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        view!!.setOnClickListener {
            if (startPoint != null) {
                bindingLiveEarth!!.liveearthmap.controller.animateTo(startPoint, 16.0, 1400)
            } else {
                getliveearthcurrentloc()
            }
        }
        bindingLiveEarth!!.ivWeather.setOnClickListener {
            val intent = Intent(this@LiveEarthMapActivity, WeatherActivity::class.java)
            StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation(
                this,
                StreetViewAppSoniMyAppAds.admobInterstitialAd,
                
                intent,bindingLiveEarth!!.whiteView
            )
        }
        bindingLiveEarth!!.ivRadio.setOnClickListener {
            val intent = Intent(this@LiveEarthMapActivity, FMActivity::class.java)
            StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation(
                this,
                StreetViewAppSoniMyAppAds.admobInterstitialAd,
                
                intent,bindingLiveEarth!!.whiteView
            )
        }
        streetViewBannerAdsSmall()
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks("StreetViewLiveEarthMapOnExit", this@LiveEarthMapActivity)

            StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation(
                this@LiveEarthMapActivity,
                StreetViewAppSoniMyAppAds.admobInterstitialAd,
                bindingLiveEarth!!.whiteView
            )
        }
    }

    private fun imageClickStyleListener() {
        bindingLiveEarth!!.mapLayerStyleImg.setOnClickListener {
            if (bindingLiveEarth!!.mapLayerLayout.isVisible) {
                bindingLiveEarth!!.mapLayerLayout.visibility = View.GONE
            } else {
                bindingLiveEarth!!.mapLayerLayout.visibility = View.VISIBLE
            }
        }
        bindingLiveEarth!!.trafficMap.setOnClickListener {

            OsmHelper.setStdTileProvider(this, bindingLiveEarth!!.liveearthmap)
            bindingLiveEarth!!.liveearthmap.setTileSource(TileSourceFactory.OpenTopo)

            bindingLiveEarth!!.mapLayerLayout.visibility = View.GONE
        }
        bindingLiveEarth!!.satelliteMap.setOnClickListener {
            OsmHelper.bingMapStyle(bindingLiveEarth!!.liveearthmap)
            bindingLiveEarth!!.mapLayerLayout.visibility = View.GONE
        }
        bindingLiveEarth!!.normalMapStyle.setOnClickListener {
            OsmHelper.setStdTileProvider(this, bindingLiveEarth!!.liveearthmap)
            bindingLiveEarth!!.liveearthmap.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)

            bindingLiveEarth!!.mapLayerLayout.visibility = View.GONE
        }
        bindingLiveEarth!!.darkMapStyle.setOnClickListener {
            setMapBoxMapStyleLayer()
            bindingLiveEarth!!.mapLayerLayout.visibility = View.GONE
        }

    }

    private fun setMapBoxMapStyleLayer() {
        val mapBoxTileSourceFixed = OSMTileSourceFixed("MapBoxSatelliteLabelled", 1, 19, 256)
        this.mOSMStyle = mapBoxTileSourceFixed
        mapBoxTileSourceFixed.retrieveAccessToken(this)
        (mOSMStyle as MapBoxTileSource?)!!.retrieveMapBoxMapId(this)
        bindingLiveEarth!!.liveearthmap.setTileSource(this.mOSMStyle)

    }

    private fun streetViewBannerAdsSmall() {
        val billingHelper = StreetViewAppSoniBillingHelper(this)
        val adContainer: LinearLayout = bindingLiveEarth!!.bannerAdPlace.adContainer
        val adView = AdView(this)
        adView.setAdSize(StreetViewAppSoniMyAppAds.getAdSize(this))
        adView.adUnitId = StreetViewAppSoniMyAppAds.banner_admob_inApp
        if (billingHelper.isNotAdPurchased) {
            StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediation(
                adContainer, adView, this
            )
        } else {
            bindingLiveEarth!!.bannerID.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 1) {

        } else if (requestCode == 302) {
            gpsONOFF(this)
            getliveearthcurrentloc()
        }
    }

    private fun getliveearthcurrentloc() {
        fusedLocationClientLiveEarth = FusedLocationProviderClient(this)
        locationLiveEarthMap = Location("service Provider")
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
        fusedLocationClientLiveEarth!!.lastLocation.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                locationLiveEarthMap = task.result
                if (locationLiveEarthMap == null) {
                    gpsONOFF(this@LiveEarthMapActivity)
                    //    Toast.makeText(LiveEarthMapActivity.this, "Permission not granted", Toast.LENGTH_LONG).show();
                } else {
                    lat = locationLiveEarthMap!!.latitude
                    lng = locationLiveEarthMap!!.longitude
                    initializeMap(lat, lng)
                    myliveearthloc()
                    Log.d(
                        ContentValues.TAG,
                        "onComplete: " + locationLiveEarthMap!!.latitude + "," + locationLiveEarthMap!!.longitude
                    )
                }
            }
        }
    }

    fun gpsONOFF(context: Context) {
        val lm = context.getSystemService(LOCATION_SERVICE) as LocationManager
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle("Enable GPS")
            alertDialog.setMessage("Your GPS setting is not enabled. Please enabled it in settings menu.")
            alertDialog.setPositiveButton("Settings") { dialog, which ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(intent, 302)
            }
            alertDialog.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
            val alert = alertDialog.create()
            alert.show()
        }
    }


    private fun myliveearthloc() {

        try {
            val geo = Geocoder(this@LiveEarthMapActivity.applicationContext, Locale.getDefault())
            val addresses = geo.getFromLocation(lat, lng, 4)

            //latituteField.setText("Loading...");
            if (addresses != null && addresses.size > 0) {
                val locality = addresses[0].getAddressLine(0)
                text1 = locality
                textCurrentLiveLocLiveEarth!!.text = locality
                val country = addresses[0].countryName
                val state = addresses[0].adminArea
                val sub_admin = addresses[0].subAdminArea
                val city = addresses[0].featureName
                val pincode = addresses[0].postalCode
                val locality_city = addresses[0].locality
                val sub_localoty = addresses[0].subLocality
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        var origin: Point? = null
        var destination: Point? = null
    }

    private fun initializeMap(lat: Double, lng: Double) {
        bindingLiveEarth!!.liveearthmap.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        bindingLiveEarth!!.liveearthmap.setBuiltInZoomControls(true)
        bindingLiveEarth!!.liveearthmap.setMultiTouchControls(true)
        val mRotationGestureOverlay = RotationGestureOverlay(this, bindingLiveEarth!!.liveearthmap)
        mRotationGestureOverlay.isEnabled = true
        bindingLiveEarth!!.liveearthmap.overlays.add(mRotationGestureOverlay)
        val mapController: IMapController = bindingLiveEarth!!.liveearthmap.controller
        mapController.setZoom(6)
        bindingLiveEarth!!.liveearthmap.maxZoomLevel=19.0
        startPoint = GeoPoint(lat, lng)
        mapController.setCenter(startPoint)

        bindingLiveEarth!!.liveearthmap.getZoomController().setVisibility(
            CustomZoomButtonsController.Visibility.NEVER
        );
        // val mapshowpoint = GeoPoint(51.5072, 0.1276)
        //marker
        val marker = Marker(bindingLiveEarth!!.liveearthmap)
        marker.title = ("Current Location")
        marker.textLabelFontSize = 11
        //must set the icon to null last
        marker.icon = null
        marker.position = startPoint
        val icon = BitmapFactory.decodeResource(resources, R.drawable.currentlocarion)
        setMarkerIconAsPhoto(marker, icon!!)
        bindingLiveEarth!!.liveearthmap.overlays.add(marker)
        bindingLiveEarth!!.liveearthmap.invalidate()
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

    private fun getlocation() {

        val geocoder = Geocoder(this)
        val addressList: List<Address>

        try {
            addressList =
                geocoder.getFromLocationName(
                    textSearchLiveEarthMap!!.text.toString(),
                    1
                ) as List<Address>
            if (addressList.isEmpty()) {
                Toast.makeText(this, "Please Enter valid address", Toast.LENGTH_SHORT).show()
//                binding.txtDestination.text = "Latitude: $Lat | Longitude: $Long"
            } else {
//                Destinationlat = addressList[0].latitude
//                Destinationlong = addressList[0].longitude
            }
            if (addressList.isNotEmpty()) {
                bindingLiveEarth!!.progressBar.visibility = View.VISIBLE
                endPoint = GeoPoint(addressList.get(0).latitude, addressList.get(0).longitude)
                NaviView?.visibility = View.VISIBLE
                bindingLiveEarth!!.imageView8.visibility = View.VISIBLE
                GlobalScope.launch(Dispatchers.IO) {
                    showroute(carRoute)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private suspend fun showroute(type: String) {
        try {
            bindingLiveEarth!!.liveearthmap.overlays.clear()
            val mRotationGestureOverlay =
                RotationGestureOverlay(this, bindingLiveEarth!!.liveearthmap)
            mRotationGestureOverlay.isEnabled = true
            bindingLiveEarth!!.liveearthmap.overlays.add(mRotationGestureOverlay)
            val MY_USERAGENT = "com.beview.mygeoapp"
//        val roadManager: RoadManager = OSRMRoadManager(this, "MY_USER_AGENT")
            val roadManager: RoadManager = OSRMRoadManager(this, MY_USERAGENT)
            (roadManager as OSRMRoadManager).setMean(type)
            waypoints = ArrayList<GeoPoint>()
            waypoints.add(startPoint!!)
            waypoints.add(endPoint!!)
            //marker
            val marker = Marker(bindingLiveEarth!!.liveearthmap)
            marker.title = ("Current Location")
            marker.textLabelFontSize = 11
            //must set the icon to null last
            marker.icon = null
            marker.position = startPoint
            val icon = BitmapFactory.decodeResource(resources, R.drawable.currentlocarion)
            setMarkerIconAsPhoto(marker, icon!!)
            bindingLiveEarth!!.liveearthmap.overlays.add(marker)
            //marker
            val markerend = Marker(bindingLiveEarth!!.liveearthmap)
            markerend.title = ("destination Location")
            marker.textLabelFontSize = 11
            markerend.icon = null
            markerend.position = endPoint
            val iconend = BitmapFactory.decodeResource(resources, R.drawable.currentlocarion)
            setMarkerIconAsPhoto(markerend, iconend!!)
            bindingLiveEarth!!.liveearthmap.overlays.add(markerend)
            var road: Road = roadManager.getRoad(waypoints)
//            CalculateDistnce(roadManager, waypoints)
//            CalculateDistnce(roadManager, waypoints)
            withContext(Dispatchers.Main)
            {
                val line: Polyline = RoadManager.buildRoadOverlay(road)
                line.color = Color.BLUE
                line.width = 16.0F
                line.outlinePaint.colorFilter
                bindingLiveEarth!!.liveearthmap.overlays.add(line)
                bindingLiveEarth!!.liveearthmap.invalidate()
                bindingLiveEarth!!.liveearthmap.controller.animateTo(startPoint, 12.0, 1400)
                bindingLiveEarth!!.progressBar.visibility = View.GONE
            }

        } catch (E: NullPointerException) {

        }
    }
}