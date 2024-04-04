package com.live.streetview.navigation.earthmap.compass.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.multidex.BuildConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.live.streetview.navigation.earthmap.compass.map.Ads.AdmobCollapsiveAd
import com.live.streetview.navigation.earthmap.compass.map.Ads.BannerCallBack
import com.live.streetview.navigation.earthmap.compass.map.Ads.CollapsibleType
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniPurchaseHelper
import com.live.streetview.navigation.earthmap.compass.map.Model.CountryNameModel
import com.live.streetview.navigation.earthmap.compass.map.Model.ToolModel
import com.live.streetview.navigation.earthmap.compass.map.activities.AltimeterActivity
import com.live.streetview.navigation.earthmap.compass.map.activities.ChoseKmlTypeActivity
import com.live.streetview.navigation.earthmap.compass.map.activities.CountryActivities
import com.live.streetview.navigation.earthmap.compass.map.activities.CountryInfoActivity
import com.live.streetview.navigation.earthmap.compass.map.activities.DistanceCalculatorActivity
import com.live.streetview.navigation.earthmap.compass.map.activities.EarthMap2DActivity
import com.live.streetview.navigation.earthmap.compass.map.activities.FMActivity
import com.live.streetview.navigation.earthmap.compass.map.activities.FuelCalculatorActivity
import com.live.streetview.navigation.earthmap.compass.map.activities.ISDCodeActivity
import com.live.streetview.navigation.earthmap.compass.map.activities.LiveEarthMapActivity
import com.live.streetview.navigation.earthmap.compass.map.activities.NearByActivity
import com.live.streetview.navigation.earthmap.compass.map.activities.NewAreaCalculatorActivity
import com.live.streetview.navigation.earthmap.compass.map.activities.NewCompassActivity
import com.live.streetview.navigation.earthmap.compass.map.activities.SpeedMeterActivity
import com.live.streetview.navigation.earthmap.compass.map.activities.WeatherActivity
import com.live.streetview.navigation.earthmap.compass.map.activities.WebcamActivity
import com.live.streetview.navigation.earthmap.compass.map.activities.callack.RadioClickCallBack
import com.live.streetview.navigation.earthmap.compass.map.activities.callack.toolsCallback
import com.live.streetview.navigation.earthmap.compass.map.activities.utils.GPSONOFF
import com.live.streetview.navigation.earthmap.compass.map.activities.utils.JsonConverter
import com.live.streetview.navigation.earthmap.compass.map.activities.utils.SharedPreferencesHelper
import com.live.streetview.navigation.earthmap.compass.map.activities.utils.UtilityClass
import com.live.streetview.navigation.earthmap.compass.map.adapters.HorizentalAdapter
import com.live.streetview.navigation.earthmap.compass.map.adapters.ToolAdapter
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityMainBinding
import com.live.streetview.navigation.earthmap.compass.map.fragments.FinishbottomSheetFragment
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.FuelManagerActivity
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.StartFuelManagerActivity
import org.json.JSONArray
import org.json.JSONException

class MainActivity : AppCompatActivity(), RadioClickCallBack, Animation.AnimationListener,
    toolsCallback {
    var drawerLayout: DrawerLayout? = null
    var recyclerViewHorizental: RecyclerView? = null
    var recyclerViewtoo: RecyclerView? = null
    var dialog: Dialog? = null
    var fueldialog: Dialog? = null
    var card_dialog: CardView? = null
    var dialogDismiss: ImageView? = null
    var animBlink: Animation? = null

    var countryNameModelArrayList: ArrayList<CountryNameModel>? = null
    var horizentalAdapter: HorizentalAdapter? = null
    var constraintLayoutstreetViewMap: ConstraintLayout? = null
    var constraintLayoutgps: ConstraintLayout? = null
    var constraintLayoutEarthMap3D: ConstraintLayout? = null
    var constraintLayoutnearby: ConstraintLayout? = null
    var constraintLayoutliveearthmap: ConstraintLayout? = null
    var constraintLayoutwebcam: ConstraintLayout? = null
    var constraintLayoutspeed: ConstraintLayout? = null
    var constraintHomeLayout: ConstraintLayout? = null
    var constraintLayoutWeather: ConstraintLayout? = null
    var constraintLayoutFm: ConstraintLayout? = null
    var constraintLayoutHomeGps: ConstraintLayout? = null
    var constraintLayoutShare: ConstraintLayout? = null
    var constraintLayoutRate: ConstraintLayout? = null
    var constraintLayoutTerms: ConstraintLayout? = null
    var constraintLayoutPremium: ConstraintLayout? = null
    var imagedraw: ImageView? = null
    var preferences: SharedPreferences? = null
    var bindingmain: ActivityMainBinding? = null
    var omarStreetViewPurchaseHelper: StreetViewAppSoniPurchaseHelper? = null
    private val admobBannerAds by lazy { AdmobCollapsiveAd() }

    // OmarStreetViewAppPurchaseHelper omarStreetViewAppPurchaseHelper;
    private var modelClasses: ArrayList<ToolModel>? = null
    private var toolAdapter: ToolAdapter? = null
    var sharedPreferencesHelper: SharedPreferencesHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindingmain = ActivityMainBinding.inflate(
            layoutInflater
        )

        setContentView(bindingmain!!.root)
        sharedPreferencesHelper = SharedPreferencesHelper(this)
        // rewardedadshow = new MyRewardedAd();
        preferences = getSharedPreferences(packageName, MODE_PRIVATE)
        UtilityClass.MAPBOX_KEY = preferences!!.getString(
            "Map_key",
            "pk.eyJ1Ijoia2l0ZWNobm9sb2dpZXMiLCJhIjoiY2wzeTNvaHM0MGI5NTNya2ZnbnV6NHdybCJ9.J1kDZR1I7lLBnb5bl49hvw"
        ).toString()
        omarStreetViewPurchaseHelper = StreetViewAppSoniPurchaseHelper(this)
        logAnalyticsForClicks("StreetViewMainScreenOnCreate", this)
        dialog = Dialog(this)
        dialog!!.setContentView(R.layout.premium_dialog)
        dialog?.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);

        val headingText = dialog!!.findViewById<TextView>(R.id.heading_txt_premium)
        var submit = dialog!!.findViewById<Button>(R.id.submit_btn)
        submit.setOnClickListener {
            dialog?.dismiss()
            omarStreetViewPurchaseHelper!!.purchaseLiveEarthLocationAdsPackage()
        }


        card_dialog = dialog!!.findViewById(R.id.cd_dialog)
        dialogDismiss = dialog!!.findViewById(R.id.dialogDismiss)
        val firstWord = getColoredSpanned("Get", "#332626")
        val secondWord = getColoredSpanned("Premium", "#2D44B5")
        val thirdWord = getColoredSpanned("Feature", "#332626")
        headingText.text = Html.fromHtml("$firstWord $secondWord $thirdWord")
        dialog!!.show();
        val obj = GPSONOFF()
        obj.gpsONOFF(this)
        iniAlMainId()
        askNotificationPermission()

        loadCollapseBanner()
        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce_animation)
        bounceAnimation.setAnimationListener(this)
        animBlink = AnimationUtils.loadAnimation(
            this, R.anim.blink
        )
        animBlink?.setAnimationListener(this)
        //        if (FloatingBubbleService.isRunning()) {
//            bindingmain.checkMenu.setChecked(true);
//
//        } else {
//            showActionDialog(MainActivity.this);
//        }
//        bindingmain.checkMenu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (!Settings.canDrawOverlays(MainActivity.this)) {
//                        requestSystemAlertWindowPermission();
//                    } else {
//                        Intent intent = new Intent(MainActivity.this, MyServiceKt.class);
//                        if (isChecked) {
//                            if (!FloatingBubbleService.isRunning()) {
//                                startMyService(intent);
//                            }
//
//                        } else {
//                            if (FloatingBubbleService.isRunning()) {
//                                stopMyService(intent);
//                            }
//                        }
//                    }
//                } else {
//                    Intent intent = new Intent(MainActivity.this, MyServiceKt.class);
//                    if (isChecked) {
//                        if (!FloatingBubbleService.isRunning()) {
//                            startMyService(intent);
//                        }
//                    } else {
//                        if (FloatingBubbleService.isRunning()) {
//                            stopMyService(intent);
//                        }
//                    }
//                }
//
//            }
//        });
        bindingmain!!.animationView.setOnClickListener { dialog!!.show() }
        dialogDismiss?.setOnClickListener(View.OnClickListener { dialog!!.dismiss() })
        imagedraw!!.setOnClickListener {
            try {
//                    if (FloatingBubbleService.isRunning()) {
//                        bindingmain.checkMenu.setChecked(true);
//                    } else {
//                        bindingmain.checkMenu.setChecked(false);
//                    }
                if (!drawerLayout!!.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout!!.open()
                } else {
                    drawerLayout!!.close()
                }
            } catch (e: Exception) {
            }
        }
        fueldialog = Dialog(this)
        fueldialog!!.setContentView(R.layout.fuel_manager_dialog)
        if (sharedPreferencesHelper!!.getString(
                "PRODUCT_YEARLY", ""
            ) == "PRODUCT_YEARLY" || sharedPreferencesHelper!!.getString(
                "PRODUCT_MONTHLY", ""
            ) == "PRODUCT_MONTHLY"
        ) {
            fueldialog!!.dismiss()
        } else {
            //  fueldialog!!.show()
        }
        fueldialog!!.findViewById<View>(R.id.dialogDismiss).setOnClickListener {
            fueldialog!!.dismiss()
            dialog!!.show()
        }
        fueldialog!!.findViewById<View>(R.id.cd_dialog)
            .setOnClickListener { //                StreetViewAppSoniBillingHelper billingHelper = new StreetViewAppSoniBillingHelper(MainActivity.this);
                if (sharedPreferencesHelper!!.getString(
                        "PRODUCT_YEARLY", ""
                    ) == "PRODUCT_YEARLY" || sharedPreferencesHelper!!.getString(
                        "PRODUCT_MONTHLY", ""
                    ) == "PRODUCT_MONTHLY"
                ) {
                    startActivity(Intent(this@MainActivity, FuelManagerActivity::class.java))
                    Log.d("BillingLogger", "onClick: StartFuelManagerActivity")
                } else {
                    startActivity(Intent(this@MainActivity, StartFuelManagerActivity::class.java))
                    Log.d("BillingLogger", "onClick: FuelManagerActivity")
                }
            }
        modelClasses = ArrayList()
        recyclerViewtoo!!.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        toolAdapter =
            ToolAdapter(this@MainActivity, modelClasses!!, this@MainActivity, bindingmain!!.whiteView)
        recyclerViewtoo!!.adapter = toolAdapter
        toolData
/*        bindingmain!!.cardFuelManager.setOnClickListener { //                Intent intent = new Intent(MainActivity.this, StartFuelManagerActivity.class);
//                startActivity(intent);
            *//*  if (sharedPreferencesHelper!!.getString(
                      "PRODUCT_YEARLY", ""
                  ) == "PRODUCT_YEARLY" || sharedPreferencesHelper!!.getString(
                      "PRODUCT_MONTHLY", ""
                  ) == "PRODUCT_MONTHLY"
              ) {*//*
            val intent = (Intent(this@MainActivity, FuelManagerActivity::class.java))
            meidationForClickSimpleSmartToolsLocation(
                this@MainActivity,
                StreetViewAppSoniMyAppAds.admobInterstitialAd,
                intent,
                bindingmain!!.whiteView
            )
            Log.d("BillingLogger", "cardFuelManager: StartFuelManagerActivity")
            *//* } else {
                 startActivity(Intent(this@MainActivity, StartFuelManagerActivity::class.java))
                 Log.d("BillingLogger", "cardFuelManager: FuelManagerActivity")
             }*//*
        }*/
        constraintLayoutstreetViewMap!!.setOnClickListener {
            Log.d("TAG", "onClick: " + StreetViewAppSoniMyAppAds.admobInterstitialAd)
            logAnalyticsForClicks("StreetViewMainScreenOnClickStreetView", this@MainActivity)
            val intent = Intent(this@MainActivity, CountryActivities::class.java)
            meidationForClickSimpleSmartToolsLocation(
                this@MainActivity,
                StreetViewAppSoniMyAppAds.admobInterstitialAd,
                intent,
                bindingmain!!.whiteView
            )
            //                StreetViewAppSoniMyAppShowAds.setFirstShow(false);
//                startActivity(intent);
        }

        bindingmain!!.constraintGps.setOnClickListener {
            //
            if (!checkPermission()) {
                requestPermission()
            } else {
//                    StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                logAnalyticsForClicks("StreetViewMainScreenOnClickGPsNavigation", this@MainActivity)
                val intent = Intent(this@MainActivity, GPSTESTINGActivity2::class.java)
                intent.putExtra("data", "main")
                meidationForClickSimpleSmartToolsLocation(
                    this@MainActivity,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,
                    intent,
                    bindingmain!!.whiteView
                )

//                    Snackbar.make(view, "Permission already granted.", Snackbar.LENGTH_LONG).show();
            }
        }
        bindingmain!!.constraintEarthmap3D.setOnClickListener {
            if (!checkPermission()) {
                requestPermission()
            } else {
                logAnalyticsForClicks(
                    "StreetViewMainScreenOnClickLiveEarthMapActivity", this@MainActivity
                )
                val intent = Intent(this@MainActivity, LiveEarthMapActivity::class.java)
                meidationForClickSimpleSmartToolsLocation(
                    this@MainActivity,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,

                    intent,
                    bindingmain!!.whiteView
                )
                //                    StreetViewAppSoniMyAppShowAds.setFirstShow(false);
            }
        }
        bindingmain!!.constraintNearByPlaces.setOnClickListener {
            if (!checkPermission()) {
                requestPermission()
            } else {
//                    StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                logAnalyticsForClicks(
                    "StreetViewMainScreenOnClickChoseKmlTypeActivity", this@MainActivity
                )
                val intent = Intent(this@MainActivity, ChoseKmlTypeActivity::class.java)
                meidationForClickSimpleSmartToolsLocation(
                    this@MainActivity,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,

                    intent,
                    bindingmain!!.whiteView
                )
                //                    Snackbar.make(view, "Permission already granted.", Snackbar.LENGTH_LONG).show();
            }
        }
        constraintLayoutliveearthmap!!.setOnClickListener {
            if (!checkPermission()) {
                requestPermission()
            } else {
//                    StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                logAnalyticsForClicks(
                    "StreetViewMainScreenOnClickISDCodeActivity", this@MainActivity
                )
                val intent = Intent(this@MainActivity, ISDCodeActivity::class.java)
                //                    MyRewardedAd.Companion.rewardedAdShow(MainActivity.this, intent);
//                    startActivity(intent);
                meidationForClickSimpleSmartToolsLocation(
                    this@MainActivity,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,

                    intent,
                    bindingmain!!.whiteView
                )
            }
        }

        constraintLayoutwebcam!!.setOnClickListener { //                StreetViewAppSoniMyAppShowAds.setFirstShow(false);
            if(!checkPermission()){
                requestPermission()
            }else{
                logAnalyticsForClicks("StreetViewMainScreenOnClickNearByActivity", this@MainActivity)
                val intent = Intent(this@MainActivity, NearByActivity::class.java)
                meidationForClickSimpleSmartToolsLocation(
                    this@MainActivity,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,

                    intent,
                    bindingmain!!.whiteView
                )
            }
        }
        constraintLayoutspeed!!.setOnClickListener {
            if (!checkPermission()) {
                requestPermission()
            } else {
//                    StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                logAnalyticsForClicks(
                    "StreetViewMainScreenOnClickVoiceNavigationActivity", this@MainActivity
                )
                val intent = Intent(this@MainActivity, GPSTESTINGActivity2::class.java)
                intent.putExtra("data", "voice")
                meidationForClickSimpleSmartToolsLocation(
                    this@MainActivity,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,
                    intent,
                    bindingmain!!.whiteView
                )
            }
        }
       /* constraintLayoutWeather!!.setOnClickListener {
            if (!checkPermission()) {
                requestPermission()
            } else {
//                    StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                logAnalyticsForClicks("StreetViewMainScreenOnClickWeather", this@MainActivity)
                val intent = Intent(this@MainActivity, WeatherActivity::class.java)
                meidationForClickSimpleSmartToolsLocation(
                    this@MainActivity,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,
                    intent,
                    bindingmain!!.whiteView
                )
            }
        }*/
        constraintLayoutFm!!.setOnClickListener {
            if (!checkPermission()) {
                requestPermission()
            } else {
//                    StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                logAnalyticsForClicks(
                    "StreetViewMainScreenOnClickDistanceCalculatorActivity", this@MainActivity
                )
                val intent = Intent(this@MainActivity, DistanceCalculatorActivity::class.java)
                meidationForClickSimpleSmartToolsLocation(
                    this@MainActivity,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,

                    intent,
                    bindingmain!!.whiteView
                )
            }
        }
        constraintHomeLayout!!.setOnClickListener {
            try {
                drawerLayout!!.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        constraintLayoutHomeGps!!.setOnClickListener {
            drawerLayout!!.close()
            //                StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                //
                if (!checkPermission()) {
                    requestPermission()
                } else {
//                    StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                    logAnalyticsForClicks("StreetViewMainScreenOnClickGPsNavigation", this@MainActivity)
                    val intent = Intent(this@MainActivity, GPSTESTINGActivity2::class.java)
                    intent.putExtra("data", "main")
                    meidationForClickSimpleSmartToolsLocation(
                        this@MainActivity,
                        StreetViewAppSoniMyAppAds.admobInterstitialAd,
                        intent,
                        bindingmain!!.whiteView
                    )

//                    Snackbar.make(view, "Permission already granted.", Snackbar.LENGTH_LONG).show();
                }

        }
        constraintLayoutRate!!.setOnClickListener { rateMeDialog() }
        constraintLayoutTerms!!.setOnClickListener {
            try {
                val theurl = "https://3dearthstreet.blogspot.com/2022/07/3d-earth-map.html"
                val urlstr = Uri.parse(theurl)
                val urlintent = Intent()
                urlintent.data = urlstr
                urlintent.action = Intent.ACTION_VIEW
                startActivity(urlintent)
                drawerLayout!!.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        constraintLayoutShare!!.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
            )
            sendIntent.type = "text/plain"
            startActivity(sendIntent)
            try {
                drawerLayout!!.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        constraintLayoutPremium = findViewById(R.id.constraintLayoutPremium)
        constraintLayoutPremium?.setOnClickListener(View.OnClickListener {
            drawerLayout!!.close()
            omarStreetViewPurchaseHelper!!.purchaseLiveEarthLocationAdsPackage()
            // Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
        })
        card_dialog?.setOnClickListener(View.OnClickListener {
            val billingHelper = StreetViewAppSoniBillingHelper(this@MainActivity)
            if (billingHelper.isNotAdPurchased) {
                omarStreetViewPurchaseHelper!!.purchaseLiveEarthLocationAdsPackage()
                dialog!!.dismiss()
            }
        })
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout!!.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout!!.close()
                }
                val dialogBuilder = AlertDialog.Builder(this@MainActivity)
                // ...Irrelevant code for customizing the buttons and title
                val inflater = this@MainActivity.layoutInflater
                val dialogView = inflater.inflate(R.layout.exit_dialouge, null)
                dialogBuilder.setView(dialogView)
                val dialog = dialogBuilder.create()
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val yesBtn = dialogView.findViewById<ImageView>(R.id.exitYesButton)
                val no = dialogView.findViewById<ImageView>(R.id.exitNoButton)

                dialog.show()

                yesBtn?.setOnClickListener {
                    finishAffinity()
                }
                no?.setOnClickListener {
                    dialog.dismiss()
                }


            }
        })
    }

    override fun onResume() {
        super.onResume()
        countryNameModelArrayList = ArrayList()
        countryDataflag
    }

    private fun loadCollapseBanner() {
        val billingHelper = this.let { StreetViewAppSoniBillingHelper(it) }
        admobBannerAds.loadCollapseBannerAds(this,
            bindingmain!!.bannerID,
            StreetViewAppSoniMyAppAds.admob_collaspable_banner_id,
            1,// default enable vale i kept 1
            billingHelper.isNotAdPurchased,
            CollapsibleType.BOTTOM,
            isConnectedToNetwork(this),
            object : BannerCallBack {
                override fun onAdFailedToLoad(adError: String) {}
                override fun onAdLoaded() {}
                override fun onAdImpression() {}
                override fun onPreloaded() {}
                override fun onAdClicked() {}
                override fun onAdSwipeGestureClicked() {}
                override fun onAdClosed() {

                }

                override fun onAdOpened() {
                }
            })
    }

    fun isConnectedToNetwork(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private val countryDataflag: Unit
        private get() {
            try {
                val response = JsonConverter.getJsonFromAssets(this, "countryName.json")
                val jsonArray = JSONArray(response)
                for (i in 0 until jsonArray.length()) {
                    val jsonObject1 = jsonArray.getJSONObject(i)
                    val nameCountry = jsonObject1.getString("Name")
                    Log.d("msg", "getCountryData: $nameCountry")
                    // val mCountry = CountryNameModel(nameCountry)
                    //  mCountry.textcountryname = nameCountry
                    countryNameModelArrayList?.add(CountryNameModel(nameCountry))

                }
            } catch (e: JSONException) {
                Log.d("msg", "msg: " + e.message)
            }
            if (countryNameModelArrayList!!.size > 0) {
                try {
                    recyclerViewHorizental!!.layoutManager =
                        LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
                    horizentalAdapter =
                        HorizentalAdapter(this, countryNameModelArrayList!!, bindingmain!!.whiteView)
                    recyclerViewHorizental!!.adapter = horizentalAdapter
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    private fun getColoredSpanned(text: String, color: String): String? {
        return "<font color=$color>$text</font>"
    }
    private fun iniAlMainId() {
        constraintHomeLayout = findViewById(R.id.constraintHomeLayout)
        recyclerViewHorizental = findViewById(R.id.horizentalrecy)
        recyclerViewtoo = findViewById(R.id.toolrecyclerview)
        drawerLayout = findViewById(R.id.drwer)
        imagedraw = findViewById(R.id.imageCircle)
        constraintLayoutstreetViewMap = findViewById(R.id.constraintHos)
        constraintLayoutHomeGps = findViewById(R.id.constraintHomeGps)
        constraintLayoutFm = findViewById(R.id.constrainFM)
        constraintLayoutWeather = findViewById(R.id.constraintWeather)
        constraintLayoutspeed = findViewById(R.id.conspeedmeter)
        constraintLayoutwebcam = findViewById(R.id.constraintWebcam)
        constraintLayoutliveearthmap = findViewById(R.id.constraintLiveEarthMap)
        constraintLayoutnearby = findViewById(R.id.constraintNearByPlaces)
        constraintLayoutEarthMap3D = findViewById(R.id.constraintEarthmap3D)
        constraintLayoutgps = findViewById(R.id.constraintGps)
        constraintLayoutRate = findViewById(R.id.constraintLayoutRateUs)
        constraintLayoutShare = findViewById(R.id.constraintLayoutShare)
        constraintLayoutTerms = findViewById(R.id.terms)
    }

    // custom dialogue
    fun rateMeDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        // ...Irrelevant code for customizing the buttons and title
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.rateus_layout, null)
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        // Button btnExist=dialog.findViewById(R.id.btnrateExist);
        val btnSubmitt = dialog.findViewById<Button>(R.id.btnrateCancel)
        val ratingBar = dialog.findViewById<RatingBar>(R.id.ratingBar2)
       // val btnclose = dialog.findViewById<ImageView>(R.id.btnClose)
      //  btnclose!!.setOnClickListener { dialog.dismiss() }
        btnSubmitt!!.setOnClickListener {
            dialog.dismiss()
            val stars = ratingBar!!.rating
            Log.d("stars", "stars: $stars")
            if (stars == 0f) {
//                    Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
            } else if (stars > 3) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + "com.live.streetview.navigation.earthmap.compass.map")
                    )
                )
            } else {
                Toast.makeText(this@MainActivity, "Thanks for the feedback", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        dialog.show()
        drawerLayout!!.close()
    }

    private val toolData: Unit
        private get() {
            val list = ToolModel(R.drawable.fuel_calculator, "Fuel  \nCalculator")
            val list9 = ToolModel(R.drawable.live_earth_map, "Live Earth  \n2D Map")
            val list1 = ToolModel(R.drawable.area_calculator, "Area  \nCalculator")
            //        ToolModel list2 = new ToolModel(R.drawable.chatbot, "ChatBot");
            val list3 = ToolModel(R.drawable.fm_radio, "Fm\nRadio")
            val list4 = ToolModel(R.drawable.country_info, "Country  \nInfo")
            val list5 = ToolModel(R.drawable.altimeter, "Altimeter")
            val list6 = ToolModel(R.drawable.compass, "Compass")
            val list7 = ToolModel(R.drawable.live_webcam, "WebCam")
            val list8 = ToolModel(R.drawable.speedometer, "SpeedoMeter")
            //        ToolModel list10 = new ToolModel(R.drawable.iso_codes, "Iso Code");
            modelClasses!!.add(list9)
            modelClasses!!.add(list7)
            modelClasses!!.add(list1)
            modelClasses!!.add(list)
            //        modelClasses.add(list2);
            modelClasses!!.add(list3)
            modelClasses!!.add(list4)
            modelClasses!!.add(list5)
            modelClasses!!.add(list6)
            modelClasses!!.add(list8)
            //        modelClasses.add(list10);
        }

    private fun checkPermission(): Boolean {
        val result1 = ContextCompat.checkSelfPermission(
            applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
        )
        return result1 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            ), PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.size > 0) {
                val locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                //                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (locationAccepted) {
//                        Toast.makeText(this, "Permission Granted, Now you can access camera.", Toast.LENGTH_SHORT).show();
                    Log.d("msg", "Permission Granted, Now you can access camera.")
                } else {
                    Toast.makeText(
                        this, "Permission Denied, You cannot access camera.", Toast.LENGTH_SHORT
                    ).show()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                            showMessageOKCancel(
                                "You need to allow access to the permission"
                            ) { dialog, which ->
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        ), PERMISSION_REQUEST_CODE
                                    )
                                }
                            }
                            return
                        } else {
                            AlertDialog.Builder(this@MainActivity)
                                .setMessage("You have denied some permissions to the app. Please allow all permissions at [Setting] > [Permissions] screen")
                                .setPositiveButton("OK") { dialogInterface, i ->
                                    dialogInterface.dismiss()
                                    // Go to app settings
                                    val intent = Intent(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", packageName, null)
                                    )
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                }.setNegativeButton("No, Exit app") { dialogInterface, i ->
                                    dialogInterface.dismiss()
                                    finishAffinity()
                                }.create().show()
                        }
                    }
                }
            }
        }
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this@MainActivity).setMessage(message)
            .setPositiveButton("OK", okListener).setNegativeButton("Cancel", null).create().show()
    }

    override fun onClickListener(pos: Int) {}
    override fun callBackForPermissionChecking(type: String?) {
            if (!checkPermission()) {
                requestPermission()
            } else {
                when (type) {
                    "compass" -> {


//                    StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                        logAnalyticsForClicks(
                            "StreetViewMainScreenOnClickNewCompassActivity", this@MainActivity
                        )
                        val intent2 = Intent(this@MainActivity, NewCompassActivity::class.java)
                        meidationForClickSimpleSmartToolsLocation(
                            this@MainActivity,
                            StreetViewAppSoniMyAppAds.admobInterstitialAd,

                            intent2,
                            bindingmain!!.whiteView
                        )
                    }

                    "areaCalculator" -> {

//                    StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                        logAnalyticsForClicks(
                            "StreetViewMainScreenOnClickAreaCalculator", this@MainActivity
                        )
                        val intent3 = Intent(this@MainActivity, NewAreaCalculatorActivity::class.java)
                        meidationForClickSimpleSmartToolsLocation(
                            this@MainActivity,
                            StreetViewAppSoniMyAppAds.admobInterstitialAd,

                            intent3,
                            bindingmain!!.whiteView
                        )
                    }

                    "distancaCall" -> {

//                    StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                        logAnalyticsForClicks(
                            "StreetViewMainScreenOnClickDistanceCalculator", this@MainActivity
                        )
                        val intent4 = Intent(this@MainActivity, DistanceCalculatorActivity::class.java)
                        meidationForClickSimpleSmartToolsLocation(
                            this@MainActivity,
                            StreetViewAppSoniMyAppAds.admobInterstitialAd,

                            intent4,
                            bindingmain!!.whiteView
                        )
                    }
                }
            }

    }



    //    private void stopMyService(Intent intent) {
    //        stopService(intent);
    //        bindingmain.checkMenu.setChecked(false);
    //        Toast.makeText(this, "stop action menu", Toast.LENGTH_SHORT).show();
    //    }
    //    private void startMyService(Intent intent) {
    //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    //            startForegroundService(intent);
    //        } else {
    //            startService(intent);
    //        }
    //        bindingmain.checkMenu.setChecked(true);
    //    }
    fun requestSystemAlertWindowPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.fromParts(
                        "package", packageName, null
                    )
                )
                startActivityForResult(intent, 101)
            }
        }
    }

    //    @Override
    //    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    //        super.onActivityResult(requestCode, resultCode, data);
    //        if (requestCode == 101) {
    //            if (bindingmain.checkMenu.isChecked()) {
    //                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    //                    if (!Settings.canDrawOverlays(MainActivity.this)) {
    //                        bindingmain.checkMenu.setChecked(false);
    //                    } else {
    //                        Intent intent = new Intent(MainActivity.this, MyServiceKt.class);
    //                        startMyService(intent);
    //                    }
    //                }
    //            }
    //        }
    //    }
    fun showActionDialog(activity: Activity?) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.premium_dialog)
        val dialogButton = dialog.findViewById<View>(R.id.cd_dialog) as CardView
        //        CardView cancel = (CardView) dialog.findViewById(R.id.cancel);
        dialogButton.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private val requestPermissionLauncher =
        registerForActivityResult<String, Boolean>(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Snackbar.make(
                    (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0),
                    getString(R.string.notification_permission),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

    private fun askNotificationPermission() {
        // This is only necessary for API Level > 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.d("TAG", "askNotificationPermission: permission is granted")
                // FCM SDK (and your app) can post notifications.
            } else {
                // Directly ask for the permission
                Log.d("TAG", "askNotificationPermission: Directly ask for permission")
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onAnimationStart(animation: Animation) {}
    override fun onAnimationEnd(animation: Animation) {}
    override fun onAnimationRepeat(animation: Animation) {}
    override fun onItemClick(position: Int) {
        when (position) {
            0 -> {
                if (!checkPermission()) {
                    requestPermission()
                } else {
//                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                    logAnalyticsForClicks("StreetViewMainScreenOnClickEarthMap2D", this)
                    //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                    val intent = Intent(this, EarthMap2DActivity::class.java)
                    meidationForClickSimpleSmartToolsLocation(
                        this,
                        StreetViewAppSoniMyAppAds.admobInterstitialAd,

                        intent,
                        bindingmain!!.whiteView
                    )
                }
            }

            1 -> {

//                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                logAnalyticsForClicks("StreetViewMainScreenOnWebcamActivity", this)
                val intent = Intent(this, WebcamActivity::class.java)
                meidationForClickSimpleSmartToolsLocation(
                    this,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,

                    intent,
                    bindingmain!!.whiteView
                )
            }

            2 -> {

//                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                callBackForPermissionChecking("areaCalculator")
            }

            3 -> {

//                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                logAnalyticsForClicks("StreetViewMainScreenOnClickFuelCalculatorActivity", this)
                val intent = Intent(this, FuelCalculatorActivity::class.java)
                meidationForClickSimpleSmartToolsLocation(
                    this,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,
                    intent,
                    bindingmain!!.whiteView
                )
            }

            4 -> {

//                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                logAnalyticsForClicks("StreetViewMainScreenOnClickFMActivity", this)
                val intent2 = Intent(this, FMActivity::class.java)
                meidationForClickSimpleSmartToolsLocation(
                    this,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,

                    intent2,
                    bindingmain!!.whiteView
                )
            }

            5 -> {

//                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                logAnalyticsForClicks("StreetViewMainScreenOnClickCountryInfoActivity", this)
                val intent3 = Intent(this, CountryInfoActivity::class.java)
                meidationForClickSimpleSmartToolsLocation(
                    this,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,

                    intent3,
                    bindingmain!!.whiteView
                )
            }

            6 -> {
                    if (!checkPermission()){
                        requestPermission()
                    }else{
                        logAnalyticsForClicks("StreetViewMainScreenOnClickAltimeter", this)
                        val intent3 = Intent(this, AltimeterActivity::class.java)
                        meidationForClickSimpleSmartToolsLocation(
                            this,
                            StreetViewAppSoniMyAppAds.admobInterstitialAd,

                            intent3,
                            bindingmain!!.whiteView
                        )
                    }
//                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);

            }

            7 -> {

                /*
                        radioClickCallBack.callBackForPermissionChecking("compass");
*/
//                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                logAnalyticsForClicks("StreetViewMainScreenOnClickNewCompassActivity", this)
                val intent4 = Intent(this, NewCompassActivity::class.java)
                meidationForClickSimpleSmartToolsLocation(
                    this,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,

                    intent4,
                    bindingmain!!.whiteView
                )
            }

            8 -> {

//                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                logAnalyticsForClicks("StreetViewMainScreenOnClickSpeedMeterActivity", this)
                val intent5 = Intent(this, SpeedMeterActivity::class.java)
                meidationForClickSimpleSmartToolsLocation(
                    this,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,

                    intent5,
                    bindingmain!!.whiteView
                )
            }

            else -> {}
        }
    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 200
    }
}