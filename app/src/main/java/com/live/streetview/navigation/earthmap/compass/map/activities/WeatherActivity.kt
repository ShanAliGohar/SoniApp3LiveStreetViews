package com.live.streetview.navigation.earthmap.compass.map.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdView
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds.getAdSize
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediation
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation
import com.live.streetview.navigation.earthmap.compass.map.Model.WeatherHorizentalModel
import com.live.streetview.navigation.earthmap.compass.map.Model.WeatherVerticleModel
import com.live.streetview.navigation.earthmap.compass.map.Model.weatherModel.CustomWeatherModel
import com.live.streetview.navigation.earthmap.compass.map.Model.weatherModel.WeeklyWeatherData
import com.live.streetview.navigation.earthmap.compass.map.OpenWeatheData.CurrentWeatherData
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.activities.callack.MyLocationCallBack
import com.live.streetview.navigation.earthmap.compass.map.activities.utils.GPSONOFF
import com.live.streetview.navigation.earthmap.compass.map.adapters.WeatherHorizentalAdapter
import com.live.streetview.navigation.earthmap.compass.map.adapters.WeatherVerticleAdapter
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityWeatherBinding
import com.live.streetview.navigation.earthmap.compass.map.weatherinstance.OpenWeatherAPI
import com.live.streetview.navigation.earthmap.compass.map.weatherinstance.RetrofitInstanceClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects

class WeatherActivity : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var Weatherrecy: RecyclerView? = null
    var progressbar: ProgressBar? = null
    var progressbar1: ProgressBar? = null
    var progressBaropenWeather: ProgressBar? = null
    var weatherHorizentalAdapter: WeatherHorizentalAdapter? = null
    var weatherHorizentalModelArrayList: ArrayList<WeatherHorizentalModel>? = null
    var weatherVerticleAdapter: WeatherVerticleAdapter? = null
    var weatherVerticleModelArrayList: ArrayList<WeatherVerticleModel>? = null
    var txtcurrentplace: TextView? = null
    var txtpartiall: TextView? = null
    var txtshowdegree: TextView? = null
    var txthumadity: TextView? = null
    var txtwind: TextView? = null
    var txtrain: TextView? = null
    var apikey = "8cb8a52abd52aa8e77100e36bb36fbea"

    // private String url = "api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}";
    var myList: ArrayList<CustomWeatherModel>? = null
    var myList1: ArrayList<CustomWeatherModel>? = null
    var currentWeatherData: CurrentWeatherData? = null
    private var mLocation: MyLocation? = null
    var lat = 0.0
    var lng = 0.0
    var image: ImageView? = null
    var weatherSearch: EditText? = null
    var imageClick: ImageView? = null
    var textFarenhiet: TextView? = null
    var bindingWeather: ActivityWeatherBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  setContentView(R.layout.activity_weather);
        bindingWeather = ActivityWeatherBinding.inflate(
            layoutInflater
        )
        setContentView(bindingWeather!!.root)
        //call back back press
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        logAnalyticsForClicks("StreetViewWeatherActivityOnCreate", this)
        recyclerView = findViewById(R.id.weatherhorirecy)
        txtcurrentplace = findViewById(R.id.weathercurrentplace)
        txtpartiall = findViewById(R.id.txtpartial)
        Weatherrecy = findViewById(R.id.verticleweatjerrecy)
        txtshowdegree = findViewById(R.id.textweatherdesgree)
        txthumadity = findViewById(R.id.textHumadity)
        txtwind = findViewById(R.id.wind)
        textFarenhiet = findViewById(R.id.textFarenhiet)
        progressbar = findViewById(R.id.progressbar)
        progressbar1 = findViewById(R.id.progressverticle)
        progressBaropenWeather = findViewById(R.id.progressopenweather)
        txtrain = findViewById(R.id.textrainy)
        image = findViewById(R.id.imagewatherarrow)
        weatherSearch = findViewById(R.id.weatherSearch)
        val obj = GPSONOFF()
        obj.gpsONOFF(this)
        //initBannerAdLiveWeather();
        image?.setOnClickListener(View.OnClickListener {
            logAnalyticsForClicks("StreetViewWeatherActivityOnBtnExit", this@WeatherActivity)
            onBackPressedDispatcher.onBackPressed()
        })
        myList = ArrayList()
        myList1 = ArrayList()
        //getcurrentweatherUpdatedata(444,555);
        mLocation = MyLocation(this, object : MyLocationCallBack {
            override fun onLocationListener(location: Location?) {
                Log.d("52663g", location!!.latitude.toString() + "")
                Log.d("52663g", location.longitude.toString() + "")
                getLocationFromLatLong(location)
                //getcurrentweatherUpdatedata(location.getLatitude(), location.getLongitude());
                getTodayWeatherUpdatesList(location.latitude, location.longitude)
                //getNextDaysWeatherUpdatesList(location.getLatitude(), location.getLongitude());
                mLocation!!.onStop()
            }

            override fun MyLocationListener(location: Location?) {}
        })
        mLocation!!.location
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        imageClick = findViewById(R.id.imageClick)
        imageClick?.setOnClickListener(View.OnClickListener {
            val strAdress = weatherSearch?.getText().toString()
            if (strAdress.isEmpty()) {
                Toast.makeText(this@WeatherActivity, "Empty Field", Toast.LENGTH_SHORT).show()
            } else {
                hideKeyboard(this@WeatherActivity, weatherSearch)
                getLocationFromAddress(strAdress)
            }
        })
        streetViewBannerAdsSmall()
    }

    private fun streetViewBannerAdsSmall() {
        val billingHelper = StreetViewAppSoniBillingHelper(this)
        val adContainer = bindingWeather!!.bannerAdPlace.adContainer
        val adView = AdView(this)
        adView.adUnitId = StreetViewAppSoniMyAppAds.banner_admob_inApp
        adView.setAdSize(getAdSize(this))
        if (billingHelper.isNotAdPurchased) {
            loadSmartToolsBannerForMainMediation(adContainer, adView, this)
        } else {
            bindingWeather!!.bannerID.visibility = View.GONE
        }
    }

    //    private void initBannerAdLiveWeather() {
    //        StreetViewLoadAds.loadSoniStreetViewBannerAdMob(
    //                bindingWeather.bannerAdPlace.adContainer,
    //                bindingWeather.bannerID,
    //                this
    //        );
    //    }
    fun getLocationFromAddress(strAddress: String?) {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        bindingWeather!!.progressBar2.visibility = View.VISIBLE
        val coder = Geocoder(this)
        val address: List<Address>?
        try {
            address = coder.getFromLocationName(strAddress!!, 5)
            if (address == null) {
                //  return null;
            }
            val location = address!![0]
            lat = location.latitude
            lng = location.longitude
            txtcurrentplace!!.text = weatherSearch!!.text.toString().trim { it <= ' ' }
            //getcurrentweatherUpdatedata(location.getLatitude(), location.getLongitude());
            getTodayWeatherUpdatesList(location.latitude, location.longitude)
            //getNextDaysWeatherUpdatesList(location.getLatitude(), location.getLongitude());
            // return p1;
        } catch (e: Exception) {
            Toast.makeText(this, "address not found", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    //    public void getLocationFromLatLong(Location location) {
    //        Geocoder coder = new Geocoder(this);
    //        List<Address> address;
    //        try {
    //            address = coder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
    //            if (address == null) {
    //                //  return null;
    //            } else {
    //                txtcurrentplace.setSelected(true);
    //                txtcurrentplace.setText(address.get(0).getAddressLine(0));
    //            }
    //
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //
    //    }
    fun getLocationFromLatLong(location: Location?) {
        val coder = Geocoder(this)
        val address: List<Address>?
        try {
            address = coder.getFromLocation(location!!.latitude, location.longitude, 1)
            if (address != null && !address.isEmpty()) {
                txtcurrentplace!!.isSelected = true
                txtcurrentplace!!.text = address[0].getAddressLine(0)
            } else {
                // Handle the case when the address list is empty
                // You may want to set a default value or perform other actions.
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getcurrentweatherUpdatedata(lat: Double, lng: Double) {
        val openWeatherAPI = RetrofitInstanceClass.retrofitInstance?.create(
            OpenWeatherAPI::class.java
        )
        val cloudsCall = openWeatherAPI?.getCurrentWeatherData(lat, lng, apikey)
        cloudsCall?.enqueue(object : Callback<CurrentWeatherData?> {
            override fun onResponse(
                call: Call<CurrentWeatherData?>,
                response: Response<CurrentWeatherData?>
            ) {
                if (response.isSuccessful) {
                    // txtshowdegree.setText(response.body().getWeather().get(0).getMain());
                    currentWeatherData = response.body()
                    currentWeatherData!!.main?.temp
                    progressBaropenWeather!!.visibility = View.GONE
                    txtwind!!.text = currentWeatherData!!.wind?.speed.toString() + "km/h"
                    txtcurrentplace!!.text = currentWeatherData!!.name
                    txthumadity!!.text = currentWeatherData!!.main?.humidity.toString() + " ℃"
                    txtpartiall!!.text = currentWeatherData!!.main?.feelsLike.toString() + " ℃"
                    //txtshowdegree.setText(toCelciusLiveEarth1(currentWeatherData.getMain().getTemp()) + " F");
                }
            }

            override fun onFailure(call: Call<CurrentWeatherData?>, t: Throwable) {}
        })
    }

    fun toCelciusLiveEarth(x: Double): Int {
        return (x - 273.0).toInt()
    }

    fun toCelciusLiveEarth1(f: Double): Int {
        return (f / 5 * 1.2 + 32).toInt()
    }

    fun kalvinToForenHeat(x: Double): Int {
        val c = (x - 273.15).toInt()
        return c * 9 / 5 + 32
    }

    private fun getTodayWeatherUpdatesList(latitude: Double, longitude: Double) {
        val openWeatherAPI = RetrofitInstanceClass.retrofitInstance?.create(
            OpenWeatherAPI::class.java
        )
        val cloudsCall = openWeatherAPI?.getWeatherDataOf5Days(latitude, longitude, apikey)
        cloudsCall?.enqueue(object : Callback<WeeklyWeatherData?> {
            override fun onResponse(
                call: Call<WeeklyWeatherData?>,
                response: Response<WeeklyWeatherData?>

            ) {

                try {
                    if (response.isSuccessful) {
                        bindingWeather!!.progressBar2.visibility = View.INVISIBLE
                        weatherSearch!!.text.clear()
                        val weeklyWeatherData = response.body()
                        txtshowdegree!!.text =
                            "" + weeklyWeatherData!!.listData?.get(0)?.main?.let {
                                toCelciusLiveEarth(
                                    it.temp
                                )
                            } + " ℃"
                        txtpartiall!!.text =
                            "" + weeklyWeatherData.listData?.get(0)?.main?.let {
                                toCelciusLiveEarth(
                                    it.feelsLike
                                )
                            } + " ℃"
                        textFarenhiet!!.text =
                            "" + weeklyWeatherData.listData?.get(0)?.main?.let {
                                kalvinToForenHeat(
                                    it.temp
                                )
                            } + " F"
                        txtwind!!.text =
                            weeklyWeatherData.listData?.get(0)?.wind?.speed.toString() + "Km/h"
                        txthumadity!!.text =
                            weeklyWeatherData.listData?.get(0)?.main?.humidity.toString() + " ℃"
                        // txtpartiall.setText(weeklyWeatherData.getListData().get(0).getMain().getTemp()+ " ℃");
                        // txtpartiall.setText(toCelciusLiveEarth(currentWeatherData.getMain().getFeelsLike()) + " ℃");
                        myList1!!.clear()
                        myList!!.clear()
                        for (i in weeklyWeatherData.listData?.indices!!) {
                            if (compareDates1(weeklyWeatherData.listData!![i].dtTxt) == "yes") {
                                val myData = weeklyWeatherData.listData!![i].main?.let {
                                    CustomWeatherModel(
                                        it.temp,
                                        weeklyWeatherData.listData!![i].dt
                                    )
                                }
                                if (myData != null) {
                                    myList1!!.add(myData)
                                }
                                Log.d("omarList", "myList : " + myList!!.size)
                                // txtshowdegree.setText(toCelciusLiveEarth(currentWeatherData.getMain().getTemp()) + " ℃");
                            }
                        }
                        weeklyWeatherData.listData!![7].main?.let {
                            CustomWeatherModel(
                                it.temp,
                                weeklyWeatherData.listData!![7].dt
                            )
                        }?.let {
                            myList!!.add(
                                it
                            )
                        }

                        weeklyWeatherData.listData!![14].main?.let {
                            CustomWeatherModel(
                                it.temp,
                                weeklyWeatherData.listData!![14].dt
                            )
                        }?.let {
                            myList!!.add(
                                it
                            )
                        }
                        weeklyWeatherData.listData!![21].main?.let {
                            CustomWeatherModel(
                                it.temp,
                                weeklyWeatherData.listData!![21].dt
                            )
                        }?.let {
                            myList!!.add(
                                it
                            )
                        }
                        weeklyWeatherData.listData!![28].main?.let {
                            CustomWeatherModel(
                                it.temp,
                                weeklyWeatherData.listData!![28].dt
                            )
                        }?.let {
                            myList!!.add(
                                it
                            )
                        }
                        weeklyWeatherData.listData!![35].main?.let {
                            CustomWeatherModel(
                                it.temp,
                                weeklyWeatherData.listData!![35].dt
                            )
                        }?.let {
                            myList!!.add(
                                it
                            )
                        }
                        progressbar!!.visibility = View.GONE
                        progressbar1!!.visibility = View.GONE
                        recyclerView!!.visibility = View.VISIBLE
                        recyclerView!!.layoutManager = LinearLayoutManager(
                            this@WeatherActivity,
                            RecyclerView.HORIZONTAL,
                            false
                        )
                        weatherHorizentalAdapter =
                            WeatherHorizentalAdapter(this@WeatherActivity, myList1!!)
                        recyclerView!!.adapter = weatherHorizentalAdapter
                        Weatherrecy!!.visibility = View.VISIBLE
                        Weatherrecy!!.layoutManager = LinearLayoutManager(this@WeatherActivity)
                        weatherVerticleAdapter =
                            WeatherVerticleAdapter(this@WeatherActivity, myList!!)
                        Weatherrecy!!.adapter = weatherVerticleAdapter
                    } else {
                        progressbar?.visibility = View.GONE
                        Log.d("TAG", "else: no exception or result")
//                        progressbar!!.visibility = View.GONE
                        progressbar?.visibility = View.GONE
                        progressbar1!!.visibility = View.GONE
                        Toast.makeText(this@WeatherActivity, "Data could not be found please try later", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: NullPointerException) {
                    Log.d("TAG", "weatherexcerption: $e")
                    progressbar?.visibility = View.GONE
                    progressbar1!!.visibility = View.GONE
                    Toast.makeText(this@WeatherActivity, "Data could not be found please try later", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<WeeklyWeatherData?>, t: Throwable) {
                Log.d("omarList", "Throwable : " + t.localizedMessage)
                progressbar?.visibility = View.GONE
                progressbar1!!.visibility = View.GONE
                Toast.makeText(this@WeatherActivity, "Data could not be found please try later", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getNextDaysWeatherUpdatesList(latitude: Double, longitude: Double) {
        val openWeatherAPI = RetrofitInstanceClass.retrofitInstance?.create(
            OpenWeatherAPI::class.java
        )
        val cloudsCall = openWeatherAPI?.getWeatherDataOf5Days(latitude, longitude, apikey)
        cloudsCall?.enqueue(object : Callback<WeeklyWeatherData?> {
            override fun onResponse(
                call: Call<WeeklyWeatherData?>,
                response: Response<WeeklyWeatherData?>
            ) {
                if (response.isSuccessful) {
                    val weeklyWeatherData = response.body()
                    myList!!.clear()
                    for (i in Objects.requireNonNull(weeklyWeatherData)?.listData?.indices!!) {
                        if (compareDates(weeklyWeatherData!!.listData?.get(i)?.dtTxt) != "yes") {
                            val myData = weeklyWeatherData.listData?.get(i)?.main?.temp?.let {
                                CustomWeatherModel(
                                    it,
                                    weeklyWeatherData.listData!![i].dt
                                )
                            }
                            if (myData != null) {
                                myList!!.add(myData)
                            }
                            Log.d("omarList", "myList: " + myList!!.size)
                        }
                    }
                    progressbar1!!.visibility = View.GONE
                    Weatherrecy!!.visibility = View.VISIBLE
                    Weatherrecy!!.layoutManager = LinearLayoutManager(this@WeatherActivity)
                    weatherVerticleAdapter = WeatherVerticleAdapter(this@WeatherActivity, myList!!)
                    Weatherrecy!!.adapter = weatherVerticleAdapter
                }
            }

            override fun onFailure(call: Call<WeeklyWeatherData?>, t: Throwable) {}
        })
    }

    private fun compareDates(date2: String?): String {
        var isMactching = "no"
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val currentDate = sdf.format(Date())
            val strs2 = date2!!.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            Log.d("omar", "original List Date: $date2")
            Log.d("omar", "strs2 part 1: " + strs2[0])
            Log.d("omar", "strs2 part 2: " + strs2[1])
            Log.d("omar", "currentDate: $currentDate")
            if (date2 != null && currentDate != null) {
                isMactching = if (strs2[0] == currentDate) {
                    "yes"
                } else {
                    "no"

                    //both date are not equal("use after and before to check occurrence of dates")
                }
            }
        } catch (e1: Exception) {
            Log.d("yes", "failed " + e1.message)
        }
        return isMactching
    }

    private fun compareDates1(date2: String?): String {
        var isMactching = "no"
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val currentDate = sdf.format(Date())
            val strs2 = date2!!.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            Log.d("omar", "original List Date: $date2")
            Log.d("omar", "strs2 part 1: " + strs2[0])
            Log.d("omar", "strs2 part 2: " + strs2[1])
            Log.d("omar", "currentDate: $currentDate")
            if (date2 != null && currentDate != null) {
                isMactching = if (strs2[0] == currentDate) {
                    "yes"
                } else {
                    "no"

                    //both date are not equal("use after and before to check occurrence of dates")
                }
            }
        } catch (e1: Exception) {
            Log.d("yes", "failed " + e1.message)
        }
        return isMactching
    }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Show ads logic
                logAnalyticsForClicks("StreetViewWeatherActivityOnExit", this@WeatherActivity)
                mediationBackPressedSimpleStreetViewLocation(
                    this@WeatherActivity,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd, bindingWeather!!.whiteView
                )
            }
        }

    fun hideKeyboard(context: Context, currentFocus: View?) {
        val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        if (currentFocus != null) {
            imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }
}