package com.live.streetview.navigation.earthmap.compass.map.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdView
import com.google.android.gms.location.FusedLocationProviderClient
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.activities.osmNavigation.OsmHelper
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityDistanceCalculatorBinding
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import java.text.DecimalFormat

class DistanceCalculatorActivity : AppCompatActivity() {
    var mapController: IMapController? = null
    var binding: ActivityDistanceCalculatorBinding? = null
    var dialog: Dialog? = null
    private val df: DecimalFormat = DecimalFormat("0.000")
    private var fusedLocationClient: FusedLocationProviderClient? = null
    var userLoc: Location? = null
    var location: Location? = null
    var lat = 0.0
    var lon = 0.0
    var totalDistance: Double = 0.0
    var lastDistance: Double = 0.0
    var gPt0: GeoPoint? = null
    var gPt1: GeoPoint? = null
    private lateinit var list: ArrayList<GeoPoint>
    var counter = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ctx = applicationContext
        val prefs = PreferenceManager.getDefaultSharedPreferences(ctx)

        if (prefs != null) {
            Configuration.getInstance().load(ctx, prefs)
        } else {
            Toast.makeText(this, "Failed to load configuration", Toast.LENGTH_SHORT).show()
        }

        binding = ActivityDistanceCalculatorBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        this.onBackPressedDispatcher.addCallback(onBackPressedCallback)
        list = ArrayList<GeoPoint>()
        intilization()
        streetViewBannerAdsSmall()

        StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks("StreetViewDistanceCalculatorOnCreate", this@DistanceCalculatorActivity)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks("StreetViewDistanceCalculatorOnExit", this@DistanceCalculatorActivity)

            StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation(
                this@DistanceCalculatorActivity,
                StreetViewAppSoniMyAppAds.admobInterstitialAd,
                binding!!.whiteView
            )
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
                        this@DistanceCalculatorActivity,
                        "Permission not granted",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Log.d("TAG", "intilization: ${location}")
                    userLoc!!.latitude = location!!.latitude
                    userLoc!!.longitude = location!!.longitude
                    lat = location!!.latitude
                    lon = location!!.longitude
                    initializeMap(location!!.latitude, location!!.longitude)
                }
            }
        }

        binding!!.btnClearAll.setOnClickListener {
            clearPolyLine()
        }
        binding!!.btnClearONE.setOnClickListener {
            MarkerUndoFunction()
        }
        binding!!.imageView.setOnClickListener {
            StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks("StreetViewDistanceCalculatorOnBtnExit", this@DistanceCalculatorActivity)

            onBackPressedDispatcher.onBackPressed() }
    }

    private fun clearPolyLine() {
        binding!!.DisTxt.text = "0km"
        binding!!.mapView.overlays.clear()
        list = ArrayList<GeoPoint>()
        initializeMap(lat, lon)
        counter = 0
    }

    val mReceive: MapEventsReceiver = object : MapEventsReceiver {
        override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
            list.add(p)
            if (list.size >= 1) {
                loadMaker(p)
                if (counter >= 1) {
                    lastDistance = getDistance(list.get(counter - 1), list.get(counter))
                    totalDistance = totalDistance + lastDistance
                    val dis =
                        df.format(totalDistance)
                    binding!!.DisTxt.text = dis.toString() + "Km"
                    drawPolyLine()
                }
                counter++
            }
            else {
                loadMaker(p)
            }
            return true
        }

        override fun longPressHelper(p: GeoPoint): Boolean {
            // write your code here
            return false
        }
    }

    private fun getDistance(lStart: GeoPoint, lEnd: GeoPoint): Double {
        var valueInMeter: Double = 0.0
        var valuesInKm: Double = 0.0
        if (lStart.latitude != 0.0 && lStart.longitude != 0.0) {
            valueInMeter = OsmHelper.computeDistanceBetween(
                GeoPoint(lStart.latitude, lStart.longitude),
                GeoPoint(lEnd.latitude, lEnd.longitude)
            )
            valuesInKm = valueInMeter / 1000
        }
        return valuesInKm
    }

    fun drawPolyLine() {
        val line = Polyline(binding!!.mapView)
        line.setPoints(list)
        binding!!.mapView.overlays.add(line)
        binding!!.mapView.invalidate()
        gPt0 = gPt1
//        counter++
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

    fun MarkerUndoFunction() {
        binding!!.mapView.overlays.clear()
        Log.d("TAGMARK", "MarkerUndoFunction Before: " + list.size)
//        TotalDistance = TotalDistance - LastDistance
        totalDistance = totalDistance - lastDistance
        counter = 0
        if (list.size > 1) {
            list.removeAt(list.size - 1)
            if (list.size == 1) {
                binding!!.DisTxt.text = "0km"
                totalDistance = 0.0
                list.clear()
            }

            if (list.size > 0) {
                for (item in list) {
                    loadMaker(item)
//                    loadMaker(p!!)
                    if (counter >= 1) {
                        lastDistance = getDistance(list.get(counter - 1), list.get(counter))
                        val dis = df.format(totalDistance)
                        binding!!.DisTxt.text = dis.toString() + "Km"
                        drawPolyLine()
                    }

                    counter++
                }
//                counter--
            }
        } else {
            list.clear()
            counter = 0
            Log.d("TAGMARK", "MarkerUndoFunction : " + list.size)
            binding!!.DisTxt.text = "0km"
            totalDistance = 0.0
        }
        val OverlayEvents = MapEventsOverlay(baseContext, mReceive)
        binding!!.mapView.overlays.add(OverlayEvents)
        binding!!.mapView.invalidate()


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
            binding!!.bannerID.visibility = View.GONE
        }
    }

}