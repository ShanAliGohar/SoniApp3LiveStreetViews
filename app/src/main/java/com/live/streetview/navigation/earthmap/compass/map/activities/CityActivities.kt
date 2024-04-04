package com.live.streetview.navigation.earthmap.compass.map.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation
import com.live.streetview.navigation.earthmap.compass.map.Model.CityModel
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.activities.utils.UtilityClass
import com.live.streetview.navigation.earthmap.compass.map.adapters.CityAdapter
import com.live.streetview.navigation.earthmap.compass.map.adapters.OnItemClickListener
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityCityActivitiesBinding

class CityActivities : AppCompatActivity(), OnItemClickListener {
    var recyclerView: RecyclerView? = null
    var cityAdapter: CityAdapter? = null
    var imageCity: ImageView? = null
    private var textView: TextView? = null
    private var cityModelArrayList: ArrayList<CityModel>? = null
    var countryName: String? = "United Kingdom"
    var bindingCity: ActivityCityActivitiesBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_city_activities);
        bindingCity = ActivityCityActivitiesBinding.inflate(
            layoutInflater
        )
        setContentView(bindingCity!!.root)
        textView = findViewById(R.id.textspecificountry)
        recyclerView = findViewById(R.id.cityRecyclerview)
        if (intent.hasExtra("key")) {
            countryName = intent.getStringExtra("key")
        }
        UtilityClass.countryName = countryName!!
        textView?.text = countryName
        logAnalyticsForClicks(
            "StreetViewDisplayStreetViewCountry" + countryName + "CitiesOnCreate",
            this
        )
        //call back back press
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        cityModelArrayList = ArrayList()
        cityAdapter = CityAdapter(this@CityActivities, cityModelArrayList!!, this)
        val gridLayoutManager1 = GridLayoutManager(this@CityActivities, 2)
        gridLayoutManager1.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (cityAdapter!!.getItemViewType(position)) {
                    0 -> 2
                    1 -> 1
                    2 -> 2
                    else -> 1
                }
            }
        }
        recyclerView?.layoutManager = gridLayoutManager1
        recyclerView?.adapter = cityAdapter
        bindingCity!!.imageArrowBackCity.setOnClickListener {
            logAnalyticsForClicks(
                "StreetViewDisplayStreetViewCountryOnBtnExit",
                this@CityActivities
            )
            onBackPressedDispatcher.onBackPressed()
        }

        if (countryName != null) {
            if (countryName == "United Kingdom") {
                addUnitedKingdomDataist()
            }
            if (countryName == "Switzerland") {
                addSwitzerlandDataList()
            }
            if (countryName == "Germany") {
                addGermanyDataList()
            }
            if (countryName == "France") {
                addFranceDataList()
            }
            if (countryName == "Canada") {
                addCanadaDataList()
            }
            if (countryName == "Australia") {
                addAustrilliaDataList()
            }
            if (countryName == "Turkey") {
                addTurkeyilippinesDataList()
            }
            if (countryName == "Norway") {
                addNorwayDataList()
            }
            if (countryName == "India") {
                addIndiaDataList()
            }
            if (countryName == "United States") {
                addUnitedStatesDataList()
            }
        } else {
            Toast.makeText(this, "No Country Found", Toast.LENGTH_SHORT).show()
        }
        //streetViewBannerAdsSmall();
    }

    private fun addUnitedKingdomDataist() {
        val list = CityModel(
            "https://drive.google.com/uc?export=download&id=1Bz9yoW7Ov5LfZTbOO56tcfUwgkThX0gN",
            "Edinburgh"
        )
        val list1 = CityModel(
            "https://drive.google.com/uc?export=download&id=1CJWKLzZXnW7wAstv98Qm6i3DVmiuCbf5",
            "York"
        )
        val list2 = CityModel(
            "https://drive.google.com/uc?export=download&id=1sVXOADA_t0znQOoH-kYSHPB5SYRNqzdZ",
            "Cardiff"
        )
        val list3 = CityModel(
            "https://drive.google.com/uc?export=download&id=1S2N_oB8zutvoCwQafsrwiaPMEwwA7ZtE",
            "Manchester"
        )
        val list4 = CityModel(
            "https://drive.google.com/uc?export=download&id=1u0w8GXqICfuy0fZaidSFmh5sKhNThY5j",
            "Glasgow"
        )
        cityModelArrayList!!.add(list)
        cityModelArrayList!!.add(list1)
        cityModelArrayList!!.add(list2)
        cityModelArrayList!!.add(list3)
        cityModelArrayList!!.add(list4)
        cityAdapter!!.notifyDataSetChanged()
    }

    private fun addSwitzerlandDataList() {
        val list = CityModel(
            "https://drive.google.com/uc?export=download&id=14p-EDSeApOQB4fmR5aH_QfJjHlL5Jv1H",
            "Zürich"
        )
        val list1 = CityModel(
            "https://drive.google.com/uc?export=download&id=1PeUKpXa0MavA_KnHEZznLHURyjx0PNs2",
            "Basel"
        )
        val list2 = CityModel(
            "https://drive.google.com/uc?export=download&id=1QC0ZG2YBfuMMnPYLA9d_jOLLYFT-9t0k",
            "Bern"
        )
        val list3 = CityModel(
            "https://drive.google.com/uc?export=download&id=1WP4N_avvGVtIeiac5JSabhYiEfxXA0Wl",
            "Geneva"
        )
        val list4 = CityModel(
            "https://drive.google.com/uc?export=download&id=19DYG0kbJA0hbZud6lkOHgZzaIeasyWpN",
            "Lausanne"
        )
        cityModelArrayList!!.add(list)
        cityModelArrayList!!.add(list1)
        cityModelArrayList!!.add(list2)
        cityModelArrayList!!.add(list3)
        cityModelArrayList!!.add(list4)
        cityAdapter!!.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addGermanyDataList() {
        val list = CityModel(
            "https://drive.google.com/uc?export=download&id=18NC8xo-QmsdfDpWED9pyEiBv8izdZ0JY",
            "Berlin"
        )
        val list1 = CityModel(
            "https://drive.google.com/uc?export=download&id=1x56zcQCMv2c1y2Ku5F23QJNBnX_rsox5",
            "Munich"
        )
        val list2 = CityModel(
            "https://drive.google.com/uc?export=download&id=17QohYExWlU6wSPjDzGJT4Hj8WN8vLaEo",
            "Nuremberg"
        )
        val list3 = CityModel(
            "https://drive.google.com/uc?export=download&id=1b7yNltHuBCn-lOiXLUFK8lfED3HhkeLv",
            "Mainz"
        )
        val list4 = CityModel(
            "https://drive.google.com/uc?export=download&id=19hh7yIhD6rFjsFtvBkdKqMc4U_nlBCeG",
            "Hamburg"
        )
        cityModelArrayList!!.add(list)
        cityModelArrayList!!.add(list1)
        cityModelArrayList!!.add(list2)
        cityModelArrayList!!.add(list3)
        cityModelArrayList!!.add(list4)
        cityAdapter!!.notifyDataSetChanged()
    }

    private fun addFranceDataList() {
        val list = CityModel(
            "https://drive.google.com/uc?export=download&id=1k8hAPwFY1AbGkTzExD522q96bAJHASMa",
            "Merseille"
        )
        val list1 = CityModel(
            "https://drive.google.com/uc?export=download&id=1g1srb2vinmzy3MVeidsl3i-A-nL_BO65",
            "Strasbourg"
        )
        val list2 = CityModel(
            "https://drive.google.com/uc?export=download&id=1sOR_MIJd4_zSGEnfNLaa9-bojgWnUCgJ",
            "Colmar"
        )
        val list3 = CityModel(
            "https://drive.google.com/uc?export=download&id=1qGHtp7goMGX52Gh5pofF3XqPqe9jGTKv",
            "Paris"
        )
        val list4 = CityModel(
            "https://drive.google.com/uc?export=download&id=1p7SPq9Liovf_8M2Ka8tsXKjULNW3eKag",
            "Nice"
        )
        cityModelArrayList!!.add(list)
        cityModelArrayList!!.add(list1)
        cityModelArrayList!!.add(list2)
        cityModelArrayList!!.add(list3)
        cityModelArrayList!!.add(list4)
        cityAdapter!!.notifyDataSetChanged()
    }

    private fun addCanadaDataList() {
        val list = CityModel(
            "https://drive.google.com/uc?export=download&id=1APt0uY2BEGTjwOBxxKK1tRrIx77OUaHH",
            "Toronto"
        )
        val list1 = CityModel(
            "https://drive.google.com/uc?export=download&id=1yuB8EG0ajEBwLGFwDBBDs9jd5AX0Y66y",
            "Niagara Falls"
        )
        val list2 = CityModel(
            "https://drive.google.com/uc?export=download&id=1CVVsRz9xELzus4SNschAXUe4GO2hWY2G",
            "Ottawa"
        )
        val list3 = CityModel(
            "https://drive.google.com/uc?export=download&id=1uG3e9kxI_0i0nFg3Mo-waXmaoJXlPgIp",
            "Halifax"
        )
        cityModelArrayList!!.add(list)
        cityModelArrayList!!.add(list1)
        cityModelArrayList!!.add(list2)
        cityModelArrayList!!.add(list3)
        // cityModelArrayList.add(list4);
        cityAdapter!!.notifyDataSetChanged()
    }

    private fun addAustrilliaDataList() {
        val list = CityModel(
            "https://drive.google.com/uc?export=download&id=1XvOPscvIuXjwy5h_twSNH8ou0qY-IMbC",
            "Adelaide"
        )
        val list1 = CityModel(
            "https://drive.google.com/uc?export=download&id=1ZdYpYMuD07SKa6MCYLeAh_w_-Vg5faN3",
            "Cairns"
        )
        val list2 = CityModel(
            "https://drive.google.com/uc?export=download&id=1Lnb7KLh8fJ_HC0MYRN2Wn14HWRdwiL2v",
            "Darwin"
        )
        val list3 = CityModel(
            "https://drive.google.com/uc?export=download&id=1FUlRHiXcsejTEL3h63ydSt7T2eJdbTyL",
            "Perth"
        )
        val list4 = CityModel(
            "https://drive.google.com/uc?export=download&id=1EvUEr0MGWTS1ktWeJ6Q5FMFxvqA0aaV2",
            "Gold Coast"
        )
        cityModelArrayList!!.add(list)
        cityModelArrayList!!.add(list1)
        cityModelArrayList!!.add(list2)
        cityModelArrayList!!.add(list3)
        cityModelArrayList!!.add(list4)
        cityAdapter!!.notifyDataSetChanged()
    }

    private fun addTurkeyilippinesDataList() {
        val list = CityModel(
            "https://drive.google.com/uc?export=download&id=1lpL0YnR21eqD_60WRyAZym-Cp9kbCjAn",
            "Istanbul"
        )
        val list1 = CityModel(
            "https://drive.google.com/uc?export=download&id=14c8vbjjmkmorLcuKFIFMyqk2o7D04LQq",
            "Ankara"
        )
        val list2 = CityModel(
            "https://drive.google.com/uc?export=download&id=1GIk9yDUf7VvkAEXhXWu_g-G_Vs-Hn5fy",
            "İzmir"
        )
        val list3 = CityModel(
            "https://drive.google.com/uc?export=download&id=1jAVAcvO4cmSv8UpMXAv0WwYP7VFUaKs4",
            "Antalya"
        )
        val list4 = CityModel(
            "https://drive.google.com/uc?export=download&id=1QJ2_Qsb0tqR1O24bfCGP51RLLtl2Hbdz",
            "Edirne"
        )
        cityModelArrayList!!.add(list)
        cityModelArrayList!!.add(list1)
        cityModelArrayList!!.add(list2)
        cityModelArrayList!!.add(list3)
        cityModelArrayList!!.add(list4)
        cityAdapter!!.notifyDataSetChanged()
    }

    private fun addNorwayDataList() {
        val list = CityModel(
            "https://drive.google.com/uc?export=download&id=1tFf3mrmMHgC2huJ_evW_1w4JObquE6K6",
            "Haugesund"
        )
        val list1 = CityModel(
            "https://drive.google.com/uc?export=download&id=1ovHhOAM0yA4N0VJZg1304QecEJAxctsw",
            "Oslo"
        )
        val list2 = CityModel(
            "https://drive.google.com/uc?export=download&id=1hZjs0hAwL97ANHbsMSt3Lrto4WVvXlFU",
            "Bergen"
        )
        val list3 = CityModel(
            "https://drive.google.com/uc?export=download&id=1P1iwIvW48GNwHDMBvcjqWFbXnQFBxOmK",
            "Trondheim"
        )
        val list4 = CityModel(
            "https://drive.google.com/uc?export=download&id=1BSdy-5fnlNc2poPAOw4cyTXCptjIyZD8",
            "Alesund"
        )
        cityModelArrayList!!.add(list)
        cityModelArrayList!!.add(list1)
        cityModelArrayList!!.add(list2)
        cityModelArrayList!!.add(list3)
        cityModelArrayList!!.add(list4)
    }

    private fun addIndiaDataList() {
        val list = CityModel(
            "https://drive.google.com/uc?export=download&id=1w-AY8imWcrWyMBY55Fl2Sd4VHG0LsCcs",
            "Jaipur"
        )
        val list1 = CityModel(
            "https://drive.google.com/uc?export=download&id=1RIafXiOVa-EXLc2_xKEmWdj4hdrfEO3f",
            "Bangalore Urban"
        )
        val list2 = CityModel(
            "https://drive.google.com/uc?export=download&id=1qrhBYL-xTPn_rkw5SCTQm6POFvDDCd_S",
            "Delhi"
        )
        val list3 = CityModel(
            "https://drive.google.com/uc?export=download&id=1t4E1bUNPKzTY_mgHBt8igJkTcqGzY_PQ",
            "Chennai"
        )
        cityModelArrayList!!.add(list)
        cityModelArrayList!!.add(list1)
        cityModelArrayList!!.add(list2)
        cityModelArrayList!!.add(list3)
    }

    private fun addUnitedStatesDataList() {
        val list = CityModel(
            "https://drive.google.com/uc?export=download&id=1Kkj2430x8-iThQLgCEqDiUqUc307zrUn",
            "San Diego"
        )
        val list1 = CityModel(
            "https://drive.google.com/uc?export=download&id=111vr-oF87F3i4ufsg4f4zXfLacPVUXS3",
            "Wyoming"
        )
        val list2 = CityModel(
            "https://drive.google.com/uc?export=download&id=1BSdy-5fnlNc2poPAOw4cyTXCptjIyZD8",
            "California"
        )
        val list3 = CityModel(
            "https://drive.google.com/uc?export=download&id=1yofSupjMxLUFmG_6M0INWvgfoZ_BNJwt",
            "Washington"
        )
        val list4 = CityModel(
            "https://drive.google.com/uc?export=download&id=1jbwJ0due4X9Obxtul4fKwb-ll2EIaYrQ",
            "Mesa"
        )
        cityModelArrayList!!.add(list)
        cityModelArrayList!!.add(list1)
        cityModelArrayList!!.add(list2)
        cityModelArrayList!!.add(list3)
        cityModelArrayList!!.add(list4)
    }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                logAnalyticsForClicks(
                    "StreetViewDisplayStreetViewCountryOnExit",
                    this@CityActivities
                )
                mediationBackPressedSimpleStreetViewLocation(
                    this@CityActivities,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,
                    bindingCity!!.whiteView
                )
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback.remove()
    }




    override fun onPointerCaptureChanged(hasCapture: Boolean) {
        super.onPointerCaptureChanged(hasCapture)
    }

    override fun onItemClick(cityModel: CityModel?) {
        logAnalyticsForClicks(
            "StreetViewDisplayStreetViewCountryCitiesClickImage" + cityModel?.text,
            this
        )
        //        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
        val i = Intent(this, PlacesActivity::class.java)
        i.putExtra("Cityname", cityModel?.text)
        i.putExtra("key", UtilityClass.countryName)
        meidationForClickSimpleSmartToolsLocation(
            this@CityActivities,
            StreetViewAppSoniMyAppAds.admobInterstitialAd,
            i, bindingCity!!.whiteView
        )
        // Handle the item click in the activity
    }
}