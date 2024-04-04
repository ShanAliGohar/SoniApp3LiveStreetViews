package com.live.streetview.navigation.earthmap.compass.map.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdView
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds.getAdSize
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediation
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation
import com.live.streetview.navigation.earthmap.compass.map.InfoRetrofitInstamce.RetrofitClientforInfoCountryData
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.RetrofitInfoModel.AllCountryDataModl
import com.live.streetview.navigation.earthmap.compass.map.adapters.InfoAdapter
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityCountryInfoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountryInfoActivity : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var allCountryDataModl: AllCountryDataModl? = null
    private var mylist = ArrayList<AllCountryDataModl?>()
    var infoAdapter: InfoAdapter? = null
    var imageinfo: ImageView? = null
    var progressBar: ProgressBar? = null
    var InfoSearch: EditText? = null
    var bindingInfo: ActivityCountryInfoBinding? = null
    private val callbackEnabled = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingInfo = ActivityCountryInfoBinding.inflate(
            layoutInflater
        )
        setContentView(bindingInfo!!.root)
        // setContentView(R.layout.activity_country_info);
        logAnalyticsForClicks("StreetViewCountryInfoOnCreate", this@CountryInfoActivity)

        //call back back press
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        progressBar = findViewById(R.id.progressiso)
        InfoSearch = findViewById(R.id.weatherSearch)
        imageinfo = findViewById(R.id.imageinfo)
        imageinfo?.setOnClickListener(View.OnClickListener {
            logAnalyticsForClicks("StreetViewCountryInfoOnBtnExit", this@CountryInfoActivity)
            mediationBackPressedSimpleStreetViewLocation(
                this@CountryInfoActivity,
                StreetViewAppSoniMyAppAds.admobInterstitialAd, bindingInfo!!.whiteView
            )
        })
        mylist = ArrayList()
        recyclerView = findViewById(R.id.inforecyclerview)
        showflasg()
        //initBannerCountryInfo();
        InfoSearch?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                try {
                    filter(charSequence.toString())
                } catch (e: Exception) {
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        streetViewBannerAdsSmall()
    }

    private fun streetViewBannerAdsSmall() {
        val billingHelper = StreetViewAppSoniBillingHelper(this)
        val adContainer = bindingInfo!!.bannerAdPlace.adContainer
        val adView = AdView(this)
        adView.adUnitId = StreetViewAppSoniMyAppAds.banner_admob_inApp
        adView.setAdSize(getAdSize(this))
        if (billingHelper.isNotAdPurchased) {
            loadSmartToolsBannerForMainMediation(
                adContainer, adView, this
            )
        } else {
            bindingInfo!!.bannerID.visibility = View.GONE
        }
    }

    private fun filter(newText: String) {
        val filteredList = ArrayList<AllCountryDataModl?>()
        for (item in mylist) {
            if (item?.name?.lowercase()!!.contains(newText.lowercase())) {
                filteredList.add(item)
            }
        }
        infoAdapter!!.filterList(filteredList)
    }

    private fun showflasg() {

//        InfoInterfaceApi infoInterfaceApi = InfoInterfaceApi.().create(InfoInterfaceApi.class);
        val cloudsCall = RetrofitClientforInfoCountryData.instance?.myApi?.data
        cloudsCall?.enqueue(object : Callback<ArrayList<AllCountryDataModl?>?> {
            override fun onResponse(
                call: Call<ArrayList<AllCountryDataModl?>?>,
                response: Response<ArrayList<AllCountryDataModl?>?>
            ) {
                if (response.isSuccessful) {
                    mylist = response.body()!!
                    progressBar!!.visibility = View.GONE
                    recyclerView!!.visibility = View.VISIBLE
                    infoAdapter =
                        InfoAdapter(this@CountryInfoActivity, mylist, bindingInfo!!.whiteView)
                    val gridLayoutManager1 = GridLayoutManager(this@CountryInfoActivity, 3)
                    gridLayoutManager1.spanSizeLookup =
                        object : GridLayoutManager.SpanSizeLookup() {
                            override fun getSpanSize(position: Int): Int {
                                return when (infoAdapter!!.getItemViewType(position)) {
                                    0 -> 3
                                    1 -> 1
                                    2 -> 3
                                    else -> 1
                                }
                            }
                        }
                    recyclerView!!.layoutManager = gridLayoutManager1
                    recyclerView!!.adapter = infoAdapter
                }
            }
            override fun onFailure(call: Call<ArrayList<AllCountryDataModl?>?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                logAnalyticsForClicks("StreetViewCountryInfoOnExit", this@CountryInfoActivity)
                mediationBackPressedSimpleStreetViewLocation(
                    this@CountryInfoActivity,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,
                    bindingInfo!!.whiteView
                )
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback.remove()
    }

    companion object {
        @JvmField
        var modelList: AllCountryDataModl? = null
    }
}
