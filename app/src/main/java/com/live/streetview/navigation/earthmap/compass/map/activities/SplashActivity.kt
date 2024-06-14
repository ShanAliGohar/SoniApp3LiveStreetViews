package com.live.streetview.navigation.earthmap.compass.map.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.MobileAds
import com.google.firebase.messaging.FirebaseMessaging
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppOpenSplashAdsManager.Companion.appOpenAd
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppClass.Companion.liveEarthAppOpenSplashAdsManager
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks
import com.live.streetview.navigation.earthmap.compass.map.MainActivity
import com.live.streetview.navigation.earthmap.compass.map.R


class SplashActivity : AppCompatActivity() {
    private var handler: Handler? = null
    private var bt_go: Button? = null
    private var simpleDotImg: ImageView? = null
    private var liveGlobeMapBillingHelper: StreetViewAppSoniBillingHelper? = null
    private var controlSplash = false
    private val PREFS_NAME = "MyPrefs"
    private val BOOLEAN_KEY = "OnBoarding"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        MobileAds.initialize(this) { }



        simpleDotImg = findViewById(R.id.simpleDotImg)
        logAnalyticsForClicks("StreetViewSplashScreenOnCreate", this)
        liveGlobeMapBillingHelper = StreetViewAppSoniBillingHelper(this)
        handler = Handler(Looper.getMainLooper())
        bt_go = findViewById(R.id.bt_go)

      /*  AppLovinSdk.getInstance(this).mediationProvider = "max"
        AppLovinSdk.initializeSdk(this)
        {
            //  AppLovin SDK is initialized, start loading ads
            Log.d("ConstantAdsLoadAds:", "onInitializationComplete: ===Init Max")
        }*/

        FirebaseMessaging.getInstance()
            .subscribeToTopic("com.live.streetview.navigation.earthmap.compass.map")

        loadAppOpen()
       // preloadMax(this)


        handler!!.postDelayed({
            val animationUtils = AnimationUtils.loadAnimation(applicationContext, R.anim.zoom)
            bt_go!!.animation = animationUtils
            if (!controlSplash) {
                controlSplash = true
                moveToMainAll()
                Log.d("AppOpenSplashLogNormal:", "loadAppOpen:  on timer base")
            }
            Log.d("AppOpenSplashLogNormal:", "loadAppOpen: called without ad loading on timer base")
        }, 12000)//12000
    }

    private fun loadAppOpen() {
        if (liveGlobeMapBillingHelper!!.isNotAdPurchased) {
            Log.d("AppOpenSplashLogNormal:", "loadAppOpen:start loading splash load here")
            liveEarthAppOpenSplashAdsManager!!.loadAdForSplash {
                if (!controlSplash) {
                    if (it) {
                        controlSplash = true
                        moveToMainAll()
                        try {
                            handler = Handler(Looper.getMainLooper())
                            handler!!.removeCallbacksAndMessages(null)
                        } catch (_: Exception) {
                        }
                        Log.d("AppOpenSplashLogNormal:", "loadAppOpen: add laod splash")
                    } else {
                        controlSplash = true
                        Log.d("AppOpenSplashLogNormal:", "loadAppOpen: add laod failed splash")
                        moveDone()
                    }
                } else {
                    try {
                        handler = Handler(Looper.getMainLooper())
                        handler!!.removeCallbacksAndMessages(null)
                    } catch (_: Exception) {
                    }
                    Log.d(
                        "AppOpenSplashLogNormal:",
                        "loadAppOpen: already called without ad loading"
                    )
                }

            }
        }
    }

    private fun moveToMainAll() {
        if (liveGlobeMapBillingHelper!!.isNotAdPurchased) {
            if (appOpenAd != null) {
                liveEarthAppOpenSplashAdsManager!!.showAppOpenAdForSplash(
                    this,
                    object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent()
                            Log.d("AppOpenSplashLogNormal:", "onAdDismissedFullScreenContent")
                            moveDone()
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            super.onAdFailedToShowFullScreenContent(adError)
                            moveDone()
                        }
                    })
            } else {
                moveDone()
            }
        } else {
            moveDone()
        }
    }

    fun moveDone() {
        if (getBooleanFromPrefs()) {
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            saveBooleanToPrefs(true)
            val intent = Intent(this@SplashActivity, OnBoardingScreen::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun saveBooleanToPrefs(value: Boolean) {
        val sharedPreferences: SharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(BOOLEAN_KEY, value)
        editor.apply()
    }

    fun getBooleanFromPrefs(): Boolean {
        val sharedPreferences: SharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        return sharedPreferences.getBoolean(BOOLEAN_KEY, false)
    }
}