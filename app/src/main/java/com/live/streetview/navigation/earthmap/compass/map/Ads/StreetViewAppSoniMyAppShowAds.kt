package com.live.streetview.navigation.earthmap.compass.map.Ads

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.firebase.analytics.FirebaseAnalytics

object StreetViewAppSoniMyAppShowAds {
    //for simple backPressed


    @JvmStatic
    fun mediationBackPressedSimpleStreetViewLocation(
        context: Activity,
        mInterstitialAd: InterstitialAd?,
//        mInterstitialHighAd: InterstitialAd?,
        /* maxInterstitialAd: MaxInterstitialAd?*/ view: View
    ) {
        if (StreetViewAppSoniMyAppAds.adShowCounter()) {
            if (mInterstitialAd != null && StreetViewAppSoniMyAppAds.shouldGoForAds) {
                mInterstitialAd.show(context)
                mInterstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        StreetViewAppSoniMyAppAds.admobInterstitialAd = null
                        StreetViewAppSoniMyAppAds.isIntrestitalAdShowing = false
                        StreetViewAppSoniMyAppAds.preReLoadAds(context)
                        // Handler().postDelayed({ view.visibility = View.GONE }, 500)
                        context.finish()
                    }

                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()
                        StreetViewAppSoniMyAppAds.isIntrestitalAdShowing = true
                        //  view.visibility = View.VISIBLE

                    }
                }
            }
            /* else if (maxInterstitialAd != null && maxInterstitialAd.isReady) {
                 maxInterstitialAd.setListener(object : MaxAdListener {
                     override fun onAdLoaded(ad: MaxAd) {
                         Log.d("ConstantAdsLoadAds", "ShowMax loaded")
                     }

                     override fun onAdDisplayed(ad: MaxAd) {
                         Log.d("ConstantAdsLoadAds", "ShowMax onAdDisplayed")
                         StreetViewAppSoniMyAppAds.isIntrestitalAdShowing = true
                     }

                     override fun onAdHidden(ad: MaxAd) {
                         Log.d("ConstantAdsLoadAds", "ShowMax onAdHidden")
                         StreetViewAppSoniMyAppAds.isIntrestitalAdShowing = false
                         StreetViewAppSoniMyAppAds.preReLoadAds(context)
                         //                        StreetViewAppSoniMyAppAds.setHandlerForAd();
                         context.finish()
                         Handler().postDelayed({ view.visibility = View.GONE }, 500)
                     }

                     override fun onAdClicked(ad: MaxAd) {}
                     override fun onAdLoadFailed(adUnitId: String, error: MaxError) {
                         Log.d("ConstantAdsLoadAds", "ShowMax error$error")
                     }

                     override fun onAdDisplayFailed(ad: MaxAd, error: MaxError) {
                         Log.d("ConstantAdsLoadAds", "ShowMax onAdDisplayFailed$error")
                     }
                 })
                 maxInterstitialAd.showAd()
             }*/
            else {
                StreetViewAppSoniMyAppAds.preReLoadAds(context)
                context.finish()
                //  Handler().postDelayed({ view.visibility = View.GONE }, 500)
            }
        } else {
            StreetViewAppSoniMyAppAds.preReLoadAds(context)
            context.finish()
            // Handler().postDelayed({ view.visibility = View.GONE }, 500)
        }
    }

    fun mediationsameactivityStreetViewLocation(
        context: Activity,
        mInterstitialAd: InterstitialAd?,
        /* maxInterstitialAd: MaxInterstitialAd?*/
    ) {
        if (mInterstitialAd != null && StreetViewAppSoniMyAppAds.shouldGoForAds) {
            mInterstitialAd.show(context)
            mInterstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    StreetViewAppSoniMyAppAds.admobInterstitialAd = null
                    StreetViewAppSoniMyAppAds.isIntrestitalAdShowing = false
                    StreetViewAppSoniMyAppAds.preReLoadAds(context)
                    //                    StreetViewAppSoniMyAppAds.setHandlerForAd();
                }

                override fun onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent()
                    StreetViewAppSoniMyAppAds.isIntrestitalAdShowing = true
                }
            }
        }
        /*  else if (maxInterstitialAd != null && maxInterstitialAd.isReady) {
              maxInterstitialAd.setListener(object : MaxAdListener {
                  override fun onAdLoaded(ad: MaxAd) {
                      Log.d("ConstantAdsLoadAds", "ShowMax loaded")
                  }

                  override fun onAdDisplayed(ad: MaxAd) {
                      Log.d("ConstantAdsLoadAds", "ShowMax onAdDisplayed")
                      StreetViewAppSoniMyAppAds.isIntrestitalAdShowing = true
                  }

                  override fun onAdHidden(ad: MaxAd) {
                      Log.d("ConstantAdsLoadAds", "ShowMax onAdHidden")
                      StreetViewAppSoniMyAppAds.isIntrestitalAdShowing = false
                      StreetViewAppSoniMyAppAds.preReLoadAds(context)
                      //                    StreetViewAppSoniMyAppAds.setHandlerForAd();
                      context.finish()
                  }

                  override fun onAdClicked(ad: MaxAd) {
                      StreetViewAppSoniMyAppAds.isIntrestitalAdShowing = true
                  }

                  override fun onAdLoadFailed(adUnitId: String, error: MaxError) {
                      Log.d("ConstantAdsLoadAds", "ShowMax error$error")
                  }

                  override fun onAdDisplayFailed(ad: MaxAd, error: MaxError) {
                      Log.d("ConstantAdsLoadAds", "ShowMax onAdDisplayFailed$error")
                  }
              })
              maxInterstitialAd.showAd()
          }*/
        else {
            StreetViewAppSoniMyAppAds.preReLoadAds(context)
        }
    }


//    @JvmStatic
//    var firstShow = true
//    @JvmStatic
//    var secondClickShow = true


    @JvmStatic
    fun meidationForClickSimpleSmartToolsLocation(
        context: Context, mInterstitialAd: InterstitialAd?,
//        mHighInterstitialAd: InterstitialAd?,
        /* maxInterstitialAd: MaxInterstitialAd?*/
        intent: Intent?, view: View
    ) {

//        if (firstShow) {
//            if (mInterstitialAd != null && StreetViewAppSoniMyAppAds.shouldGoForAds) {
//                Log.d("TAG", "meidationForClickSimpleSmartToolsLocation111111: $firstShow $mInterstitialAd")
//
//                mInterstitialAd.show((context as Activity))
//                mInterstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
//                    override fun onAdDismissedFullScreenContent() {
//                        StreetViewAppSoniMyAppAds.admobInterstitialAd = null
//                        StreetViewAppSoniMyAppAds.isIntrestitalAdShowing = false
//                        StreetViewAppSoniMyAppAds.preReLoadAds(context)
//                        Handler().postDelayed({view.visibility=View.VISIBLE},500)
//                        context.startActivity(intent)
//                    }
//
//                    override fun onAdShowedFullScreenContent() {
//                        super.onAdShowedFullScreenContent()
//                        StreetViewAppSoniMyAppAds.isIntrestitalAdShowing = true
//                        view.visibility=View.VISIBLE
//                    }
//                }
//            } else if (maxInterstitialAd != null && maxInterstitialAd.isReady) {
//                maxInterstitialAd.setListener(object : MaxAdListener {
//                    override fun onAdLoaded(ad: MaxAd) {
//                        Log.d("ConstantAdsLoadAds", "ShowMax loaded")
//                    }
//
//                    override fun onAdDisplayed(ad: MaxAd) {
//                        Log.d("ConstantAdsLoadAds", "ShowMax onAdDisplayed")
//                        StreetViewAppSoniMyAppAds.isIntrestitalAdShowing = true
//                    }
//
//                    override fun onAdHidden(ad: MaxAd) {
//                        Log.d("ConstantAdsLoadAds", "ShowMax onAdHidden")
//                        StreetViewAppSoniMyAppAds.isIntrestitalAdShowing = false
//                        StreetViewAppSoniMyAppAds.preReLoadAds(context)
//                        //                        StreetViewAppSoniMyAppAds.setHandlerForAd();
//                        context.startActivity(intent)
//                    }
//
//                    override fun onAdClicked(ad: MaxAd) {
//                        StreetViewAppSoniMyAppAds.isIntrestitalAdShowing = true
//                    }
//
//                    override fun onAdLoadFailed(adUnitId: String, error: MaxError) {
//                        Log.d("ConstantAdsLoadAds", "ShowMax error$error")
//                    }
//
//                    override fun onAdDisplayFailed(ad: MaxAd, error: MaxError) {
//                        Log.d("ConstantAdsLoadAds", "ShowMax onAdDisplayFailed$error")
//                    }
//                })
//                maxInterstitialAd.showAd()
//            } else {
//                StreetViewAppSoniMyAppAds.preReLoadAds(context)
//                context.startActivity(intent)
//            }
//        } /*else if (secondClickShow && ){
//
//
//        }*/else
        if (StreetViewAppSoniMyAppAds.adShowCounter()) {

            if (mInterstitialAd != null && StreetViewAppSoniMyAppAds.shouldGoForAds) {
                mInterstitialAd.show((context as Activity))
                mInterstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        StreetViewAppSoniMyAppAds.admobInterstitialAd = null
                        StreetViewAppSoniMyAppAds.isIntrestitalAdShowing = false
                        StreetViewAppSoniMyAppAds.preReLoadAds(context)
                        // Handler().postDelayed({ view.visibility = View.GONE }, 500)

                        context.startActivity(intent)
                    }

                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()
                        StreetViewAppSoniMyAppAds.isIntrestitalAdShowing = true
                        //  view.visibility = View.VISIBLE

                    }
                }
            }
            /* else if (maxInterstitialAd != null && maxInterstitialAd.isReady) {
                 maxInterstitialAd.setListener(object : MaxAdListener {
                     override fun onAdLoaded(ad: MaxAd) {
                         Log.d("ConstantAdsLoadAds", "ShowMax loaded")
                     }

                     override fun onAdDisplayed(ad: MaxAd) {
                         Log.d("ConstantAdsLoadAds", "ShowMax onAdDisplayed")
                         StreetViewAppSoniMyAppAds.isIntrestitalAdShowing = true
                     }

                     override fun onAdHidden(ad: MaxAd) {
                         Log.d("ConstantAdsLoadAds", "ShowMax onAdHidden")
                         StreetViewAppSoniMyAppAds.isIntrestitalAdShowing = false
                         StreetViewAppSoniMyAppAds.preReLoadAds(context)
                         //                        StreetViewAppSoniMyAppAds.setHandlerForAd();
                         context.startActivity(intent)
                         Handler().postDelayed({ view.visibility = View.GONE }, 500)
                     }

                     override fun onAdClicked(ad: MaxAd) {
                         StreetViewAppSoniMyAppAds.isIntrestitalAdShowing = true
                     }

                     override fun onAdLoadFailed(adUnitId: String, error: MaxError) {
                         Log.d("ConstantAdsLoadAds", "ShowMax error$error")
                     }

                     override fun onAdDisplayFailed(ad: MaxAd, error: MaxError) {
                         Log.d("ConstantAdsLoadAds", "ShowMax onAdDisplayFailed$error")
                     }
                 })
                 maxInterstitialAd.showAd()
             }*/ else {
                StreetViewAppSoniMyAppAds.preReLoadAds(context)
                context.startActivity(intent)
                //  Handler().postDelayed({ view.visibility = View.GONE }, 500)
            }
        } else {
            StreetViewAppSoniMyAppAds.preReLoadAds(context)
            context.startActivity(intent)
            //  Handler().postDelayed({ view.visibility = View.GONE }, 500)
        }
    }

    @JvmStatic
    fun logAnalyticsForClicks(value: String?, context: Context?) {
        val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context!!)
        val bundle = Bundle()
        try {
            firebaseAnalytics.logEvent(value!!, bundle)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}