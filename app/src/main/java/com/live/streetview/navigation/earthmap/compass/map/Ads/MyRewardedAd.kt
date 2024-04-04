package com.live.streetview.navigation.earthmap.compass.map.Ads

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class MyRewardedAd {

    companion object {
        private var rewardedAd: RewardedAd? = null
        fun rewardedAdsLoad(mContext: Context) {
            if (rewardedAd == null) {
                val adRequest = AdRequest.Builder().build()
                RewardedAd.load(mContext,
                    StreetViewAppSoniMyAppAds.admob_rewardedAdd_id,
                    adRequest,
                    object : RewardedAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            Log.d("RewardedAd", adError.toString())
//                            reLoadRewardedAdsLoad(mContext)
                        }

                        override fun onAdLoaded(p0: RewardedAd) {
                            Log.d("RewardedAd", "rewardedAdsLoad Ad was loaded.")
                            rewardedAd = p0
                        }
                    })
            }
        }

      fun rewardedAdShow(mContext: Activity, intent: Intent?) {

            if (rewardedAd != null) {
                rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdClicked() {
                        Log.d("RewardedAd", "rewardedAds onAdClicked.")
                    }

                    override fun onAdDismissedFullScreenContent() {
                        /*super.onAdDismissedFullScreenContent()*/
                        Log.d("RewardedAd", "rewardedAds onAdDismissedFullScreenContent.")
                        rewardedAd = null
                        reLoadRewardedAdsLoad(mContext)
                        mContext.startActivity(intent)
                        mContext.finish()
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        mContext.startActivity(intent)
                        mContext.finish()
                    }

                    override fun onAdImpression() {
                        // Called when an impression is recorded for an ad.
                        Log.d("RewardedAd", "rewardedAds onAdImpression.")
                    }

                    override fun onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        Log.d("TAG", "Ad showed fullscreen content.")
                        Log.d(
                            "RewardedAd", "rewardedAds onAdShowedFullScreenContent."
                        )
                    }
                }


                rewardedAd.let { ad ->
                    ad!!.show(mContext, OnUserEarnedRewardListener {
                        // Handle the reward.
                        Log.d("RewardedAd", "rewardedAdShow: here")
                    })
                }
            } else {
                reLoadRewardedAdsLoad(mContext)
                mContext.startActivity(intent)
                mContext.finish()
            }


        }

        fun reLoadRewardedAdsLoad(mContext: Context) {

            if (rewardedAd != null) {
                Log.d("ConstantAdsLoadAds", "reLoadRewardedAdsLoad ReAlReady loaded")
            } else {

                val adRequest = AdRequest.Builder().build()
                RewardedAd.load(mContext,
                    StreetViewAppSoniMyAppAds.admob_rewardedAdd_id,
                    adRequest,
                    object : RewardedAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            Log.d(
                                "RewardedAd", adError.toString()
                            )
                            rewardedAd = null

                        }

                        override fun onAdLoaded(ad: RewardedAd) {
                            Log.d(
                                "RewardedAd", "reLoadRewardedAdsLoad Ad was loaded."
                            )
                            rewardedAd = ad
                        }
                    })


            }


        }
    }


}