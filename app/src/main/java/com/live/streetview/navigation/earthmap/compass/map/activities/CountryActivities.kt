package com.live.streetview.navigation.earthmap.compass.map.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback

import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation
import com.live.streetview.navigation.earthmap.compass.map.Model.CountryNameModel
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.activities.utils.JsonConverter.getJsonFromAssets
import com.live.streetview.navigation.earthmap.compass.map.adapters.VerticleStreetMapAdapter
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityCountryActivitiesBinding
import org.json.JSONArray
import org.json.JSONException

class CountryActivities : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var streetViewMapModelArrayList: ArrayList<CountryNameModel>? = null
    var verticleStreetMapAdapter: VerticleStreetMapAdapter? = null
    var imageArrowBackCountry: ImageView? = null
    var status: String? = null
    var countryName: String? = null
    var bannerAdLayer: LinearLayout? = null
    var bannerID: ConstraintLayout? = null
    var bindingCountry: ActivityCountryActivitiesBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        setContentView(R.layout.activity_country_activities);
        bindingCountry = ActivityCountryActivitiesBinding.inflate(
            layoutInflater
        )
        setContentView(bindingCountry!!.root)
        recyclerView = findViewById(R.id.streetmaprecyclerview)
        bannerID = findViewById(R.id.bannerID)
        bannerAdLayer = findViewById(R.id.adContainer)
        imageArrowBackCountry = findViewById(R.id.arrowBackCountry)
        logAnalyticsForClicks("StreetViewDisplayStreetViewCountryOnCreate", this)

        //call back back press
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        status = intent.getStringExtra("status")
        //  countryName = getIntent().getStringExtra("key");
        imageArrowBackCountry?.setOnClickListener(View.OnClickListener {
            logAnalyticsForClicks(
                "StreetViewDisplayStreetViewCountryOnBtnExit",
                this@CountryActivities
            )
            onBackPressedDispatcher.onBackPressed()
        })
    }

    private val countryData: Unit
        private get() {
            try {
                val response = getJsonFromAssets(this, "countryName.json")
                val jsonArray = JSONArray(response)
                for (i in 0 until jsonArray.length()) {
                    val jsonObject1 = jsonArray.getJSONObject(i)
                    val nameCountry = jsonObject1.getString("Name")
                    Log.d("msg", "getCountryData: $nameCountry")
                    streetViewMapModelArrayList!!.add(CountryNameModel(nameCountry))
                }
                recyclerView!!.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                verticleStreetMapAdapter = VerticleStreetMapAdapter(
                    this,
                    streetViewMapModelArrayList!!,
                    bindingCountry!!.whiteView
                )
                recyclerView!!.adapter = verticleStreetMapAdapter
            } catch (e: JSONException) {
                Log.d("msg", "msg: " + e.message)
            }
        }
    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (status == "liveEarth") {
                    onBackPressedDispatcher.onBackPressed()
                    finish()
                } else {
                    // Show ads logic
                    logAnalyticsForClicks(
                        "StreetViewDisplayStreetViewCountryOnExit",
                        this@CountryActivities
                    )
                    mediationBackPressedSimpleStreetViewLocation(
                        this@CountryActivities,
                        StreetViewAppSoniMyAppAds.admobInterstitialAd,
                        bindingCountry!!.whiteView
                    )
                }
            }
        }

    override fun onResume() {
        super.onResume()
        streetViewMapModelArrayList = ArrayList()
        countryData
    }

    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback.remove()
    }
}
