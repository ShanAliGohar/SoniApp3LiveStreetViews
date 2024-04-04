package com.live.streetview.navigation.earthmap.compass.map.fuelWork

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdView
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds
import com.live.streetview.navigation.earthmap.compass.map.MainActivity
import com.live.streetview.navigation.earthmap.compass.map.adapters.fuel_db_adapters.PricesAdapter
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityTableBinding
import com.live.streetview.navigation.earthmap.compass.map.fuelWork.dataClasses.FuelDataList
import com.live.streetview.navigation.earthmap.compass.map.fuelWork.dataClasses.fuelsModel.MainFuel
import com.live.streetview.navigation.earthmap.compass.map.fuelWork.retrofit.ApiClientFuel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TableActivity : AppCompatActivity() {
    private val binding: ActivityTableBinding by lazy {
        ActivityTableBinding.inflate(layoutInflater)
    }
    private var pricesAdapter: PricesAdapter? = null
    private var currencyType: String? = null
    private var fuelDataList: ArrayList<FuelDataList> = ArrayList()
    private var electDataList: ArrayList<FuelDataList> = ArrayList()
    private var gasDataList: ArrayList<FuelDataList> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
//        streetViewBannerAdsSmall()
        // Retrieve the string values from the intent extras from fuelDbActivity
        val countryName = intent.getStringExtra("countryName")
        currencyType = intent.getStringExtra("currencyType")
        Log.e("TAG", "onCreate: countryName $countryName,,, currencyType $currencyType")
        this.onBackPressedDispatcher.addCallback(onBackPressedCallback)

        if (isNetworkConnected()) {
            countryName?.let {
                loadData(it)
            }
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
        }
        listeners()
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    private fun listeners() {
        with(binding) {
            toolbarBackIcon.setOnClickListener {
                StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation(
                    this@TableActivity,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,
                    binding.whiteView
                )
            }
            fuelPricesConst.setOnClickListener {
                fuelPricesAdapterSetting(fuelDataList, "1 ltr")
                fuelPricesCardView.backgroundTintList =
                    resources.getColorStateList(R.color.button_active)
                electPricesCardView.backgroundTintList =
                    resources.getColorStateList(R.color.button_nonactive)
                gasPricesCardView.backgroundTintList =
                    resources.getColorStateList(R.color.button_nonactive)
                fuelPricesTv.setBackgroundColor(Color.TRANSPARENT)
                fuelPricesTv.setTextColor(Color.WHITE)
                electPricesTv.setTextColor(Color.parseColor("#686668"))
                gasPricesTv.setTextColor(Color.parseColor("#686668"))
                /* pricesAdapter?.literText = "1 ltr"*/
            }
            electPricesConst.setOnClickListener {
                fuelPricesAdapterSetting(electDataList, "1 unit")
                fuelPricesCardView.backgroundTintList =
                    resources.getColorStateList(R.color.button_nonactive)
                electPricesCardView.backgroundTintList =
                    resources.getColorStateList(R.color.button_active)
                gasPricesCardView.backgroundTintList =
                    resources.getColorStateList(R.color.button_nonactive)
                electPricesConst.setBackgroundColor(Color.TRANSPARENT)
                electPricesTv.setTextColor(Color.WHITE)
                fuelPricesTv.setTextColor(Color.parseColor("#686668"))
                gasPricesTv.setTextColor(Color.parseColor("#686668"))
                /* pricesAdapter?.literText = "1 unit"*/
            }
            gasPricesConst.setOnClickListener {
                fuelPricesAdapterSetting(gasDataList, "1 kg")
                fuelPricesCardView.backgroundTintList =
                    resources.getColorStateList(R.color.button_nonactive)
                electPricesCardView.backgroundTintList =
                    resources.getColorStateList(R.color.button_nonactive)
                gasPricesCardView.backgroundTintList =
                    resources.getColorStateList(R.color.button_active)
                gasPricesConst.setBackgroundColor(Color.TRANSPARENT)
                gasPricesTv.setTextColor(Color.WHITE)
                fuelPricesTv.setTextColor(Color.parseColor("#686668"))
                electPricesTv.setTextColor(Color.parseColor("#686668"))
                /* pricesAdapter?.literText = "1 kg"*/
            }
        }
    }

    private fun loadData(country: String) {
        val apiClient = ApiClientFuel.apiService
        val call = apiClient.getPlaces2(country)
        Log.e("TAG", "loadData")
        call.enqueue(object : Callback<MainFuel> {
            @SuppressLint("UseCompatLoadingForColorStateLists")
            override fun onResponse(call: Call<MainFuel>, response: Response<MainFuel>) {
                Log.e("TAG", "onResponse: ${response.body()}")
                if (response.isSuccessful) {
                    CoroutineScope(Dispatchers.IO).launch {
                        Log.e("TAG", "Success: ${response.body()}")
                        withContext(Dispatchers.Main) {
                            binding.activityProgress.visibility = View.GONE
                        }

                        if (currencyType?.contains("LOCAL") == true) {
                            // Add each product price to the fuelDataList
                            with(fuelDataList) {
                                add(
                                    FuelDataList(
                                        "Diesel",
                                        response.body()?.data?.dieselPrices?.Currency
                                            ?: "no data found"
                                    )
                                )
                                add(
                                    FuelDataList(
                                        "Gasoline",
                                        response.body()?.data?.gasolinePrices?.Currency
                                            ?: "no data found"
                                    )
                                )
                                add(
                                    FuelDataList(
                                        "Methane",
                                        response.body()?.data?.methanePrices?.Currency
                                            ?: "no data found"
                                    )
                                )
                                add(
                                    FuelDataList(
                                        "Ethanol",
                                        response.body()?.data?.ethanolPrices?.Currency
                                            ?: "no data found"
                                    )
                                )
                                add(
                                    FuelDataList(
                                        "Kerosene",
                                        response.body()?.data?.kerosenePrices?.Currency
                                            ?: "no data found"
                                    )
                                )
                                add(
                                    FuelDataList(
                                        "Heating Oil",
                                        response.body()?.data?.heatingOilPrices?.Currency
                                            ?: "no data found"
                                    )
                                )
                                add(FuelDataList("LPG", response.body()?.data?.lpgPrices?.Currency))
                            }
                            Log.e("TAG", "onDataAdded: $fuelDataList")
                            /*setting fuel prices adapter...*/
                            withContext(Dispatchers.Main) {
                                fuelPricesAdapterSetting(fuelDataList, "1 ltr")
                            }

                            /*electricity prices list*/
                            with(electDataList) {
                                add(
                                    FuelDataList(
                                        "Households",
                                        response.body()?.electricityPrices?.Households?.Currency
                                            ?: "no data found"
                                    )
                                )
                                add(
                                    FuelDataList(
                                        "Business",
                                        response.body()?.electricityPrices?.Business?.Currency
                                            ?: "no data found"
                                    )
                                )
                            }
                            /*setting fuel prices adapter...*/
                            withContext(Dispatchers.Main) {
                                fuelPricesAdapterSetting(electDataList, "1 unit")
                            }

                            /*gas prices list adding values*/
                            with(gasDataList) {
                                add(
                                    FuelDataList(
                                        "Households",
                                        response.body()?.naturalGasPrices?.Households?.Currency
                                            ?: "no data found"
                                    )
                                )
                                add(
                                    FuelDataList(
                                        "Business",
                                        response.body()?.naturalGasPrices?.Business?.Currency
                                            ?: "no data found"
                                    )
                                )
                            }
                            /*setting fuel prices adapter...*/
                            withContext(Dispatchers.Main) {
                                fuelPricesAdapterSetting(gasDataList, "1 kg")
                                fuelPricesAdapterSetting(fuelDataList, "1 ltr")
                                binding.fuelPricesCardView.backgroundTintList =
                                    resources.getColorStateList(R.color.button_active)
                                binding.fuelPricesTv.setTextColor(Color.WHITE)
                                binding.electPricesTv.setTextColor(Color.parseColor("#686668"))
                                binding.gasPricesTv.setTextColor(Color.parseColor("#686668"))
                                binding.electPricesCardView.backgroundTintList =
                                    resources.getColorStateList(R.color.button_nonactive)
                                binding.gasPricesCardView.backgroundTintList =
                                    resources.getColorStateList(R.color.button_nonactive)
                                /* pricesAdapter?.literText = "1_ltr"*/
                            }
                        } else if (currencyType?.contains("USD") == true) {
                            // Add each product price to the fuelDataList
                            with(fuelDataList) {
                                add(
                                    FuelDataList(
                                        "Diesel",
                                        response.body()?.data?.dieselPrices?.USD + " USD"
                                    )
                                )
                                add(
                                    FuelDataList(
                                        "Gasoline",
                                        response.body()?.data?.gasolinePrices?.USD + " USD"
                                    )
                                )
                                add(
                                    FuelDataList(
                                        "Methane",
                                        response.body()?.data?.methanePrices?.USD + " USD"
                                    )
                                )
                                add(
                                    FuelDataList(
                                        "Ethanol",
                                        response.body()?.data?.ethanolPrices?.USD + " USD"
                                    )
                                )
                                add(
                                    FuelDataList(
                                        "Kerosene",
                                        response.body()?.data?.kerosenePrices?.USD + " USD"
                                    )
                                )
                                add(
                                    FuelDataList(
                                        "Heating Oil",
                                        response.body()?.data?.heatingOilPrices?.USD + " USD"
                                    )
                                )
                                add(
                                    FuelDataList(
                                        "LPG",
                                        response.body()?.data?.lpgPrices?.USD + " USD"
                                    )
                                )
                            }
                            Log.e("TAG", "onDataAdded: $fuelDataList")
                            /*setting fuel prices adapter...*/
                            withContext(Dispatchers.Main) {
                                fuelPricesAdapterSetting(fuelDataList, "1 ltr")
                            }

                            /*electricity prices list*/
                            with(electDataList) {
                                add(
                                    FuelDataList(
                                        "Households",
                                        response.body()?.electricityPrices?.Households?.USD + " USD"
                                    )
                                )
                                add(
                                    FuelDataList(
                                        "Business",
                                        response.body()?.electricityPrices?.Business?.USD + " USD"
                                    )
                                )
                            }
                            /*setting fuel prices adapter...*/
                            withContext(Dispatchers.Main) {
                                fuelPricesAdapterSetting(electDataList, "1 unit")
                            }

                            /*gas prices list adding values*/
                            with(gasDataList) {
                                add(
                                    FuelDataList(
                                        "Households",
                                        response.body()?.naturalGasPrices?.Households?.USD
                                    )
                                )
                                add(
                                    FuelDataList(
                                        "Business",
                                        response.body()?.naturalGasPrices?.Business?.USD
                                    )
                                )
                            }
                            /*setting fuel prices adapter...*/
                            withContext(Dispatchers.Main) {
                                fuelPricesAdapterSetting(gasDataList, "1 kg")
                                fuelPricesAdapterSetting(fuelDataList, "1 ltr")
                                binding.fuelPricesCardView.backgroundTintList =
                                    resources.getColorStateList(R.color.button_active)
                                binding.fuelPricesTv.setTextColor(Color.WHITE)
                                binding.electPricesTv.setTextColor(Color.parseColor("#686668"))
                                binding.gasPricesTv.setTextColor(Color.parseColor("#686668"))
                                binding.electPricesCardView.backgroundTintList =
                                    resources.getColorStateList(R.color.button_nonactive)
                                binding.gasPricesCardView.backgroundTintList =
                                    resources.getColorStateList(R.color.button_nonactive)
                                /* pricesAdapter?.literText = "1_ltr"*/
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<MainFuel>, t: Throwable) {
                Log.e("TAG", "onFailure: ${t.localizedMessage} ")
            }
        })
    }

    private fun fuelPricesAdapterSetting(
        fuelDataList: ArrayList<FuelDataList>,
        s: String
    ) {
        with(binding) {
            CoroutineScope(Dispatchers.Main).launch {
                pricesAdapter = PricesAdapter(fuelDataList, s)
                pricesRecycler.layoutManager =
                    LinearLayoutManager(this@TableActivity, LinearLayoutManager.VERTICAL, false)
                pricesRecycler.adapter = pricesAdapter
            }
        }
    }

    private fun isNetworkConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation(
                this@TableActivity,
                StreetViewAppSoniMyAppAds.admobInterstitialAd,
                binding.whiteView
            )
        }
    }
//    private fun streetViewBannerAdsSmall() {
//        val billingHelper = StreetViewAppSoniBillingHelper(this)
//        val adContainer: LinearLayout = binding.bannerAdPlace.adContainer
//        val adView = AdView(this)
//        adView.setAdSize(StreetViewAppSoniMyAppAds.getAdSize(this))
//        adView.adUnitId = StreetViewAppSoniMyAppAds.banner_admob_inApp
//        if (billingHelper.isNotAdPurchased) {
//            StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediation(
//                adContainer, adView, this
//            )
//        }else{
//            binding.bannerID.visibility= View.GONE
//        }
//    }
    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback.remove()
    }

}