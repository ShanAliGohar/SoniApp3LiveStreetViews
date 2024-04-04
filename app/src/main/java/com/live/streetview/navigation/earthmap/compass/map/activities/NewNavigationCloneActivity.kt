package com.live.streetview.navigation.earthmap.compass.map.activities

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Point
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.timepicker.TimeFormat
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.activities.utils.UtilityClass
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityNewNavigationBinding




import java.util.*

class NewNavigationCloneActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewNavigationBinding
    private var destinationLat = 0.0
    private var destinationLng = 0.0
    private var destinationAddress = "Destination Address"
    private var destinationPoint: Point? = null
   // private var modelReceived: RouteNavModel? = null
    var selectedprofile:String?=null
//    private val mapboxReplayer = MapboxReplayer()
//    private val replayLocationEngine = ReplayLocationEngine(mapboxReplayer)
//    private val replayProgressObserver = ReplayProgressObserver(mapboxReplayer)
//
//    private val pixelDensity = Resources.getSystem().displayMetrics.density
//    private val overviewPadding: EdgeInsets by lazy {
//        EdgeInsets(
//            140.0 * pixelDensity,
//            40.0 * pixelDensity,
//            120.0 * pixelDensity,
//            40.0 * pixelDensity
//        )
//    }
//    private val landscapeOverviewPadding: EdgeInsets by lazy {
//        EdgeInsets(
//            30.0 * pixelDensity,
//            380.0 * pixelDensity,
//            110.0 * pixelDensity,
//            20.0 * pixelDensity
//        )
//    }
//    private val followingPadding: EdgeInsets by lazy {
//        EdgeInsets(
//            180.0 * pixelDensity,
//            40.0 * pixelDensity,
//            150.0 * pixelDensity,
//            40.0 * pixelDensity
//        )
//    }
//    private val landscapeFollowingPadding: EdgeInsets by lazy {
//        EdgeInsets(
//            30.0 * pixelDensity,
//            380.0 * pixelDensity,
//            110.0 * pixelDensity,
//            40.0 * pixelDensity
//        )
//    }

//    private lateinit var mapboxMap: com.mapbox.maps.MapboxMap
//
//    private lateinit var navigationCamera: NavigationCamera
//    private lateinit var viewportDataSource: MapboxNavigationViewportDataSource
//
//    private lateinit var maneuverApi: MapboxManeuverApi
//    private lateinit var tripProgressApi: MapboxTripProgressApi
//    private lateinit var routeLineApi: MapboxRouteLineApi
//    private lateinit var routeLineView: MapboxRouteLineView
//    private val routeArrowApi: MapboxRouteArrowApi = MapboxRouteArrowApi()
//    private lateinit var routeArrowView: MapboxRouteArrowView

    private var isVoiceInstructionsMuted = false
//        set(value) {
//            field = value
//            if (value) {
//                binding.soundButton.muteAndExtend(BUTTON_ANIMATION_DURATION)
//                voiceInstructionsPlayer.volume(SpeechVolume(0f))
//            } else {
//                binding.soundButton.unmuteAndExtend(BUTTON_ANIMATION_DURATION)
//                voiceInstructionsPlayer.volume(SpeechVolume(1f))
//            }
//        }

//    private lateinit var speechApi: MapboxSpeechApi
//    private lateinit var voiceInstructionsPlayer: MapboxVoiceInstructionsPlayer
//
//    private val voiceInstructionsObserver = VoiceInstructionsObserver { voiceInstructions ->
//        speechApi.generate(voiceInstructions, speechCallback)
//    }

//    private val speechCallback =
//        MapboxNavigationConsumer<Expected<SpeechError, SpeechValue>> { expected ->
//            expected.fold(
//                { error -> voiceInstructionsPlayer.play(
//                    error.fallback,
//                    voiceInstructionsPlayerCallback
//                )
//                },
//                { value -> voiceInstructionsPlayer.play(
//                    value.announcement,
//                    voiceInstructionsPlayerCallback
//                )
//                }
//            )
//        }

//    private val voiceInstructionsPlayerCallback =
//        MapboxNavigationConsumer<SpeechAnnouncement> { value ->
//            speechApi.clean(value)
//        }
//
//    private val navigationLocationProvider = NavigationLocationProvider()
//
//    private val locationObserver = object : LocationObserver {
//        var firstLocationUpdateReceived = false
//
//        override fun onNewRawLocation(rawLocation: Location) {}
//
//        override fun onNewLocationMatcherResult(locationMatcherResult: LocationMatcherResult) {
//            val enhancedLocation = locationMatcherResult.enhancedLocation
//            navigationLocationProvider.changePosition(
//                location = enhancedLocation,
//                keyPoints = locationMatcherResult.keyPoints,
//            )
//
//            viewportDataSource.onLocationChanged(enhancedLocation)
//            viewportDataSource.evaluate()
//
//            if (!firstLocationUpdateReceived) {
//                firstLocationUpdateReceived = true
//                navigationCamera.requestNavigationCameraToOverview(
//                    stateTransitionOptions = NavigationCameraTransitionOptions.Builder()
//                        .maxDuration(0) // instant transition
//                        .build()
//                )
//            }
//        }
//    }

//    private val routeProgressObserver = RouteProgressObserver { routeProgress ->
//        viewportDataSource.onRouteProgressChanged(routeProgress)
//        viewportDataSource.evaluate()
//
//        val style = mapboxMap.getStyle()
//        if (style != null) {
//            val maneuverArrowResult = routeArrowApi.addUpcomingManeuverArrow(routeProgress)
//            routeArrowView.renderManeuverUpdate(style, maneuverArrowResult)
//        }
//
//        val maneuvers = maneuverApi.getManeuvers(routeProgress)
//        maneuvers.fold(
//            { error ->
//                Toast.makeText(
//                    this@NewNavigationCloneActivity,
//                    error.errorMessage,
//                    Toast.LENGTH_SHORT
//                ).show()
//            },
//            {
//                binding.maneuverView.visibility = View.VISIBLE
//                binding.maneuverView.renderManeuvers(maneuvers)
//            }
//        )
//
//        binding.tripProgressView.render(
//            tripProgressApi.getTripProgress(routeProgress)
//        )
//    }

//    private val routesObserver = RoutesObserver { routeUpdateResult ->
//        if (routeUpdateResult.routes.isNotEmpty()) {
//            val routeLines = routeUpdateResult.routes.map { RouteLine(it, null) }
//
//            routeLineApi.setRoutes(
//                routeLines
//            ) { value ->
//                mapboxMap.getStyle()?.apply {
//                    routeLineView.renderRouteDrawData(this, value)
//                }
//            }
//
//            viewportDataSource.onRouteChanged(routeUpdateResult.routes.first())
//            viewportDataSource.evaluate()
//        } else {
////            val style = mapboxMap.getStyle()
//            if (style != null) {
////                routeLineApi.clearRouteLine { value ->
////                    routeLineView.renderClearRouteLineValue(
////                        style,
////                        value
////                    )
////                }
////                routeArrowView.render(style, routeArrowApi.clearArrows())
//            }
//
//            viewportDataSource.clearRouteData()
//            viewportDataSource.evaluate()
//        }
//    }

//    private val mapBoxNavigation by lazy {
//        if(MapboxNavigationProvider.isCreated()){
//            MapboxNavigationProvider.retrieve()
//        }else{
//
//            val navigationOptions = NavigationOptions.Builder(this)
//                .accessToken(UtilityClass.MAPBOX_KEY)
//                .build()
//
//            MapboxNavigationProvider.create(navigationOptions)
//        }
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        ResourceOptionsManager.getDefault(this, UtilityClass.MAPBOX_KEY).update {
//            tileStoreUsageMode(TileStoreUsageMode.READ_ONLY)
//        }


        binding= ActivityNewNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        mapboxMap = binding.mapView.getMapboxMap()

        destinationLat=intent.extras!!.getDouble("lat")
        destinationLng=intent.extras!!.getDouble("lng")

        selectedprofile=intent.extras!!.getString("directionroute")

//        if (destinationLat != null && destinationLng != null) {
//
//            destinationPoint = Point.fromLngLat(destinationLng, destinationLat)
//        }


//        binding.mapView.location.apply {
//            this.locationPuck = LocationPuck2D(
//                bearingImage = ContextCompat.getDrawable(
//                    this@NewNavigationCloneActivity,
//                    R.drawable.mapbox_navigation_puck_icon
//                )
//            )
//            setLocationProvider(navigationLocationProvider)
//            enabled = true
//        }

        // initialize Navigation Camera
//        viewportDataSource = MapboxNavigationViewportDataSource(mapboxMap)
//        navigationCamera = NavigationCamera(
//            mapboxMap,
//            binding.mapView.camera,
//            viewportDataSource
//        )

//        binding.mapView.camera.addCameraAnimationsLifecycleListener(
//            NavigationBasicGesturesHandler(navigationCamera)
//        )
//        navigationCamera.registerNavigationCameraStateChangeObserver { navigationCameraState ->
//            when (navigationCameraState) {
//                NavigationCameraState.TRANSITION_TO_FOLLOWING,
//                NavigationCameraState.FOLLOWING -> binding.recenter.visibility = View.INVISIBLE
//                NavigationCameraState.TRANSITION_TO_OVERVIEW,
//                NavigationCameraState.OVERVIEW,
//                NavigationCameraState.IDLE -> binding.recenter.visibility = View.VISIBLE
//            }
//        }

//        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            viewportDataSource.overviewPadding = landscapeOverviewPadding
//        } else {
//            viewportDataSource.overviewPadding = overviewPadding
//        }
//        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            viewportDataSource.followingPadding = landscapeFollowingPadding
//        } else {
//            viewportDataSource.followingPadding = followingPadding
//        }

//        val distanceFormatterOptions = mapBoxNavigation.navigationOptions.distanceFormatterOptions

//        maneuverApi = MapboxManeuverApi(
//            MapboxDistanceFormatter(distanceFormatterOptions)
//        )
//
//        tripProgressApi = MapboxTripProgressApi(
//            TripProgressUpdateFormatter.Builder(this)
//                .distanceRemainingFormatter(
//                    DistanceRemainingFormatter(distanceFormatterOptions)
//                )
//                .timeRemainingFormatter(
//                    TimeRemainingFormatter(this)
//                )
//                .percentRouteTraveledFormatter(
//                    PercentDistanceTraveledFormatter()
//                )
//                .estimatedTimeToArrivalFormatter(
//                    EstimatedTimeToArrivalFormatter(this, TimeFormat.NONE_SPECIFIED)
//                )
//                .build()
//        )

//        speechApi = MapboxSpeechApi(
//            this,
//            UtilityClass.MAPBOX_KEY,
//            Locale.US.language
//        )
//        voiceInstructionsPlayer = MapboxVoiceInstructionsPlayer(
//            this,
//            UtilityClass.MAPBOX_KEY,
//            Locale.US.language
//        )

//        val mapboxRouteLineOptions = MapboxRouteLineOptions.Builder(this)
//            .withRouteLineBelowLayerId("road-label")
//            .build()
//        routeLineApi = MapboxRouteLineApi(mapboxRouteLineOptions)
//        routeLineView = MapboxRouteLineView(mapboxRouteLineOptions)
//
//        val routeArrowOptions = RouteArrowOptions.Builder(this).build()
//        routeArrowView = MapboxRouteArrowView(routeArrowOptions)
//
//        mapboxMap.loadStyleUri(
//            Style.MAPBOX_STREETS
//        ) {
//            binding.mapView.gestures.addOnMapLongClickListener { point ->
//                findRoute(point)
//                true
//            }
//        }

//        binding.stop.setOnClickListener {
//            clearRouteAndStopNavigation()
//        }
//        binding.recenter.setOnClickListener {
//            navigationCamera.requestNavigationCameraToFollowing()
//            binding.routeOverview.showTextAndExtend(BUTTON_ANIMATION_DURATION)
//        }
//        binding.routeOverview.setOnClickListener {
//            navigationCamera.requestNavigationCameraToOverview()
//            binding.recenter.showTextAndExtend(BUTTON_ANIMATION_DURATION)
//        }
//        binding.soundButton.setOnClickListener {
//            isVoiceInstructionsMuted = !isVoiceInstructionsMuted
//        }

//        binding.soundButton.unmute()

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
//        mapBoxNavigation.startTripSession()


        Handler().postDelayed({
            if (destinationPoint!=null){
//                findRoute(destinationPoint!!)
            } }, 1000)


    }

    override fun onStart() {
        super.onStart()

//        mapBoxNavigation.registerRoutesObserver(routesObserver)
//        mapBoxNavigation.registerRouteProgressObserver(routeProgressObserver)
//        mapBoxNavigation.registerLocationObserver(locationObserver)
//        mapBoxNavigation.registerVoiceInstructionsObserver(voiceInstructionsObserver)
//        mapBoxNavigation.registerRouteProgressObserver(replayProgressObserver)
//
//        if (mapBoxNavigation.getRoutes().isEmpty()) {
//            mapboxReplayer.pushEvents(
//                listOf(
//                    ReplayRouteMapper.mapToUpdateLocation(
//                        eventTimestamp = 0.0,
//                        point = Point.fromLngLat(-122.39726512303575, 37.785128345296805)
//                    )
//                )
//            )
//            mapboxReplayer.playFirstLocation()
//        }
    }

    override fun onStop() {
        super.onStop()

// unregister event listeners to prevent leaks or unnecessary resource consumption
//        mapBoxNavigation.unregisterRoutesObserver(routesObserver)
//        mapBoxNavigation.unregisterRouteProgressObserver(routeProgressObserver)
//        mapBoxNavigation.unregisterLocationObserver(locationObserver)
//        mapBoxNavigation.unregisterVoiceInstructionsObserver(voiceInstructionsObserver)
//        mapBoxNavigation.unregisterRouteProgressObserver(replayProgressObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
//        MapboxNavigationProvider.destroy()
//        mapboxReplayer.finish()
//        maneuverApi.cancel()
//        routeLineApi.cancel()
//        routeLineView.cancel()
//        speechApi.cancel()
//        voiceInstructionsPlayer.shutdown()
    }

//    private fun findRoute(destination: Point) {
//        val originLocation = navigationLocationProvider.lastLocation
//        val originPoint = originLocation?.let {
//            Point.fromLngLat(it.longitude, it.latitude)
//        } ?: return
//
//        mapBoxNavigation.requestRoutes(
//            RouteOptions.builder()
//                .applyDefaultNavigationOptions()
//                .applyLanguageAndVoiceUnitOptions(this)
//                .coordinatesList(listOf(originPoint, destination))
//                .profile(selectedprofile!!)
//                .bearingsList(
//                    listOf(
//                        Bearing.builder()
//                            .angle(originLocation.bearing.toDouble())
//                            .degrees(45.0)
//                            .build(),
//                        null
//                    )
//                )
//                .layersList(listOf(mapBoxNavigation.getZLevel(), null))
//                .build(),
//            object : RouterCallback {
//                override fun onRoutesReady(
//                    routes: List<DirectionsRoute>,
//                    routerOrigin: RouterOrigin
//                ) {
//                    setRouteAndStartNavigation(routes)
//                }
//
//                override fun onFailure(
//                    reasons: List<RouterFailure>,
//                    routeOptions: RouteOptions
//                ) {}
//
//                override fun onCanceled(routeOptions: RouteOptions, routerOrigin: RouterOrigin) {}
//            }
//        )
//    }

//    private fun setRouteAndStartNavigation(routes: List<DirectionsRoute>) {
//        mapBoxNavigation.setRoutes(routes)
//
//// start location simulation along the primary route
//        startSimulation(routes.last())
//
//// show UI elements
//        binding.soundButton.visibility = View.VISIBLE
//        binding.routeOverview.visibility = View.VISIBLE
//        binding.tripProgressCard.visibility = View.VISIBLE
//
//// move the camera to overview when new route is available
//        navigationCamera.requestNavigationCameraToOverview()
//    }

//    private fun clearRouteAndStopNavigation() {
//// clear
//        mapBoxNavigation.setRoutes(listOf())
//
//// stop simulation
//        mapboxReplayer.stop()
//
//// hide UI elements
//        binding.soundButton.visibility = View.INVISIBLE
//        binding.maneuverView.visibility = View.INVISIBLE
//        binding.routeOverview.visibility = View.INVISIBLE
//        binding.tripProgressCard.visibility = View.INVISIBLE
//    }

//    private fun startSimulation(route: DirectionsRoute) {
//        mapboxReplayer.run {
//            stop()
//            clearEvents()
//            val replayEvents = ReplayRouteMapper().mapDirectionsRouteGeometry(route)
//            pushEvents(replayEvents)
//            try {
//                seekTo(replayEvents.first())
//            } catch (e: Exception) {
//            }
//            play()
//        }
//    }

    companion object {
        private const val TAG = "ActivityNavigation"
        private const val BUTTON_ANIMATION_DURATION = 1500L
    }

}