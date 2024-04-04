package com.live.streetview.navigation.earthmap.compass.map.activities

import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.ads.AdView
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityKmlViewerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.bonuspack.kml.KmlDocument
import org.osmdroid.bonuspack.kml.KmlFeature
import org.osmdroid.bonuspack.kml.KmlPlacemark
import org.osmdroid.bonuspack.kml.KmlTrack
import org.osmdroid.bonuspack.kml.LineStyle
import org.osmdroid.bonuspack.kml.Style
import org.osmdroid.bonuspack.location.OverpassAPIProvider
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.NetworkLocationIgnorer
import org.osmdroid.util.TileSystem
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.FolderOverlay
import org.osmdroid.views.overlay.ScaleBarOverlay
import org.osmdroid.views.overlay.TilesOverlay
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay
import java.io.InputStream

class KmlViewerActivity : AppCompatActivity(), LocationListener {
    var map: MapView? = null
    var myLocationOverlay: DirectedLocationOverlay? = null

    var mLocationManager: LocationManager? = null

    var mTrackingMode = false
    var mAzimuthAngleSpeed = 0.0f

    var mKmlOverlay //root container of overlays from KML reading
            : FolderOverlay? = null
    var mKmlDocument //made static to pass between activities
            : KmlDocument? = null
    var mIsRecordingTrack = false
    var buttonBack: ImageView? = null
    var mNightMode = false
    var binding: ActivityKmlViewerBinding? = null


    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

            StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation(
                this@KmlViewerActivity,
                StreetViewAppSoniMyAppAds.admobInterstitialAd,
                binding!!.whiteView
            )

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        binding = ActivityKmlViewerBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        Log.d("backTag", "onCreate")


//        callback.remove()
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)


        val prefs = getSharedPreferences("OSMNAVIGATOR", android.content.Context.MODE_PRIVATE)
        map = findViewById<android.view.View>(R.id.map) as MapView
        val tileProviderName = prefs.getString("TILE_PROVIDER", "Mapnik")
        mNightMode = prefs.getBoolean("NIGHT_MODE", false)
        try {
            val tileSource = TileSourceFactory.getTileSource(tileProviderName)
            map!!.setTileSource(tileSource)
        } catch (e: java.lang.IllegalArgumentException) {
            map!!.setTileSource(TileSourceFactory.MAPNIK)
        }


        if (mNightMode) map!!.overlayManager.tilesOverlay.setColorFilter(TilesOverlay.INVERT_COLORS)
        map!!.isTilesScaledToDpi = true
        map!!.setMultiTouchControls(true)
        map!!.minZoomLevel = 1.0
        map!!.maxZoomLevel = 21.0
        map!!.isVerticalMapRepetitionEnabled = false
        map!!.setScrollableAreaLimitLatitude(
            TileSystem.MaxLatitude, -TileSystem.MaxLatitude, 0 /*map.getHeight()/2*/
        )
        val mapController = map!!.controller
        mLocationManager =
            getSystemService(android.content.Context.LOCATION_SERVICE) as LocationManager

        buttonBack = findViewById(R.id.imageView39)

        buttonBack!!.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        mapController.setZoom(prefs.getFloat("MAP_ZOOM_LEVEL_F", 5f).toDouble())
        mapController.setCenter(
            org.osmdroid.util.GeoPoint(
                prefs.getFloat("MAP_CENTER_LAT", 48.5f).toDouble(),
                prefs.getFloat("MAP_CENTER_LON", 2.5f).toDouble()
            )
        )
        myLocationOverlay = DirectedLocationOverlay(this)
        map!!.overlays.add(myLocationOverlay)
        val scaleBarOverlay = ScaleBarOverlay(map)
        map!!.overlays.add(scaleBarOverlay)
        //KML handling:
        mKmlOverlay = null
        checkPermissions()
        val path = intent.extras!!.getString("path")
        if (path != null) {
            Log.d("TAG", "onCreate: path ${path}")
            openFile(path, false, false)
        }
        streetViewBannerAdsSmall()
    }

    private val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124

    fun checkPermissions() {
        val permissions: MutableList<String> = java.util.ArrayList<String>()
        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (!permissions.isEmpty()) {
            val params = permissions.toTypedArray<String>()
            ActivityCompat.requestPermissions(this, params, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS)
        } // else: We already have permissions, so handle as normal
    }

    fun setViewOn(bb: BoundingBox?) {
        if (bb != null) {
            map!!.zoomToBoundingBox(bb, true)
        }
    }

    //--- Stuff for setting the mapview on a box at startup:
    var mInitialBoundingBox: BoundingBox? = null
    fun setInitialViewOn(bb: BoundingBox?) {
        if (map!!.getScreenRect(null).height() == 0) {
            mInitialBoundingBox = bb
        } else map!!.zoomToBoundingBox(bb, false)
    }


    override fun onActivityResult(
        requestCode: Int, resultCode: Int, intent: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (intent != null) {
            val uri = intent.data
            openFile(uri.toString(), false, false)
        }
    }


    fun startLocationUpdates(): Boolean {
        var result = false
        for (provider in mLocationManager!!.getProviders(true)) {
            if (ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                mLocationManager!!.requestLocationUpdates(
                    provider!!, (2 * 1000).toLong(), 0.0f, this
                )
                result = true
            }
        }
        return result
    }

    override fun onResume() {
        super.onResume()
        val isOneProviderEnabled = startLocationUpdates()
        myLocationOverlay!!.isEnabled = isOneProviderEnabled

    }

    override fun onPause() {
        super.onPause()
        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mLocationManager!!.removeUpdates(this)
        }
        //TODO: mSensorManager.unregisterListener(this);
        /*    mFriendsManager.onPause()*/
    }


    //------------ KML handling

    //------------ KML handling
    val PICK_KML_FILE = 18
    fun openLoadFileDialog() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        startActivityForResult(intent, PICK_KML_FILE)
    }

    fun openUrlDialog() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.url_kml_open))
        val input = EditText(this)
        input.inputType = android.text.InputType.TYPE_CLASS_TEXT
        val defaultUri = "https://raw.githubusercontent.com/googlemaps/kml-samples/gh-pages/a/h.kml"
        val uri = getSharedPreferences(
            "OSMNAVIGATOR", android.content.Context.MODE_PRIVATE
        ).getString("KML_URI", defaultUri)
        input.setText(uri)
        builder.setView(input)
        builder.setPositiveButton(
            getString(R.string.ok)
        ) { dialog, which ->
            val uri = input.text.toString()
            val prefs = getSharedPreferences("OSMNAVIGATOR", android.content.Context.MODE_PRIVATE)
            prefs.edit().putString("KML_URI", uri).apply()
            dialog.cancel()
            openFile(uri, false, false)
        }
        builder.setNegativeButton(
            getString(R.string.cancel)
        ) { dialog, which -> dialog.cancel() }
        builder.show()
    }


    fun getKMLFromOverpass(query: String?): Boolean {
        val overpassProvider = OverpassAPIProvider()
        val oUrl = overpassProvider.urlForTagSearchKml(query, map!!.boundingBox, 500, 30)
        return overpassProvider.addInKmlFolder(
            mKmlDocument!!.mKmlRoot, oUrl
        )
    }

    fun createSpinningDialog(title: String?): ProgressDialog {
        val pd = ProgressDialog(map!!.context)
        pd.setTitle(title)
        pd.setMessage(getString(R.string.wait))
        pd.setCancelable(false)
        pd.isIndeterminate = true
        return pd
    }

    suspend fun loadKmlFile(
        mMessage: String,
        isOverpassRequest: Boolean,
        mOnCreate: Boolean,
        mUri: String,
        mPD: ProgressDialog
    ) {

        withContext(Dispatchers.Main) {
            mPD.show()
        }
        var ok = false
        mKmlDocument = KmlDocument()
        val job = CoroutineScope(Dispatchers.IO).async {
            if (isOverpassRequest) {
                //mUri contains the query
                ok = getKMLFromOverpass(mUri)
            } else if (mUri.startsWith("http")) {
                ok = mKmlDocument!!.parseKMLUrl(mUri)
            } else if (mUri.startsWith("content://")) {
                try {
                    val uri = android.net.Uri.parse(mUri)
                    val inputStream: InputStream? =
                        this@KmlViewerActivity.contentResolver.openInputStream(uri)
                    ok = if (mUri.endsWith(".json")) mKmlDocument!!.parseKMLStream(
                        inputStream, null
                    )
                    else  //assume KML
                        mKmlDocument!!.parseKMLStream(
                            inputStream, null
                        )
                    inputStream!!.close()
                } catch (e: java.lang.Exception) {
                }
            }

        }
        job.await()

//        Handler().postDelayed({
        withContext(Dispatchers.Main) {
            mPD.dismiss()
            if (!ok) Toast.makeText(
                applicationContext, "Sorry, unable to read $mUri", Toast.LENGTH_SHORT
            ).show()
            updateUIWithKml()
            if (ok) {
                val bb: BoundingBox = mKmlDocument!!.mKmlRoot.boundingBox

                if (bb != null) {
                    if (!mOnCreate) setViewOn(bb) else  //KO in onCreate (osmdroid bug) - Workaround:
                        setInitialViewOn(bb)
                }
            }
        }


//        }, 5000)


    }

    fun openFile(uri: String?, onCreate: Boolean, isOverpassRequest: Boolean) {
        val mPD = createSpinningDialog(getString(R.string.loading) + " " + uri)
        CoroutineScope(Dispatchers.IO).launch {
            loadKmlFile(
                getString(R.string.loading) + " " + uri, onCreate, isOverpassRequest, uri!!, mPD
            )
        }

    }

    /** save fileName locally, as KML or GeoJSON depending on the extension  */

    fun buildDefaultStyle(): Style {
        val defaultKmlMarker =
            ResourcesCompat.getDrawable(resources, R.drawable.marker_kml_point, null)
        val bitmap = (defaultKmlMarker as BitmapDrawable?)!!.bitmap
        return Style(bitmap, -0x6fefef56, 3.0f, 0x20AA1010)
    }

    fun updateUIWithKml() {
        if (mKmlOverlay != null) {
            mKmlOverlay!!.closeAllInfoWindows()
            map!!.overlays.remove(mKmlOverlay)
        }
        mKmlOverlay = mKmlDocument!!.mKmlRoot.buildOverlay(
            map, buildDefaultStyle(), null, mKmlDocument
        ) as FolderOverlay
        map!!.overlays.add(mKmlOverlay)
        map!!.invalidate()
    }


    //------------ LocationListener implementation
    private val mIgnorer = NetworkLocationIgnorer()
    var mLastTime: kotlin.Long = 0 // milliseconds

    var mSpeed = 0.0 // km/h

    override fun onLocationChanged(pLoc: android.location.Location) {
        val currentTime = System.currentTimeMillis()
        if (mIgnorer.shouldIgnore(pLoc.provider, currentTime)) return
        val dT = (currentTime - mLastTime).toDouble()
        if (dT < 100.0) {
//            Toast.makeText(this, pLoc.provider + " dT=" + dT, Toast.LENGTH_SHORT).show();
            Log.d("TAG", "onLocationChanged: ${pLoc.provider + " dT=" + dT}")
            return
        }
        mLastTime = currentTime
        val newLocation = org.osmdroid.util.GeoPoint(pLoc)
        if (!myLocationOverlay!!.isEnabled) {
            myLocationOverlay!!.isEnabled = true
            map!!.controller.animateTo(newLocation)
        }
        val prevLocation = myLocationOverlay!!.location
        myLocationOverlay!!.location = newLocation
        myLocationOverlay!!.setAccuracy(pLoc.accuracy.toInt())
        if (prevLocation != null && pLoc.provider == LocationManager.GPS_PROVIDER) {
            mSpeed = pLoc.speed * 3.6
            //TODO: check if speed is not too small
            if (mSpeed >= 0.1) {
                mAzimuthAngleSpeed = pLoc.bearing
                myLocationOverlay!!.setBearing(mAzimuthAngleSpeed)
            }
        }
        if (mTrackingMode) {
            //keep the map view centered on current location:
            map!!.controller.animateTo(newLocation)
            map!!.mapOrientation = -mAzimuthAngleSpeed
        } else {
            //just redraw the location overlay:
            map!!.invalidate()
        }
        if (mIsRecordingTrack) {
            recordCurrentLocationInTrack("my_track", "My Track", newLocation)
        }
    }

    var TrackColor = intArrayOf(
        android.graphics.Color.CYAN - 0x20000000,
        android.graphics.Color.BLUE - 0x20000000,
        android.graphics.Color.MAGENTA - 0x20000000,
        android.graphics.Color.RED - 0x20000000,
        android.graphics.Color.YELLOW - 0x20000000
    )

    fun createTrack(id: String, name: String?): KmlTrack {
        val t = KmlTrack()
        val p = KmlPlacemark()
        p.mId = id
        p.mName = name
        p.mGeometry = t
        mKmlDocument!!.mKmlRoot.add(p)
        //set a color to this track by creating a style:
        val s = Style()
        var color: Int
        try {
            color = id.toInt()
            color %= TrackColor.size
            color = TrackColor[color]
        } catch (e: java.lang.NumberFormatException) {
            color = android.graphics.Color.GREEN - 0x20000000
        }
        s.mLineStyle = LineStyle(color, 8.0f)
        p.mStyle = mKmlDocument!!.addStyle(s)
        return t
    }

    fun recordCurrentLocationInTrack(
        trackId: String, trackName: String?, currentLocation: org.osmdroid.util.GeoPoint?
    ) {
        //Find the KML track in the current KML structure - and create it if necessary:
        val t: KmlTrack
        val f: KmlFeature = mKmlDocument!!.mKmlRoot.findFeatureId(trackId, false)
        t = if (f == null) createTrack(
            trackId, trackName
        ) else if (f !is KmlPlacemark) //id already defined but is not a PlaceMark
            return else {
            val p = f as KmlPlacemark
            if (p.mGeometry !is KmlTrack) //id already defined but is not a Track
                return else p.mGeometry as KmlTrack
        }
        //record in the track the current location at current time:
        t.add(currentLocation, java.util.Date())
        //refresh KML:
        updateUIWithKml()
    }


    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    private fun streetViewBannerAdsSmall() {
        val billingHelper = StreetViewAppSoniBillingHelper(this)
        val adContainer: LinearLayout = binding!!.bannerAdPlace.adContainer
        val adView = AdView(this)
        adView.adUnitId = StreetViewAppSoniMyAppAds.banner_admob_inApp
        if (billingHelper.isNotAdPurchased) {
            StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediation(
                adContainer, adView, this
            )
        } else {
            binding!!.bannerID.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback.remove()
    }


}