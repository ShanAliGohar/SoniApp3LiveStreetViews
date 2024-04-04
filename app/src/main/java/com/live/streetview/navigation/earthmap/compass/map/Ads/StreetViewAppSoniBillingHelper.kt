package com.live.streetview.navigation.earthmap.compass.map.Ads

import android.content.Context
import android.content.SharedPreferences

class StreetViewAppSoniBillingHelper(content: Context) {
    private val billingPreferences: SharedPreferences

    init {
        billingPreferences = content.getSharedPreferences("PurchasePrefs", Context.MODE_PRIVATE)
    }

    val isNotAdPurchased: Boolean
        get() = !billingPreferences.getBoolean("ads_remove", false)

    fun setBillingPreferences(status: Boolean) {
        billingPreferences.edit().putBoolean("ads_remove", status).apply()
    }





}