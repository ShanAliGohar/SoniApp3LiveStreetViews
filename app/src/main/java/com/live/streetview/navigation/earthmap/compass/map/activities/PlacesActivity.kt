package com.live.streetview.navigation.earthmap.compass.map.activities

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation
import com.live.streetview.navigation.earthmap.compass.map.Model.PlaceModel
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.activities.utils.UtilityClass
import com.live.streetview.navigation.earthmap.compass.map.adapters.PlacesAdapter
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityPlacesBinding

class PlacesActivity : AppCompatActivity() {
    var placeRecyclerview: RecyclerView? = null
    var placeModelArrayList: ArrayList<PlaceModel?>? = null
    var placesAdapter: PlacesAdapter? = null
    var textCurrentPlace: TextView? = null
    var imagePlace: ImageView? = null
    var status: String? = null
    var countryName: String? = null
    var Cityname: String? = null
    var bindingplace: ActivityPlacesBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_places);
        bindingplace = ActivityPlacesBinding.inflate(
            layoutInflater
        )
        setContentView(bindingplace!!.root)
        placeRecyclerview = findViewById(R.id.plaacerecyclerview)
        textCurrentPlace = findViewById(R.id.currentPlaceName)
        imagePlace = findViewById(R.id.arrowPlaces)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        Cityname = intent.getStringExtra("Cityname")
        countryName = intent.getStringExtra("key")
        status = intent.getStringExtra("status")
        logAnalyticsForClicks(
            "StreetViewDisplayStreetViewCountry" + countryName + " of " + Cityname + "PlacesOnCreate",
            this
        )
        Log.d("status", "status: $status")
        if (status == null) {
            status = "city"
        }
        UtilityClass.Cityname = Cityname!!
        UtilityClass.countryName = countryName!!
        //  String famousCityImage = getIntent().getStringExtra("imageCity");
        textCurrentPlace?.setText(Cityname)
        listnerplaces()
        //initBannerAdPlace();


        //streetViewBannerAdsSmall();
    }

    override fun onResume() {
        super.onResume()
        placeModelArrayList = ArrayList<PlaceModel?>()
        // United State Information
        if (Cityname == "San Diego") {
            addSianDagoDataList()
        }
        if (Cityname == "Wyoming") {
            addWyomingDataList()
        }
        if (Cityname == "California") {
            addCaliFmPlace()
        }
        if (Cityname == "Washington") {
            addWashingtonfmPlace()
        }
        if (Cityname == "Mesa") {
            addMesaFmData()
        }

        //United KingdomInfo
        if (Cityname == "Edinburgh") {
            addEdinburghFmPlace()
        }
        if (Cityname == "York") {
            addYorkFmPlace()
        }
        if (Cityname == "Cardiff") {
            addCardiffFmPl()
        }
        if (Cityname == "Manchester") {
            addManchesterFmPl()
        }
        if (Cityname == "Glasgow") {
            addGlasgowFmPl()
        }

        // Turkey Info
        if (Cityname == "Istanbul") {
            addIstanbulFmP()
        }
        if (Cityname == "Ankara") {
            addAnkaraFmPlace()
        }
        if (Cityname == "İzmir") {
            addİzmirFmPl()
        }
        if (Cityname == "Antalya") {
            addAntalyaFmPl()
        }
        if (Cityname == "Edirne") {
            addEdirneFmPl()
        }
        // Switz
        if (Cityname == "Zürich") {
            addZürichFmPl()
        }
        if (Cityname == "Basel") {
            addBaselFmPl()
        }
        if (Cityname == "Bern") {
            addBernFmPl()
        }
        if (Cityname == "Geneva") {
            addGenevaFmPl()
        }
        if (Cityname == "Lausanne") {
            addLausanneFmPl()
        }
        //Germany
        if (Cityname == "Berlin") {
            addBerlinFmPl()
        }
        if (Cityname == "Munich") {
            addMunichFmPl()
        }
        if (Cityname == "Nuremberg") {
            addNurembergFmPl()
        }
        if (Cityname == "Mainz") {
            addMainzFmPl()
        }
        if (Cityname == "Hamburg") {
            addHamburgFmPl()
        }
        //France
        if (Cityname == "Merseille") {
            addMerseilleFmPl()
        }
        if (Cityname == "Strasbourg") {
            addStrasbourgFmPl()
        }
        if (Cityname == "Colmar") {
            addColmarFmPl()
        }
        if (Cityname == "Paris") {
            addParisFmPl()
        }
        if (Cityname == "Nice") {
            addNiceFmPl()
        }
        // Aus
        if (Cityname == "Adelaide") {
            addAdelaideFmPl()
        }
        if (Cityname == "Cairns") {
            addCairnsFmPl()
        }
        if (Cityname == "Darwin") {
            addDarwinFmPl()
        }
        if (Cityname == "Perth") {
            addPerthFmPl()
        }
        if (Cityname == "Gold Coast") {
            addGoldCoastFmPl()
        }
        //Norway
        if (Cityname == "Haugesund") {
            addHaugesundFmPl()
        }
        if (Cityname == "Oslo") {
            addOsloFmPl()
        }
        if (Cityname == "Bergen") {
            addBergenFmPl()
        }
        if (Cityname == "Trondheim") {
            addTrondheimFmPl()
        }
        if (Cityname == "Alesund") {
            addAlesundFmPl()
        }
        //Canada
        if (Cityname == "Toronto") {
            addToronFmPl()
        }
        if (Cityname == "Niagara Falls") {
            addNiagaraFallsFmPl()
        }
        if (Cityname == "Ottawa") {
            addOttawaFmPl()
        }
        if (Cityname == "Halifax") {
            addHalifax()
        }
        // India
        if (Cityname == "Jaipur") {
            addJaipurFmPl()
        }
        if (Cityname == "Bangalore Urban") {
            addBangaloreUrban()
        }
        if (Cityname == "Delhi") {
            addDelhiFmPl()
        }
        if (Cityname == "Chennai") {
            addChennaiFmPl()
        }
    }

    private fun listnerplaces() {
        imagePlace!!.setOnClickListener {
            logAnalyticsForClicks(
                "StreetViewDisplayStreetViewCountryOnBtnExit",
                this@PlacesActivity
            )
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                logAnalyticsForClicks(
                    "StreetViewDisplayStreetViewCountryOnExit",
                    this@PlacesActivity
                )
                mediationBackPressedSimpleStreetViewLocation(
                    this@PlacesActivity,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,
                    bindingplace!!.whiteView
                )
            }
        }

    private fun addSianDagoDataList() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1Kkj2430x8-iThQLgCEqDiUqUc307zrUn",
            "Balboa Park",
            32.7337361,
            -117.1667136
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=105PvOe4HglI5vYRZs4A2TNslYAo9QwNW",
            "USS Midway Museum",
            32.7136902,
            -117.1763293
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1WYTIPyT0Y0bldjqIjAOtx6RBmJ38HXnq",
            "La Jolla Cove",
            32.8504861,
            -117.2749877
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1fbOZl_G3wty-9Ld7tNfzkOu2glchdOYl",
            "SeaWorld San Diego",
            32.7643219,
            -117.2293652
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1Tnt3m1jL70KpI8AfMc3DfWXesoFJvvEK",
            "Seaport Village",
            32.707656,
            -117.1695116
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addWyomingDataList() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=111vr-oF87F3i4ufsg4f4zXfLacPVUXS3",
            "Yellowstone National Park",
            48.2618429,
            -117.4448421
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1xU_httcPtUNZKF3yjL7GzzfEkl-X8dj8",
            "Grand Teton National Park",
            44.024321,
            -112.3308548
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1NsugV5TKpck4eddZAKFE8ZaB6aPe5lVM",
            "Jackson Hole Mountain Resort",
            43.5303601,
            -110.8658809
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1Ri5T3k6dY_nzHPeaXNW_Efj0If-u7A6W",
            "Devils Tower National Monument",
            44.5902136,
            -104.7168055
        )
        // PlaceModel list4=new PlaceModel("https://drive.google.com/uc?export=download&id=1EuTPRfng41dasfX-47DVz33BEqysoS8T","Seaport Village",32.707656,-117.1695116);
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addCaliFmPlace() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1wL071kDf2RdHnUaG0DlfVPCuxTI3N4yB",
            "Disneyland Park",
            33.8116139,
            -117.9215716
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1AHKnspppRQn7HbPhMpN_6Sqmv3r218lb",
            "San Diego Zoo",
            33.3709439,
            -118.344192
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1QQdYXiWHH8_taOzvcBurVYpRWnTZkUFC",
            "Joshua Tree National Park",
            33.9002347,
            -116.1730248
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1yTE8IzIQac3zhKrUVmo7SlRCTMsUTpBY",
            "The Getty",
            34.0669527,
            -118.5544978
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1-meDpkF-netpeS5807E0Yd2xm_Fnj6ef",
            "Yosemite Valley",
            37.7387871,
            -119.670135
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addWashingtonfmPlace() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1yofSupjMxLUFmG_6M0INWvgfoZ_BNJwt",
            "The White House",
            30.2573622,
            -97.9473006
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1K-GGbs3s-0hLCR08vEy0k_ivzUlRXwcc",
            "Washington Monument",
            34.2733677,
            -96.4025354
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1LGFUpCR-10NjwFUoLXAIPT7a0LpV1GIc",
            "JSmithsonian National Museum",
            38.8898221,
            -77.0289361
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1UGP12VoVfCd0E1JhpHic2-8fzEhxHJJ4",
            "Thomas Jefferson Memorial",
            38.8810184,
            38.8810184
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1nHTjZKZPbbaLFHol2pIhPURbhEY-7nxu",
            "Rock Creek Park",
            38.8899431,
            -77.0945993
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addMesaFmData() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1jbwJ0due4X9Obxtul4fKwb-ll2EIaYrQ",
            "Papago Park",
            33.4539855,
            -111.9660744
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1XGrc2U6q1Kf8enacj87mEYhj9FyiVPdR",
            "Painted Mountain Golf Resort",
            33.4524687,
            -111.6980033
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1c74iHoMwKwQXPrNDDyWNgcN6QxO683-3",
            "Desert Canyon Golf Club",
            33.5821033,
            -111.717289
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=10mcS0YKl0ado8goWnWN2jb-K48K0Xtiy",
            "Schnepf Farms",
            33.2240017,
            -111.5910473
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=11TZtBlgPE12Xjwp7SoTu8XtRseSa5pX_b",
            "Riverview Park",
            32.8373921,
            -111.9998016
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addEdinburghFmPlace() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=17g902mQkxZ7W60B61WzTgbpnaDGVmNHS",
            "Calton Hill",
            55.9550577,
            -3.1914957
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1kPOHwfYZ7rQUES1HLKc9UyoshN1W1Zl0",
            "Edinburgh Zoo",
            55.9423649,
            -3.2707825
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=11dIIarLpkylsiknozGTmGhcXGVqVo2BP",
            "Royal Yacht Britannia",
            55.9821584,
            -3.1794408
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1A56SnEtEZPopJ_9V81gFntz0BlmQg5Oc",
            "Forth Bridge",
            55.9947701,
            -3.4112225
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1Bv2WpFsE3xLVTvxaV7Xj-mj6hN2U6DUr",
            "Princes Street Gardens",
            55.9499702,
            -3.2035743
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addYorkFmPlace() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1pKDd3HlwFi6qFPziJy9P45GTr1VQK_bW",
            "National Railway Museum York",
            53.9605498,
            -1.0985547
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=12r_dkC4-dhWa5hcOqyM8dx5xILldroRV",
            "York Minister",
            53.9623323,
            -1.0841092
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1tmgr5w76Ar-6tny3Qc9unFJAA43QL2jH",
            "The Web Adventure Park",
            53.9974801,
            -1.0924097
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1riCw6ry4nRTs08-B5LahRG991Rl7HXJa",
            "Knavesmire",
            53.9383946,
            -1.1661602
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1P4iS6wHKuThOpZmB2wpRrgwH6rmP83kS",
            "Hollywood Bowl York",
            53.9830467,
            -1.0549265
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addCardiffFmPl() {

        // thirdLine link are missing
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1TuoOYepNJ2mNLD87CpuJhJ5Psj4ioYqz",
            "Cardiff Castle",
            51.4822346,
            -3.1833654
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1RXES2nxm_KVY-JL7BPqYZdL21ahp88kX",
            "National Museum Cardiff",
            51.4856853,
            -3.1790386
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1fzZdrvLK0mk6wetPeXVMXg08iJtf5X-K",
            "Principality Stadium",
            51.4782118,
            -3.1848228
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1yT08-M8m-hiIMd5RCEuWcXReAaeoiA3T",
            "Cardiff Bay",
            51.4537533,
            -3.1914057
        )
        //  PlaceModel list4=new PlaceModel("https://drive.google.com/uc?export=download&id=1tbETpYPKYQ9mihBVGvRvIfJra_fOGs9w","Principality Stadium",51.4782118,-3.1848228);
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        // placeModelArrayList.add(list4);
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addManchesterFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1jtuAW5Fgu0ups4SXCWbyfeuUkMTsuND9",
            "Manchester Art Gallery",
            53.4696342,
            -2.2529287
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1upUpdTNZ69a0pu787Rt2Fj0dF717x8ar",
            "IWM North",
            53.469739,
            -2.3009292
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1MI1kO6GX4RlDYVrY9_DmQbPlGk3jCJ4E",
            "Old Trafford",
            53.4630589,
            -2.3000948
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=15N35DBv717_otuN5ZF69xAbHiClwFNP2",
            "Etihad Stadium",
            53.4823847,
            -2.2036348
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1TZduec7DdMyEvguT1ub0M8I2RMCPJdWk",
            "The Quays",
            53.4709923,
            -2.2961403
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addGlasgowFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=11dm3ejCjb0G3Mss48jfXMKcPr2KU2TUAC",
            "Riverside Museum",
            55.865106,
            -4.3083949
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1acb2FUCUrsAFMo5nZR9q0IYUj_-DKDl8",
            "Gallery of Modern Art",
            55.8651522,
            -4.3412256
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1b0NoT_HhS3dmdQOmKIWcHyqNFWGfAPoa",
            "People's Palace",
            55.8513336,
            -4.2390998
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1QsshWmy447cD4t5RVJFpzk1itXs4zqGr",
            "Glasgow Science Centre",
            55.858545,
            -4.2959917
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1DGnadgtCK0Q72Op89_aDKHuAweTvtQWe",
            "Glasgow City Chambers",
            55.8609934,
            -4.2509989
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addIstanbulFmP() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=12s0lKzSdim3OJZn-qtEdiUXn9wRJKONR",
            "Hagia Sophia",
            41.008587,
            28.9779863
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1sp5-oJ6p1UrjWcNv81xyS5qLjDZ42zhd",
            "The Blue Mosque",
            41.0054136,
            28.9746251
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=",
            "Topkapi Palace Museum",
            41.0115235,
            28.9811902
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1kdjZSU-NlZq1SmWhF8G5JHrsVdsdto2w",
            "Grand Bazaar",
            41.0106888,
            28.9658794
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1Hi3KinubgAiGjncdnfbeKZsYJ52Y5LYZ",
            "Basilica Cistern",
            41.008388,
            28.9756893
        )
        val list5 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=15g_B5oYLoBBfqqRHnUwplu1-ZNxisWeF",
            "Galata Tower",
            41.0256817,
            28.9719683
        )
        val list6 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1cbAHmzDogHwj6kljDZjYPuOYM70ps1Ce",
            "Istanbul Archaeological Museums",
            41.0116895,
            28.9791418
        )
        val list7 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1cbAHmzDogHwj6kljDZjYPuOYM70ps1Ce",
            "Isfanbul Theme Park",
            41.0719065,
            28.9190793
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeModelArrayList!!.add(list5)
        placeModelArrayList!!.add(list6)
        placeModelArrayList!!.add(list7)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addAnkaraFmPlace() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1d9bH9POZWEPBiwHjOgC3s4jRW3NPLC-Y",
            "Anıtkabir",
            39.925054,
            32.8325665
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=17P3e3iA8LyG6QNNp5jPPpJ3P2PSPRO7D",
            "Ankara Castle",
            39.9389881,
            32.8632104
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=",
            "Etnoğrafya Müzesi",
            39.9326914,
            32.8531931
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1HrVzKelZ8cXb-Bix3s6BxUrIqP1xE1WM",
            "Wonderland Eurasia",
            39.9466048,
            32.7812284
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1YL3g2GO9FVC53ZZ7ankPbSrwEfKOumf2",
            "Rahmi M. Koc Museum Ankara",
            39.9372505,
            32.8612996
        )
        val list5 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1Wir4q-cjmkBvMVSg-1NfR0gt7aKtg_Jh",
            "Altınköy",
            39.9729273,
            32.9531936
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeModelArrayList!!.add(list5)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addİzmirFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1tDINzpkKqVsUONs3Ryl1g1p1V7O6SPOv",
            "Ephesus Archaeological Museum",
            37.9489409,
            27.3654781
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=",
            "Izmir Wildlife Park",
            38.491302,
            26.9593944
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1M0ZRv8VrvL1Wjoe7OFOiwtcNO6PyMWpZ",
            "Museo archeologico di Smirne",
            38.4137024,
            27.1263466
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=15RtFysUscUUrl6coQo87z0u_pjqXaCml",
            "KEY Museum",
            38.2016535,
            27.3474038
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1zSGjCxKqfqb_PqoVlOHkbSvVrQNGgm8a",
            "Smyrna Agora Ancient City",
            38.4184382,
            27.1367195
        )
        val list5 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=",
            "Aqua City",
            38.3887118,
            27.032649
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeModelArrayList!!.add(list5)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addAntalyaFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1Y2tY2sq_hqphETo9mi83vZ2GhBlcAMBb",
            "Antalya Kaleiçi",
            36.8718322,
            30.7045659
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=11BX2e7ZavIyvubmPiZP0QY1dPtu_og7oK",
            "Konyaaltı Plajları",
            36.8646661,
            30.6351117
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1dVB-thfP5K8VC2IYTBBj1A-tDHOxQG26",
            "Antalya Museum",
            36.8860237,
            30.6774669
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1U-VqbVHos-SoqR90tlDtV_o9RzOMBXUC",
            "Antalya Aquarium",
            36.8792175,
            30.6580911
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1rJI0PZ9Ec2nozEOh7-WQgHsCwFpBrEeB",
            "Karaalioglu Park",
            36.8796482,
            30.7026915
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addEdirneFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1cNR_g6UhznuYoz6Cr_Bju1gHg5Fx3ru_",
            "Selimiye Mosque",
            41.6781067,
            26.5570951
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=11TuvbVU9Y6aKndqCPxlMM8fgPOzKxx4ZZ",
            "Lausanne Monument",
            41.65269,
            26.5187173
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addZürichFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1kNwWD_Il1fUTp6-RBThG38rgc6mSN6Ot",
            "Bahnhofstrasse",
            47.3715915,
            8.5364151
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=127OuoQCmwYDnT4XYblF8FqRDi39t0MsG",
            "Swiss National Museum",
            47.3790558,
            8.5383605
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1Rb_fgE8WMgmRER1da_d3tSRfkYjsHaLI",
            "Zoo Zürich",
            47.3870227,
            8.572342
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1s69JePLjXHVf8DDPLcf09MQrwPW_KOoK",
            "Lindenhof",
            47.3730337,
            8.5385999
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1jsZ9IBM9O5gGL1zzMUZCKVZlEAACqweP",
            "China Garden",
            47.3548182,
            8.5345826
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addBaselFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1lA_uxZK-rYNf0-bDOu_9niwfRFxTnoCy",
            "Zoo Basel",
            47.547416,
            7.5765749
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=1wTjNq6YqBdKkiOPFqymIa2o6bu4g6j7t",
            "Spalentor",
            47.5579357,
            7.5792573
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1KSvMf_RVRqpX_DWU6EoXAgTAWDGFZq32",
            "Basel Minster",
            47.5564619,
            7.5902546
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1yiJYoKnsG_hxXbx5rpZH4NQuJrdaTmCV",
            "Basel Paper Mill",
            47.5546797,
            7.6012793
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addBernFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1olUZ-zVIuDQNhCo-eJMUnmNCboC4-aEV",
            "Zentrum Paul Klee",
            46.9488946,
            7.4719093
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=15T0nmS-P0YyREckP5kdqYAe8I2WjXh0h",
            "Zytglogge",
            46.947978,
            7.4456023
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1AMmS1Uv4FFwc8Qj5yGnKWFW9bmv9F6n9",
            "The Parliament Building",
            46.9465645,
            7.4420672
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1KPCqhkV_O31rQe-0-lAThRR4G2vV471K",
            "Oeschinen Lake",
            46.4985322,
            7.7167284
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1NrAhPveCwOY7_34IJRR7Z3ClY1T78HB4",
            "Universität Bern Botanical Garden",
            46.9528881,
            7.4425215
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addGenevaFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=14K_s6ZFS71zCtJWrt4QLtcZue_-gmeg9",
            "The Geneva Water Fountain",
            46.2073926,
            6.1537141
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1Us9nTD35a424jJAT-II1ANef4ySJVOn0",
            "Musée Ariana",
            46.2254335,
            6.1366133
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1xX4eVTntxRahleYp1P-UM6TXqJoQVtsW",
            "Parc de La Perle du Lac",
            46.2205096,
            6.1504502
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1_Kx2jqybVdEgrJnFFvzoiFsF-7GExJel",
            "Mon Repos Park",
            46.2195797,
            6.1490191
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=198pk9gTUNWky0osNwSIkQRAPo0gkwa-1",
            "La Rade",
            45.6164961,
            -1.0421514
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addLausanneFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1JJ5QDFoeGv841U029S5yQyvIEmnVrKsq",
            "Cantonal museum",
            46.362197,
            6.2085439
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=11cPMDZNrLrqN_PDQeBMkKDVN7C_AuT0r",
            "Lac de Bret",
            46.5137114,
            6.7650146
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1xjiyFvm3xtnqhtL4VhGiqsN8WUCZqoL0",
            "Palais de Rumine",
            46.5234231,
            6.631903
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addBerlinFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1fwJaufvuc0oURzSpP-teBnzttIEZSg_N",
            "Brandenburg Gate",
            52.5162746,
            13.3689494
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=114rMY89AKSpWy6EoT0WYSDlKsfwabd8k",
            "Reichstag Building",
            52.5186234,
            13.3739985
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1mlAGAF6JEGHUi-8d4wfFFM-oSrf9vmz8",
            "Berliner Fernsehturm",
            52.5219381,
            13.4065362
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1LyVaZyqEpUe5BzZMRaSMe95--wvvpqAk",
            "Altes Museum",
            52.5194697,
            13.3965558
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1j4sO_FoL7bLFoLkT-rWqTSUjX1AazmgC",
            "Bode Museum",
            52.5218969,
            13.392068
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addMunichFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=12fk3MHisQ_fZJksf0q7iEMO6MZD8mMpk",
            "Nymphenburg Palace",
            48.1582711,
            11.5011256
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1YdZbGqgFogjSA-Pi61rcJbKKQVA3otaq",
            "Marienplatz",
            48.1373968,
            11.5732598
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1K9Kp1iIn6IZ3rFTQfYJQ5gAJ23GisuED",
            "Deutsches Museum",
            48.1298707,
            11.5746975
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1x-EBPQYTlVvn_I8-xAA91KRXwFLoxbCM",
            "Olympiapark München",
            48.1754682,
            11.5496083
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1JoCPn57GxX6jZatlSKZYXdOjSCy_ofFh",
            "BMW Museum",
            48.177016,
            11.5566021
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addNurembergFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=11MOCK88tzcRSemLVF6JryjcTfLR5CPZVT",
            "Imperial Castle of Nuremberg",
            49.4579255,
            11.0736493
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1iNI1_Qcg31e8NLH6bZvjSJ6JPVsWP_tb",
            "Germanisches Nationalmuseum",
            49.4482487,
            11.0733227
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1hVz35rrfC2H_0E2xAqiwXE15fhnCtw2i",
            "Nuremberg Zoo",
            49.4472914,
            11.1428978
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1jn-F5P8RdbcraX98oRF_sz1A7QF0wb2o",
            "Klingender Wasserfall",
            49.476378,
            11.2957958
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1G3Agj4Dzv2NjRv6mcUJ85NYcm2zEXaXr",
            "Stadt Park",
            49.398441,
            10.9634332
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addMainzFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=13n1nv9cgfziUy0g9ylDaD8Yv-srPmPBN",
            "Gutenberg Museum",
            49.9994822,
            8.2732668
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1T9JpSqaF9hRtveT_Fe8izwElCs71TULL",
            "Johannes Gutenberg University Mainz Botanical Garden",
            49.9908251,
            8.2385091
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1FdNqGoiq1YQxo3hEcJc-vpIs6on2aSer",
            "Rettbergsaue",
            50.0329406,
            8.2046081
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1MjBmqKmGSxC6Pa_yauERrN-JWXoMMgcl",
            "Spielpark Hochheim",
            50.0298574,
            8.3530363
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addHamburgFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1FdB4mUGnB920vvbTF1WH9j3BL_ZdrfEv",
            "Miniatur Wunderland\n",
            53.5437308,
            9.9867191
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1HoC6CJDgHdirebAbLWcuzBkppKJiqges",
            "Alster",
            53.6170425,
            9.9688654
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1IqQ34zfqPw0EnHTFKW2kN4tEt5ozALMj",
            "Heinrich Hertz Tower\n",
            53.5631104,
            9.9737409
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1kedVFsQF4gqSv-0x84crPUcbpP90wJUA",
            "Arriba Erlebnisbad & Saunadorf",
            53.6893832,
            9.9990813
        )
        // PlaceModel list4=new PlaceModel("https://drive.google.com/uc?export=download&id=","Biebrich Palace",50.0374105,8.2319085);
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        // placeModelArrayList.add(list4);
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addMerseilleFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1o9ocSe9fgoYFNGsz8b0hDABr2qsXLx9Q",
            "Mucem - Museum of Civilizations of Europe and the Mediterranean",
            43.2966941,
            5.3588938
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1wpHutGWHBRZxTfZC-_txfxQagZrrow_r",
            "Old Port of Marseille",
            43.2944643,
            5.3557599
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1PLBVGLjkuuvLodieZk1SevWMyuLqRMfc",
            "Basilique Notre-Dame de la Garde",
            43.2839533,
            5.369049
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1_xP6qzEaNqKmNNdTqTP8v2m5ZUvbuxpx",
            "Massif des Calanques",
            43.2166662,
            5.4245782
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1PqdxW86fjnTmSCefiBOpo0DCYBwdvHzC",
            "Palais Longchamp",
            43.3054783,
            5.3946108
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addStrasbourgFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=12wVKjJgi7hdw6IWtgaaavm-iBAkKQNPv",
            "Cathédrale Notre Dame de Strasbourg",
            48.5818799,
            7.7488461
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1n2HZ4TcjERtdon41R2zgHf05-ge31ZPA",
            "Ponts Couverts de Strasbourg",
            48.5800806,
            7.7371305
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1yeQ4xgzVjEnfpVYcCOxrnlN-Dhiy9NWu",
            "Batorama",
            48.5811574,
            7.7489159
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=13OfDOBDRLjglX32ffmxQp_Mi4V8t-K0b",
            "Jardin des Deux Rives",
            48.5682576,
            7.7981307
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1qAs5xldsclYISlfKArHO3J-C9HHdJXLo",
            "Botanical Gardens of Strasbourg University",
            48.5837647,
            7.7640521
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addColmarFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1hatKzT53jvMreXJBMtiX_4UMh4wRiSlE",
            "La Petite Venise",
            48.0741632,
            6.7992735
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1lusESt5GbL2qRreC0LAyX_cd_cu-ihla",
            "Château de Pflixbourg\n",
            48.067126,
            7.2521711
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1-ofWJC0JN57Cu3Itew-9c9jUVKXishv_",
            "Little Venice\n",
            48.0746473,
            7.3573959
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1PZqeemcknZb2nP3kwqqrdAdJLwEMS6bn",
            "Parc du Champ de Mars",
            48.4332691,
            2.5835694
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1yytZ8gQJk1ICnMpuzEQ8qjpGGT-8DFRT",
            "Base Nautique",
            47.7963908,
            0.304408
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addParisFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1C2muQZQhgkKP_NShqbqvxaRvEIL3teYj",
            "Eiffel Tower",
            48.853909,
            2.2818805
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1uJyb1X_xD51QliSRbfWbs4UcNzaLEc2B",
            "Louvre Museum",
            48.8606111,
            2.3354553
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1AWr0U3q4eK0DkkB2EXUu1oihGWFaTq6_",
            "Cathédrale Notre-Dame de Paris",
            48.8529682,
            2.3477134
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1Yq6XLLEJd8OUfN7f9PLQRgKEYjSQPZhC",
            "Arc de Triomphe",
            48.8737917,
            2.2928388
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1JibcwGe-gKk5kLWq4Q4Omk0zu1iU2T_4",
            "Montmartre",
            48.886192,
            2.3343347
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addNiceFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1oOwzvBwWduqlzCOtuYtrzzekeAzonGrN",
            "Promenade des Anglais",
            43.6951073,
            7.2656366
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1IH-FCRNWdJsBU7v6qNq4VLKGUv5lmDLS",
            "Modern and Contemporary Art Museum (MAMAC)",
            43.7015032,
            7.2761626
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1qtncfF8SjFIZhUj4_7jnbYID2AOlmo-t",
            "Castle of Nice",
            43.6999991,
            7.2745785
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1i8Y3fz7cbe6EGFm8JZ9BNeeDHzUnSxtW",
            "Nice Cathedral",
            43.6971123,
            7.2672077
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1BJPGmEqWzXZMAVbUxEsLCkEl25FI1Iqk",
            "Avenue Jean Médecin",
            43.702144,
            7.2649361
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addAdelaideFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1RAtODrsz_AAEd8punpXYo5pA20L03Dkp",
            "Rundle Mall",
            -34.9237023,
            138.6000075
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1bHUEVZ_HhUZiWQsMLZGB6i8zME0vQwKy",
            "SkyCity Adelaide",
            -34.9208174,
            138.595088
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1KBIvmDBpH2V936D5OxlEcOTLobEFE6n3",
            "Adelaide Parklands",
            -34.9394369,
            138.5967256
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1I0jnBFeRZE5V4l-OSpdvBhSb6OcGfZxP",
            "Semaphore Beach",
            -34.8369199,
            138.4694956
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1GwlLKXbCrcRjNuaewRhF87hduyaqhQWl",
            "Elder Park",
            -34.9180527,
            138.5962305
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addCairnsFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1LAQa5IDLpXOcQYgM1VUkrBOb_DM3LjYg",
            "Skyrail Rainforest Cableway",
            -16.847597,
            145.6928746
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1nBZCM7Hj6icwC4FEpv3P613cL5JDssHM",
            "Fitzroy Island",
            -16.9329019,
            145.9851632
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1tdTz8grE2OJDIsKq54wopPM9YXr8j7zW",
            "Kuranda Scenic Railway",
            -16.8183623,
            145.6365339
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1XvvwuCx0TvRiMjk7Imy-jU7SUEg7VDjR",
            "Cairns Esplanade Lagoon",
            -16.9194249,
            145.7760021
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1luh19610Z_iCkuDPIqblr23miFYSMWih",
            "Muddy‘s Playground",
            -16.9146152,
            145.7704095
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addDarwinFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1KU6OLgLWtlc9C0Q13___4BrRyGWWEeIS",
            "Darwin Military Museum",
            -12.4074412,
            130.8170836
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1AeMPQi0H62hPQ6XfpQtmNdthbnmVWYK2",
            "Darwin Wave Pool",
            -12.4667965,
            130.8451044
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1wtUma7boNrXWiewJLpcckO7i8hKM5aH",
            "Mindil Beach Sunset Market",
            -12.466796,
            130.8297834
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1NbuoPqnRLrrGGenPLwhXIygan8wmKzTq",
            "Fort Hill Wharf",
            -12.4721461,
            130.8434209
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1V4691PwSlMQCBXdhfUCPfyEN1cxcFvjR",
            "Sunset Beach",
            33.8955733,
            -78.5532003
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addPerthFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1SdTBzS4MRdz4JifMRpY6GXYXJYUM7pb0",
            "Kings Park and Botanic Garden",
            -31.9609106,
            115.8300042
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1rVBWgOpXPwj7ONWUv5cQ9tcrfvJjsppn",
            "Cottesloe Beach",
            -31.9561717,
            115.8510164
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1z8NtSSvYenTzL0rwQeV08dlLFQUGgF9Z",
            "Elizabeth Quay",
            -31.9561717,
            115.8510164
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1h8AliZfY1SrGLadHePOs7PhiL_DKC4Ap",
            "The Bell Tower",
            -31.9589457,
            115.8560317
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1tHY6WlobsoWqh16CPHUD9WWKdcsAxSGp",
            "The Perth Mint",
            -31.956821,
            115.8672163
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addGoldCoastFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1LUGoXkasTJ9GcAucGrDPR_p4LlQ7jvER",
            "Warner Bros. Movie World",
            -27.9069522,
            153.3109216
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1SNyYAWhQegqL7L03EUvn3LLxCZZk38uV",
            "Dreamworld",
            -27.8630715,
            153.3138102
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1hjAjAJpO5UZE1Fzcmo9H4rW10lBaYqei",
            "Sea World",
            -27.9770767,
            153.3899858
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1G5L4NKKV11M5ScD_MjWcss-NY49J6ll_",
            "SkyPoint Observation Deck",
            -28.00618,
            153.4290886
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1JoqNGMTuoD0TelkolE1velNAl97kgZXI",
            "Crown Melbourne",
            -37.8228266,
            144.9568675
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addHaugesundFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=12tUGl2_VK5tLpGvBsFnIjh6Wo_UjmB7b",
            "Haraldshaugen",
            59.4289457,
            5.2568433
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1yta2tEv2fcK9oq0k-7MJnic4h2OMQIDd",
            "Risøy",
            59.4093087,
            5.2557961
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1TB0yIhoZ9kjs6JTUNkyWZZAUfOyaGuCO",
            "Karmsund bridge",
            59.3754332,
            5.2931035
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addOsloFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1mga_dDn45psuBp7yiapXSDScUnfhKsxO",
            "Viking Ship Museum",
            59.9049448,
            10.6822188
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1kpTXfgSYbpO_BLF4MF9FPHoPIcOBmBwk",
            "Oslo Opera House",
            59.9049448,
            10.750342
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1jSaHJi-cF5zCVL0w52PCAaEZnvGNykoo",
            "The Royal Palace",
            59.9170428,
            10.7251882
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1-Uh_bvT7J9Jw-bzxgxd_TuH3qFjoUQIu",
            "Nobel Peace Center",
            59.911543,
            10.7283573
        )
        // PlaceModel list4=new PlaceModel("https://drive.google.com/uc?export=download&id=1W5wuKexXnGl1krS-LaKCRnBSmv2OAs0x","Oslo Winter Park",59.9878873,10.6646585);
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        // placeModelArrayList.add(list4);
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addBergenFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1cmiAwCq8dY3j4S9rNyhPR5MAP_Nnp6PP",
            "Bryggen",
            60.3975672,
            5.3223606
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1ZToot0nKqLpEKp5rXuXPb0sCHUCglsBB",
            "Ulriken",
            60.3774855,
            5.3781973
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1I3vvETtexCvFIb4t_ns2_B9CQferAUaD",
            "Rundemanen",
            60.4127771,
            5.3562452
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1z51OZNznNq-GQY6xSdQSx-cvuOKh7lSb",
            "Gullfjellet",
            60.3531029,
            5.3697894
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addTrondheimFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=19EN5U5qk2AK9W9J7_u548bkSN74Z7gPA",
            "Ringve Music Museum",
            63.4473256,
            10.4524368
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1r69ehSZgPfbBxagfJncipAmcRpfAC_gE",
            "Old Town Bridge",
            63.428218,
            10.3993599
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=14kIaG-tDqkrlLRVRwbKFbszSdrd2zHBJ",
            "Rockheim",
            63.4387649,
            10.3993725
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1S-wNlQyLuV4xtad3oYklig2A6pLCwYPA",
            "Munkholmen",
            63.4523505,
            10.3802024
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addAlesundFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1T8N0KsPK__0nRYx6JEcLdQR2F6TevxFr",
            "Alnes Fyr",
            62.4899551,
            5.9653075
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1MOxncWZa2rrE461n_KBHiGq-Zc8dJglD",
            "Atlantic Sea-Park",
            62.4654468,
            6.0944536
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1KF7Bw_Y7kqMfngIWcfpuVmgUZB2KLKrc",
            "Godøy",
            62.4714399,
            5.9559244
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1phauGhRPaaExva3TEpGyQiE0FL2t9ye7",
            "Aksla Viewpoint, Alesund",
            62.4747285,
            6.1629116
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addToronFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1r8EGUHpmoahcj6DwsQz-rIlo0zURys6f",
            "CN Tower",
            43.6425662,
            -79.3892455
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=19elxD2tYopfKn-34MSIQTN0opYVDB9VC",
            "Royal Ontario Museum",
            43.6677097,
            -79.3969658
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1y8-Y9yGaN2EJ9LaxiDIoY3NahoQufnW0",
            "Toronto Islands",
            43.6230226,
            -79.394696
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1gzO64SmkiUBTmRyZC_2YudOrpd29lP8V",
            "Ripley's Aquarium of Canada",
            43.6424036,
            -79.3881603
        )
        val list5 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=18lQCgUtmejI8Tho_t6ykm7W9bPxou0ZA",
            "Canada's Wonderland",
            43.8430176,
            -79.5416512
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list4)
        placeModelArrayList!!.add(list5)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addNiagaraFallsFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1f9rmtuH3YOe7jYTtQBdxz2jithXPU3iv",
            "Niagara Falls",
            43.0538471,
            -79.2281213
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1d0Ds4Ba_CEA6ne6DjublUIF-nfr6RGgb",
            "Skylon Tower",
            43.0850931,
            -79.0817507
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1sWe122kP4VpLHhSUh9Fw7laUbHK1UHa8",
            "Cave of the Winds",
            43.0824906,
            -79.0728259
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=12K2t2iInYrQcnSaJ-0EOtBuSPF3VhXNj",
            "Niagara SkyWheel",
            43.090929,
            -79.0776986
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1pfe25NAVdqM0PFDrPrgPZgIdbQyav1Ur",
            "RWhirlpool Aero Car",
            43.1180306,
            -79.0709776
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addOttawaFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1C_p0WK0G0lfbsHRZQFlofiWPLGZy1D2P",
            "National Gallery of Canada",
            45.4295387,
            -75.7010949
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1OOGN5AaUK6_luepLz85IDLuH2eqkOc1t",
            "Parliament Hill",
            45.4235937,
            -75.7096837
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=12nAtZ9GVEBYTSa9kJA5cI7xtTczH3t8E",
            "Rideau Canal",
            45.3964917,
            -75.7270781
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1eNkf1IIYTaQ0I_gtwLS1BS9YLOezr0rX",
            "Canada Aviation and Space Museum",
            45.4586167,
            -75.6459753
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addHalifax() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1ClrMHIYC5fAPPEQWFXH03_rxvDABrhhl",
            "Halifax Citadel National Historic Site",
            44.6470128,
            -63.5848097
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1Pl8fF_Sa5KxutbZ_0uMQHjXlu_25Hgj4",
            "Halifax Public Gardens",
            44.642761,
            -63.5843125
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1diWqB9ExtU6KKad8Av2Kouz7liEAhqkk",
            "Point Pleasant Park",
            44.6277351,
            -63.5689829
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1PhIcaddFXRTklwHxUfDaSWGkOqUHYVfX",
            "Halifax Waterfront",
            44.6457239,
            -63.5868433
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1akvOpNYh046s474l7Q26dmIDmqtX0WRO",
            "Blue Mountain - Birch Cove Lakes Wilderness Area",
            44.679231,
            -63.7300587
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addJaipurFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1Rdv9jLAOQhFauvb1X33Ld4Zh_LGeBjR3",
            "Jal Mahal",
            26.9854913,
            75.8491567
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=16ay3MTgPTlG7NGXVUCtttPbxNIgmLgSx",
            "Maotha Lake",
            26.9832446,
            75.8478848
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=15Gn7bYK1SxKPIDTH4YcXXGDYQSkZPw9H",
            "Ramniwas Bagh",
            26.9127473,
            75.8158028
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=10vPY3i1wbcQOgzOG6hgGk8WMx__BUupV",
            "New Hawai-Jahaj waterpark",
            26.9442611,
            75.6716863
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=19H0GUD7Hjc9g8BXg-HbWA1kWdP541qhb",
            "Hanuman Sagar Lake",
            26.9892776,
            75.843284
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addBangaloreUrban() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1guYBG7UdS_ebY7jsSljZGT5-zgsQe8za",
            "Bangalore Palace",
            12.9987712,
            77.5899184
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1d3BhncZKnRN23y8wq8YA3Qr60nXtxqlF",
            "Cubbon Park",
            12.9763602,
            77.5754
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=16ESAtcN6rvJRVeJ1j2Q4fk2WYGuj-zdj",
            "Commercial Street",
            12.9821947,
            77.6061598
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1Ukamy5EIbxjQjFU178NrwKIUPZWgTFeg",
            "Turahalli",
            12.8883753,
            77.519305
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1OhVuvQSH_bRU1Yq8S7Sw0UYDRDsGLqzP",
            "Tipu Sultan's Summer Palace",
            12.9593565,
            77.5714528
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addDelhiFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1BBSknsw0UvPy38pvykbqds_e0hpJ0pzX",
            "Red Fort",
            28.6561639,
            77.2388316
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1msnJ-fPNBPVJ7BMTSBVxYfjRNeSdPQYw",
            "India Gate",
            28.6129167,
            77.227321
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1rwdZtuv7Qeb89__LNvhGpS-z4cK7HIN4",
            "Akshardham",
            28.6126735,
            77.242243
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1PeiUP0KQj9XVHQ14br989ad3Fb8nbL_e",
            "Lotus Temple",
            28.5534967,
            77.2566377
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1ZnNIr9NCBjzLTENsU_TTFZUGOX2Ve-VC",
            "Deer Park",
            28.5535723,
            77.2238067
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }

    private fun addChennaiFmPl() {
        val list = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1Yl3_AqlVG-4-fpAtvrh1f-kRnQqpRym8",
            "Marina Beach",
            13.0437642,
            80.2663439
        )
        val list1 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1winUvHKCvIcy-tIVePvz9o6h0MXNJFBO",
            "Queensland Amusement Park",
            13.0300407,
            80.0254247
        )
        val list2 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1Hjhgy-Yhn-L0_1oZd-RUSLXx100xAjO4",
            "Chembarambakkam Lake",
            13.0062679,
            80.0127831
        )
        val list3 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=1q3Vpe3ZSWAvn69rd3nY_e7qKcr7YlVd1",
            "Cholavaram aeri",
            13.2275905,
            80.1414852
        )
        val list4 = PlaceModel(
            "https://drive.google.com/uc?export=download&id=16DT6kB3Gzm7YWvDv3BIf5mkGdO-QDJ74",
            "Snow Kingdom Chennai",
            12.9131717,
            80.2489913
        )
        placeModelArrayList!!.add(list)
        placeModelArrayList!!.add(list1)
        placeModelArrayList!!.add(list2)
        placeModelArrayList!!.add(list3)
        placeModelArrayList!!.add(list4)
        placeRecyclerview!!.layoutManager = LinearLayoutManager(this)
        placesAdapter =
            PlacesAdapter(this@PlacesActivity, placeModelArrayList!!, bindingplace!!.whiteView)
        placeRecyclerview!!.adapter = placesAdapter
    }
}
