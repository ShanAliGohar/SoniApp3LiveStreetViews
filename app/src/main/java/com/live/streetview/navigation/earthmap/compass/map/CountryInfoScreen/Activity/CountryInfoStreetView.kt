package com.live.streetview.navigation.earthmap.compass.map.CountryInfoScreen.Activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gps.navigation.streetview.earthmap.satelliteview.compass.pro.CountryInfoModelStreetView
import com.gps.navigation.streetview.earthmap.satelliteview.compass.pro.CountryInfoScreen.Adapters.CountryFetchingAdapterStreetView
import com.gps.navigation.streetview.earthmap.satelliteview.compass.pro.CountryInfoScreen.Interfaces.CountryInfoInterfaceStreetView
import com.gps.navigation.streetview.earthmap.satelliteview.compass.pro.CountryInfoScreen.RetrofitApi.RetrofitInstanceCountryInfo
import com.gps.navigation.streetview.earthmap.satelliteview.compass.pro.CountryInfoScreen.Utils.MyContext
import com.live.streetview.navigation.earthmap.compass.map.CountryInfoScreen.Interfaces.OnItemClickListener
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityCountryInfoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CountryInfoStreetView : AppCompatActivity(), OnItemClickListener {
    // var swipeRefreshLayout: SwipeRefreshLayout? = null
    var binding: ActivityCountryInfoBinding? = null
    private var searchBar: EditText? = null
    var activityProgress: ProgressBar? = null
    var searchBtn: ImageView? = null
    var countryInfoModelStreetView: List<CountryInfoModelStreetView> = ArrayList()
    var countryFetchingAdapterStreetView: CountryFetchingAdapterStreetView? = null
    var noInternetLayout: LinearLayout? = null
    private var rvListCountryInfo: RecyclerView? = null

    //var streetViewPurchaseHelper: StreetViewPurchaseHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountryInfoBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        //   streetViewPurchaseHelper = StreetViewPurchaseHelper(this)
        // variableIni()
        setClickListener()
        //  checkConnection()
        // BannerAdsSmall()
    }

//    private fun BannerAdsSmall() {
////        val billingHelper = StreetViewBillingHelper(
////                this
////            )
//        val adContainer = binding!!.smallAd.adContainer
//        binding!!.smallAd.removeAds.setAnimation(R.raw.remove_ad)
//        binding!!.smallAd.removeAds.setOnClickListener { v -> removeAds() }
//        val adView = AdView(this)
//        adView.adUnitId = StreetViewMyAppAds.banner_admob_inApp
//        adView.setAdSize(StreetViewMyAppAds.getAdSize(this))
//        if (billingHelper.isNotAdPurchased) {
//            //   binding!!.smallAd.removeAds.visibility = View.VISIBLE
//            StreetViewMyAppAds.loadStreetViewBannerForMainMediation(
//                adContainer, adView, this
//            )
//        }
//    }

//    private fun removeAds() {
//        streetViewPurchaseHelper!!.purchaseLiveEarthLocationAdsPackage()
//    }

    private fun setClickListener() {

        searchBtn?.setOnClickListener {
            searchBar!!.visibility = View.VISIBLE
        }
        searchBar?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //countryFetchingAdapter?.filter?.filter(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        //swipeRefreshLayout!!.setOnRefreshListener { checkConnection() }
    }

    private fun countryInfoRetrofitServerHit() {
        //   swipeRefreshLayout!!.isRefreshing = false
        val countryInfoInterfaceStreetView: CountryInfoInterfaceStreetView =
            RetrofitInstanceCountryInfo.getInstance(this)!!
                .create(CountryInfoInterfaceStreetView::class.java)
        val call: Call<List<CountryInfoModelStreetView>>? =
            countryInfoInterfaceStreetView.getGeoResults()
        call?.enqueue(object : Callback<List<CountryInfoModelStreetView>> {
            override fun onResponse(
                call: Call<List<CountryInfoModelStreetView>>,
                response: Response<List<CountryInfoModelStreetView>>,
            ) {
                try {
                    if (rvListCountryInfo!!.visibility == View.GONE) {
                        rvListCountryInfo!!.visibility = View.VISIBLE
                    }
                    if (response.body() != null && response.isSuccessful) {
                        countryInfoModelStreetView = response.body()!!
                        activityProgress!!.visibility = View.GONE
                        noInternetLayout!!.visibility = View.GONE
                        if (countryInfoModelStreetView != null) {
                            countryFetchingAdapterStreetView =
                                CountryFetchingAdapterStreetView(
                                    this@CountryInfoStreetView,
                                    countryInfoModelStreetView,
                                    this@CountryInfoStreetView
                                )
                            val gridLayoutManager = GridLayoutManager(
                                this@CountryInfoStreetView,
                                3
                            )
                            gridLayoutManager.setSpanSizeLookup(object :
                                androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup() {
                                override fun getSpanSize(position: Int): Int {

                                    return when (countryFetchingAdapterStreetView!!.getItemViewType(
                                        position
                                    )) {
                                        0 -> gridLayoutManager.getSpanCount()
                                        1 -> 1
                                        2 -> gridLayoutManager.getSpanCount()
                                        else -> -1
                                    }
                                }
                            })
                            rvListCountryInfo?.layoutManager = gridLayoutManager
                            rvListCountryInfo!!.setHasFixedSize(true)
                            rvListCountryInfo?.adapter = countryFetchingAdapterStreetView
                        }
                    } else {
                        Log.d("Response", "onResponse: ")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<List<CountryInfoModelStreetView>>, t: Throwable) {
                Log.i("Error", ":::" + t.message)
            }
        })
    }

//    private fun variableIni() {
//        searchBar = binding?.countryInfoSearchBar
//        rvListCountryInfo = binding?.countryinfoRecylerView
//        searchBtn = binding?.countryInfoSearchViewbtn
//        activityProgress = binding?.activityProgress
//        noInternetLayout = binding?.noInternetLayout
//        swipeRefreshLayout = binding!!.SwipeRefresh
//    }

    fun countryInfoToHomeScreen(view: View) {
        onBackPressed()

    }

    override fun onBackPressed() {
        super.onBackPressed()
//        StreetViewMyAppShowAds.meidationBackPressedSimpleLiveEarthLocation(
//            this,
//            StreetViewMyAppAds.admobInterstitialAd, StreetViewMyAppAds.maxInterstitialAdInvitation
//        )
    }

    override fun onItemClick(position: Int) {
        if (!countryInfoModelStreetView[position].equals(null)) {
            val modelStreetView: CountryInfoModelStreetView = countryInfoModelStreetView[position]
            val toTheCountriesDetailsScreen = Intent(this, CountryDetailsStreetView::class.java)
            this.startActivity(toTheCountriesDetailsScreen)
//            StreetViewMyAppShowAds.meidationForClickSimpleLiveEarthLocation(
//                this,
//                StreetViewMyAppAds.admobInterstitialAd,
//                StreetViewMyAppAds.maxInterstitialAdInvitation,
//                toTheCountriesDetailsScreen,
//                "From All Country to View Country"
//            )

            try {
                if (modelStreetView.name != "") {
                    MyContext.countryName = modelStreetView.name
                }
                if (modelStreetView.alpha2Code != "null") {
                    MyContext.countryFlag = modelStreetView.alpha2Code
                }
                if (modelStreetView.callingCodes.isNotEmpty()) {
                    MyContext.countryCode = modelStreetView.callingCodes[0]
                }
                if (modelStreetView.population != 0) {
                    MyContext.population = modelStreetView.population.toString()
                }
                if (modelStreetView.area != 0f) {
                    MyContext.area = modelStreetView.area.toString()
                }
                if (modelStreetView.timezones.isNotEmpty()) {
                    MyContext.timezone = modelStreetView.timezones[0]
                }


                MyContext.currency = modelStreetView.currencies[0].name


                if (modelStreetView.languages.isNotEmpty()) {
                    MyContext.language = modelStreetView.languages[0].name
                }
                if (modelStreetView.capital != "") {
                    MyContext.capital = modelStreetView.capital
                }

            } catch (e: Exception) {
            }

        }
    }

//    private fun checkConnection() {
//        val isConnected: Boolean = ConnectivityReceiverStreetView.isConnected
//        setView(isConnected)
//
//    }

    private fun setView(isConnected: Boolean) {

        if (isConnected) {
            if (activityProgress!!.visibility == View.GONE) {
                activityProgress!!.visibility = View.VISIBLE
                noInternetLayout!!.visibility = View.GONE
            }
            countryInfoRetrofitServerHit()
        } else {
            //swipeRefreshLayout!!.isRefreshing = false
            activityProgress!!.visibility = View.GONE
            noInternetLayout!!.visibility = View.VISIBLE
            rvListCountryInfo!!.visibility = View.GONE


        }
    }

    override fun onResume() {
        super.onResume()
        // register connection status listener
        //  MyApplicationStreetView.instance!!.setConnectivityListener(this)
    }
}