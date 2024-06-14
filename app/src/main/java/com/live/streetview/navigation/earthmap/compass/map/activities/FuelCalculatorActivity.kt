package com.live.streetview.navigation.earthmap.compass.map.activities

import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdView
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds.getAdSize
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediation
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation
import com.live.streetview.navigation.earthmap.compass.map.Constants
import com.live.streetview.navigation.earthmap.compass.map.InfoRetrofitInstamce.RetrofitClientforInfoCountryData
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.RetrofitInfoModel.AllCountryDataModl
import com.live.streetview.navigation.earthmap.compass.map.adapters.CustomSpinnerAdapterFlag
import com.live.streetview.navigation.earthmap.compass.map.adapters.CustomSpinnerAdapterFuel
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityFuelCalculatorBinding
import com.live.streetview.navigation.earthmap.compass.map.fuelWork.dataClasses.fuelsModel.FuelPrice
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.nearbyplaces.pogoclasses.Parent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class FuelCalculatorActivity : AppCompatActivity() {
    private val imagefuelarrowback: ImageView? = null
    lateinit var arrayDistanceFuel: Array<String>
    lateinit var arrayMileageFuel: Array<String>
    lateinit var arrayPerLitreFuel: Array<String>
    var arrayAdapter: ArrayAdapter<String>? = null
    var adapterMileageFuel: ArrayAdapter<String>? = null
    var adapterPerlitreFuel: ArrayAdapter<String>? = null
    var spindistanceFuelCal: Spinner? = null
    var bindingFuel: ActivityFuelCalculatorBinding? = null
    var customSpinnerAdapterFuel: CustomSpinnerAdapterFuel? = null
    var modelCountryInfos: ArrayList<AllCountryDataModl?> = ArrayList()
    var CountName: String? = null
    var petrolPrice: String? = null
    lateinit var pPrice: Array<String>
    lateinit var p1Price: Array<String>
    var TripdistanceFuel = 0f
    var perLitreCal = 0f
    var pricePerlitreFuel = 0f
    var df = DecimalFormat("#.#")
    private val callbackEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingFuel = ActivityFuelCalculatorBinding.inflate(
            layoutInflater
        )

        setContentView(bindingFuel!!.root)
        // initBannerFuel();
        logAnalyticsForClicks("StreetViewFuelCalculatorOnCreate", this@FuelCalculatorActivity)
        bindingFuel!!.ivLeftarrowFuel.setOnClickListener {
            logAnalyticsForClicks("StreetViewFuelCalculatorOnBtnExit", this@FuelCalculatorActivity)
            mediationBackPressedSimpleStreetViewLocation(
                this@FuelCalculatorActivity,
                StreetViewAppSoniMyAppAds.admobInterstitialAd, bindingFuel!!.whiteView
            )
        }
        //call back back press
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        spindistanceFuelCal = findViewById(R.id.spin_distance)
        arrayDistanceFuel = arrayOf("Km", "Mile")
        arrayMileageFuel = arrayOf("Km/Litre")
        arrayPerLitreFuel = arrayOf("Per Litre")
        adapterMileageFuel = ArrayAdapter(this, R.layout.drop_down, arrayMileageFuel)
        adapterMileageFuel!!.setDropDownViewResource(android.R.layout.simple_list_item_1)
        bindingFuel!!.spinMileage.adapter = adapterMileageFuel
        bindingFuel!!.spinMileage.setSelection(0)
        adapterPerlitreFuel = ArrayAdapter(this, R.layout.drop_down, arrayPerLitreFuel)
        adapterPerlitreFuel!!.setDropDownViewResource(android.R.layout.simple_list_item_1)
        bindingFuel!!.spinFuelpriceCal.adapter = adapterPerlitreFuel
        bindingFuel!!.spinFuelpriceCal.setSelection(0)
        arrayAdapter = ArrayAdapter(this, R.layout.drop_down, arrayDistanceFuel)
        arrayAdapter!!.setDropDownViewResource(android.R.layout.simple_list_item_1)
        spindistanceFuelCal?.setAdapter(arrayAdapter)
        spindistanceFuelCal?.setSelection(0)
 //       countries

        val adapter = ArrayAdapter(this, R.layout.drop_down, Constants.countries)
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
        bindingFuel!!.spinCtyflagsFuel.adapter = adapter

            bindingFuel!!.spinCtyflagsFuel.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        val selectedItem = Constants.countries[position]
                        Log.d("TAG", "Selected Item: $selectedItem")
                           // fuelPrices(modelCountryInfos.get(position)?.name);
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }


        bindingFuel!!.btCalculateFuel.setOnClickListener {
            val view = currentFocus
            if (view != null) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
            logAnalyticsForClicks(
                "StreetViewFuelCalculatorBtnCalculateFuelClick",
                this@FuelCalculatorActivity
            )
            if (bindingFuel!!.edDistance.text.toString() != ""
                && bindingFuel!!.edMileage.text.toString() != "" && bindingFuel!!.edFuelprice.text.toString() != ""
            ) {
                if (bindingFuel!!.spinDistance.selectedItem == "Km") {
                    TripdistanceFuel = bindingFuel!!.edDistance.text.toString().toFloat()
                }
                if (bindingFuel!!.spinDistance.selectedItem == "Mile") {
                    TripdistanceFuel =
                        bindingFuel!!.edDistance.text.toString().toFloat() * 0.621371f
                }
                if (bindingFuel!!.spinMileage.selectedItem == "Km/Litre") {
                    perLitreCal = bindingFuel!!.edMileage.text.toString().toFloat()
                }
                if (bindingFuel!!.spinFuelpriceCal.selectedItem == "Per Litre") {
                    pricePerlitreFuel = bindingFuel!!.edFuelprice.text.toString().toFloat()
                }
                val perliterfinal = TripdistanceFuel / perLitreCal
                val calulate = pricePerlitreFuel * perliterfinal
                bindingFuel!!.tvCost.text = " " + df.format(calulate.toDouble())
                bindingFuel!!.tvLitres.text = "" + df.format(perliterfinal.toDouble())
                //binding.textView14.setText(p1Price[0]);
                //  binding.textView14.setText(binding.edFuelprice.getText());
                //txtShow.setText("Estimated Fuel Cost:"+calulate+"\n"+"Fuel Required:"+perLitre);
            } else {
                Toast.makeText(this@FuelCalculatorActivity, "Enter Value", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        bindingFuel!!.ivLeftarrowFuel.setOnClickListener { onBackPressed() }
        streetViewBannerAdsSmall()
    }

    private fun streetViewBannerAdsSmall() {
        val billingHelper = StreetViewAppSoniBillingHelper(this)
        val adContainer = bindingFuel!!.bannerAdPlace.adContainer
        val adView = AdView(this)
        adView.adUnitId = StreetViewAppSoniMyAppAds.banner_admob_inApp
        adView.setAdSize(getAdSize(this))
        if (billingHelper.isNotAdPurchased) {
            loadSmartToolsBannerForMainMediation(
                adContainer, adView, this
            )
        } else {
            bindingFuel!!.bannerID.visibility = View.GONE
        }
    }

/*    private val countries: Unit
        //    private void initBannerFuel() {
        private get() {
            val cloudsCall = RetrofitClientforInfoCountryData.instance?.myApi?.data
            cloudsCall?.enqueue(object : Callback<ArrayList<AllCountryDataModl?>?> {
                override fun onResponse(
                    call: Call<ArrayList<AllCountryDataModl?>?>,
                    response: Response<ArrayList<AllCountryDataModl?>?>
                ) {
                    Log.d("TAG", "onResponse: ${response.body()}")

                        if (response.isSuccessful) {
                            modelCountryInfos = response.body()!!
                           for (i in modelCountryInfos.indices) {
                                CountName = modelCountryInfos[i]!!.name
                                Log.d("TAG", "onResponse: $CountName")
                            }
                            //val spinner: Spinner = findViewById(R.id.spinner)

                            // Sample data for spinner items
                            val items = arrayListOf("Item 1", "Item 2", "Item 3")

                        *//*    val adapter = CustomSpinnerAdapterFuel(this@FuelCalculatorActivity, modelCountryInfos)
                            bindingFuel!!.spinCtyflagsFuel.adapter = adapter*//*

                         *//*   customSpinnerAdapterFuel =
                                CustomSpinnerAdapterFuel(applicationContext,modelCountryInfos)
                           bindingFuel!!.spinCtyflagsFuel.adapter = customSpinnerAdapterFuel*//*
                            // fuelPrices(CountName);
                    }
                }

                override fun onFailure(call: Call<ArrayList<AllCountryDataModl?>?>, t: Throwable) {
                    Log.d("TAG", "onResponse: $t")
                }
            })
        }*/


    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                logAnalyticsForClicks(
                    "StreetViewFuelCalculatorOnBtnExit",
                    this@FuelCalculatorActivity
                )
                mediationBackPressedSimpleStreetViewLocation(
                    this@FuelCalculatorActivity,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,
                    bindingFuel!!.whiteView
                )
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback.remove()
    }
}