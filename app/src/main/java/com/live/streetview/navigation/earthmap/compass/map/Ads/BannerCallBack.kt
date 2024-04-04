package com.live.streetview.navigation.earthmap.compass.map.Ads

interface BannerCallBack {
    fun onAdFailedToLoad(adError: String)
    fun onAdLoaded()
    fun onAdImpression()
    fun onPreloaded()
    fun onAdClicked()
    fun onAdClosed()
    fun onAdOpened()
    fun onAdSwipeGestureClicked()
}