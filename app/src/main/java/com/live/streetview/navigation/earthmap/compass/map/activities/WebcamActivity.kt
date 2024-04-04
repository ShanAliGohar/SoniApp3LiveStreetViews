package com.live.streetview.navigation.earthmap.compass.map.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdView
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds.getAdSize
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediation
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation
import com.live.streetview.navigation.earthmap.compass.map.Model.WebcamModel
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.adapters.WebcamAdapter
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityWebcamBinding

class WebcamActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    var webcamAdapter: WebcamAdapter? = null
    var webcamModelArrayList: ArrayList<WebcamModel>? = null
    var imageView: ImageView? = null
    var bindingWebcam: ActivityWebcamBinding? = null
    private val callbackEnabled = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingWebcam = ActivityWebcamBinding.inflate(
            layoutInflater
        )
        setContentView(bindingWebcam!!.root)
        // setContentView(R.layout.activity_webcam);
        // webcamModelArrayList=new ArrayList<>();
        recyclerView = findViewById(R.id.webcamrecyclerview)
        imageView = findViewById(R.id.backarrowwebcam)
        logAnalyticsForClicks("StreetViewWebcamActivityOnCreate", this)

        //call back back press
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        //initBannerWebcam();
        imageView?.setOnClickListener(View.OnClickListener {
            logAnalyticsForClicks("StreetViewWebcamActivityOnBtnExit", this@WebcamActivity)
            mediationBackPressedSimpleStreetViewLocation(
                this@WebcamActivity,
                StreetViewAppSoniMyAppAds.admobInterstitialAd, bindingWebcam!!.whiteView
            )
        })
        webcamModelArrayList = ArrayList()
        recyclerView?.setLayoutManager(LinearLayoutManager(this))
        webcamAdapter = WebcamAdapter(this@WebcamActivity, webcamModelArrayList!!)
        recyclerView?.setAdapter(webcamAdapter)
        webcamdatalist()
        streetViewBannerAdsSmall()
    }

    private fun streetViewBannerAdsSmall() {
        val billingHelper = StreetViewAppSoniBillingHelper(this)
        val adContainer = bindingWebcam!!.bannerAdPlace.adContainer
        val adView = AdView(this)
        adView.adUnitId = StreetViewAppSoniMyAppAds.banner_admob_inApp
        adView.setAdSize(getAdSize(this))
        if (billingHelper.isNotAdPurchased) {
            loadSmartToolsBannerForMainMediation(
                adContainer, adView, this
            )
        } else {
            bindingWebcam!!.bannerID.visibility = View.GONE
        }
    }

    //    private void initBannerWebcam() {
    //        StreetViewLoadAds.loadSoniStreetViewBannerAdMob(
    //                bindingWebcam.bannerAdPlace.adContainer,
    //                bindingWebcam.bannerID,
    //                this
    //        );
    //    }
    /*    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent=new Intent(this,MainActivity.class);
//        StreetViewShowAds.streetViewFinishWithAdmobActivity(
//                this,
//                StreetViewLoadAds.admobInterstitialAdMyLiveStreetView,
//                StreetViewLoadAds.maxInterstitialAdLiveEarth,
//                intent);
        StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation(this,
                StreetViewAppSoniMyAppAds.admobInterstitialAd, StreetViewAppSoniMyAppAds.maxInterstitialAdStreetViewST);
    }*/
    private fun webcamdatalist() {
        val list = WebcamModel("i", "vE3BAgh_VAQ")
        val list1 = WebcamModel("i", "h-FOCuy4d7k")
        // WebcamModel list2=new WebcamModel("i","7_WuTK9fH9Y&t");
        val list3 = WebcamModel("i", "aaGJ_fDcceg")
        val list4 = WebcamModel("i", "72kRM86V-dw")
        val list5 = WebcamModel("i", "jGZs29qZAVQ")
        val list6 = WebcamModel("i", "SO9tUbxIIWU")
        val list7 = WebcamModel("i", "tG20DR0vDDA")
        val list8 = WebcamModel("i", "HSsqzzuGTPo")
        val list9 = WebcamModel("i", "MuflV6qAs3I")
        val list10 = WebcamModel("i", "Jqf9haCd6mM")
        val list11 = WebcamModel("i", "wWYK6IVszPk")
        val list12 = WebcamModel("i", "SUff9wXxlfE")
        val list13 = WebcamModel("i", "AyWDjZjBwC8")
        val list14 = WebcamModel("i", "WOJ6dWxPiLs")
        val list15 = WebcamModel("i", "YrPeoi7xAcw")
        val list16 = WebcamModel("i", "TYfYX0ngLkE")
        val list17 = WebcamModel("i", "AI0p4T1-f88")
        val list18 = WebcamModel("i", "FCPdIvXo2rU")
        val list19 = WebcamModel("i", "FCPdIvXo2rU")
        webcamModelArrayList!!.add(list)
        webcamModelArrayList!!.add(list1)
        // webcamModelArrayList.add(list2);
        webcamModelArrayList!!.add(list3)
        webcamModelArrayList!!.add(list4)
        webcamModelArrayList!!.add(list5)
        webcamModelArrayList!!.add(list6)
        webcamModelArrayList!!.add(list7)
        webcamModelArrayList!!.add(list8)
        webcamModelArrayList!!.add(list9)
        webcamModelArrayList!!.add(list10)
        webcamModelArrayList!!.add(list11)
        webcamModelArrayList!!.add(list12)
        webcamModelArrayList!!.add(list13)
        webcamModelArrayList!!.add(list14)
        webcamModelArrayList!!.add(list15)
        webcamModelArrayList!!.add(list16)
        webcamModelArrayList!!.add(list17)
        webcamModelArrayList!!.add(list18)
        webcamModelArrayList!!.add(list19)
        webcamAdapter!!.notifyDataSetChanged()
    }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                logAnalyticsForClicks("StreetViewWebcamActivityOnExit", this@WebcamActivity)
                mediationBackPressedSimpleStreetViewLocation(
                    this@WebcamActivity,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,
                    bindingWebcam!!.whiteView
                )
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback.remove()
    }
}
