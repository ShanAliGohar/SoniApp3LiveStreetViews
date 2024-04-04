package com.live.streetview.navigation.earthmap.compass.map.activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdView
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds.getAdSize
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediation
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation
import com.live.streetview.navigation.earthmap.compass.map.MainActivity
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.activities.callack.MyLocationCallBack
import com.live.streetview.navigation.earthmap.compass.map.activities.utils.GPSONOFF
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityAltimeterBinding
import java.io.IOException
import java.text.DecimalFormat
import java.util.Locale

class AltimeterActivity : AppCompatActivity(), LocationListener {
    private var txtaltLat: TextView? = null
    private var txtaltLong: TextView? = null
    private var txtcurrentlocalt: TextView? = null
    private var txtaltimeter: TextView? = null
    var altlat = 0.0
    var altlng = 0.0
    var mylocation: MyLocation? = null
    var progressBar: ProgressBar? = null
    var progressBar1: ProgressBar? = null
    var constraintLayout: ConstraintLayout? = null
    var constraintLayout1: ConstraintLayout? = null
    var btnMeter: RadioButton? = null
    var btnFeet: RadioButton? = null
    var radioGroup: RadioGroup? = null
    private var imagealt: ImageView? = null
    var bindingAltimeter: ActivityAltimeterBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingAltimeter = ActivityAltimeterBinding.inflate(
            layoutInflater
        )
        setContentView(bindingAltimeter!!.root)
        logAnalyticsForClicks("StreetViewAltimeterOnCreate", this@AltimeterActivity)
        txtaltLat = findViewById(R.id.txtaltilat)
        txtaltLong = findViewById(R.id.textaltilng)
        txtcurrentlocalt = findViewById(R.id.currentlocalti)
        txtcurrentlocalt?.setSelected(true)
        txtaltimeter = findViewById(R.id.textaltimeter)
        btnMeter = findViewById(R.id.btnMeter)
        btnFeet = findViewById(R.id.btnFeet)
        radioGroup = findViewById(R.id.radiogroup)
        progressBar = findViewById(R.id.textaltimeter1)
        // progressBar1=findViewById(R.id.progressbarcurrentloc);
        constraintLayout = findViewById(R.id.constraintLayout9)
        constraintLayout1 = findViewById(R.id.constraintLayout8)
        val obj = GPSONOFF()
        obj.gpsONOFF(this)
        //   initBannerAdLiveAlti();
        imagealt = findViewById(R.id.imageAltimterback)
        //call back back press
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        imagealt?.setOnClickListener(View.OnClickListener {
            logAnalyticsForClicks("StreetViewAltimeterOnCreateBtnExit", this@AltimeterActivity)
            mediationBackPressedSimpleStreetViewLocation(
                this@AltimeterActivity,
                StreetViewAppSoniMyAppAds.admobInterstitialAd, bindingAltimeter!!.whiteView
            )
        })
        btnMeter?.setChecked(true)
      /*  if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }*/
        if (!checkPermission()){
            requestPermission()
        }else{
            mylocation = MyLocation(this, object : MyLocationCallBack {
                override fun onLocationListener(location: Location?) {

                    progressBar?.setVisibility(View.GONE)
                    constraintLayout?.setVisibility(View.VISIBLE)
                    txtaltLat?.setText(location!!.latitude.toString())
                    txtaltLong?.setText(location?.longitude.toString())
                    onLocationChanged(location!!)
                }

                override fun MyLocationListener(location: Location?) {}
            })
            mylocation!!.location
            streetViewBannerAdsSmall()
        }

    }

    private fun streetViewBannerAdsSmall() {
        val billingHelper = StreetViewAppSoniBillingHelper(this)
        val adContainer = bindingAltimeter!!.bannerAdPlace.adContainer
        val adView = AdView(this)
        adView.adUnitId = StreetViewAppSoniMyAppAds.banner_admob_inApp
        adView.setAdSize(getAdSize(this))
        if (billingHelper.isNotAdPurchased) {
            loadSmartToolsBannerForMainMediation(
                adContainer, adView, this
            )
        } else {
            bindingAltimeter!!.bannerID.visibility = View.GONE
        }
    }

    override fun onLocationChanged(location: Location) {
        altlat = location.latitude
        altlng = location.longitude
        val altiValue = location.altitude
        try {
            val geo = Geocoder(this@AltimeterActivity.applicationContext, Locale.getDefault())
            val addresses = geo.getFromLocation(altlat, altlng, 4)

            //latituteField.setText("Loading...");
            if (addresses != null && addresses.size > 0) {
                val locality = addresses[0].getAddressLine(0)
                txtcurrentlocalt!!.movementMethod = ScrollingMovementMethod()
                txtcurrentlocalt!!.text = locality
                val country = addresses[0].countryName
                val state = addresses[0].adminArea
                val sub_admin = addresses[0].subAdminArea
                val city = addresses[0].featureName
                val pincode = addresses[0].postalCode
                val locality_city = addresses[0].locality
                val sub_localoty = addresses[0].subLocality
                altitudeWork(altiValue)
                mylocation!!.onStop()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun altitudeWork(altiValue: Double) {
        txtaltimeter!!.visibility = View.VISIBLE
        val meters = altiValue / 3.2808
        txtaltimeter!!.text = DecimalFormat("#.#").format(meters) + "m"
        val feet = altiValue * 3.2808
        //txtaltimeter.setText(new DecimalFormat("#.#").format(feet) + "f");

        //now check which radio button is selected
        //android switch statement
        radioGroup!!.setOnCheckedChangeListener { radioGroup, i ->
            if (btnMeter!!.isChecked) {
                txtaltimeter!!.text = DecimalFormat("#.#").format(meters) + "m"
            }
            if (btnFeet!!.isChecked) {
                txtaltimeter!!.text = DecimalFormat("#.#").format(feet) + "f"
            }
        }
    }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                logAnalyticsForClicks("StreetViewAltimeterOnCreateExit", this@AltimeterActivity)
                mediationBackPressedSimpleStreetViewLocation(
                    this@AltimeterActivity,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,
                    bindingAltimeter!!.whiteView
                )
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback.remove()
    }
    private fun checkPermission(): Boolean {
        val result1 = ContextCompat.checkSelfPermission(
            applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
        )
        return result1 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            ), MainActivity.PERMISSION_REQUEST_CODE
        )
    }
}
