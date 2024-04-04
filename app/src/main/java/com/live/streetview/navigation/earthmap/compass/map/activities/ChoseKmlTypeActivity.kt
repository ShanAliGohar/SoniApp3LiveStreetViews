package com.live.streetview.navigation.earthmap.compass.map.activities


import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.gms.ads.AdView
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityChoseKmlTypeBinding


class ChoseKmlTypeActivity : AppCompatActivity() {
    val PICK_KML_FILE = 18
    private lateinit var binding: ActivityChoseKmlTypeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChoseKmlTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks(
            "ChoseKmlTypeActivityOnCreate",
            this@ChoseKmlTypeActivity
        )

        this.onBackPressedDispatcher.addCallback(onBackPressedCallback)

        binding.imageView39.setOnClickListener {
            StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks(
                "ChoseKmlTypeActivityEXitOnButton",
                this@ChoseKmlTypeActivity
            )
            onBackPressedDispatcher.onBackPressed()
        }
        binding.openWithGallery.setOnClickListener {
            openLoadFileDialog()
        }
        binding.openWithURl.setOnClickListener {
            /* openUrlDialog()*/
            showDialog(this)
        }
        streetViewBannerAdsSmall()

    }

    fun openLoadFileDialog() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        startActivityForResult(intent, PICK_KML_FILE)
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int, intent: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (intent != null) {
            val uri = intent.data
            val intent = Intent(this@ChoseKmlTypeActivity, KmlViewerActivity::class.java)
            intent.putExtra("path", uri.toString())
            startActivity(intent)
        }
    }


    fun showDialog(activity: Activity) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.search_dialog)
        val editText = dialog.findViewById<EditText>(R.id.editTextTextPersonName)
        val ok = dialog.findViewById<CardView>(R.id.cd_dialog)
        val cancel = dialog.findViewById<CardView>(R.id.cancel)
        ok.setOnClickListener {
            dialog.dismiss()
            if (editText.text.isNullOrEmpty()) {
                Toast.makeText(this, "please enter kml file url", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this@ChoseKmlTypeActivity, KmlViewerActivity::class.java)
                intent.putExtra("path", editText.text.toString())
                startActivity(intent)
            }
        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


    private fun streetViewBannerAdsSmall() {
        val billingHelper = StreetViewAppSoniBillingHelper(this)
        val adContainer: LinearLayout = binding.bannerAdPlace.adContainer
        val adView = AdView(this)
        adView.setAdSize(StreetViewAppSoniMyAppAds.getAdSize(this))
        adView.adUnitId = StreetViewAppSoniMyAppAds.banner_admob_inApp

        if (billingHelper.isNotAdPurchased) {
            StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediation(
                adContainer, adView, this
            )
        } else {
            binding.bannerID.visibility = View.GONE

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback.remove()
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks(
                "ChoseKmlTypeActivityEXit",
                this@ChoseKmlTypeActivity
            )
            mediationBackPressedSimpleStreetViewLocation(
                this@ChoseKmlTypeActivity,
                StreetViewAppSoniMyAppAds.admobInterstitialAd,
                binding.whiteView
            )

        }
    }
}