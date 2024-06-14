package com.live.streetview.navigation.earthmap.compass.map.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.live.streetview.navigation.earthmap.compass.map.Ads.AdmobCollapsiveAd
import com.live.streetview.navigation.earthmap.compass.map.Ads.BannerCallBack
import com.live.streetview.navigation.earthmap.compass.map.Ads.CollapsibleType
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds.bannerNativeController
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppNativeAds
import com.live.streetview.navigation.earthmap.compass.map.MainActivity
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.adapters.ViewPagerAdapter
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityOnBoardingScreenBinding
import com.live.streetview.navigation.earthmap.compass.map.fragments.OnBoardingFirst
import com.live.streetview.navigation.earthmap.compass.map.fragments.OnBoardingSecond
import com.live.streetview.navigation.earthmap.compass.map.fragments.onBoardingThird

class OnBoardingScreen : AppCompatActivity() {
    var binding: ActivityOnBoardingScreenBinding? = null
    private var mFirebaseRemoteConfig: FirebaseRemoteConfig? = null
    private val admobBannerAds by lazy { AdmobCollapsiveAd() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityOnBoardingScreenBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

    //    remoteConfigData()
        Log.d("TAG", "fbconfig 1: $bannerNativeController")


        if (bannerNativeController == "0"){
            binding!!.bannerID.visibility = View.GONE
            binding!!.adsNative.root.visibility= View.VISIBLE


            StreetViewAppSoniMyAppNativeAds.Companion.loadSmartToolsNavAdmobNativeAdPriority(
                this,
                binding!!.adsNative.adViewUnified
            );
            Log.d("TAG", "fbconfig 2: $bannerNativeController")

        } else {
            Log.d("TAG", "fbconfig 1: $bannerNativeController")
            binding!!.adsNative.root.visibility= View.GONE
            binding!!.bannerID.visibility = View.VISIBLE
            loadCollapseBanner()
        }




//        loadCollapseBanner()
        val adapter = ViewPagerAdapter(supportFragmentManager)

        val viewPager = binding!!.viewPager
        viewPager?.adapter = adapter

        val dotIndicator = binding!!.dotsIndicator
        if (viewPager != null) {
            dotIndicator?.viewPager = viewPager
        }

        binding!!.nextBtn.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem < adapter.count - 1) {
                viewPager.currentItem = currentItem + 1
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        binding!!.skipBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
/*    private fun remoteConfigData()
    {
        mFirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        mFirebaseRemoteConfig!!.setConfigSettingsAsync(configSettings)
        mFirebaseRemoteConfig?.setDefaultsAsync(R.xml.remote_config_defaults)
        mFirebaseRemoteConfig!!.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d("TAG", "Config params updated: $updated")
                    StreetViewAppSoniMyAppAds.adShowAfter =
                        mFirebaseRemoteConfig!!.getString("adShowAfter").toInt()
                    StreetViewAppSoniMyAppAds.startCounter =
                        mFirebaseRemoteConfig!!.getString("addShowBefore").toInt()
                    Log.d(
                        "TAG",
                        "remoteConfigData: ${mFirebaseRemoteConfig!!.getString("adShowAfter")}"
                    )
                } else {
                    StreetViewAppSoniMyAppAds.adShowAfter = 2
                    StreetViewAppSoniMyAppAds.startCounter = 0
                }
            }
    }*/
private fun loadCollapseBanner() {
    val billingHelper = this.let { StreetViewAppSoniBillingHelper(it) }
    admobBannerAds.loadCollapseBannerAds(this,
        binding!!.bannerID,
        StreetViewAppSoniMyAppAds.admob_collaspable_banner_id,
        1,// default enable vale i kept 1
        billingHelper.isNotAdPurchased,
        CollapsibleType.BOTTOM,
        isConnectedToNetwork(this),
        object : BannerCallBack {
            override fun onAdFailedToLoad(adError: String) {}
            override fun onAdLoaded() {}
            override fun onAdImpression() {}
            override fun onPreloaded() {}
            override fun onAdClicked() {}
            override fun onAdSwipeGestureClicked() {}
            override fun onAdClosed() {

            }

            override fun onAdOpened() {
            }
        })
}
    fun isConnectedToNetwork(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}