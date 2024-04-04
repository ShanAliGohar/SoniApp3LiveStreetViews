package com.live.streetview.navigation.earthmap.compass.map.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation
import com.live.streetview.navigation.earthmap.compass.map.R
import com.squareup.picasso.Picasso

class FlagInformartionActivity : AppCompatActivity() {
    private var txtCountryName: TextView? = null
    private var txtPopulation: TextView? = null
    private var txtArea: TextView? = null
    private var txtTimeZone: TextView? = null
    private var txtCurrency: TextView? = null
    private var txtLanguage: TextView? = null
    private var txtLat: TextView? = null
    private var txtLong: TextView? = null
    private var imagepakk: ImageView? = null
    private var imagerrowback: ImageView? = null
    private var whiteView: View? = null
    private val callbackEnabled = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flag_informartion)
        logAnalyticsForClicks("StreetViewCountryInfoDetailsOnCreate", this@FlagInformartionActivity)

        //call back back press
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        txtCountryName = findViewById(R.id.countryNamepak)
        txtPopulation = findViewById(R.id.textpopulation)
        txtArea = findViewById(R.id.textarea1)
        txtTimeZone = findViewById(R.id.texttimezone)
        txtCurrency = findViewById(R.id.textcurrency)
        txtLanguage = findViewById(R.id.textlanguage)
        txtLat = findViewById(R.id.textlatttt)
        txtLong = findViewById(R.id.textlonggg)
        imagepakk = findViewById(R.id.countryImagepak)
        imagerrowback = findViewById(R.id.arrowbackflag)
        whiteView = findViewById(R.id.whiteView)
        imagerrowback?.setOnClickListener(View.OnClickListener {
            logAnalyticsForClicks(
                "StreetViewCountryInfoDetailsOnBtnExit",
                this@FlagInformartionActivity
            )
            mediationBackPressedSimpleStreetViewLocation(
                this@FlagInformartionActivity,
                StreetViewAppSoniMyAppAds.admobInterstitialAd, whiteView!!
            )
        })
        val allCountryDataModl = CountryInfoActivity.modelList
        if (allCountryDataModl != null) {
            Picasso.get().load(allCountryDataModl.flags?.png).into(imagepakk)
            try {
                txtCountryName?.setText(allCountryDataModl.name)
                txtPopulation?.setText(allCountryDataModl.population.toString())
                txtArea?.setText(allCountryDataModl.area.toString())
                txtTimeZone?.setText(allCountryDataModl.timezones?.get(0))
                txtCurrency?.setText(allCountryDataModl.currencies?.get(0)?.name)
                txtLanguage?.setText(allCountryDataModl.languages?.get(0)?.name)
                txtLat?.setText(allCountryDataModl.latlng?.get(0).toString() + "")
                txtLong?.setText(allCountryDataModl.latlng?.get(1).toString() + "")
            } catch (e: NullPointerException) {
            }
        }
    }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                logAnalyticsForClicks(
                    "StreetViewCountryInfoDetailsOnExit",
                    this@FlagInformartionActivity
                )
                mediationBackPressedSimpleStreetViewLocation(
                    this@FlagInformartionActivity,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,
                    whiteView!!
                )
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback.remove()
    }
}