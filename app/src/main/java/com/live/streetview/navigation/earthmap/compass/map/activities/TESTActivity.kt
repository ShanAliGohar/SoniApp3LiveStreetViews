package com.live.streetview.navigation.earthmap.compass.map.activities

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.ads.AdView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds.getAdSize
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediation
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation
import com.live.streetview.navigation.earthmap.compass.map.activities.utils.GPSONOFF
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityTestactivityBinding
import org.osmdroid.util.GeoPoint

class TESTActivity() : AppCompatActivity() {
    //    private static final String DISTANCE_UNITS = UNIT_KILOMETERS; // DISTANCE_UNITS must be equal to a String
    // found in the TurfConstants class
    private val pointList: MutableList<GeoPoint>? = ArrayList()

    //    private MapboxMap mapboxMap;
    private val lineLengthTextView: TextView? = null
    private val textMeter: TextView? = null
    private var totalLineDistanceTest = 0.0
    private var fusedLocationClientDistance: FusedLocationProviderClient? = null
    var userLocDistance: Location? = null
    var bindingDistance: ActivityTestactivityBinding? = null
    var lat = 0.0
    var lng = 0.0
    var dexp = 0.0

    //    GeoJsonSource geoJsonSourceDistance;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Mapbox access token is configured here. This needs to be called either in your application
// object or in the same activity which contains the mapview.
//        Mapbox.getInstance(this, UtilityClass.MAPBOX_KEY );
        bindingDistance = ActivityTestactivityBinding.inflate(
            layoutInflater
        )
        setContentView(bindingDistance!!.root)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

// This contains the MapView in XML and needs to be called after the access token is configured.
//        mapView = findViewById(R.id.AreaDistance);
//        mapView.onCreate(savedInstanceState);
        val obj = GPSONOFF()
        obj.gpsONOFF(this)
        //initBannerAdLiveDis();

//        mapView.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(@NonNull MapboxMap mapboxMap) {
//                UiSettings uiSettings = mapboxMap.getUiSettings();
//                uiSettings.setCompassEnabled(false);
//                TESTActivity.this.mapboxMap = mapboxMap;
//               testCutrrentLoc();
//                TESTActivity.this.mapboxMap.setStyle(Style.DARK, new Style.OnStyleLoaded() {
//                    @Override
//                    public void onStyleLoaded(@NonNull Style style) {
//
//                        IconFactory factory = IconFactory.getInstance(TESTActivity.this);
//                        Icon icon = factory.fromResource(R.drawable.map_default_map_marker);
//
//
//
//                    }
//                });
//
//                lineLengthTextView = findViewById(R.id.line_length_textView);
//                textMeter=findViewById(R.id.textDistnceMeter);
//
//// DISTANCE_UNITS must be equal to a String found in the TurfConstants class
//                lineLengthTextView.setText(String.format("",
//                        DISTANCE_UNITS, String.valueOf(totalLineDistanceTest)));
//                textMeter.setText(String.format("",DISTANCE_UNITS,String.valueOf(totalLineDistanceTest)));
//
//                TESTActivity.this.mapboxMap = mapboxMap;
////                mapboxMap.setStyle(new Style.Builder()
////                                .fromUri(STYLE_URI)
////
////// Add the source to the map
////                                .withSource(new GeoJsonSource(SOURCE_ID))
////
////// Style and add the CircleLayer to the map
////                                .withLayer(new CircleLayer(CIRCLE_LAYER_ID, SOURCE_ID).withProperties(
////                                        circleColor(CIRCLE_COLOR),
////                                        circleRadius(CIRCLE_RADIUS)
////                                ))
////
////// Style and add the LineLayer to the map. The LineLayer is placed below the CircleLayer.
////                                .withLayerBelow(new LineLayer(LINE_LAYER_ID, SOURCE_ID).withProperties(
////                                        lineColor(LINE_COLOR),
////                                        lineWidth(LINE_WIDTH),
////                                        lineJoin(LINE_JOIN_ROUND)
////                                ), CIRCLE_LAYER_ID), new Style.OnStyleLoaded() {
////                            @Override
////                            public void onStyleLoaded(@NonNull Style style) {
////                                TESTActivity.this.mapboxMap.addOnMapClickListener(TESTActivity.this);
////                                //Toast.makeText(TESTActivity.this, "", Toast.LENGTH_SHORT).show();
////                            }
////                        }
////                );
//            }
//        });
        bindingDistance!!.arrowBackDistance.setOnClickListener(View.OnClickListener { })
        bindingDistance!!.imageSetallite.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                bindingDistance!!.ConDistanceLayout.visibility = View.GONE
                //                mapboxMap.setStyle(Style.SATELLITE, new Style.OnStyleLoaded() {
//                    @Override
//                    public void onStyleLoaded(@NonNull Style style) {
//
//                    }
//                });
            }
        })
        bindingDistance!!.DistanceClearOne.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {}
        })
        bindingDistance!!.arrowBackDistance.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                onBackPressedDispatcher.onBackPressed()
            }
        })
        bindingDistance!!.imageDedaultDistance.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                bindingDistance!!.ConDistanceLayout.visibility = View.GONE
                //                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
//                    @Override
//                    public void onStyleLoaded(@NonNull Style style) {
//
//                    }
//                });
            }
        })
        bindingDistance!!.imageDistanceNight.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                bindingDistance!!.ConDistanceLayout.visibility = View.GONE
                //                mapboxMap.setStyle(Style.TRAFFIC_NIGHT, new Style.OnStyleLoaded() {
//                    @Override
//                    public void onStyleLoaded(@NonNull Style style) {
//
//                    }
//                });
            }
        })
        bindingDistance!!.DistanceClearOne.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                try {
                    clearOne()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
        bindingDistance!!.viewClearAllDistance.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                try {
                    clearMap()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                pointList!!.clear()
                bindingDistance!!.lineLengthTextView.text = "0"
                bindingDistance!!.textDistnceMeter.text = "0"
                totalLineDistanceTest = 0.0
            }
        })
        bindingDistance!!.imageSetalliteStreet.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                bindingDistance!!.ConDistanceLayout.visibility = View.GONE
                //                mapboxMap.setStyle(Style.SATELLITE_STREETS, new Style.OnStyleLoaded() {
//                    @Override
//                    public void onStyleLoaded(@NonNull Style style) {
//
//                    }
//                });
            }
        })
        bindingDistance!!.CurrentLocDist.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                testCutrrentLoc()
            }
        })
        streetViewBannerAdsSmall()
    }

    private fun streetViewBannerAdsSmall() {
        val billingHelper = StreetViewAppSoniBillingHelper(this)
        val adContainer = bindingDistance!!.bannerAdPlace.adContainer
        val adView = AdView(this)
        adView.adUnitId = StreetViewAppSoniMyAppAds.banner_admob_inApp
        adView.setAdSize(getAdSize(this))
        if (billingHelper.isNotAdPurchased) {
            loadSmartToolsBannerForMainMediation(
                adContainer, adView, this
            )
        } else {
            bindingDistance!!.bannerID.visibility = View.GONE
        }
    }

    //    private void initBannerAdLiveDis() {
    //        StreetViewLoadAds.loadSoniStreetViewBannerAdMob(
    //                bindingDistance.bannerAdPlace.adContainer,
    //                bindingDistance.bannerID,
    //                this
    //        );
    //    }
    private fun clearOne() {
        if (pointList!!.size <= 1) {

//            geoJsonSourceDistance.setGeoJson(FeatureCollection.fromFeatures(new Feature[]{}));
            pointList.clear()
        }
        run {
            try {
                pointList!!.removeAt(pointList.size - 1)
                //                  geoJsonSourceDistance.setGeoJson(Feature.fromGeometry(LineString.fromLngLats(pointList)));
            } catch (e: Exception) {
                Log.d("omar", "out of bound array index message: " + e.message)
            }
        }
        //  pointList.clear();
    }

    private fun clearMap() {
//
        try {
            if (pointList != null) {
//                geoJsonSourceDistance.setGeoJson(FeatureCollection.fromFeatures(new Feature[]{}));
            }
            if (pointList != null) {
//                geoJsonSourceDistance.setGeoJson(FeatureCollection.fromFeatures(new Feature[]{}));
            }
            pointList!!.clear()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun testCutrrentLoc() {
        fusedLocationClientDistance = FusedLocationProviderClient(this)
        userLocDistance = Location("service Provider")
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClientDistance!!.lastLocation.addOnCompleteListener(object :
            OnCompleteListener<Location> {
            override fun onComplete(task: Task<Location>) {
                if (task.isSuccessful) {
                    userLocDistance = task.result
                    if (userLocDistance == null) {
                        // gpsONOFF(TESTActivity.this);
                        //    Toast.makeText(LiveEarthMapActivity.this, "Permission not granted", Toast.LENGTH_LONG).show();
                    } else {
                        lat = userLocDistance!!.latitude
                        lng = userLocDistance!!.longitude
                        myTestLoc()
                        //  Log.d(TAG, "onComplete: " + userLocLiveEarth.getLatitude() + "," + userLocLiveEarth.getLongitude());
                    }
                }
            }
        })
    }

    private fun myTestLoc() {
//        origin = Point.fromLngLat(lng, lat);
//        destination = Point.fromLngLat(lng, lat);
//
//        mapboxMap.addMarker(new MarkerOptions()
//                .position(new LatLng(lat, lng)));
//       // makeMarkerDestination(lat, lng);
//
//        CameraPosition position = new CameraPosition.Builder()
//                .target(new LatLng(lat, lng))
//                .zoom(12)
//                .bearing(180) // Rotate the camera
//                .tilt(30) // Set the camera tilt
//                .build(); // Creates a CameraPosition from the builder
//        mapboxMap.animateCamera(CameraUpdateFactory
//                .newCameraPosition(position), 7000);
    }
    //    @Override
    //    public boolean onMapClick(@NonNull LatLng point) {
    //        addClickPointToLine(point);
    //        return true;
    //    }
    /**
     * Handle the map click location and re-draw the circle and line data.
     *
     * @param clickLatLng where the map was tapped on.
     */
    private fun addClickPointToLine(clickLatLng: GeoPoint) {
//        mapboxMap.getStyle(new Style.OnStyleLoaded() {
//            @Override
//            public void onStyleLoaded(@NonNull Style style) {
//// Get the source from the map's style
//                  geoJsonSourceDistance = style.getSourceAs(SOURCE_ID);
//                if (geoJsonSourceDistance != null) {
//
//                    pointList.add(GeoPoint(clickLatLng.(), clickLatLng.getLatitude()));
//
//                    int pointListSize = pointList.size();
//
//                    double distanceBetweenLastAndSecondToLastClickPoint = 0;
//
//// Make the Turf calculation between the last tap point and the second-to-last tap point.
//                    if (pointList.size() >= 2) {
//                        distanceBetweenLastAndSecondToLastClickPoint = TurfMeasurement.distance(
//                                pointList.get(pointListSize - 2), pointList.get(pointListSize - 1));
//                    }
//
//// Re-draw the new GeoJSON data
//                    if (pointListSize >= 2 && distanceBetweenLastAndSecondToLastClickPoint > 0) {
//
//// Add the last TurfMeasurement#distance calculated distance to the total line distance.
//                        totalLineDistanceTest += distanceBetweenLastAndSecondToLastClickPoint;
//
//                        Log.d("varMsg", "onStyleLoaded: "+ totalLineDistanceTest);
//// Adjust the TextView to display the new total line distance.
//// DISTANCE_UNITS must be equal to a String found in the TurfConstants class
//
//
//                        dexp = SphericalUtilStreetView.computeArea(pointList);
//                        String numSring = new DecimalFormat("######.##").format(dexp);
//                        String numSringM = new DecimalFormat("######.##").format(dexp/1000);
////                        String numSring = String.format("%.4f", dexp);
//                        bindingDistance.textDistnceMeter.setText( numSringM + " m");
//                        //textDistnceMeter.setText
//                        lineLengthTextView.setText(numSring+" km");
//// Set the specific source's GeoJSON data
//                        geoJsonSourceDistance.setGeoJson(Feature.fromGeometry(LineString.fromLngLats(pointList)));
//
//                    }
//                }
//            }
//        });
    }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Show ads logic
                mediationBackPressedSimpleStreetViewLocation(
                    this@TESTActivity,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd, bindingDistance!!.whiteView
                )
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback.remove()
    }

    // Add the mapView lifecycle to the activity's lifecycle methods
    public override fun onResume() {
        super.onResume()
        //        mapView.onResume();
    }

    override fun onStart() {
        super.onStart()
        //        mapView.onStart();
    }

    override fun onStop() {
        super.onStop()
        //        mapView.onStop();
    }

    public override fun onPause() {
        super.onPause()
        //        mapView.onPause();
    }

    override fun onLowMemory() {
        super.onLowMemory()
        //        mapView.onLowMemory();
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //        mapView.onSaveInstanceState(outState);
    }

    companion object {
        private val SOURCE_ID = "SOURCE_ID"
        private val CIRCLE_LAYER_ID = "CIRCLE_LAYER_ID"
        private val LINE_LAYER_ID = "LINE_LAYER_ID"

        // Adjust private static final variables below to change the example's UI
        private val STYLE_URI =
            "mapbox://styles/mapbox/streets-v11" //mapbox://styles/mapbox/cjv6rzz4j3m4b1fqcchuxclhb";
        private val CIRCLE_COLOR = Color.RED
        private val LINE_COLOR = CIRCLE_COLOR
        private val CIRCLE_RADIUS = 6f
        private val LINE_WIDTH = 4f

        //    private MapView mapView;
        var origin: GeoPoint? = null
        var destination: GeoPoint? = null
    }
}