package com.live.streetview.navigation.earthmap.compass.map.CountryInfoScreen.Activity


import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.SuperscriptSpan
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.gps.navigation.streetview.earthmap.satelliteview.compass.pro.CountryInfoScreen.Utils.MyContext
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityCountryDetailsBinding
import java.util.Locale


class CountryDetailsStreetView : AppCompatActivity() {
    private var flagString: String? = null
    private var progressBarFlag: ProgressBar? = null
    private var progressBarMainFlag: ProgressBar? = null
    private var binding: ActivityCountryDetailsBinding? = null
    private var countryName: TextView? = null
    private var flagTxt: TextView? = null
    private var countryCode: TextView? = null
    private var population: TextView? = null
    private var area: TextView? = null
    private var timezone: TextView? = null
    private var currency: TextView? = null
    private var language: TextView? = null
    private var capital: TextView? = null
    private var countryFlag: ImageView? = null
    private var countryFlagMain: ImageView? = null

    //private var streetViewPurchaseHelper: StreetViewPurchaseHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountryDetailsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        // streetViewPurchaseHelper = StreetViewPurchaseHelper(this)
        variableIni()
        setValues()
        //BannerAdsSmall()
    }


    private fun removeAds() {
        // streetViewPurchaseHelper!!.purchaseLiveEarthLocationAdsPackage();
    }

    /*    private fun BannerAdsSmall() {
            val billingHelper =
                StreetViewBillingHelper(
                    this
                )
            val adContainer = binding!!.smallAd.adContainer
            binding!!.smallAd.removeAds.setAnimation(R.raw.remove_ad)
            binding!!.smallAd.removeAds.setOnClickListener { v -> removeAds() }
            val adView = AdView(this)
            adView.adUnitId = StreetViewMyAppAds.banner_admob_inApp
            adView.setAdSize( StreetViewMyAppAds.getAdSize(this))
            if (billingHelper.isNotAdPurchased) {
                //     binding!!.smallAd.removeAds.visibility = View.VISIBLE
                StreetViewMyAppAds.loadStreetViewBannerForMainMediation(
                    adContainer, adView, this
                )
            }
        }*/

    private fun setValues() {
        countryName!!.text = (MyContext.countryName)
        flagTxt!!.text = ("Flag:")
        if (MyContext.countryFlag != null) {
            flagString = "https://flagpedia.net/data/flags/normal/" +
                    MyContext.countryFlag!!.lowercase(Locale.US) +
                    ".png"
            try {
                Glide.with(this).load(flagString).listener(object :
                    RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any,
                        target: Target<Drawable?>,
                        isFirstResource: Boolean,
                    ): Boolean {
                        progressBarFlag!!.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any,
                        target: Target<Drawable?>,
                        dataSource: DataSource,
                        isFirstResource: Boolean,
                    ): Boolean {
                        progressBarFlag!!.visibility = View.GONE
                        return false
                    }
                })
                    .into(countryFlag!!)
            } catch (e: Exception) {
            }
        }
        countryCode!!.text = ("+" + MyContext.countryCode)
        population!!.text = ("${MyContext.population} People/sq.km")
        val AREA_UNIT = SpannableString("Acre")
        AREA_UNIT.setSpan(SuperscriptSpan(), 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        area!!.text = (MyContext.area + " " + AREA_UNIT)
        timezone!!.text = (MyContext.timezone)
        currency!!.text = (MyContext.currency)
        language!!.text = (MyContext.language)
        capital!!.text = (MyContext.capital)
        try {
            Glide.with(this).load(flagString).listener(object :
                RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean,
                ): Boolean {
                    progressBarMainFlag!!.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean,
                ): Boolean {
                    progressBarMainFlag!!.visibility = View.GONE
                    return false
                }
            }).into(countryFlagMain!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun variableIni() {
        countryName = binding?.countryName
        flagTxt = binding?.countryFlagTxt
        countryCode = binding?.countryCode
        population = binding?.countryPopulation
        area = binding?.countryArea
        timezone = binding?.countryTimezone
        currency = binding?.countryCurrency
        language = binding?.countryLanguage
        capital = binding?.countryCapital
        countryFlag = binding?.countryFlag
        countryFlagMain = binding?.countryFlagMain
        progressBarFlag = binding?.progressBarFlag
        progressBarMainFlag = binding?.progressBarMainFlag
    }

    fun toCountryInfoScreen(view: View) {
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        /*        StreetViewMyAppShowAds.meidationBackPressedSimpleLiveEarthLocation(
                    this,
                    StreetViewMyAppAds.admobInterstitialAd
                    ,StreetViewMyAppAds.maxInterstitialAdInvitation
                )*/

    }
}