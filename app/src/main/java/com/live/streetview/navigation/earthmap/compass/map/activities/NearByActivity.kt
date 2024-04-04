package com.live.streetview.navigation.earthmap.compass.map.activities

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.ahmadhamwi.tabsync.TabbedListMediator
import com.google.android.gms.ads.AdView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.tabs.TabLayout
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds
import com.live.streetview.navigation.earthmap.compass.map.Model.nearby.Category
import com.live.streetview.navigation.earthmap.compass.map.Model.nearby.Item
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.adapters.CategoriesAdapter
import com.live.streetview.navigation.earthmap.compass.map.callback.NearByActivityCallBack
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityNearByBinding

class NearByActivity : AppCompatActivity(), NearByActivityCallBack {
    private var fusedLocationClient: FusedLocationProviderClient? = null
    var userLoc: Location? = null
    var location: Location? = null
    var latitude: Double? = null
    var longitude: Double? = null
    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerView: RecyclerView


    private val categories = mutableListOf(
        Category(
            "Education",
            Item("School", R.drawable.college_marker, (R.color.cardgps)),
            Item("College\nUniversity", R.drawable.college_marker, (R.color.speed))

        ),
        Category(
            "Food points",
            Item("Restaurant", R.drawable.restaurant_marker, (R.color.cardgps)),
            Item("Bakery", R.drawable.bakery_maker, (R.color.earth)),
//            Item("Beer bar", R.drawable.beer_bar_marker, R.color.nearby),
            Item("Nightlife spot", R.drawable.night_life_marker, R.color.map),
            Item("Cafeteria", R.drawable.cafeteria_marker, R.color.fm),
            Item("Grocery store", R.drawable.grocerry_store_marker, R.color.cardgps)
        ),
        Category(
            "Shopping",
            Item("Department store", R.drawable.department_store_marker, R.color.weather),
            Item("Electronics store", R.drawable.electronics_store, R.color.speed),
            Item("Hardware store", R.drawable.hardware_store_marker, R.color.webcams),
            Item("Events", R.drawable.event_marker, R.color.compass),
            Item("Flower store", R.drawable.flower_store_marker, R.color.fm),
            Item("Shoe store", R.drawable.shoe_store_maker, R.color.nearby),
            Item("Shopping mall", R.drawable.shopping_mall_marker, R.color.area),
            Item("Clothing store", R.drawable.clothing_store_marker, R.color.cardgps),
            Item("Pet supplies store", R.drawable.pet_supplies_marker, R.color.weather)
        ),
        Category(
            "Services",
            Item("Atm", R.drawable.atm, R.color.area),
            Item("Automotive Service", R.drawable.automotive_service, R.color.cardgps),
            Item("Car wash", R.drawable.car_wash_marker, R.color.webcams),
            Item("Home services ", R.drawable.home_services_marker, R.color.weather),
            Item("Fuel station", R.drawable.fuel_station_mark, R.color.nearby),
            Item("Painter", R.drawable.painter_marker, R.color.fm),
            Item("Parking", R.drawable.parking_marker, R.color.compass)
        ),
        Category(
            "Office",
            Item("Banks", R.drawable.banks_marker, R.color.area),
            Item("Government office", R.drawable.government_office_marker, R.color.nearby),
            Item("Police station", R.drawable.police_station_marker, R.color.weather),
            Item("Post office", R.drawable.post_office_marker, R.color.cardgps),
            Item("Rail station", R.drawable.rail_station_marker, R.color.nearby),
            Item("Embassy", R.drawable.embassy_marker, R.color.earth),
            Item("Real estate", R.drawable.real_estate_marker, R.color.speed),
        ),
        Category(
            "Health",
            Item("Hospital", R.drawable.hospital_mark, R.color.speed),
            Item("Doctor", R.drawable.doctor_marker, R.color.earth),
            Item("Dentist", R.drawable.dentist_marker, R.color.cardgps),
            Item("Medical center", R.drawable.medical_center_marker, R.color.nearby),
            Item("Gym", R.drawable.gym_marker, R.color.area),
            Item("Out Door Gym", R.drawable.out_door_gym_marker, R.color.fuel),
            Item("Physical Therapist", R.drawable.physical_therapist_marker, R.color.altimeter)
        ),
        Category(
            "Religious",
            Item("Mosque", R.drawable.mosque_maker, (R.color.nearby)),
            Item("Temple", R.drawable.temple_marker, R.color.weather),
            Item("Buddhist temple", R.drawable.buddhist_temple_marker, R.color.speed),
            Item("Confucian temple", R.drawable.confucian_temple_marker, R.color.cardgps),
            Item("Hindu temple", R.drawable.hindu_temple_marker, R.color.nearby),
            Item("Sikh temple", R.drawable.sikh_temple_marker, R.color.webcams),
            Item("Church", R.drawable.church_marker, R.color.fuel),
        ),
        Category(
            "Other",
            Item("art gallery", R.drawable.art_gallery, R.color.weather),
            Item("bowling alley", R.drawable.bowling_alley_marker, R.color.webcams),
            Item("bus station", R.drawable.bus_station_marker, R.color.cardgps),
            Item("Music venue", R.drawable.music_venue_marker, R.color.speed),
            Item("Airport", R.drawable.airport, R.color.area),
            Item("Hotel", R.drawable.hotel_marker, R.color.webcams),
            Item("Laundry", R.drawable.laundry_marker, R.color.cardgps),
            Item("Court\nhouse", R.drawable.courthouse_marker, R.color.nearby),
            Item("Museum", R.drawable.museum, R.color.fuel),
            Item("Zoo", R.drawable.zoo_marker, R.color.cardgps),
            Item("movie theater", R.drawable.movie_theater_marker, R.color.speed),
            Item("casino", R.drawable.casino_marker, R.color.nearby),
            Item("stadium", R.drawable.stadium_marker, R.color.compass),
            Item("boutique", R.drawable.boutique_marker, R.color.fm),
            Item("Beach", R.drawable.beach_marker, R.color.cardgps),
            Item("Bike trail", R.drawable.bike_trail_marker, R.color.fuel),
            Item("Swimming Pool", R.drawable.swimming_pool_marker, R.color.nearby)
        ),
    )

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks("StreetViewNearByOnExit", this@NearByActivity)

            StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation(
                this@NearByActivity,
                StreetViewAppSoniMyAppAds.admobInterstitialAd,
                binding!!.whiteView
            )
        }
    }
    var binding: ActivityNearByBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_nearby_places);
        binding = ActivityNearByBinding.inflate(layoutInflater)
        setContentView(binding!!.getRoot())
        StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks("StreetViewNearByOnCreate", this)
        this.onBackPressedDispatcher.addCallback(onBackPressedCallback)
        // initBannerAdLiveNearBy();
        fusedLocationClient = FusedLocationProviderClient(this)
        userLoc = Location("Service Provider")
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
        fusedLocationClient!!.getLastLocation().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                location = task.result
                if (location == null) {
                    Toast.makeText(
                        this@NearByActivity,
                        "Permission not granted",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    userLoc!!.latitude = location!!.latitude
                    userLoc!!.longitude = location!!.longitude
                    latitude = userLoc!!.latitude
                    longitude = userLoc!!.longitude
                    Log.d(ContentValues.TAG, "longitude: " + userLoc!!.longitude)
                    Log.d(ContentValues.TAG, "latitude: " + userLoc!!.latitude)
                }
            }
        }
        initViews()
        initTabLayout()
        initRecycler()
        initMediator()
        streetViewBannerAdsSmall()
        binding!!.imageNearByArrow.setOnClickListener {
            StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks("StreetViewNearByOnBtnExit", this)
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initViews() {
        tabLayout = findViewById(R.id.tabLayout)
        recyclerView = findViewById(R.id.recyclerView)
//        recyclerView.layoutManager = GridLayoutManager(this, 3)
    }

    private fun initTabLayout() {
        for (category in categories) {
            tabLayout.addTab(tabLayout.newTab().setText(category.name))
        }
    }

    private fun streetViewBannerAdsSmall() {
        val billingHelper = StreetViewAppSoniBillingHelper(this)
        val adContainer: LinearLayout = binding!!.bannerAdPlace.adContainer
        val adView = AdView(this)
        adView.setAdSize(StreetViewAppSoniMyAppAds.getAdSize(this))
        adView.adUnitId = StreetViewAppSoniMyAppAds.banner_admob_inApp
        if (billingHelper.isNotAdPurchased) {
            StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediation(
                adContainer, adView, this
            )
        } else {
            binding!!.bannerID.visibility = View.GONE
        }
    }

    private fun initRecycler() {
        recyclerView.adapter = CategoriesAdapter(this, categories, this)

    }

    private fun initMediator() {
        TabbedListMediator(recyclerView, tabLayout, categories.indices.toList()).attach()
    }

    override fun onClick(name: String) {
        Log.d("TAG", "onClick: ${name}")
//        Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
        StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks("StreetViewNearByOnClick" + name, this)

        if (latitude != null && longitude != null) {
            val intent = Intent(this@NearByActivity, HospitalActivity::class.java)
            val bundle = Bundle()
            bundle.putString("key1", name)
            bundle.putDouble("lat", latitude!!)
            bundle.putDouble("lng", longitude!!)
            intent.putExtras(bundle)
            StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation(
                this@NearByActivity,
                StreetViewAppSoniMyAppAds.admobInterstitialAd,
                 intent,binding!!.whiteView
            )
        } else {
            Toast.makeText(this@NearByActivity, "No Location found", Toast.LENGTH_SHORT)
                .show()
        }
    }
}