package com.live.streetview.navigation.earthmap.compass.map.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdView
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds
import com.live.streetview.navigation.earthmap.compass.map.Model.horoscope.ApiClientForHoroscope
import com.live.streetview.navigation.earthmap.compass.map.Model.horoscope.ApiInterfaceForHoroscope
import com.live.streetview.navigation.earthmap.compass.map.Model.horoscope.ModelHoroscope
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityDetailsHoroScopBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsHoroScopActivity : AppCompatActivity() {
    var binding: ActivityDetailsHoroScopBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsHoroScopBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
//        this.onBackPressedDispatcher.addCallback(onBackPressedCallback)
        try {
            val data = intent.extras!!.getString("key")
            val image = intent.extras!!.getInt("image")
            Glide.with(this).load(image).into(binding!!.imageView36)
            binding!!.txtNamee.text = data
            CoroutineScope(Dispatchers.IO).launch {
                getData(data!!)
            }
        } catch (e: NullPointerException) {
        }

        streetViewBannerAdsSmall()
        binding!!.imageView35.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    fun getData(text: String) {

        val apiInterfaceForHoroscope =
            ApiClientForHoroscope.retrofitInstance?.create(ApiInterfaceForHoroscope::class.java)
        val call = apiInterfaceForHoroscope?.getHoroscope(text, "today")
        call?.enqueue(object : Callback<ModelHoroscope?> {
            override fun onResponse(
                call: Call<ModelHoroscope?>,
                response: Response<ModelHoroscope?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    try {

                    } catch (e: Exception) {
                    }
                    val des = response.body()!!.description
                    val date = response.body()!!.currentDate
                    val rangeDate = response.body()!!.dateRange
                    val mood = response.body()!!.mood
                    val color = response.body()!!.color
                    val luckynum = response.body()!!.luckyNumber
                    val luckytime = response.body()!!.luckyTime

                    Log.d("TAG", "onResponse: ${response.body()}")
                    populateData(date, rangeDate, des, mood, color, luckynum, luckytime)
                }
            }

            override fun onFailure(call: Call<ModelHoroscope?>, t: Throwable) {
                ifFailed()
            }
        })
    }

    private fun populateData(
        date: String?,
        rangeDate: String?,
        des: String?,
        mood: String?,
        color: String?,
        luckynum: String?,
        luckytime: String?
    ) {
        binding!!.progressBar4.visibility = View.GONE
        binding!!.txsetColor.text = color
        binding!!.txsetMood.text = mood
        binding!!.txtsetluckyTimee.text = luckytime
        binding!!.txtLuckyNumber.text = luckynum
        binding!!.textView34.text = date
        binding!!.textView75.text = rangeDate
        binding!!.textView77.text = des
    }

    private fun ifFailed() {
        binding!!.progressBar4.visibility = View.GONE
        Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
    }

    private fun streetViewBannerAdsSmall() {
        val billingHelper = StreetViewAppSoniBillingHelper(this)
        val adContainer: LinearLayout = binding!!.bannerAdPlace.adContainer
        val adView = AdView(this)
        adView.setAdSize(StreetViewAppSoniMyAppAds.getAdSize(this))
        adView.adUnitId = StreetViewAppSoniMyAppAds.banner_admob_inApp
        if (billingHelper.isNotAdPurchased) {
            StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediation(
                adContainer, adView, this
            )
        }
    }

//    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
//        override fun handleOnBackPressed() {
//            StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation(
//                this@DetailsHoroScopActivity,
//                StreetViewAppSoniMyAppAds.admobInterstitialAd,
//                binding.
//            )
//        }
//    }
}

