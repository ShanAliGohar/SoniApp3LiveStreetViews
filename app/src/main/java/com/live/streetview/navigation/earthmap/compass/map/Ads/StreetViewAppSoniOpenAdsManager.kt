package com.live.streetview.navigation.earthmap.compass.map.Ads

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.live.streetview.navigation.earthmap.compass.map.activities.SplashActivity
import com.live.streetview.navigation.earthmap.compass.map.activities.YotubeActivity
import java.util.*

class StreetViewAppSoniOpenAdsManager(private val myApplication: StreetViewAppSoniMyAppClass) :
    ActivityLifecycleCallbacks, LifecycleObserver {
    private var appOpenAd: AppOpenAd? = null
    private var loadCallback: AppOpenAdLoadCallback? = null
    var makerAppCurrentActivity: Activity? = null
    var billingHelper: StreetViewAppSoniBillingHelper
    private var adLoadTime: Long = 0

    init {
        myApplication.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        billingHelper = StreetViewAppSoniBillingHelper(myApplication)
    }

    /**
     * LifecycleObserver methods
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        //showAdIfAvailable();
        Log.d(LOG_TAG, "onStart")
        if (!(makerAppCurrentActivity is SplashActivity || makerAppCurrentActivity is YotubeActivity)) {
            if (billingHelper.isNotAdPurchased) {
                if (!StreetViewAppSoniMyAppAds.isIntrestitalAdShowing) {
                    showAdIfAvailable()
                }
            }
        } else {
            if (billingHelper.isNotAdPurchased) {
                fetchAd()
            }
        }
    }

    /**
     * Request an ad
     */
    fun fetchAd() {
        Log.d(LOG_TAG, "fetchAdRequest: ")
        // We will implement this below.
        if (isAdAvailable) {
            return
        }
        loadCallback = object : AppOpenAdLoadCallback() {
            /**
             * Called when an app open ad has loaded.
             *
             * @param ad the loaded app open ad.
             */
            override fun onAdLoaded(ad: AppOpenAd) {
                appOpenAd = ad
                adLoadTime = Date().time
            }

            /**
             * Called when an app open ad has failed to load.
             *
             * @param loadAdError the error.
             */
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                // Handle the error.
            }
        }
        val request = adRequest
        AppOpenAd.load(
            myApplication, StreetViewAppSoniMyAppAds.app_open_admob_inApp, request,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback as AppOpenAdLoadCallback
        )
    }

    /**
     * Creates and returns ad request.
     */
    private val adRequest: AdRequest
        private get() = AdRequest.Builder().build()

    /**
     * Utility method that checks if ad exists and can be shown.
     */
    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
        val dateDifference = Date().time - adLoadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }

    val isAdAvailable: Boolean
        get() = appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)

    /**
     * Shows the ad if one isn't already showing.
     */
    fun showAdIfAvailable() {
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.
        if (!isShowingAd && isAdAvailable) {
            Log.d(LOG_TAG, "Will show ad.")
            val fullScreenContentCallback: FullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        // Set the reference to null so isAdAvailable() returns false.
                        appOpenAd = null
                        isShowingAd = false
                        if (StreetViewAppSoniMyAppAds.shouldShowAppOpen) {
                            fetchAd()
                        }
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {}
                    override fun onAdShowedFullScreenContent() {
                        isShowingAd = true
                    }
                }
            appOpenAd!!.fullScreenContentCallback = fullScreenContentCallback
            appOpenAd!!.show(makerAppCurrentActivity!!)
        } else {
            Log.d(LOG_TAG, "Can not show ad.")
            if (StreetViewAppSoniMyAppAds.shouldShowAppOpen) {
                fetchAd()
            }
        }
    }

    /**
     * ActivityLifecycleCallback methods
     */
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {
        makerAppCurrentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        makerAppCurrentActivity = activity
    }

    override fun onActivityStopped(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {
        makerAppCurrentActivity = null
    }

    fun cancelAppOpenAd() {
        Log.d(LOG_TAG, "cancelAppOpenAd: ")
        appOpenAd = null
    }

    companion object {
        private const val LOG_TAG = "AppOpenLog:"
        private var isShowingAd = false
    }
}