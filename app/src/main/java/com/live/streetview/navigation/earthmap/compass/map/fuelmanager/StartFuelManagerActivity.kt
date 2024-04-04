package com.live.streetview.navigation.earthmap.compass.map.fuelmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingFlowParams.ProductDetailsParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.ProductDetailsResponseListener
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.live.streetview.navigation.earthmap.compass.map.activities.utils.SharedPreferencesHelper
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityStartFuelManagerBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions


class StartFuelManagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartFuelManagerBinding
    var sharedPreferencesHelper:SharedPreferencesHelper?=null
    //  var omarStreetViewPurchaseHelper: StreetViewAppSoniPurchaseHelper? = null
    private var isFullscreen = false
    private val videoURL = "https://www.youtube.com/shorts/rUUSGr7biO0"
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (isFullscreen) {
                // if the player is in fullscreen, exit fullscreen
                youtubePlayer.toggleFullscreen()
            } else {
                finish()
            }
        }
    }

    //////////////////////////////////////
    private var PRODUCT_YEARLY = "ads_yearly"
    private val PRODUCT_MONTHLY = "ads_monthly"
    private var billingClient: BillingClient? = null
    private val purchaseItemIDs: ArrayList<String?> = object : ArrayList<String?>() {
        init {
            add(PRODUCT_YEARLY)
            add(PRODUCT_MONTHLY)
        }
    }

    //////////////////////////////////////
     //////////////////////////////////////
    private lateinit var youtubePlayer: YouTubePlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartFuelManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        billingWork()
        //   omarStreetViewPurchaseHelper = StreetViewAppSoniPurchaseHelper(this)
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        binding.monthlySubscription.setOnClickListener {
            // omarStreetViewPurchaseHelper!!.purchaseLiveEarthLocationMonthlyPackage()
            getListsSubDetail(PRODUCT_MONTHLY)
//            Toast.makeText(this, "click month", Toast.LENGTH_SHORT).show()
        }

        binding.yearlySubscription.setOnClickListener {

            // omarStreetViewPurchaseHelper!!.purchaseLiveEarthLocationYearlyPackage()
            getListsSubDetail(PRODUCT_YEARLY)
//            Toast.makeText(this, "click yearlySubscription", Toast.LENGTH_SHORT).show()

        }

        val iFramePlayerOptions = IFramePlayerOptions.Builder().controls(1)
            // enable full screen button
            .fullscreen(1).build()

        binding.youtubePlayerView.addFullscreenListener(object : FullscreenListener {
            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                isFullscreen = true

                // the video will continue playing in fullscreenView
                binding.youtubePlayerView.visibility = View.GONE
                binding.fullScreenContainer.visibility = View.VISIBLE
                binding.fullScreenContainer.addView(fullscreenView)

                // optionally request landscape orientation
                // requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }

            override fun onExitFullscreen() {
                isFullscreen = false

                // the video will continue playing in the player
                binding.youtubePlayerView.visibility = View.VISIBLE
                binding.fullScreenContainer.visibility = View.GONE
                binding.fullScreenContainer.removeAllViews()
            }
        })

        binding.youtubePlayerView.initialize(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youtubePlayer = youTubePlayer
                youTubePlayer.loadVideo("rUUSGr7biO0", 0f)
            }
        }, iFramePlayerOptions)
        lifecycle.addObserver(binding.youtubePlayerView)
    }

    private fun billingWork() {
          sharedPreferencesHelper = SharedPreferencesHelper(this)

// Save a string value

        billingClient = BillingClient.newBuilder(this)
            .enablePendingPurchases()
            .setListener { billingResult: BillingResult, list: List<Purchase?>? ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && list != null) {
                    for (purchase in list) {
                        if (purchase != null) {
                            verifySubPurchase(purchase)
                        }
                    }
                }
            }.build()
        //start the connection after initializing the billing client
        //start the connection after initializing the billing client
        establishConnection()
    }
    fun establishConnection() {
        billingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    //Use any of function below to get details upon successful connection
                    Log.d("BillingLogger", "Connection Established")
//                    Toast.makeText(this@StartFuelManagerActivity, "Connection Established", Toast.LENGTH_SHORT).show()

                    billingClient?.queryPurchasesAsync(
                        QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build()
                    ) { billingResult, list ->
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            for (purchase in list) {
                                if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED /*&& !purchase.isAcknowledged*/) {
                                    verifySubPurchase(purchase)
                                }
                            }
                        }else{
                            establishConnection()
                            Log.d("BillingLogger", "onResume: debugMessage "+billingResult.debugMessage)
                        }
                    }


                }
            }
            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Log.d("BillingLogger", "Connection NOT Established")
//                Toast.makeText(this@StartFuelManagerActivity, "Connection NOT Established", Toast.LENGTH_SHORT).show()
                establishConnection()
            }
        })
    }
    override fun onResume() {
        super.onResume()
        billingClient?.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build()
        ) { billingResult, list ->
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                for (purchase in list) {
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED /*&& !purchase.isAcknowledged*/) {
                        verifySubPurchase(purchase)
                    }
                }
            }else{
                establishConnection()
                Log.d("BillingLogger", "onResume: debugMessage "+billingResult.debugMessage)
            }
        }
    }

    fun getSubPurchases() {
        val productList = ArrayList<QueryProductDetailsParams.Product>()
        productList.add(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(PRODUCT_YEARLY)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()
        billingClient?.queryProductDetailsAsync(params,
            ProductDetailsResponseListener { billingResult, list ->
                LaunchSubPurchase(list[0])
                Log.d("BillingLogger", "Product Price" + list[0].subscriptionOfferDetails!![0].pricingPhases.pricingPhaseList[0].formattedPrice)

            })
    }
    //Call this function using PRODUCT_PREMIUM or PRODUCT_MONTHLY as parameters.
    private fun getListsSubDetail(SKU: String) {
        val productList = ArrayList<QueryProductDetailsParams.Product>()

        //Set your In App Product ID in setProductId()
        for (ids in purchaseItemIDs) {
            productList.add(
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(ids!!)
                    .setProductType(BillingClient.ProductType.SUBS)
                    .build()
            )
        }
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()
        billingClient?.queryProductDetailsAsync(params,
            ProductDetailsResponseListener { billingResult, list ->
                //                Log.d(BillingLogger, "Total size is: " + list);
                for (li in list) {
                    if (li.productId.equals(SKU, ignoreCase = true) && SKU.equals(
                            PRODUCT_MONTHLY,
                            ignoreCase = true
                        )
                    ) {
                        LaunchSubPurchase(li)
                        Log.d(
                            "BillingLogger",
                            "Monthly Price is " + li.subscriptionOfferDetails!![0].pricingPhases.pricingPhaseList[0].formattedPrice
                        )
                        Toast.makeText(
                            this@StartFuelManagerActivity,
                            "Monthly Price is " + li.subscriptionOfferDetails!![0].pricingPhases.pricingPhaseList[0].formattedPrice,
                            Toast.LENGTH_SHORT
                        ).show()

                        return@ProductDetailsResponseListener
                    } else if (li.productId.equals(SKU, ignoreCase = true) && SKU.equals(
                            PRODUCT_YEARLY,
                            ignoreCase = true
                        )
                    ) {
                        LaunchSubPurchase(li)
                        Log.d(
                            "BillingLogger",
                            "Yearly Price is " + li.subscriptionOfferDetails!![0].pricingPhases.pricingPhaseList[0].formattedPrice
                        )
                        Toast.makeText(
                            this@StartFuelManagerActivity,
                            "Yearly Price is " + li.subscriptionOfferDetails!![0].pricingPhases.pricingPhaseList[0].formattedPrice,
                            Toast.LENGTH_SHORT
                        ).show()

                        return@ProductDetailsResponseListener
                    }
                }
                //Do Anything that you want with requested product details
            })
    }

    fun LaunchSubPurchase(productDetails: ProductDetails) {
        assert(productDetails.subscriptionOfferDetails != null)
        val productList = ArrayList<ProductDetailsParams>()
        productList.add(
            ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .setOfferToken(productDetails.subscriptionOfferDetails!![0].offerToken)
                .build()
        )
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productList)
            .build()
        billingClient?.launchBillingFlow(this, billingFlowParams)
    }

    fun verifySubPurchase(purchases: Purchase) {
        if (purchases.isAcknowledged) {
            billingClient?.acknowledgePurchase(
                AcknowledgePurchaseParams
                    .newBuilder()
                    .setPurchaseToken(purchases.purchaseToken)
                    .build()
            ) { billingResult ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    for (pur in purchases.products) {
                        if (pur.equals(PRODUCT_YEARLY, ignoreCase = true)) {
                            Log.d("BillingLogger", "Purchase is successful$pur")
                           // isPurchase = "PRODUCT_YEARLY"
                             sharedPreferencesHelper?.saveString("PRODUCT_YEARLY", "PRODUCT_YEARLY")

                            val intent = Intent(this@StartFuelManagerActivity, FuelManagerActivity::class.java)
                            startActivity(intent)
                            //     tv_status.setText("Yay! Purchased")
                        } else if (pur.equals(PRODUCT_MONTHLY, ignoreCase = true)) {
                            Log.d("BillingLogger", "Purchase is successful$pur")
                            //  tv_status.setText("Yay! Purchased")
                            sharedPreferencesHelper?.saveString( "PRODUCT_MONTHLY", "PRODUCT_MONTHLY")

                           // isPurchase = "PRODUCT_MONTHLY"
                            val intent = Intent(this@StartFuelManagerActivity, FuelManagerActivity::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this, "not buy yet", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
    companion object {
//        @kotlin.jvm.JvmField
//        var isPurchase: String? = ""
    }
}