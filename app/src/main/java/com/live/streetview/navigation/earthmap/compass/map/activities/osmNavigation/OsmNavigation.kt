package com.live.streetview.navigation.earthmap.compass.map.activities.osmNavigation

import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityOsmNavigationBinding
import kotlinx.coroutines.*
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay
import java.text.DecimalFormat
import kotlin.math.roundToInt

class OsmNavigation : AppCompatActivity() {
    lateinit var binding: ActivityOsmNavigationBinding
    lateinit var locationRepository: RepositoryLocationGpsNavigation
    private var cLat: Double = 0.0
    private var cLong: Double = 0.0
    private var dLat: Double = 0.0
    private var dLong: Double = 0.0
    private var dLat0: Double = 0.0
    private var dLong0: Double = 0.0
    private var cLat0: Double = 0.0
    private var cLong0: Double = 0.0
    private var roadManager: OSRMRoadManager? = null
    private var obtainTypedArray: TypedArray? = null
    protected var myLocationOverlay: DirectedLocationOverlay? = null
    private var road: Road? = null
    lateinit var endPoint: GeoPoint
    lateinit var startPoint: GeoPoint
    private var result: Double = 0.0
    private var chk: Boolean = false
    private var i: Int = 0

    var travelledDistance = 0.0
    private var temp: Float = 0.0f
    private val df: DecimalFormat = DecimalFormat("0.000")
    var job: Job? = null
    var mAzimuthAngleSpeed = 0.0f
    var mp: MediaPlayer? = null
    var isMediaPlay: Boolean = false
    var resourceId: Int? = null
    var resourceIdnext: Int? = null
    var activityIsAvailable: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        binding = ActivityOsmNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks(
            "StreetViewOsmNavigationOnCreate",
            this
        )
        //on back press dispatcher
        this.onBackPressedDispatcher.addCallback(onBackPressedCallback)

        binding!!.map.getZoomController().setVisibility(
            CustomZoomButtonsController.Visibility.NEVER
        );
        obtainTypedArray = resources.obtainTypedArray(R.array.direction_icons)
        try {
            cLat0 = intent.getDoubleExtra("lat", cLat)
            cLong0 = intent.getDoubleExtra("lng", cLong)
            Log.e("TAG", "onCreate: current $cLat0")
            dLat0 = intent.getDoubleExtra("d_lat", dLat)
            dLong0 = intent.getDoubleExtra("d_lng", dLong)

            Log.e("TAG", "onCreate: destination $dLat0")
        } catch (e: NullPointerException) {

        }

        myLocationOverlay = DirectedLocationOverlay(this)
        binding.map.getOverlays().add(myLocationOverlay)
        getCurrentLocation()
        navigation()

        CoroutineScope(Dispatchers.IO).launch {
            Handler(Looper.getMainLooper()).postDelayed({
                chk = true
            }, 3000)

        }
        clicklistener()

    }

    private fun clicklistener() {
        binding.btnDismiss.setOnClickListener {
            StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation(
                this,
                StreetViewAppSoniMyAppAds.admobInterstitialAd,
                binding.whiteView
            )
        }
        binding.btnRedraw.setOnClickListener { reDrawRoute() }

        binding.btnCurrentpoint.setOnClickListener {

            binding.map.controller.setCenter(myLocationOverlay!!.location)
            binding.map.controller.setZoom(20)

        }
    }

    private fun reDrawRoute() {
        binding.progressbar.visibility = View.VISIBLE
        val waypoints = ArrayList<GeoPoint>()
        try {
            waypoints.add(
                GeoPoint(
                    myLocationOverlay!!.location.latitude,
                    myLocationOverlay!!.location.longitude
                )
            )
            waypoints.add(endPoint)
        } catch (e: Exception) {
        }
        binding.map.controller.animateTo(myLocationOverlay!!.location)
        binding.map.overlays.clear()
        binding.map.overlays.add(myLocationOverlay)
        //rotation
        if (job != null && job!!.isActive) {
            job!!.cancel()
        }
        job = CoroutineScope(Dispatchers.IO).launch {
            if (waypoints.size > 0) {
                route(roadManager!!, waypoints)
            }

        }
    }

    private fun getCurrentLocation() {

        locationRepository = RepositoryLocationGpsNavigation(this, object : MyLocationListener {
            override fun onLocationChanged(location: Location) {
                Log.d("TAG", "onLocationChange: ${location}")
                if (location != null) {
                    //location known:
                    cLat = location.latitude
                    cLong = location.longitude

                    val newLocation = GeoPoint(location)
                    Log.e("TAG", "onLocationChange:newloc lat ${newLocation.latitude}")
                    binding.map.getController().animateTo(newLocation)
                    val prevLocation = myLocationOverlay!!.location
                    Log.e("TAG", "onLocationChange:preloc lat ${prevLocation}")
                    myLocationOverlay!!.location = newLocation
                    myLocationOverlay!!.setAccuracy(location.accuracy.toInt())
                    var mSpeed = 0.0 // km/h
                    mSpeed = location.getSpeed() * 3.6
                    val speedInt = Math.round(mSpeed)
                    binding.speed.text = "$speedInt\nkm/h"
                    //TODO: check if speed is not too small
                    if (mSpeed >= 0.1) {
                        mAzimuthAngleSpeed = location.bearing
                        myLocationOverlay!!.setBearing(mAzimuthAngleSpeed)
                        binding.map.getController().animateTo(newLocation)
                        binding.map.setMapOrientation(-mAzimuthAngleSpeed)
                        Log.e("TAG", "onLocationChange: bearing $mAzimuthAngleSpeed")
                        Log.e("TAG", "onLocationChange: ${location.getBearing()}")

                    }
                    try {
                        upadateDistanceRoad()
                    } catch (e: NullPointerException) {

                    }
                }
            }

        })

    }

    private fun navigation() {
        Log.d("TAG", "navigation: ")
        binding.map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        roadManager = OSRMRoadManager(this, "MY_USER_AGENT")
        binding.map.setBuiltInZoomControls(false)
        binding.map.setMultiTouchControls(true)
        val mapController = binding.map.controller
        mapController.setZoom(19)
        startPoint = GeoPoint(cLat0, cLong0)
        mapController.animateTo(startPoint)
        val waypoints = ArrayList<GeoPoint>()
        waypoints.add(startPoint)
        endPoint = GeoPoint(dLat0, dLong0)
        waypoints.add(endPoint)
        mapController.animateTo(startPoint)
        binding.map.setMultiTouchControls(true)
        mapController.setZoom(19)
        CoroutineScope(Dispatchers.IO).launch {
            route(roadManager!!, waypoints)
        }
        myLocationOverlay!!.isEnabled = true
    }

    private suspend fun route(roadManager: RoadManager, waypoints: java.util.ArrayList<GeoPoint>) {
        i = 0
        CoroutineScope(Dispatchers.IO).launch {
            road = roadManager.getRoad(waypoints)
            val roadOverlay = RoadManager.buildRoadOverlay(road)
            roadOverlay.width = 13.0f
            roadOverlay.color = resources.getColor(R.color.purple_500)
            binding.map.overlays.add(roadOverlay)
            if (road!!.mLength >= 100.0) {
                result = road!!.mLength
            } else if (road!!.mLength >= 1.0) {
                result = (road!!.mLength * 10).roundToInt() / 10.0
            } else {
                result = road!!.mLength * 1000
            }
            for (i in road!!.mNodes.indices) {
                val node = road!!.mNodes[i]

                withContext(Dispatchers.Main) {
                    if (activityIsAvailable) {
                        val nodeMarker = Marker(binding.map)
                        nodeMarker.position = node.mLocation
                        nodeMarker.title = "Step $i "
                        nodeMarker
                        nodeMarker.snippet = node.mInstructions
                        Log.e("TAG", "route:instr ${road!!.mNodes[i].mInstructions}")
                        nodeMarker.subDescription =
                            Road.getLengthDurationText(
                                this@OsmNavigation,
                                node.mLength,
                                node.mDuration
                            )
                        val resourceId: Int =
                            obtainTypedArray!!.getResourceId(
                                node.mManeuverType,
                                R.drawable.ic_empty
                            )
                        if (resourceId != R.drawable.ic_empty) {
                            nodeMarker.image =
                                ResourcesCompat.getDrawable(
                                    resources,
                                    resourceId,
                                    null as Resources.Theme?
                                )
                        }
                        Log.e("TAG", "route:discription ${nodeMarker.subDescription}")

                        //add turn to route
                        val icon = BitmapFactory.decodeResource(resources, resourceId)
                        nodeMarker.icon = null
                        setMarkerIconAsPhoto(nodeMarker, icon!!)
                        binding.map.overlays.add(nodeMarker)
                        binding.progressbar.visibility = View.GONE
                    }
                }
            }
        }

        binding.map.invalidate()
    }

    fun setMarkerIconAsPhoto(marker: Marker, thumbnail: Bitmap) {
        var thumbnail = thumbnail
        val borderSize = 2
        thumbnail = Bitmap.createScaledBitmap(thumbnail, 60, 60, true)
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

    private fun upadateDistanceRoad() {
        try {
            if (myLocationOverlay!!.location != null && road!!.mNodes.size > 0 && road?.mNodes?.size!! > i) {
                if (myLocationOverlay!!.location.latitude != null && myLocationOverlay!!.location.longitude != null &&
                    road!!.mNodes[i].mLocation != null
                ) {
                    Log.e("TAG", "onReceive: start5")
                    val d1: Double = GetDistance(
                        GeoPoint(
                            myLocationOverlay!!.location.latitude,
                            myLocationOverlay!!.location.longitude
                        ),
                        GeoPoint(
                            road!!.mNodes[i].mLocation.latitude,
                            road!!.mNodes[i].mLocation.longitude
                        )
                    )
                    travelledDistance += d1
                    val dis = df.format(d1)
                    if (dis != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val roadManager =
                                    OSRMRoadManager(
                                        this@OsmNavigation,
                                        "MY_USER_AGENT"
                                    )
                                val ways = arrayListOf<GeoPoint>()
                                ways.add(myLocationOverlay!!.location)
                                ways.add(endPoint)
                                val roads = roadManager.getRoad(ways)
                                val remaintime = roads.mDuration
                                Log.d("TAG", "upadateDistanceRoad time: ${remaintime}")
                                val remaindistance = roads.mLength
                                val remdistance =
                                    DecimalFormat("#.#").format(remaindistance)
                                withContext(Dispatchers.Main) {
                                    binding.progressbar.visibility =
                                        View.VISIBLE
                                    binding.remains.text = remdistance + "km"
                                    binding.remainstime.text =
                                        calculateTime(remaintime.toInt())
                                    binding.progressbar.visibility = View.GONE

                                    binding.map.controller.animateTo(
                                        myLocationOverlay!!.location
                                    )
                                }
                            } catch (e: NullPointerException) {
                            }
                        }
                        if (temp == 0.0f)
                            temp = d1.toFloat()
                        else {
                            if (myLocationOverlay!!.location.distanceToAsDouble(road!!.mNodes[i].mLocation) in (temp + 5)..(temp - 10)) {
                                Log.e("TAG:", "onReceive: Fine")

                            } else {
                                Log.e("TAG:", "onReceive: Wrong direction")
                            }
                        }
                        try {
                            if (myLocationOverlay!!.location.distanceToAsDouble(road!!.mNodes[i].mLocation) <= 50) {

                                i += 1
                                Log.e("LegNumber:", "onReceive: $i,$dis")
                                //first step
                                try {
                                    resourceId = obtainTypedArray!!.getResourceId(
                                        road!!.mNodes[i].mManeuverType,
                                        R.drawable.ic_empty
                                    )
                                    //then next step
                                    resourceIdnext =
                                        obtainTypedArray!!.getResourceId(
                                            road!!.mNodes[i + 1].mManeuverType,
                                            R.drawable.ic_empty
                                        )
                                    binding.turnTxt1.text =
                                        road!!.mNodes[i + 1].mInstructions
                                    //next step distancetime
                                    binding.nextstepmeter.text =
                                        Road.getLengthDurationText(
                                            this@OsmNavigation,
                                            road!!.mNodes[i].mLength,
                                            road!!.mNodes[i].mDuration
                                        )
                                } catch (e: IndexOutOfBoundsException) {
                                }
                                if (!this.isFinishing) {
                                    if (resourceIdnext != R.drawable.ic_empty) {
                                        Glide.with(this@OsmNavigation).load(
                                            ResourcesCompat.getDrawable(
                                                resources,
                                                resourceIdnext!!,
                                                null as Resources.Theme?
                                            )
                                        ).into(binding.turnImg1)
                                    } else {
                                        Glide.with(this@OsmNavigation).load(
                                            ResourcesCompat.getDrawable(
                                                resources,
                                                R.drawable.ic_continue,
                                                null as Resources.Theme?
                                            )
                                        ).into(binding.turnImg1)
                                    }
                                    try {
                                        binding.turntxt.text = road!!.mNodes[i].mInstructions


                                        if (resourceId != R.drawable.ic_empty) {
                                            Glide.with(this@OsmNavigation).load(
                                                resourceId?.let {
                                                    ResourcesCompat.getDrawable(
                                                        resources,
                                                        it,
                                                        null as Resources.Theme?
                                                    )
                                                }
                                            ).into(binding.turnimg)
                                        } else {
                                            Glide.with(this@OsmNavigation).load(
                                                ResourcesCompat.getDrawable(
                                                    resources,
                                                    R.drawable.ic_continue,
                                                    null as Resources.Theme?
                                                )
                                            ).into(binding.turnimg)
                                        }
                                    } catch (e: IndexOutOfBoundsException) {
                                    }
                                }
                            } else if (OsmHelper.computeDistanceBetween(
                                    GeoPoint(
                                        road!!.mNodes[i].mLocation.latitude,
                                        road!!.mNodes[i].mLocation.longitude
                                    ),
                                    GeoPoint(
                                        road!!.mNodes[i + 1].mLocation.latitude,
                                        road!!.mNodes[i + 1].mLocation.longitude
                                    )
                                ) + 10 <
                                OsmHelper.computeDistanceBetween(
                                    GeoPoint(
                                        myLocationOverlay!!.location.latitude,
                                        myLocationOverlay!!.location.longitude
                                    ),
                                    GeoPoint(
                                        road!!.mNodes[i].mLocation.latitude,
                                        road!!.mNodes[i].mLocation.longitude
                                    )
                                )
                            ) {
                            }
                        } catch (e: IndexOutOfBoundsException) {
                        }
                    }
                }
            }
        } catch (e: NullPointerException) {
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
            Log.d("TAG", "convertSecondDay:-----546------------- $minutes")
            timeString = "$hour hrs $minutes minutes"

            if (hour == 0) {
                Log.d("TAG", "convertSecondDay:-----549------------- $minutes")

                timeString = "$minutes minutes"
            }
        } else {
            Log.d("TAG", "convertSecondDay:-----554------------- $minutes")

            timeString = "$day d $hour hrs $minutes minutes"
        }
        return timeString
    }

    private fun GetDistance(lStart: GeoPoint, lEnd: GeoPoint): Double {
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

    override fun onStart() {
        super.onStart()
        activityIsAvailable = true
    }

    override fun onStop() {
        super.onStop()
        activityIsAvailable = false
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        binding.map.onPause()
        super.onPause()
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation(
                this@OsmNavigation,
                StreetViewAppSoniMyAppAds.admobInterstitialAd,
                binding.whiteView
            )
        }
    }
    override fun onDestroy() {
        super.onDestroy()

        try {
            locationRepository.stopLocation()
        } catch (e: Exception) {
        }
        onBackPressedCallback.remove()
    }
}