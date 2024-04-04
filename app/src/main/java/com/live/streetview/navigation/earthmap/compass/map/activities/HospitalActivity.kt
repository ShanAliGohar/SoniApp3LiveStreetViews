package com.live.streetview.navigation.earthmap.compass.map.activities


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.ads.AdView
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds
import com.live.streetview.navigation.earthmap.compass.map.NearBy.NearByPlacesApi
import com.live.streetview.navigation.earthmap.compass.map.NearBy.NearbyRetrofitInstance
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.activities.ModelClass.ModelNearBy
import com.live.streetview.navigation.earthmap.compass.map.activities.ModelClass.Result
import com.live.streetview.navigation.earthmap.compass.map.activities.osmNavigation.OsmNavigation
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityHospitalBinding
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar

class HospitalActivity : AppCompatActivity() {
    var view: View? = null
    var lat = 0.0
    var lng = 0.0
    var tolatitue = 0.0
    var tolongitude = 0.0
    var txtdistance: TextView? = null
    var txttime: TextView? = null

    //    var mapboxNavigation: MapboxNavigation? = null
//    var token = "fsq3RmJryyzOUTZBxBidTYkqw+NFuVXIRvP15qOtwiYwZP4="
//    var clientId = "JY0QWD5LMMU5GR24WW5XD5K12TMSHGLGKOQUTQRMILDUFLE2"
//    var ClientSecret = "5S2GIJPRRSGCCCSNYA5MHQIQQGPBEU4R0NB5OQ4PYGYQFITG"
    var token = "fsq3y/UoZLe6r5My8y0ms7pQVWIN6pdByQGXMQs41hFl0M0="
    var clientId = "RQ4WXX2HRDY3I0SQ5URFUZ4YHWX1HH101PGTXAND4ALXSI1B"
    var ClientSecret = "HJ5GJAJNMHHIH241JZPUWRUCOMFPTUR1VSWU01EVX1IFOXPJ"
    var c = Calendar.getInstance()
    var dateformat = SimpleDateFormat("yyyymmdd")
    var v = dateformat.format(c.time)
    var conHide: ConstraintLayout? = null
    var bindingHospital: ActivityHospitalBinding? = null
    var title: String? = null
    private lateinit var startPoint: GeoPoint

    //    private var lastSelectedDirectionsProfile: String? = DirectionsCriteria.PROFILE_DRIVING
//    var earthDirectionsRoute: DirectionsRoute? = null
//    var earthDirectionClient: MapboxDirections? = null
    var listPosits = ArrayList<GeoPoint>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Mapbox.getInstance(this, UtilityClass.MAPBOX_KEY)
        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        bindingHospital = ActivityHospitalBinding.inflate(
            layoutInflater
        )

        setContentView(bindingHospital!!.root)

        this.onBackPressedDispatcher.addCallback(onBackPressedCallback)

        StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks(
            "StreetViewNearByDisplayOnMap",
            this@HospitalActivity
        )

        // setContentView(R.layout.activity_hospital);
        conHide = findViewById(R.id.conHide)
        txtdistance = findViewById(R.id.txtdistance)
        txttime = findViewById(R.id.txttime)
//        mapView = findViewById(R.id.mapViewHospital)
//        mapView!!.getMapAsync(this)
        //initBannerAdHospital()
        val bundle1 = intent.extras
        lat = bundle1!!.getDouble("lat")
        lng = bundle1.getDouble("lng")

        initializeMap(lat, lng)
        val bundle = intent.extras
        // getting the string back
        title = bundle!!.getString("key1")
        /*-------------------------------------------------------------------*/

//        NavigationOptions navigationOptions = MapboxNavigation
//                .defaultNavigationOptionsBuilder(this, "sk.eyJ1IjoidGE5OWxoYXMiLCJhIjoiY2t2czJyenE3MXBjdDJ2cWg5ZDV5eDIxbyJ9.XjCICyXDUovwKCrxPOu9Dw")
//                .build();
//       try {
//            mapboxNavigation = MapboxNavigationProvider.create(navigationOptions);
//        }catch (Exception e){
//
//       }


        /*-------------------------------------------------------------------*/
        if (title == "Gas") {
            getNearbyPlaces(lat, lng, "19007")
        } else if (title == "Hospital") {


            getNearbyPlaces(lat, lng, "15014")
        } else if (title == "School") {
            getNearbyPlaces(lat, lng, "12057")
        } else if (title == "Airport") {


            getNearbyPlaces(lat, lng, "19040")
        } else if (title == "Hotel") {
//

            getNearbyPlaces(lat, lng, "19014")
        } else if (title == "FastFood") {


            getNearbyPlaces(lat, lng, "13145")
        } else if (title == "Atm") {


            getNearbyPlaces(lat, lng, "11044")
        } else if (title == "Coffee") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "13035")
        } else if (title == "Worship") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "12106")
        } else if (title == "Fire") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "12071")
        } else if (title == "Gym") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "18021")
        } else if (title == "Library") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "12080")
        } else if (title == "Museum") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "10027")
        } else if (title == "Bakery") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "13002")
        } else if (title == "Movie") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "10024")
        } else if (title == "Grocery") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "17069")
        } else if (title == "Bank") {

           // //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "11045")
        } else if (title == "Court") {

           // //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "13052")
        } else if (title == "College\nUniversity") {

          //  //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "12013")
        } else if (title == "Restaurant") {

          //  //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "13065")
        } else if (title == "Beer bar") {

          //  //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "13006")
        } else if (title == "Nightlife spot") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "10032")
        } else if (title == "Cafeteria") {
        //    //Toast.makeText(this, "this is cafeteria" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "13032")
        } else if (title == "Grocery store") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "17069")
        } else if (title == "Department store") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "17033")
        } else if (title == "Electronics store") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "17025")
        } else if (title == "Hardware store") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "17090")
        } else if (title == "Events") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "14000")
        } else if (title == "Flower store") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "17056")
        } else if (title == "Shoe store") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "17048")
        } else if (title == "Shopping mall") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "17114")
        } else if (title == "Clothing store") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "17043")
        } else if (title == "Pet supplies store") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "17110")
        } else if (title == "Automotive Service") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "11009")
        } else if (title == "Car wash") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "11011")
        } else if (title == "Home services") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "11089")
        } else if (title == "Fuel station") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "19007")
        } else if (title == "Painter") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "11094")
        } else if (title == "Parking") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "19020")
        } else if (title == "Banks") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "11045")
        } else if (title == "Government office") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "12069")
        } else if (title == "Police station") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "12072")
        } else if (title == "Post office") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "12075")
        } else if (title == "Rail station") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "19047")
        } else if (title == "Embassy") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "12068")
        } else if (title == "Real estate") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "11145")
        } else if (title == "Doctor") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "15031")
        } else if (title == "Dentist") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "15007")
        } else if (title == "Medical center") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "15016")
        } else if (title == "Out Door Gym") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "18026")
        } else if (title == "Physical Therapist") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "15026")
        } else if (title == "Mosque") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "12106")
        } else if (title == "Temple") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "12111")
        } else if (title == "Buddhist temple") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "12099")
        } else if (title == "Confucian temple") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "12102")
        } else if (title == "Hindu temple") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "12103")
        } else if (title == "Sikh temple") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "12109")
        } else if (title == "Church") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "12101")
        } else if (title == "art gallery") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "10004")
        } else if (title == "bowling alley") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "10006")
        } else if (title == "bus station") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "19042")
        } else if (title == "Music venue") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "10039")
        } else if (title == "Laundry") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "11069")
        } else if (title == "Courthouse") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "12067")
        } else if (title == "Zoo") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "10056")
        } else if (title == "movie theater") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "10024")
        } else if (title == "casino") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "10008")
        } else if (title == "stadium") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "10051")
        } else if (title == "boutique") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "17020")
        } else if (title == "Beach") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "16003")
        } else if (title == "Bike trail") {

            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "16004")
        } else if (title == "Swimming Pool") {
            //Toast.makeText(this, "" + title, Toast.LENGTH_SHORT).show()
            getNearbyPlaces(lat, lng, "18075")
        }
        view = findViewById(R.id.ViewNaviN)
        view!!.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@HospitalActivity, NewNavigationCloneActivity::class.java)
            val bundle = Bundle()
//            bundle.putString("directionroute", lastSelectedDirectionsProfile)
            bundle.putDouble("lat", tolatitue)
            bundle.putDouble("lng", tolongitude)
            intent.putExtras(bundle)
            startActivity(intent)
        })
        streetViewBannerAdsSmall()

        bindingHospital!!.ivLeftarrowFuel.setOnClickListener {
            StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks(
                "StreetViewNearByDisplayOnBtnExit",
                this@HospitalActivity
            )

            onBackPressedDispatcher.onBackPressed()
        }
    }


    private fun streetViewBannerAdsSmall() {
        val billingHelper =
            StreetViewAppSoniBillingHelper(
                this
            )
        val adContainer: LinearLayout = bindingHospital!!.bannerAdPlace.adContainer
        val adView = AdView(this)
        adView.setAdSize(StreetViewAppSoniMyAppAds.getAdSize(this))
        adView.adUnitId = StreetViewAppSoniMyAppAds.banner_admob_inApp
        if (billingHelper.isNotAdPurchased) {
            StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediation(
                adContainer, adView, this
            )
        } else {
            bindingHospital!!.bannerID.visibility = View.GONE
        }
    }


    private fun getRouteCar(originPoint: GeoPoint?, destinationPoint: GeoPoint?) {


        if (listPosits != null) {
            listPosits.clear()
        }
        listPosits.add(originPoint!!)
        listPosits.add(destinationPoint!!)

    }

    private fun getNearbyPlaces(lat: Double, lng: Double, id: String) {
        val nearByPlacesApi = NearbyRetrofitInstance.retrofitInstance?.create(
            NearByPlacesApi::class.java
        )

        val call =
            nearByPlacesApi?.getForCast(
                "$lat,$lng",
                clientId,
                ClientSecret,
                v,
                50,
                10000,
                id,
                token
            )
        call?.enqueue(object : Callback<ModelNearBy?> {
            override fun onResponse(call: Call<ModelNearBy?>, response: Response<ModelNearBy?>) {
                if (response.isSuccessful && response != null) {
                    var nearByList: List<Result> = ArrayList()
                    nearByList = response.body()!!.results!!

                    Log.d("TAG", "onResponse: ${nearByList.size}")
                    if (nearByList.size > 0) {
                        addMarkers(nearByList)
                    } else {

                        Toast.makeText(
                            this@HospitalActivity,
                            "No Data for this Category",
                            Toast.LENGTH_SHORT
                        ).show()
//                        onBackPressed()
                    }
                } else {
                    Log.d("SaadTAG", "onFailure2: " + response.errorBody()!!.source())
                }
            }

            override fun onFailure(call: Call<ModelNearBy?>, t: Throwable) {}
        })
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun addMarkers(result: List<Result>) {
        for (model in result) {
            if (model.categories?.size!! > 0) {
                val spiltedString =
                    model.categories!![0].icon?.prefix?.split("categories_v2/")?.toTypedArray()
                val SecondSpilit = spiltedString?.get(1)?.split("/")?.toTypedArray()
                Log.d("TAG", "addMarkers: type  ${SecondSpilit?.get(1)} ")
                if (model.geocodes?.main?.latitude != null && model.geocodes!!.main?.longitude != null && model.name != null) {
                    try {
                        val markerPoint =
                            GeoPoint(
                                model.geocodes!!.main?.latitude!!,
                                model.geocodes!!.main?.longitude!!
                            )
                        var markernearBy: Marker? = null
                        markernearBy = Marker(bindingHospital!!.mapViewHospital)
                        markernearBy.title = (model.name)
                        markernearBy.textLabelFontSize = 11
                        markernearBy.position = markerPoint

                        if (title == "Gas") {

                            markernearBy.icon =
                                resources.getDrawable(R.drawable.fuel_station_mark)

                        } else if (title == "Hospital") {

                            markernearBy.icon =
                                resources.getDrawable(R.drawable.hospital_mark)

                        } else if (title == "School") {

                            markernearBy.icon =
                                resources.getDrawable(R.drawable.school_marker)

                        } else if (title == "Airport") {

                            markernearBy.icon =
                                resources.getDrawable(R.drawable.airport_marker)

                        } else if (title == "Hotel") {
                            //            markernearBy.icon =
                            //                resources.getDrawable(R.drawable.hotel_marker)


                        } else if (title == "FastFood") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.fastfoodmarker)


                        } else if (title == "Atm") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.atmmarker)


                        } else if (title == "Coffee") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.coffee1)


                        } else if (title == "Worship") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.worship1)


                        } else if (title == "Fire") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.firemarker)


                        } else if (title == "Gym") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.gym)


                        } else if (title == "Library") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.librarymarker)


                        } else if (title == "Museum") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.museummarker)


                        } else if (title == "Bakery") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.bakery)


                        } else if (title == "Movie") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.movie_theater)


                        } else if (title == "Grocery") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.grocerry_store)


                        } else if (title == "Bank") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.banks)


                        } else if (title == "Court") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.courthouse)


                        } else if (title == "College&University") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.college)


                        } else if (title == "Restaurant") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.restaurant)


                        } else if (title == "Beer bar") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.beer_bar)


                        } else if (title == "Nightlife spot") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.night_life)

                        } else if (title == "Cafeteria") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.cafeteria)

                        } else if (title == "Grocery store") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.grocerry_store)


                        } else if (title == "Department store") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.department_store)


                        } else if (title == "Electronics store") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.electronics_store_marker)


                        } else if (title == "Hardware store") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.hardware_store)


                        } else if (title == "Events") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.event)


                        } else if (title == "Flower store") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.flower_store)


                        } else if (title == "Shoe store") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.shoe_store)


                        } else if (title == "Shopping mall") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.shopping_mall)


                        } else if (title == "Clothing store") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.clothing_store)


                        } else if (title == "Pet supplies store") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.pet_supplies)


                        } else if (title == "Automotive Service") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.automotive_service_marker)


                        } else if (title == "Car wash") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.car_wash)


                        } else if (title == "Home services") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.home_services)


                        } else if (title == "Fuel station") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.fuel_station)


                        } else if (title == "Painter") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.painter)


                        } else if (title == "Parking") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.parking)


                        } else if (title == "Banks") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.banks)


                        } else if (title == "Government office") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.government_office)


                        } else if (title == "Police station") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.police_station)


                        } else if (title == "Post office") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.post_office)


                        } else if (title == "Rail station") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.rail_station)


                        } else if (title == "Embassy") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.embassy)


                        } else if (title == "Real estate") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.real_estate)


                        } else if (title == "Doctor") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.doctor)


                        } else if (title == "Dentist") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.dentist)


                        } else if (title == "Medical center") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.cafeteria)


                        } else if (title == "Out Door Gym") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.cafeteria)


                        } else if (title == "Physical Therapist") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.physical_therapist)


                        } else if (title == "Mosque") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.mosque)


                        } else if (title == "Temple") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.temple)

                            getNearbyPlaces(lat, lng, "12111")
                        } else if (title == "Buddhist temple") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.buddhist_temple)


                        } else if (title == "Confucian temple") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.confucian_temple)


                        } else if (title == "Hindu temple") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.hindu_temple)


                        } else if (title == "Sikh temple") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.sikh_temple)


                        } else if (title == "Church") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.church)


                        } else if (title == "art gallery") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.art_gallery_marker)


                        } else if (title == "bowling alley") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.bowling_alley)


                        } else if (title == "bus station") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.bus_station)


                        } else if (title == "Music venue") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.music_venue)


                        } else if (title == "Laundry") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.laundry)


                        } else if (title == "Courthouse") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.courthouse)


                        } else if (title == "Zoo") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.zoo)


                        } else if (title == "movie theater") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.movie_theater)


                        } else if (title == "casino") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.casino)


                        } else if (title == "stadium") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.stadium)


                        } else if (title == "boutique") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.boutique)


                        } else if (title == "Beach") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.beach)


                        } else if (title == "Bike trail") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.bike_trail)


                        } else if (title == "Swimming Pool") {
                            markernearBy.icon =
                                resources.getDrawable(R.drawable.swimming_pool)


                        }

                        bindingHospital!!.mapViewHospital.overlays.add(markernearBy)
                        bindingHospital!!.mapViewHospital.invalidate()
                        markernearBy.setOnMarkerClickListener(Marker.OnMarkerClickListener { marker, mapView ->
                            marker!!.closeInfoWindow()
                            bindingHospital!!.conHide.visibility = View.VISIBLE
                            model.distance?.let {
                                model.name?.let { it1 ->
                                    model.geocodes!!.main?.latitude?.let { it2 ->
                                        model.geocodes!!.main?.longitude?.let { it3 ->
                                            showNearPlaceInfo(
                                                it,
                                                it1,
                                                it2, it3
                                            )
                                        }
                                    }
                                }
                            }
                            true
                        })
                    } catch (E: NullPointerException) {
                    }
                } else {
                    Toast.makeText(this, "try again", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }


    private fun showNearPlaceInfo(
        distance: Int,
        name: String,
        latitude: Double,
        longitude: Double
    ) {
//    binding!!.nearLocationInfo.visibility = View.VISIBLE
//    binding!!.addressNearMe.text = "$name"
//    binding!!.latNearMe.text = "Latitude: $lat    Longitude:${lng}"
        bindingHospital!!.txtdistance.isSelected = true
        bindingHospital!!.txtdistance.text = "${distance / 1000} km"

        bindingHospital!!.txttime.text = "$name"

        bindingHospital!!.ViewNaviN.setOnClickListener {

            StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks(
                "StreetViewNearByDisplayOnMapToOsmNavigation",
                this
            )
            val intent = Intent(this, OsmNavigation::class.java)
            intent.putExtra("lat", startPoint.latitude)
            intent.putExtra("lng", startPoint.longitude)
            intent.putExtra("d_lat", latitude)
            intent.putExtra("d_lng", longitude)
            StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation(
                this@HospitalActivity,
                StreetViewAppSoniMyAppAds.admobInterstitialAd,

                intent, bindingHospital!!.whiteView
            )
        }
    }

    private fun ConvertSectoDay(n: Int) {
        var n = n
        val n1 = n
        val day = n1 / (24 * 3600)
        val n2 = n1 % (24 * 3600)
        val hour = n2 / 3600
        n %= 3600
        val minutes = n / 60
        n %= 60
        val seconds = n
        if (day == 0) {
            txtdistance!!.text = "$hour H $minutes m "
            if (hour == 0) {
                txtdistance!!.text = "$minutes min"
            }
        } else {
            txtdistance!!.text = "$day D $hour H $minutes minsk "
        }
    }

    companion object {
        @JvmField
        var hospitalOrigin: Point? = null

        @JvmField
        var hospitalDestination: Point? = null

        //NavigationMapboxMap navigationMapboxMap;
//        var myroute: DirectionsRoute? = null
    }

    private fun initializeMap(lat: Double, lng: Double) {

        bindingHospital!!.mapViewHospital.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        bindingHospital!!.mapViewHospital.setBuiltInZoomControls(true)
        bindingHospital!!.mapViewHospital.setMultiTouchControls(true)
        val mRotationGestureOverlay =
            RotationGestureOverlay(this, bindingHospital!!.mapViewHospital)
        mRotationGestureOverlay.isEnabled = true
        bindingHospital!!.mapViewHospital.overlays.add(mRotationGestureOverlay)
        val mapController: IMapController = bindingHospital!!.mapViewHospital.controller

        startPoint = GeoPoint(lat, lng)
        bindingHospital!!.mapViewHospital!!.controller.animateTo(
            startPoint,
            12.0,
            1400
        )
        mapController.setCenter(startPoint)
        bindingHospital!!.mapViewHospital.getZoomController().setVisibility(
            CustomZoomButtonsController.Visibility.NEVER
        );
        // val mapshowpoint = GeoPoint(51.5072, 0.1276)
        //marker
        val marker = Marker(bindingHospital!!.mapViewHospital)
        marker.title = ("Current Location")
        marker.textLabelFontSize = 11
        //must set the icon to null last
        marker.icon = null
        marker.position = startPoint
        val icon = BitmapFactory.decodeResource(resources, (R.drawable.currentlocarion))
        setMarkerIconAsPhoto(marker, icon!!)
        bindingHospital!!.mapViewHospital.overlays.add(marker)
        bindingHospital!!.mapViewHospital.invalidate()
    }

    //load marker
    fun setMarkerIconAsPhoto(marker: Marker, thumbnail: Bitmap) {
        var thumbnail = thumbnail
        val borderSize = 2
        thumbnail = Bitmap.createScaledBitmap(thumbnail, 90, 90, true)
        val withBorder = Bitmap.createBitmap(
            thumbnail.width + borderSize * 2,
            thumbnail.height + borderSize * 2,
            thumbnail.config
        )
        val canvas = Canvas(withBorder)
        canvas.drawColor(Color.TRANSPARENT)
        canvas.drawBitmap(thumbnail, borderSize.toFloat(), borderSize.toFloat(), null)
        val icon = BitmapDrawable(resources, withBorder)
        marker.icon = icon
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks(
                "StreetViewNearByDisplayOnExit",
                this@HospitalActivity
            )
            StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation(
                this@HospitalActivity,
                StreetViewAppSoniMyAppAds.admobInterstitialAd,
                bindingHospital!!.whiteView
            )
        }
    }
}