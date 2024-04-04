package com.live.streetview.navigation.earthmap.compass.map.Ads

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.android.billingclient.api.*


class StreetViewAppSoniPurchaseHelper(private val activityContext: Context) :
    PurchasesUpdatedListener {

    private lateinit var googleBillingStreetViewClient: BillingClient
    private val TAG = "BillingLogger:"
    private val listAvailStreetViewLocationPurchases = ArrayList<SkuDetails>()
    private lateinit var billingStreetViewLocationPreferences: SharedPreferences

    init {
        initMyBillingClientStreetViewLocation()
    }

    private fun initMyBillingClientStreetViewLocation() {
        billingStreetViewLocationPreferences =
            activityContext.getSharedPreferences("PurchasePrefs", Context.MODE_PRIVATE)
        googleBillingStreetViewClient = BillingClient
            .newBuilder(activityContext)
            .enablePendingPurchases()
            .setListener(this)
            .build()
        googleBillingStreetViewClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "Google Billing is Connected")
                    fetchSmartToolsLocationAllInAppsFromConsole() /*available on console*/
                    fetchSmartToolsLocationPurchasedInAppsFromConsole()  /*user has purchased*/
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.d(TAG, "Google Billing is  Disconnected")
            }
        })
    }

    private fun fetchSmartToolsLocationAllInAppsFromConsole() {
        val skuListToQuery = ArrayList<String>()
        skuListToQuery.add("ads_remove")
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuListToQuery).setType(BillingClient.SkuType.INAPP)
        googleBillingStreetViewClient.querySkuDetailsAsync(
            params.build(),
            object : SkuDetailsResponseListener {
                override fun onSkuDetailsResponse(
                    result: BillingResult,
                    skuDetails: MutableList<SkuDetails>?
                ) {
                    //Log.i(TAG, "onSkuResponse ${result?.responseCode}")
                    if (skuDetails != null) {
                        for (skuDetail in skuDetails) {
                            listAvailStreetViewLocationPurchases.add(skuDetail)
                            Log.i(TAG, skuDetail.toString())
                        }
                    } else {
                        Log.i(TAG, "No skus for this application")
                    }
                }

            })
    }
    fun fetchSmartToolsLocationPurchasedInAppsFromConsole() {
        googleBillingStreetViewClient.queryPurchasesAsync(BillingClient.SkuType.INAPP,
            object : PurchasesResponseListener {
                override fun onQueryPurchasesResponse(
                    billingResult: BillingResult,
                    listPurchased: MutableList<Purchase>
                ) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        if (listPurchased.size > 0) {
                            for (singlePurchase in listPurchased) {
                                if (singlePurchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                                    Log.d(TAG, "Product Purchased: ${singlePurchase.skus[0]}")
                                    billingStreetViewLocationPreferences.edit()
                                        .putBoolean(singlePurchase.skus[0], true).apply()
                                } else {
                                    billingStreetViewLocationPreferences.edit()
                                        .putBoolean(singlePurchase.skus[0], false).apply()
                                    Log.d(TAG, "Product Not Purchased: ${singlePurchase.skus[0]}")
                                }
                            }
                        } else {
                            Log.d(TAG, "Array List Purchase Null$listPurchased")
                        }
                    } else {
                        Log.d(TAG, "Billing Checker Failed 1: ${billingResult.responseCode}")
                    }

                }

            })
    }
//    fun fetchSmartToolsLocationPurchasedInAppsFromConsole() {
//        val purchaseResults: Purchase.PurchasesResult =
//            googleBillingStreetViewClient.queryPurchases(BillingClient.SkuType.INAPP)
//        if (purchaseResults.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
//            val listPurchased = purchaseResults.purchasesList
//            if (listPurchased != null && listPurchased.size > 0) {
//                for (singlePurchase in listPurchased) {
//                    if (singlePurchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
//                        Log.d(TAG, "Product Purchased: ${singlePurchase.skus}")
//                        billingStreetViewLocationPreferences.edit().putBoolean(singlePurchase!!.skus.toString(), true).apply()
//                    } else {
//                        billingStreetViewLocationPreferences.edit().putBoolean(singlePurchase.skus.toString(), false).apply()
//                        Log.d(TAG, "Product Not Purchased: ${singlePurchase.skus}")
//                    }
//                }
//            } else {
//                Log.d(TAG, "Array List Purchase Null 1 $listPurchased")
//            }
//        } else {
//            Log.d(TAG, "Billing Checker Failed 1: ${purchaseResults.billingResult.responseCode}")
//        }
//
//    }

    fun purchaseLiveEarthLocationAdsPackage() {
        Log.d(TAG, "Going to purchase ads_purchase")
        if (listAvailStreetViewLocationPurchases.size > 0) {
            try {
                val flowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(listAvailStreetViewLocationPurchases[0])
                    .build()
                val responseCode =
                    googleBillingStreetViewClient.launchBillingFlow(
                        activityContext as Activity,
                        flowParams
                    ).responseCode
                Log.d(TAG, "Google Billing Response : $responseCode")
            } catch (e: Exception) {
            }
        } else {
            Log.d(TAG, "Nothing to purchase for google billing")
        }
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                Log.d(TAG, "onPurchases Successfully Purchased : " + purchase.skus)
                handlePurchase(purchase)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.d(TAG, "Google Billing Cancelled")
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            Log.d(TAG, "Google Billing Purchased Already")
            Toast.makeText(
                activityContext,
                "You have already purchased this item",
                Toast.LENGTH_LONG
            )
                .show()
        } else {
            Log.d(TAG, "Google billing other error " + billingResult.responseCode)
        }
    }

    private val purchaseAcknowledgedListener: AcknowledgePurchaseResponseListener = object
        : AcknowledgePurchaseResponseListener {
        override fun onAcknowledgePurchaseResponse(p0: BillingResult) {
            Log.d(TAG, "Success Acknowledged : ${p0.responseCode}  :${p0.debugMessage}")
            fetchSmartToolsLocationPurchasedInAppsFromConsole()
        }

    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                Log.d(TAG, "Process acknowledging: ${purchase.skus}")
                val acknowledgeParamaters = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                googleBillingStreetViewClient.acknowledgePurchase(
                    acknowledgeParamaters,
                    purchaseAcknowledgedListener
                )
                /*Now update preferences..either restart app so that query will
                execute or here make preferences true for ads*/

                /*here only one product so call preferences in acknowledged.*/

                /*or after acknowledged call query Method*/
            }
        }
    }
}