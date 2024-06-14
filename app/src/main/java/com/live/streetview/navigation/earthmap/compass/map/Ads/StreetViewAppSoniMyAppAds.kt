package com.live.streetview.navigation.earthmap.compass.map.Ads

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.widget.LinearLayout
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.database.FirebaseDatabase
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppClass.Companion.getStr
import com.live.streetview.navigation.earthmap.compass.map.R

object StreetViewAppSoniMyAppAds {
    var appid_admob_inApp = getStr(R.string.admob_ad_id)
    var interstitial_admob_inApp = getStr(R.string.admob_interstitial_id)
    var interstitial_admob_inApp_high = getStr(R.string.admob_interstitial_id_high)
    var admob_interstitial_Splash_Inter = getStr(R.string.splash_admob_interstitial_id)
    var admob_appOpen_Splash = getStr(R.string.splash_admob_appOpen_id)
    var admob_appOpen_Splash_high = getStr(R.string.splash_admob_appOpen_id_high)
    var admob_rewardedAdd_id = getStr(R.string.rewarded_ad_id_admob)

    @JvmField
    var banner_admob_inApp = getStr(R.string.admob_banner_id)
    var admob_collaspable_banner_id = getStr(R.string.admob_collaspable_banner_id)

    @JvmField
    var banner_admob_inApp_medium = getStr(R.string.admob_banner_id_medium)
    var native_admob_inApp = getStr(R.string.admob_native_id)
    var onboarding_native_admob_inApp = getStr(R.string.admob_oboarding_native_id)
    var app_open_admob_inApp = getStr(R.string.app_open_ad_id_admob)
    var max_banner_id = getStr(R.string.max_banner_id)
    var max_interstitial_id = getStr(R.string.max_interstitial_id)
    var max_native_id = getStr(R.string.max_native_id)

    @JvmField
   // var maxInterstitialAdStreetViewST: MaxInterstitialAd? = null
    var canReLoadedMax = false
    var shouldShowAdmob = false

    @JvmField
    var haveGotSnapshot = false
    var ctr_control = false
    var is_splash_show = true
    var canReLoadedAdMob = false
    var isIntrestitalAdShowing = false

    @JvmField
    var admobInterstitialAd: InterstitialAd? = null

    @JvmField
    var admobInterstitialHighAd: InterstitialAd? = null
    var admobInterstitialAdsplash: InterstitialAd? = null
    var shouldGoForAds = true
    var next_ads_time: Long = 10000

    @JvmField
    var current_counter = 51.0
    var shouldShowAppOpen = true
    var adClickCounter = 1
    var adShowAfter = 2
    var startCounter = 0
    var bannerNativeController = ""
    private val adsDatabaseReferenceSmartTools =
        FirebaseDatabase.getInstance().getReference("Live_StreetView")
    private val myHandler = Handler()
    var flag = false

    private fun loadStreetViewAdMobBanner(
        adContainer: LinearLayout,
        mAdView: AdView,
        context: Context
    ) {

        try {
            mAdView.loadAd(AdRequest.Builder().build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        /*  MainActivity.flag = false;*/mAdView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                Log.d("ConstantAdsLoadAds", "Banner AdMob Loaded")
                try {
                    //  adContainer.removeAllViews();
                    adContainer.addView(mAdView)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
                mAdView.destroy()
              //  loadStreetViewBannerMax(adContainer, context)
                Log.d(
                    "ConstantAdsLoadAds",
                    "loadAdError : " + loadAdError.message + " : " + loadAdError.responseInfo!!
                        .responseId
                )
                //MakerMyAppAds.loadMoPubBannerAd(adContainer, context);
            }
        }
    }

    private fun loadStreetViewAdMobBannerMedium(
        adContainer: LinearLayout,
        mAdView: AdView,
        context: Context
    ) {

        try {
            mAdView.loadAd(AdRequest.Builder().build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mAdView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                Log.d("ConstantAdsLoadAds", "Banner AdMob Loaded")
                try {
                    //  adContainer.removeAllViews();
                    adContainer.addView(mAdView)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
                mAdView.destroy()
               // loadStreetViewBannerMax(adContainer, context)
                Log.d(
                    "ConstantAdsLoadAds",
                    "loadAdError : " + loadAdError.message + " : " + loadAdError.responseInfo!!
                        .responseId
                )
                //MakerMyAppAds.loadMoPubBannerAd(adContainer, context);
            }
        }
    }

    //MAX Banner
   /* fun loadStreetViewBannerMax(adContainer: LinearLayout, context: Context?) {
        val billingHelper = context?.let { StreetViewAppSoniBillingHelper(it) }
        if (billingHelper != null) {
            if (billingHelper.isNotAdPurchased) {
                val adView = MaxAdView(max_banner_id, context)
                adView.setListener(object : MaxAdViewAdListener {
                    override fun onAdExpanded(ad: MaxAd) {}
                    override fun onAdCollapsed(ad: MaxAd) {}
                    override fun onAdLoaded(ad: MaxAd) {
                        Log.i("ConstantAdsLoadAds ", "Max banner onAdLoaded")
                        try {
                            adContainer.removeAllViews()
                            adContainer.addView(adView)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onAdDisplayed(ad: MaxAd) {
                        Log.i("ConstantAdsLoadAds ", "Max banner onAdDisplayed")
                    }

                    override fun onAdHidden(ad: MaxAd) {}
                    override fun onAdClicked(ad: MaxAd) {}
                    override fun onAdLoadFailed(adUnitId: String, error: MaxError) {
                        Log.i("ConstantAdsLoadAds ", "Max banner onAdLoadFailed")
                        adView.destroy()
                    }

                    override fun onAdDisplayFailed(ad: MaxAd, error: MaxError) {
                        adView.destroy()
                    }
                })
                adView.loadAd()
            }
        }
    }*/

    @JvmStatic
    fun loadSmartToolsBannerForMainMediation(
        adContainer: LinearLayout,
        mAdView: AdView,
        context: Context
    ) {
//        if (MainActivity.flag) {
        loadStreetViewAdMobBanner(adContainer, mAdView, context)
        //        }
    }

    @JvmStatic
    fun loadSmartToolsBannerForMainMediationMedium(
        adContainer: LinearLayout,
        mAdView: AdView,
        context: Context
    ) {
//        if (MainActivity.flag) {
        loadStreetViewAdMobBanner(adContainer, mAdView, context)
        //        }
    }

    @JvmStatic
    fun getAdSize(context: Activity): AdSize {
        //Determine the screen width to use for the ad width.
        val display = context.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels.toFloat()
        val density = outMetrics.density

        //you can also pass your selected width here in dp
        val adWidth = (widthPixels / density).toInt()

        //return the optimal size depends on your orientation (landscape or portrait)
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
    }

    @JvmStatic
    fun preLoadAds(context: Context?) {
        val billingHelper = context?.let { StreetViewAppSoniBillingHelper(it) }
        Log.d("ConstantAdsLoadAds", "StreetViewAppSoniInterstitial preLoadAds called")

        if (billingHelper != null) {
            if (billingHelper.isNotAdPurchased) {
                //admobeload
                if (admobInterstitialAd == null) {
                    canReLoadedAdMob = false
                    InterstitialAd.load(
                        context,
                        interstitial_admob_inApp,
                        AdRequest.Builder().build(),
                        object : InterstitialAdLoadCallback() {
                            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                                super.onAdLoaded(interstitialAd)
                                Log.d("ConstantAdsLoadAds", "Admob loaded")
                                Log.d("ConstantAdsLoadAds", "StreetViewAppSoniInterstitial Admob loaded")
                                canReLoadedAdMob = true
                                admobInterstitialAd = interstitialAd
                            }

                            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                                super.onAdFailedToLoad(loadAdError)
                                Log.d("ConstantAdsLoadAds", "Admob Faild: $loadAdError")
                                Log.d("ConstantAdsLoadAds", "StreetViewAppSoniInterstitial Admob Faild: $loadAdError")
                                canReLoadedAdMob = true
                                admobInterstitialAd = null
                            }
                        })
                } else {
                    Log.d("ConstantAdsLoadAds", "StreetViewAppSoniInterstitial admobe AlReady loaded")
                }
            }
        }
    }

    /*    @JvmStatic
        fun preLoadAdsHighInterstitial(context: Context?) {
            val billingHelper = context?.let { StreetViewAppSoniBillingHelper(it) }
            if (billingHelper != null) {
                if (billingHelper.isNotAdPurchased) {
                    //admobeload
                    if (admobInterstitialHighAd == null) {
                        canReLoadedAdMob = false
                        InterstitialAd.load(
                            context,
                            interstitial_admob_inApp_high,
                            AdRequest.Builder().build(),
                            object : InterstitialAdLoadCallback() {
                                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                                    super.onAdLoaded(interstitialAd)
                                    Log.d("ConstantAdsLoadAdsHigh", "Admob loaded")
                                    canReLoadedAdMob = true
                                    admobInterstitialHighAd = interstitialAd
                                }

                                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                                    super.onAdFailedToLoad(loadAdError)
                                    Log.d("ConstantAdsLoadAdsHigh", "Admob Faild: $loadAdError")
                                    canReLoadedAdMob = true
                                    admobInterstitialHighAd = null
                                }
                            })
                    } else {
                        Log.d("ConstantAdsLoadAdsHigh", "admobe AlReady loaded")
                    }
                }
            }
        }*/

    @JvmStatic
    fun preSplashLoadAds(context: Context?) {
        Log.d("InterstitialAdLog:", "Entered preLoadAds")
        val billingHelper = context?.let { StreetViewAppSoniBillingHelper(it) }
        if (billingHelper != null) {
            if (billingHelper.isNotAdPurchased) {
                Log.d("InterstitialAdLog:", "shouldShowAds preLoadAds")

                //admobeload
                if (admobInterstitialAdsplash == null) {
                    Log.d("InterstitialAdLog:", "Admob is null")
                    InterstitialAd.load(
                        context,
                        admob_interstitial_Splash_Inter,
                        AdRequest.Builder().build(),
                        object : InterstitialAdLoadCallback() {
                            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                                super.onAdLoaded(interstitialAd)
                                Log.d(
                                    "InterstitialAdLog:",
                                    "admobInterstitialAdsplash Admob loaded"
                                )
                                admobInterstitialAdsplash = interstitialAd
                            }

                            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                                super.onAdFailedToLoad(loadAdError)
                                Log.d("InterstitialAdLog:", "admobInterstitialAdsplash Admob Faild")
                                admobInterstitialAdsplash = null
                            }
                        })
                } else {
                    Log.d("InterstitialAdLog:", "admobInterstitialAdsplash admobe AlReady loaded")
                }
            } else {
                Log.d("InterstitialAdLog:", "not shouldShowAds preLoadAds")
            }
        }
    }

    fun preReLoadAds(context: Context?) {
        val billingHelper = context?.let { StreetViewAppSoniBillingHelper(it) }

        Log.d("ConstantAdsLoadAds", "StreetViewAppSoniInterstitial preReLoadAds called")

        if (billingHelper != null) {
            if (billingHelper.isNotAdPurchased) {
                //admobeload
                if (admobInterstitialAd != null) {
                    Log.d("ConstantAdsLoadAds", "admobe ReAlReady loaded")
                    Log.d("ConstantAdsLoadAds", "StreetViewAppSoniInterstitial admobe ReAlReady loaded")
                } else

                {

                    Log.d("ConstantAdsLoadAds", "StreetViewAppSoniInterstitial canReLoadedAdMob " + canReLoadedAdMob)
                    canReLoadedAdMob = false
                    InterstitialAd.load(
                        context,
                        interstitial_admob_inApp,
                        AdRequest.Builder().build(),
                        object : InterstitialAdLoadCallback() {
                            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                                super.onAdLoaded(interstitialAd)
                                Log.d("ConstantAdsLoadAds", "StreetViewAppSoniInterstitial Admob Reloaded")
                                canReLoadedAdMob = true
                                admobInterstitialAd = interstitialAd
                            }

                            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                                super.onAdFailedToLoad(loadAdError)
                                Log.d("ConstantAdsLoadAds", "StreetViewAppSoniInterstitial Admob ReFaild")
                                canReLoadedAdMob = true
                                admobInterstitialAd = null
                            }
                        })

                }

                /*//admobeloadhigh
                if (admobInterstitialHighAd != null) {
                    Log.d("ConstantAdsHighLoadAds", "admobe ReAlReady loaded")
                } else {
                    Log.d("ConstantAdsHighLoadAds", "canReLoadedAdMob " + canReLoadedAdMob)
                    canReLoadedAdMob = false
                    InterstitialAd.load(
                        context,
                        interstitial_admob_inApp_high,
                        AdRequest.Builder().build(),
                        object : InterstitialAdLoadCallback() {
                            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                                super.onAdLoaded(interstitialAd)
                                Log.d("ConstantAdsHighLoadAds", "Admob Reloaded")
                                canReLoadedAdMob = true
                                admobInterstitialHighAd = interstitialAd
                            }

                            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                                super.onAdFailedToLoad(loadAdError)
                                Log.d("ConstantAdsHighLoadAds", "Admob ReFaild")
                                canReLoadedAdMob = true
                                admobInterstitialHighAd = null
                            }
                        })

                }*/
            }
        }
    }

    /*  fun preReLoadHighAds(context: Context?) {
          val billingHelper = context?.let { StreetViewAppSoniBillingHelper(it) }
          if (billingHelper != null) {
              if (billingHelper.isNotAdPurchased) {
                  //admobeload
                  if (admobInterstitialHighAd != null) {
                      Log.d("ConstantAdsHighLoadAds", "admobe ReAlReady loaded")
                  } else {
                      Log.d("ConstantAdsHighLoadAds", "canReLoadedAdMob " + canReLoadedAdMob)
                      canReLoadedAdMob = false
                      InterstitialAd.load(
                          context,
                          interstitial_admob_inApp_high,
                          AdRequest.Builder().build(),
                          object : InterstitialAdLoadCallback() {
                              override fun onAdLoaded(interstitialAd: InterstitialAd) {
                                  super.onAdLoaded(interstitialAd)
                                  Log.d("ConstantAdsHighLoadAds", "Admob Reloaded")
                                  canReLoadedAdMob = true
                                  admobInterstitialHighAd = interstitialAd
                              }

                              override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                                  super.onAdFailedToLoad(loadAdError)
                                  Log.d("ConstantAdsHighLoadAds", "Admob ReFaild")
                                  canReLoadedAdMob = true
                                  admobInterstitialHighAd = null
                              }
                          })

                  }
              }
          }
      }*/

   /* @JvmStatic
    fun preloadMax(context: Activity?) {
        //maxload
        val billingHelper = context?.let { StreetViewAppSoniBillingHelper(it) }
        if (billingHelper != null) {
            if (billingHelper.isNotAdPurchased) {
                if (maxInterstitialAdStreetViewST == null) {
                    canReLoadedMax = false
                    maxInterstitialAdStreetViewST = MaxInterstitialAd(max_interstitial_id, context)
                    maxInterstitialAdStreetViewST!!.setListener(object : MaxAdListener {
                        override fun onAdLoaded(ad: MaxAd) {
                            Log.d("ConstantAdsLoadAds", "Max loaded")
                            canReLoadedMax = true
                        }

                        override fun onAdDisplayed(ad: MaxAd) {
                            Log.d("ConstantAdsLoadAds", "Max onAdDisplayed")
                        }

                        override fun onAdHidden(ad: MaxAd) {}
                        override fun onAdClicked(ad: MaxAd) {}
                        override fun onAdLoadFailed(adUnitId: String, error: MaxError) {
                            Log.d("ConstantAdsLoadAds", "Max error$error")
                            canReLoadedMax = true
                        }

                        override fun onAdDisplayFailed(ad: MaxAd, error: MaxError) {
                            Log.d("ConstantAdsLoadAds", "Max onAdDisplayFailed$error")
                        }
                    })
                    maxInterstitialAdStreetViewST!!.loadAd()
                }
            } else {
                Log.d("ConstantAdsLoadAds", "Max AlReady loaded")
            }
        }
    }*/

    fun showForSplashIntrestitialStreetView(
        context: Activity,
        mInterstitialAd: InterstitialAd?,
        intent: Intent?
    ) {
        if (mInterstitialAd != null) {
            Log.d("InterstitialAdLog:", "not null")
            mInterstitialAd.show(context)
            mInterstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    Log.d("InterstitialAdLog:", "dissmissed")
                    shouldShowAppOpen = true
                    admobInterstitialAdsplash = null
//                    preLoadAds(context)
                    context.startActivity(intent)
                    context.finish()
                }

                override fun onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent()
                    shouldShowAppOpen = false
                    Log.d("InterstitialAdLog:", "Showing")
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    shouldShowAppOpen = true
                    Log.d("InterstitialAdLog:", "Faild " + adError.message)
//                    preLoadAds(context)
                    context.startActivity(intent)
                    context.finish()
                }
            }
        } else {
            Log.d("InterstitialAdLog:", " ad is  null")
//            preLoadAds(context)
            context.startActivity(intent)
            context.finish()
        }
    }

    fun adShowCounter(): Boolean {
        Log.d("adShowCounter", " " + adClickCounter)
        Log.d("adShowCounter", " " + startCounter)
        adClickCounter++
        return if (adClickCounter == startCounter - 1) {
            if (flag) {
                false
            } else {
                flag = true
                adClickCounter = startCounter
                Log.d("adShowCounter", "one:  1  show")
                true
            }
        } else if (adClickCounter > adShowAfter) {
            adClickCounter = startCounter
            Log.d("adShowCounter", "two:   >  show ")
            true
        } else {
            Log.d("adShowCounter", " three  else  No")
            false
        }
    }


}