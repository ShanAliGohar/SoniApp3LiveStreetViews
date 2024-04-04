package com.live.streetview.navigation.earthmap.compass.map.Ads

import android.app.Application.ActivityLifecycleCallbacks
import androidx.lifecycle.LifecycleObserver
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import android.app.Activity
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.ads.LoadAdError
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.FullScreenContentCallback
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import java.lang.Exception
import java.util.*

class StreetViewAppOpenSplashAdsManagerHigh(private val mMyAppClass: StreetViewAppSoniMyAppClass) :
    ActivityLifecycleCallbacks, LifecycleObserver {

    private var loadCallback: AppOpenAdLoadCallback? = null
    var makerAppCurrentActivity: Activity? = null
    private var adLoadTime: Long = 0
    var billingHelper: StreetViewAppSoniBillingHelper
    private var isDisplayingSmartToolsAd = false

    init {
        mMyAppClass.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        billingHelper = StreetViewAppSoniBillingHelper(mMyAppClass)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
    }

    fun loadAdForSplash() {
        Log.d(TAG, "fetchAdForSplash: ")
        // We will implement this below.
        if (isAdAvailable) {
            Log.d(TAG, "fetchAdForSplash: ")
            return
        }
        loadCallback = object : AppOpenAdLoadCallback() {
            override fun onAdLoaded(ad: AppOpenAd) {
                Log.d(TAG, "onAdLoaded yes: load ")
                appOpenAd = ad
                adLoadTime = Date().time
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                Log.d(TAG, "onAdFailedToLoad: " + loadAdError.message)
            }
        }
        val request = adRequest
        AppOpenAd.load(
            mMyAppClass,
            StreetViewAppSoniMyAppAds.admob_appOpen_Splash_high,
            request,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            loadCallback as AppOpenAdLoadCallback
        )
    }

    private val adRequest: AdRequest
        private get() = AdRequest.Builder().build()

    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
        val dateDifference = Date().time - adLoadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }

    val isAdAvailable: Boolean
        get() = appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)

    fun showAppOpenAdForSplash(
        toolboxActivity: AppCompatActivity?,
        smartToolsListener: FullScreenContentCallback
    ) {
        Log.d(TAG, "showToolboxAppOpenAd: Splash")
        if (!isDisplayingSmartToolsAd && isAdAvailable) {
            val fullScreenContentCallback: FullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        super.onAdFailedToShowFullScreenContent(adError)
                        smartToolsListener.onAdFailedToShowFullScreenContent(adError)
                    }

                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()
                        isDisplayingSmartToolsAd = true
                        smartToolsListener.onAdShowedFullScreenContent()
                    }

                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        smartToolsListener.onAdDismissedFullScreenContent()
                        appOpenAd = null
                        isDisplayingSmartToolsAd = false
                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                    }

                    override fun onAdClicked() {
                        super.onAdClicked()
                    }
                }
            try {
                appOpenAd!!.fullScreenContentCallback = fullScreenContentCallback
                appOpenAd!!.show(toolboxActivity!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            Log.d(TAG, "Smart Tools Can not show ad")
            smartToolsListener.onAdDismissedFullScreenContent()
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

    companion object {
         var appOpenAd: AppOpenAd? = null
        private const val TAG = "AppOpenSplashLogHigh:"
        private const val isShowingAd = false
    }
}