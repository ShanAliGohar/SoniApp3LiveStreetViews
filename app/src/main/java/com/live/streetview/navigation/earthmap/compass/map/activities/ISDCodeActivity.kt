package com.live.streetview.navigation.earthmap.compass.map.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback

import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation
import com.live.streetview.navigation.earthmap.compass.map.Model.IsoModel
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.activities.utils.JsonConverter.getJsonFromAssets
import com.live.streetview.navigation.earthmap.compass.map.adapters.IsoAdapter
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityIcoCodeBinding
import org.json.JSONArray
import org.json.JSONException
import java.util.Locale

class ISDCodeActivity : AppCompatActivity() {
    var isorecy: RecyclerView? = null
    var isoAdapter: IsoAdapter? = null
    var mymodellist: ArrayList<IsoModel>? = null
    private var isoarrowback: ImageView? = null
    var searchView: EditText? = null
    var cardView: CardView? = null
    var bindingIso: ActivityIcoCodeBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingIso = ActivityIcoCodeBinding.inflate(
            layoutInflater
        )
        setContentView(bindingIso!!.root)
        isoarrowback = findViewById(R.id.imageisoarrowback)
        searchView = findViewById(R.id.weatherSearch)
        cardView = findViewById(R.id.cardView4)
        logAnalyticsForClicks("StreetViewIsoCodeOnCreate", this@ISDCodeActivity)
        //call back back press
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        isoarrowback?.setOnClickListener(View.OnClickListener {
            logAnalyticsForClicks("StreetViewIsoCodeOnBtnExit", this@ISDCodeActivity)
            onBackPressedDispatcher.onBackPressed()
        })
        mymodellist = ArrayList()
        isorecy = findViewById(R.id.isorecyclerview)
        fetchIsoData()
        searchView?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                filter(charSequence.toString())
            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }

    private fun filter(newText: String) {
        val filteredList = ArrayList<IsoModel>()
        for (item in mymodellist!!) {
            if (item.name?.lowercase()?.contains(newText.lowercase()) == true) {
                filteredList.add(item)
            }
        }
        isoAdapter!!.filterList(filteredList)
    }

    private fun fetchIsoData() {
        val jsonFileString = getJsonFromAssets(this, "isdcodess.json")
        //  Log.i("data", jsonFileString);
        var callingCodes = ""
        var alpha2Code = ""
        val lat = ""
        val lng = ""
        val latlng = ""
        val flagUrl = ""
        var name = ""
        val capital = ""
        val flag = ""
        val population = ""
        val area = ""
        val timezones = ""
        val region = ""
        val image = ""
        //   String currency="";
        // String countrycodes="";
        try {

            // so here we use json array
            val jsonArray = JSONArray(jsonFileString)
            for (i in 0 until jsonArray.length()) {
                val jsonObject1 = jsonArray.getJSONObject(i)
                val jsonArray1 = jsonObject1.getJSONArray("callingCodes")
                val jsonArrayLatlng = jsonObject1.getJSONArray("latlng")

                // JSONArray jsonArray2=jsonObject1.getJSONArray("capital");
                name = jsonObject1.getString("name")
                alpha2Code = jsonObject1.getString("alpha2Code")
                val iconFlags =
                    "https://flagcdn.com/w320/" + alpha2Code.lowercase(Locale.getDefault()) + ".png"
                callingCodes = jsonArray1.getString(0)
                val isoModel = IsoModel()
                isoModel.name = name
                isoModel.flagUrl = iconFlags
                isoModel.countryCodeNumber = callingCodes
                isoModel.countryCodeAlpha = alpha2Code
                mymodellist!!.add(isoModel)
                Log.i("callingCodes", "callingCodes: $callingCodes")
            }
            isorecy!!.layoutManager =
                LinearLayoutManager(this@ISDCodeActivity, LinearLayoutManager.VERTICAL, false)
            isoAdapter = IsoAdapter(this, this@ISDCodeActivity, mymodellist)
            isorecy!!.adapter = isoAdapter
        } catch (e: JSONException) {
            Log.i("error23", "myJsonError: " + e.message)
        }
    }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                logAnalyticsForClicks("StreetViewIsoCodeOnExit", this@ISDCodeActivity)
                mediationBackPressedSimpleStreetViewLocation(
                    this@ISDCodeActivity,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,
                    bindingIso!!.whiteView
                )
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback.remove()
    }
}
