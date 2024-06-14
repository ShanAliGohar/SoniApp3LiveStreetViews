package com.live.streetview.navigation.earthmap.compass.map.activities

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import com.google.android.gms.ads.AdView
import com.google.android.gms.location.FusedLocationProviderClient
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityNewAreaCalculatorBinding
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import java.text.DecimalFormat

class NewAreaCalculatorActivity : AppCompatActivity() {
    var binding: ActivityNewAreaCalculatorBinding? = null
    var MAPBOXSATELLITELABELLED: OnlineTileSourceBase? = null
    private var mCalculatoredArea = 0.0
    var gPt0: GeoPoint? = null
    var gPt1: GeoPoint? = null
    var mapController: IMapController? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    var userLoc: Location? = null
    var location: Location? = null

    private var callbackEnabled = true

    var lat = 0.0
    var lon = 0.0
    var normalLayer: CardView? = null
    var nightLayer: CardView? = null
    var satteliteLayer: CardView? = null
    var dialog: Dialog? = null
    private val EARTH_RADIUS = 6371000.0 // meters

    private lateinit var list: ArrayList<GeoPoint>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_area_calculator)
        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        binding = ActivityNewAreaCalculatorBinding.inflate(layoutInflater)
        this.onBackPressedDispatcher.addCallback(onBackPressedCallback)
        StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks("StreetViewAreaCalculatorOnCreate", this@NewAreaCalculatorActivity)
        list = ArrayList<GeoPoint>()
        setContentView(binding!!.root)
        intilization()
        streetViewBannerAdsSmall()


    }

    private fun streetViewBannerAdsSmall() {
        val billingHelper = StreetViewAppSoniBillingHelper(this)
        val adContainer: LinearLayout = binding!!.bannerAdPlace.adContainer
        val adView = AdView(this)
        adView.setAdSize(StreetViewAppSoniMyAppAds.getAdSize(this))
        adView.adUnitId = StreetViewAppSoniMyAppAds.banner_admob_inApp
        if (billingHelper.isNotAdPurchased) {
            StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediation(
                adContainer, adView, this
            )
        } else {
            binding!!.bannerID.visibility = View.GONE
        }
    }

    val mReceive: MapEventsReceiver = object : MapEventsReceiver {
        override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
            list.add(p)
            loadMaker(p!!)
            if (list.size > 1) {
                drawPolyLine()
                mCalculatoredArea = (calculateAreaOfGPSPolygonOnEarthInSquareMeters(list)) / 1000
                val res = DecimalFormat("#.#").format(mCalculatoredArea)
                binding!!.txtMete.text = res.toString() + "m2"
            }
            return true
        }

        override fun longPressHelper(p: GeoPoint): Boolean {
            // write your code here
            return false
        }
    }

    private fun calculateAreaOfGPSPolygonOnEarthInSquareMeters(locations: MutableList<GeoPoint>): Double {
        return calculateAreaOfGPSPolygonOnSphereInSquareMeters(locations, EARTH_RADIUS)
    }

    private fun calculateAreaOfGPSPolygonOnSphereInSquareMeters(
        locations: List<GeoPoint>,
        radius: Double
    ): Double {
        if (locations.size < 1) {
            return 0.0
        }
        val diameter = radius * 2
        val circumference = diameter * Math.PI
        val listY: MutableList<Double> = ArrayList()
        val listX: MutableList<Double> = ArrayList()
        val listArea: MutableList<Double> = ArrayList()
        val latitudeRef: Double = locations[0].latitude
        val longitudeRef: Double = locations[0].longitude
        for (i in 1 until locations.size) {
            val latitude: Double = locations[i].latitude
            val longitude: Double = locations[i].longitude
            listY.add(calculateYSegment(latitudeRef, latitude, circumference))
            listX.add(calculateXSegment(longitudeRef, longitude, latitude, circumference))
        }
        // calculate areas for each triangle segment
        for (i in 1 until listX.size) {
            val x1 = listX[i - 1]
            val y1 = listY[i - 1]
            val x2 = listX[i]
            val y2 = listY[i]
            listArea.add(calculateAreaInSquareMeters(x1, x2, y1, y2))
        }
        // sum areas of all triangle segments
        var areasSum = 0.0
        for (area in listArea) {
            areasSum += area
        }
        // get abolute value of area, it can't be negative
        return Math.abs(areasSum) // Math.sqrt(areasSum * areasSum);
    }

    private fun calculateAreaInSquareMeters(
        x1: Double,
        x2: Double,
        y1: Double,
        y2: Double
    ): Double {
        return (y1 * x2 - x1 * y2) / 2
    }

    private fun calculateYSegment(
        latitudeRef: Double,
        latitude: Double,
        circumference: Double
    ): Double {
        return (latitude - latitudeRef) * circumference / 360.0
    }

    private fun calculateXSegment(
        longitudeRef: Double, longitude: Double, latitude: Double,
        circumference: Double
    ): Double {
        return (longitude - longitudeRef) * circumference * Math.cos(Math.toRadians(latitude)) / 360.0
    }

    fun drawPolyLine() {
        val polygon = Polygon(binding!!.mapView)
        polygon.points = list
        polygon.fillColor = resources.getColor(R.color.polygone)
        binding!!.mapView.overlays.add(polygon)
        binding!!.mapView.invalidate()
        gPt0 = gPt1
    }

    private fun loadMaker(startPoint: GeoPoint) {
        val marker = org.osmdroid.views.overlay.Marker(binding!!.mapView)
        marker.title = ("Current Location")
        marker.textLabelFontSize = 11
        //must set the icon to null last
        marker.icon = null
        marker.position = startPoint
        val icon = BitmapFactory.decodeResource(resources, R.drawable.currentlocarion)
        setMarkerIconAsPhoto(marker, icon!!)
        binding!!.mapView.overlays.add(marker)
        binding!!.mapView.invalidate()
    }

    private fun initializeMap(lat: Double, lng: Double) {
//        binding.mapViewMyLocation.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        binding!!.mapView.setBuiltInZoomControls(true)
        binding!!.mapView.setMultiTouchControls(true)
        val mRotationGestureOverlay = RotationGestureOverlay(this, binding!!.mapView)
        mRotationGestureOverlay.isEnabled = true
        binding!!.mapView.overlays.add(mRotationGestureOverlay)
        mapController = binding!!.mapView.controller
        binding!!.mapView.getZoomController().setVisibility(
            CustomZoomButtonsController.Visibility.NEVER
        );
        val startPoint = GeoPoint(lat, lng)
        mapController!!.setCenter(startPoint)
        mapController!!.animateTo(startPoint, 12.0, 1400)
        val OverlayEvents = MapEventsOverlay(baseContext, mReceive)
        binding!!.mapView.overlays.add(OverlayEvents)
        //marker
        val marker = org.osmdroid.views.overlay.Marker(binding!!.mapView)
        marker.title = ("Current Location")
        marker.textLabelFontSize = 11
        //must set the icon to null last
        marker.icon = null
        marker.position = startPoint
        val icon = BitmapFactory.decodeResource(resources, R.drawable.currentlocarion)
        setMarkerIconAsPhoto(marker, icon!!)
        binding!!.mapView.overlays.add(marker)

    }

    @TargetApi(Build.VERSION_CODES.DONUT)
    fun setMarkerIconAsPhoto(marker: org.osmdroid.views.overlay.Marker, thumbnail: Bitmap) {
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


    private fun clearPolyLine() {
        try {
            binding!!.mapView.overlays.clear()
            list = ArrayList<GeoPoint>()
            initializeMap(lat, lon)
            binding!!.txtMete.text = "0m2"
        } catch (e: Exception) {

        }


    }

    @SuppressLint("MissingPermission")
    private fun intilization() {
        dialog = Dialog(this)
        fusedLocationClient = FusedLocationProviderClient(this)
        userLoc = Location("service Provider")
        fusedLocationClient!!.lastLocation.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                location = task.result
                if (location == null) {
                    Toast.makeText(
                        this@NewAreaCalculatorActivity,
                        "Permission not granted",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    userLoc!!.latitude = location!!.latitude
                    userLoc!!.longitude = location!!.longitude
                    lat = location!!.latitude
                    lon = location!!.longitude
                    initializeMap(location!!.latitude, location!!.longitude)
                }
            }
        }
        binding!!.currentlocationview.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this@NewAreaCalculatorActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    this@NewAreaCalculatorActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@NewAreaCalculatorActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
                ActivityCompat.requestPermissions(
                    this@NewAreaCalculatorActivity,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    2
                )
            } else {
                val lm =
                    this@NewAreaCalculatorActivity.getSystemService(LOCATION_SERVICE) as LocationManager
                if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    val alertDialog = AlertDialog.Builder(this@NewAreaCalculatorActivity)
                    alertDialog.setTitle("Enable Location")
                    alertDialog.setMessage("Your locations setting is not enabled. Please enabled it in settings menu.")
                    alertDialog.setPositiveButton("Location Settings") { dialog, which ->
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(intent)
                    }
                    alertDialog.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
                    val alert = alertDialog.create()
                    alert.show()

                }
                if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (userLoc!!.latitude != 0.0 && userLoc!!.longitude != 0.0) {

                        if (lat != 0.0 && lon != 0.0) {
                            mapController!!.animateTo(GeoPoint(lat, lon), 17.0, 1400)
                        }
                    } else {
                        val alertDialog = AlertDialog.Builder(this@NewAreaCalculatorActivity)
                        alertDialog.setTitle("Error")
                        alertDialog.setMessage("Map didnt Get your Current Location please press Current Location button again !")
                        alertDialog.setCancelable(true)
                        alertDialog.setPositiveButton("OK") { dialog, which -> dialog.cancel() }
                        val alert = alertDialog.create()
                        alert.show()
                    }
                }
            }
        }
        binding!!.btnclearall.setOnClickListener {
            clearPolyLine()
        }
        binding!!.btnclearone.setOnClickListener {
            MarkerUndoFunction()
        }
        binding!!.calarrowback.setOnClickListener {
            StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks("StreetViewAreaCalculatorOnBtnExit", this@NewAreaCalculatorActivity)

            StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation(
                this,
                StreetViewAppSoniMyAppAds.admobInterstitialAd,
                binding!!.whiteView
            )
        }
    }



    override fun onResume() {
        try {
            binding!!.mapView.onResume()
        } catch (e: NullPointerException) {
        }

        super.onResume()
    }

    override fun onPause() {
        try {
            binding!!.mapView.onPause()
        } catch (e: NullPointerException) {
        }

        super.onPause()
    }

    /*    override fun onBackPressed() {
            StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation(
                this,
                StreetViewAppSoniMyAppAds.admobInterstitialAd,
                StreetViewAppSoniMyAppAds.maxInterstitialAdStreetViewST
            )
    //        super.onBackPressed()
        }*/

    companion object {
        private const val CIRCLE_SOURCE_GeoJason_ID = "circle-source-id"
        private const val FILL_SOURCE_GeoJason_ID = "fill-source-id"
        private const val LINE_SOURCE_GeoJason_ID = "line-source-id"
        private const val CIRCLE_LAYER_GeoJason_ID = "circle-layer-id"
        private const val FILL_LAYER_GeoJason_ID = "fill-layer-polygon-id"
        private const val LINE_LAYER_GeoJason_ID = "line-layer-id"
    }

    fun MarkerUndoFunction() {
        binding!!.mapView.overlays.clear()
        Log.d("TAGMARK", "MarkerUndoFunction Before: " + list!!.size)
//        TotalDistance = TotalDistance - LastDistance
        if (list.size >= 1) {
            list.removeAt(list.size - 1)
            if (list.size > 0) {
                for (item in list) {
                    loadMaker(item)
                    if (list.size > 1) {
                        drawPolyLine()
                        mCalculatoredArea =
                            (calculateAreaOfGPSPolygonOnEarthInSquareMeters(list)) / 1000
                        val res = DecimalFormat("#.#").format(mCalculatoredArea)
                        binding!!.txtMete.text = res.toString() + "m2"
                    }
                }
            }
        }
        val OverlayEvents = MapEventsOverlay(baseContext, mReceive)
        binding!!.mapView.overlays.add(OverlayEvents)

    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

            StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks("StreetViewAreaCalculatorOnExit", this@NewAreaCalculatorActivity)
            StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation(
                this@NewAreaCalculatorActivity,
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