package com.live.streetview.navigation.earthmap.compass.map.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.github.anastr.speedviewlib.SpeedView
import com.google.android.gms.ads.AdView
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds.getAdSize
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediation
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.activities.utils.GPSONOFF
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivitySpeedMeterBinding
import com.live.streetview.navigation.earthmap.compass.map.location.LocationService

class SpeedMeterActivity : AppCompatActivity() {
    var myService: LocationService? = null
    var status = false
    var locationManager: LocationManager? = null


    var time: TextView? = null

    var start: Button? = null
    var pause: android.widget.Button? = null
    var stop: android.widget.Button? = null


    var exitDialog: Dialog? = null

    companion object {
        var p = 0
        var endTime: kotlin.Long = 0
        var dialog: Dialog? = null
        var startTime: Long = 0
        var lastTravelTime: TextView? = null
        var speed: SpeedView? = null
        var distance: TextView? = null
    }

    var imageView: ImageView? = null
    private var checkLocationDialog: Dialog? = null
    var binding: ActivitySpeedMeterBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpeedMeterBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        streetViewBannerAdsSmall()
        logAnalyticsForClicks("StreetViewSpeedoMeterOnCreate", this)

        val obj = GPSONOFF()
        obj.gpsONOFF(this)
        imageView = findViewById(R.id.imageviewspeedometer)
        imageView?.setOnClickListener(View.OnClickListener {
            logAnalyticsForClicks("StreetViewSpeedoMeterOnBtnExit", this@SpeedMeterActivity)
            mediationBackPressedSimpleStreetViewLocation(
                this@SpeedMeterActivity,
                StreetViewAppSoniMyAppAds.admobInterstitialAd, binding!!.whiteView
            )
        })
        variableIni()
        setOnClick()


    }

    @SuppressLint("NewApi", "SetTextI18n")
    private fun setOnClick() {
        start!!.setOnClickListener { v: View? ->
            checkGps()
            locationManager =
                getSystemService(LOCATION_SERVICE) as LocationManager
            if (!locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                return@setOnClickListener
            }
            if (!status) //Here, the Location Service gets bound and the GPS Speedometer gets Active.
                bindService()
            val builder = AlertDialog.Builder(this)
            builder.setView(R.layout.progress_layout)
            builder.setCancelable(true)
            dialog =
                builder.create()
            dialog!!.show()
            dialog!!.setCancelable(
                false
            )
            start!!.visibility = View.GONE
            binding!!.startBtn.setVisibility(View.GONE)
            pause!!.visibility = View.VISIBLE
            binding!!.pauseBtn.setVisibility(View.VISIBLE)
            pause!!.text = "Pause"
            stop!!.visibility = View.VISIBLE
            binding!!.stopBtn.setVisibility(View.VISIBLE)
        }
        pause!!.setOnClickListener { v: View? ->
            if (pause!!.text.toString().equals("pause", ignoreCase = true)) {
                pause!!.text = "Resume"
                p =
                    1
            } else if (pause!!.text.toString().equals("Resume", ignoreCase = true)) {
                checkGps()
                locationManager =
                    getSystemService(LOCATION_SERVICE) as LocationManager
                if (!locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    //Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
                    return@setOnClickListener
                }
                pause!!.text = "Pause"
                p =
                    0
            }
        }
        stop!!.setOnClickListener { v: View? ->
            if (status) unbindService()
            start!!.visibility = View.VISIBLE
            binding!!.startBtn.setVisibility(View.VISIBLE)
            pause!!.text = "Pause"
            pause!!.visibility = View.GONE
            stop!!.visibility = View.GONE
            binding!!.stopBtn.setVisibility(View.GONE)
            binding!!.pauseBtn.setVisibility(View.GONE)
            p =
                0
        }
    }

    private fun variableIni() {
        checkLocationDialog = Dialog(this)
        start = binding!!.start
        pause = binding!!.pause
        stop = binding!!.stop
        speed =
            binding!!.speedometer
        distance =
            binding!!.totalDistance
        //  maxSpeed = binding.maxSpeed;
        // minSpeed = binding.minSpeed;
        //   avgSpeed = binding.avgSpeed;
        time =
            binding!!.time
        lastTravelTime =
            binding!!.timeTreavel
        exitDialog = Dialog(this)
    }

    private val sc: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as LocationService.LocalBinder
            myService = binder.service
            status = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            status = false
        }
    }

    fun bindService() {
        if (status) return
        val i = Intent(applicationContext, LocationService::class.java)
        bindService(i, sc, BIND_AUTO_CREATE)
        status = true
        startTime = System.currentTimeMillis()
    }

    fun unbindService() {
        if (!status) return
        val i = Intent(applicationContext, LocationService::class.java)
        unbindService(sc)
        status = false
    }

    private fun checkGps() {
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        } else {
            if (checkLocationDialog!!.isShowing);
            run { checkLocationDialog!!.cancel() }
        }
    }

    private fun streetViewBannerAdsSmall() {
        val billingHelper = StreetViewAppSoniBillingHelper(this)
        val adContainer = binding!!.bannerAdPlace.adContainer
        val adView = AdView(this)
        adView.adUnitId = StreetViewAppSoniMyAppAds.banner_admob_inApp
        adView.setAdSize(getAdSize(this))
        if (billingHelper.isNotAdPurchased) {
            loadSmartToolsBannerForMainMediation(
                adContainer, adView, this
            )
        } else {
            binding!!.bannerID.visibility = View.GONE
        }
    }

    private fun buildAlertMessageNoGps() {
        checkLocationDialog!!.setContentView(R.layout.location_check_dialog)
        checkLocationDialog!!.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        checkLocationDialog!!.setCancelable(false)
        checkLocationDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val enable = checkLocationDialog!!.findViewById<Button>(R.id.enable)
        val cancel = checkLocationDialog!!.findViewById<Button>(R.id.cancel)
        enable.setOnClickListener { v: View? ->
            try {
                val callGPSSettingIntent = Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS
                )
                startActivity(callGPSSettingIntent)
            } catch (e: Exception) {
            }
        }
        cancel.setOnClickListener { v: View? ->
            try {
                checkLocationDialog!!.cancel()
            } catch (e: Exception) {
            }
        }
        checkLocationDialog!!.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        showExitDialog()
    }

    private fun showExitDialog() {
        exitDialog!!.setContentView(R.layout.exit_dialog)
        exitDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val icon = exitDialog!!.findViewById<ImageView>(R.id.app_icon)
        val cancelTxt = exitDialog!!.findViewById<TextView>(R.id.canelTxt)
        val yesBtn = exitDialog!!.findViewById<Button>(R.id.yesBtn)
        val noBtn = exitDialog!!.findViewById<Button>(R.id.noBtn)
        try {
            Glide.with(this).load(R.drawable.speedometer).into(icon)
        } catch (e: java.lang.Exception) {
        }
        cancelTxt.text =
            "Your process will be cancelled after clicking yes button.\nDo you cancel it?"
        yesBtn.setOnClickListener { view: View? ->
            if (status) unbindService()
            mediationBackPressedSimpleStreetViewLocation(
                this@SpeedMeterActivity,
                StreetViewAppSoniMyAppAds.admobInterstitialAd,
                binding!!.whiteView
            )
        }
        noBtn.setOnClickListener { view: View? -> exitDialog!!.cancel() }
        exitDialog!!.show()
        exitDialog!!.setCancelable(false)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}