package com.live.streetview.navigation.earthmap.compass.map.Ads

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.live.streetview.navigation.earthmap.compass.map.R
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class StreetViewAppSoniMyAppClass : Application() {
    private var adsDatabaseReferenceStreetView: DatabaseReference? = null
    private var mFirebaseRemoteConfig: FirebaseRemoteConfig? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        try {
            FirebaseApp.initializeApp(this)
        } catch (e: Exception) {
            Log.d("msg", "firebase exception:" + e.message)
        }

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        remoteConfigDataByGohar()
        //  StreetViewLoadAds.insertADSToFirebase(this);
//        AppLovinSdk.getInstance(this).mediationProvider = "max"
//        AppLovinSdk.initializeSdk(this)
//        {
//            //  AppLovin SDK is initialized, start loading ads
//            Log.d("ConstantAdsLoaondAds:", "onInitializationComplete: ===Init Max")
//        }

        cloneAppOpenAdsManager =
            StreetViewAppSoniOpenAdsManager(this)
    //    AudienceNetworkAds.initialize(this)
        liveEarthAppOpenSplashAdsManager = StreetViewAppOpenSplashAdsManager(this)
//        liveEarthAppOpenSplashAdsManagerHigh = StreetViewAppOpenSplashAdsManagerHigh(this)
        adsDatabaseReferenceStreetView =
            FirebaseDatabase.getInstance().getReference("Live_StreetView")
        CoroutineScope(Dispatchers.IO).launch {
          //   getDataFromFirebase()
        }
//        addModelToFirebase(this)
    //    remoteConfigData()
    }

    /* public static void stopHandlerAndRestart() {
        myHandler.removeCallbacksAndMessages(null);
    }*/
    fun addModelToFirebase(mContext: Context) {
        Log.d("AdsLog:", "addModelToFirebase: ")
        adsDatabaseReferenceStreetView!!.child("ADS_IDS").setValue(
            StreetViewAppSoniMyAdModelNew(
                mContext.getString(R.string.admob_ad_id),
                mContext.getString(R.string.admob_banner_id),
                mContext.getString(R.string.admob_interstitial_id),
                mContext.getString(R.string.admob_native_id),
                StreetViewAppSoniMyAppAds.shouldShowAdmob,
                StreetViewAppSoniMyAppAds.ctr_control,
                StreetViewAppSoniMyAppAds.next_ads_time.toDouble(),
                StreetViewAppSoniMyAppAds.current_counter,
                StreetViewAppSoniMyAppAds.app_open_admob_inApp,
                StreetViewAppSoniMyAppAds.shouldShowAppOpen,
                StreetViewAppSoniMyAppAds.is_splash_show,
                StreetViewAppSoniMyAppAds.adShowAfter
            )
        )
//        Log.i("InvitaionCardMyAppAds", "uploaded : ");
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        //MultiDex.install(this)
    }

    private fun getDataFromFirebase() {
        Log.i("LocationTracking", "getDataFromFirebase : ")
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("Live_StreetView")
        //  databaseReference.child("TestAds").addValueEventListener(object : ValueEventListener {
        databaseReference.child("ADS_IDS").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.i("LocationTracking", "onCancelled : $p0")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val model = dataSnapshot.getValue<StreetViewAppSoniMyAdModelNew>(
                    StreetViewAppSoniMyAdModelNew::class.java
                )

                Log.i("LocationTracking", "model : ")
                if (model != null) {
                    Log.i("LocationTracking", "model != null : ")
                    StreetViewAppSoniMyAppAds.shouldShowAppOpen = model.isShould_show_app_open
                    StreetViewAppSoniMyAppAds.appid_admob_inApp = model.appid_admob_inApp.toString()
                    StreetViewAppSoniMyAppAds.app_open_admob_inApp =
                        model.app_open_admob_inApp.toString()
                    StreetViewAppSoniMyAppAds.interstitial_admob_inApp =
                        model.interstitial_admob_inApp.toString()
                    StreetViewAppSoniMyAppAds.banner_admob_inApp =
                        model.banner_admob_inApp.toString()
                    StreetViewAppSoniMyAppAds.native_admob_inApp =
                        model.native_admob_inApp.toString()
                    StreetViewAppSoniMyAppAds.haveGotSnapshot = true
                    StreetViewAppSoniMyAppAds.shouldShowAdmob = model.isShould_show_admob
                    StreetViewAppSoniMyAppAds.is_splash_show = model.isIs_splash_show
                    StreetViewAppSoniMyAppAds.ctr_control = model.isCtr_control
                    StreetViewAppSoniMyAppAds.next_ads_time = model.next_ads_time.toLong()
                    StreetViewAppSoniMyAppAds.current_counter = model.current_counter
                   StreetViewAppSoniMyAppAds.adShowAfter = model.adShowAfter
                    Log.i(
                        "LocationTracking",
                        "Splash value : " + model.isIs_splash_show
                    )
                    Log.i(
                        "LocationTracking",
                        "Splash value  adShowAfter: " + model.adShowAfter
                    )
                    Log.i(
                        "LocationTracking",
                        "should_show_app_open : " + model.isShould_show_app_open
                    )
                    Log.i(
                        "LocationTracking",
                        "banner_admob_inApp : " + model.banner_admob_inApp
                    )
                    Log.i(
                        "LocationTracking",
                        " model.next_ads_time : " + model.next_ads_time.toLong()
                    )
                    Log.i(
                        "LocationTracking",
                        " should_show_admob : " + model.isShould_show_admob
                    )
                }
            }
        })
    }

    companion object {
        var instance: StreetViewAppSoniMyAppClass? = null
            private set
        var cloneAppOpenAdsManager: StreetViewAppSoniOpenAdsManager? = null
        var liveEarthAppOpenSplashAdsManager: StreetViewAppOpenSplashAdsManager? = null

        //        var liveEarthAppOpenSplashAdsManagerHigh: StreetViewAppOpenSplashAdsManagerHigh? = null
        fun getStr(id: Int): String {
            return instance!!.getString(id)
        }
    }

    private fun remoteConfigData()
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
    }

    private fun remoteConfigDataByGohar()
    {
        mFirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3
        }
        mFirebaseRemoteConfig!!.setConfigSettingsAsync(configSettings)
    //    mFirebaseRemoteConfig?.setDefaultsAsync(R.xml.remote_config_defaults)
        mFirebaseRemoteConfig!!.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d("TAG", "Config params updated: $updated")

                    StreetViewAppSoniMyAppAds.bannerNativeController =
                        mFirebaseRemoteConfig!!.getString("banner_native_controller")
                    Log.d(
                        "TAG",
                        "remoteConfigData: banner_native_controller ${mFirebaseRemoteConfig!!.getString("banner_native_controller")}"
                    )
                } else {
                    Log.d("TAG", "Config is not Successful")

                    StreetViewAppSoniMyAppAds.bannerNativeController = "0"
                }
            }
    }

}