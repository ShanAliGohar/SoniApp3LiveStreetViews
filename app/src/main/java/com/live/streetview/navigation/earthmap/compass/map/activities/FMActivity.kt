package com.live.streetview.navigation.earthmap.compass.map.activities

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jean.jcplayer.model.JcAudio
import com.example.jean.jcplayer.view.JcPlayerView
import com.google.android.gms.ads.AdView
import com.google.android.gms.location.FusedLocationProviderClient
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds.getAdSize
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediation
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation
import com.live.streetview.navigation.earthmap.compass.map.FM.FMApiInterface
import com.live.streetview.navigation.earthmap.compass.map.FM.FMRetrofitInstance
import com.live.streetview.navigation.earthmap.compass.map.InfoRetrofitInstamce.RetrofitClientforInfoCountryData
import com.live.streetview.navigation.earthmap.compass.map.Model.FMModel
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.RetrofitInfoModel.AllCountryDataModl
import com.live.streetview.navigation.earthmap.compass.map.activities.callack.MyLocationCallBack
import com.live.streetview.navigation.earthmap.compass.map.activities.callack.RadioClickCallBack
import com.live.streetview.navigation.earthmap.compass.map.adapters.CustomSpinnerAdapterFlag
import com.live.streetview.navigation.earthmap.compass.map.adapters.CustomSpinnerAdapterFuel
import com.live.streetview.navigation.earthmap.compass.map.adapters.FMAdapter
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityFmactivityBinding
import com.live.streetview.navigation.earthmap.compass.map.fmmodel.CountryFMModelInformation
import com.streetview.liveearth.satellitemap.gpsnavigation.app.utilites.GeocoderCoroutineCloneTask1
import org.osmdroid.util.GeoPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FMActivity : AppCompatActivity(), RadioClickCallBack {
    private var bindingFm: ActivityFmactivityBinding? = null
    var recyclerView: RecyclerView? = null
    var fmAdapter: FMAdapter? = null
    var fmModelArrayList: ArrayList<FMModel>? = null
    var imageView: ImageView? = null
    var CountName1: String? = null
    var spinner: Spinner? = null
    var location: MyLocation? = null
    var jcAudioArrayList: ArrayList<JcAudio>? = null
    var progressBar: ProgressBar? = null
    var jcAudio: JcPlayerView? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    var userLoc: Location? = null
    var location12: Location? = null
    var user_name: String? = null
    var countryname = ArrayList<String?>()
    var countryAdapter: ArrayAdapter<*>? = null
    var customSpinnerAdapterFlag: CustomSpinnerAdapterFlag? = null
    var adapterflag: CustomSpinnerAdapterFuel? = null
    var countryFMModelList: ArrayList<CountryFMModelInformation?>? = null
    var allCountryDataModlArrayListf: ArrayList<AllCountryDataModl?>? = null
    private val callbackEnabled = true

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingFm = ActivityFmactivityBinding.inflate(
            layoutInflater
        )
        setContentView(bindingFm!!.root)
        recyclerView = findViewById(R.id.fmrecycyclerview)
        spinner = findViewById(R.id.spinnerflag)
        progressBar = findViewById(R.id.fmprogressbar)
        // imageView=findViewById(R.id.imagebuttonplay);
        jcAudio = findViewById(R.id.jcPlayerRadio)
        logAnalyticsForClicks("StreetViewFmActivityOnCreate", this)

        //call back back press
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        fusedLocationClient = FusedLocationProviderClient(this)
        userLoc = Location("service Provider")
        fusedLocationClient!!.lastLocation.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                location12 = task.result
                if (location12 == null) {
                    Toast.makeText(this@FMActivity, "Permission not granted", Toast.LENGTH_LONG)
                        .show()
                } else {
                    userLoc!!.latitude = location12!!.latitude
                    userLoc!!.longitude = location12!!.longitude
                    val latLng = GeoPoint(location12!!.latitude, location12!!.longitude)
                    //                        latLng.setLatitude(location12.getLatitude(),);
//                        latLng.setLongitude(location12.getLongitude());
                    val geocoderCoroutineCloneTask = GeocoderCoroutineCloneTask1(this@FMActivity,
                        latLng,
                        object : GeocoderCoroutineCloneTask1.GeoTaskCallback {
                            override fun onSuccessLocationFetched(fetchedAddress: String?) {
                                if (fetchedAddress != null) {
                                    user_name = fetchedAddress
                                    Log.d(TAG, "onSuccessLocationFetched: $user_name")
                                    getPakistandata(user_name)
                                }
                            }

                            override fun onFailedLocationFetched() {}
                        })
                    geocoderCoroutineCloneTask.execute()
                }
            }
        }
        //initBannerAdLiveFm();
        bindingFm!!.imagefm.setOnClickListener {
            logAnalyticsForClicks("StreetViewFmActivityOnBtnExit", this@FMActivity)
            mediationBackPressedSimpleStreetViewLocation(
                this@FMActivity,
                StreetViewAppSoniMyAppAds.admobInterstitialAd,
                bindingFm!!.whiteView
            )
        }
        jcAudioArrayList = ArrayList()
        countryFMModelList = ArrayList()
        location = MyLocation(this, object : MyLocationCallBack {
            override fun onLocationListener(location: Location?) {}
            override fun MyLocationListener(location: Location?) {}
        })
        fMCountries
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                val name = countryname[i]
                getPakistandata(name)
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
        streetViewBannerAdsSmall()
    }

    private fun streetViewBannerAdsSmall() {
        val billingHelper = StreetViewAppSoniBillingHelper(this)
        val adContainer = bindingFm!!.bannerAdPlace.adContainer
        val adView = AdView(this)
        adView.adUnitId = StreetViewAppSoniMyAppAds.banner_admob_inApp
        adView.setAdSize(getAdSize(this))
        if (billingHelper.isNotAdPurchased) {
            loadSmartToolsBannerForMainMediation(
                adContainer, adView, this
            )
        } else {
            bindingFm!!.bannerID.visibility = View.GONE
        }
    }

    private fun getJCAudioRadioPak(pos: Int) {
        try {
            jcAudioArrayList!!.clear()
            val size = countryFMModelList!!.size
            if (countryFMModelList!!.size > 0) {
                for (i in 0 until size - 1) {
                    // jcAudioArrayList.add(JcAudio.createFromURL(countryFMModelList[i].getName, countryFMModelList[i].urlResolved));
                    countryFMModelList!![i]?.name?.let {
                        countryFMModelList!![i]?.urlResolved?.let { it1 ->
                            JcAudio.createFromURL(
                                it, it1
                            )
                        }
                    }?.let {
                        jcAudioArrayList!!.add(
                            it
                        )
                    }
                }
                Log.d(TAG, "getJCAudio: ==jc=" + jcAudioArrayList!!.size + "===server=" + size)
            }

            //
            if (jcAudioArrayList!![pos].path != null && jcAudioArrayList!![pos].path !== "") {
                jcAudio!!.playAudio(jcAudioArrayList!![pos])
            }
            bindingFm!!.jcPlayerRadio.initPlaylist(jcAudioArrayList!!, null)
            //jcAudio.createNotification(R.drawable.airport);
        } catch (e: Exception) {
            Log.d(TAG, "Error " + e.localizedMessage)
        }
    }

    private fun getPakistandata(name: String?) {
        // these function used for to show all country channel
        // getJCAudio();
        val fmApiInterface = FMRetrofitInstance.retrofitInstance?.create(
            FMApiInterface::class.java
        )
        val cloudsCall = fmApiInterface?.getFM(name)
        cloudsCall?.enqueue(object : Callback<ArrayList<CountryFMModelInformation?>?> {
            override fun onResponse(
                call: Call<ArrayList<CountryFMModelInformation?>?>,
                response: Response<ArrayList<CountryFMModelInformation?>?>
            ) {
                if (response.isSuccessful) {
                    countryFMModelList = response.body()!!
                    if (countryFMModelList != null && !countryFMModelList!!.isEmpty()) {
                        progressBar!!.visibility = View.GONE
                        recyclerView!!.visibility = View.VISIBLE
                        recyclerView!!.layoutManager =
                            LinearLayoutManager(this@FMActivity, RecyclerView.HORIZONTAL, false)
                        fmAdapter = FMAdapter(this@FMActivity, countryFMModelList!!, this@FMActivity)
                        recyclerView!!.adapter = fmAdapter
                        getJCAudioRadioPak(-1)
                    } else {
                        Toast.makeText(this@FMActivity, "Empty field", Toast.LENGTH_SHORT).show()
                    }
                }


            }

            override fun onFailure(
                call: Call<ArrayList<CountryFMModelInformation?>?>, t: Throwable
            ) {
                TODO("Not yet implemented")
            }
        })
    }


    private val fMCountries: Unit
        private get() {
            // these function used for to show country in spinner
            val cloudsCall = RetrofitClientforInfoCountryData.instance?.myApi?.data
            cloudsCall?.enqueue(object : Callback<ArrayList<AllCountryDataModl?>?> {
                override fun onResponse(
                    call: Call<ArrayList<AllCountryDataModl?>?>,
                    response: Response<ArrayList<AllCountryDataModl?>?>
                ) {
                    if (response.isSuccessful) {
                        allCountryDataModlArrayListf = response.body()!!
                        // ArrayList<CountryFMModelInformation> modelCountries = response.body();
                        for (i in allCountryDataModlArrayListf!!.indices) {
                            countryname.add(allCountryDataModlArrayListf!![i]?.name)
                        }
                        if (user_name != null) {
                            for (i in countryname.indices) {
                                if (countryname[i]!!.contains(user_name!!)) {
                                    val firstindex = allCountryDataModlArrayListf!![0]?.name
                                    countryname[0] = user_name
                                    countryname[i] = firstindex
                                }
                            }
                        } else {
                            Toast.makeText(
                                this@FMActivity, "Location not found", Toast.LENGTH_SHORT
                            ).show()
                        }


//                    for (int i = 0; i < allCountryDataModlArrayListf.size(); i++) {
//                        CountName1 = allCountryDataModlArrayListf.get(i).getName();
//                        Log.d("TAG", "onResponse: " + CountName1);
//
//                    }

                        /*   adapterflag = new CustomSpinnerAdapterFlag(FMActivity.this,allCountryDataModlArrayListf);
                    spinner.setAdapter(adapterflag);*/
//                       adapterflag = new CustomSpinnerAdapterFuel(FMActivity.this,countryname);
//                    spinner.setAdapter(adapterflag);
                        countryAdapter = ArrayAdapter<String?>(
                            this@FMActivity, R.layout.custom_dropdown, countryname
                        )
                        countryAdapter?.setDropDownViewResource(R.layout.custom_dropdown)
                        bindingFm!!.spinnerflag.adapter = countryAdapter
                    }
                }

                override fun onFailure(call: Call<ArrayList<AllCountryDataModl?>?>, t: Throwable) {}
            })
        }

    override fun onClickListener(pos: Int) {
        getJCAudioRadioPak(pos)

        // jcAudio.initPlaylist(jcAudio);
        //jcAudio.in.createNotification(R.drawable.icon_radio);
    }

    override fun callBackForPermissionChecking(type: String?) {}

    /*    @Override
    public void onBackPressed() {
        if (jcAudio != null) {
            if (jcAudio.isPlaying()) {
                jcAudio.pause();
                jcAudio.kill();
            }
        }
        super.onBackPressed();

    }*/
    override fun onResume() {
        super.onResume()
        jcAudio!!.continueAudio()
    }

    override fun onPause() {
        super.onPause()
        if (jcAudio != null) {
            jcAudio!!.pause()
        }
    }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (jcAudio != null && jcAudio!!.isPlaying) {
                    jcAudio!!.pause()
                    jcAudio!!.kill()
                }
                logAnalyticsForClicks("StreetViewFmActivityOnExit", this@FMActivity)
                mediationBackPressedSimpleStreetViewLocation(
                    this@FMActivity,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,
                    bindingFm!!.whiteView
                )
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback.remove()
    }

    companion object {
        var TAG = "FMActivity"
    }
}
