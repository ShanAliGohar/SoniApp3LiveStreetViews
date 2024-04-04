package com.live.streetview.navigation.earthmap.compass.map.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.google.android.gms.ads.AdView
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds
import com.live.streetview.navigation.earthmap.compass.map.Model.horoscope.ApiClientForHoroscope
import com.live.streetview.navigation.earthmap.compass.map.Model.horoscope.ApiInterfaceForHoroscope
import com.live.streetview.navigation.earthmap.compass.map.Model.horoscope.HoroScopeMainModel
import com.live.streetview.navigation.earthmap.compass.map.Model.horoscope.ModelHoroscope
import com.live.streetview.navigation.earthmap.compass.map.Model.utils.SnapHelperOneByOne
import com.live.streetview.navigation.earthmap.compass.map.Model.utils.onSwapInterface
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.adapters.HoroScopeAdapter
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityDialyHoroScopeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//class DialyHoroScopeActivity : AppCompatActivity(), onSwapInterface {
//    var binding: ActivityDialyHoroScopeBinding? = null
//    var image: Int = R.drawable.h1
//    private var list: ArrayList<HoroScopeMainModel>? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityDialyHoroScopeBinding.inflate(layoutInflater)
//        setContentView(binding!!.root)
//        list = ArrayList<HoroScopeMainModel>()
//        list = getList()
//        val linearSnapHelper: LinearSnapHelper = SnapHelperOneByOne(this@DialyHoroScopeActivity)
//        linearSnapHelper.attachToRecyclerView(binding!!.carouselRecyclerview)
//        binding!!.carouselRecyclerview.layoutManager =
//            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        binding!!.carouselRecyclerview.adapter = HoroScopeAdapter(
//            this@DialyHoroScopeActivity, list!!
//            /*Log.d("TAG", "onClick: ${binding!!.carouselRecyclerview.getSelectedPosition()}")
//            binding!!.txtName.text = list.get(binding!!.carouselRecyclerview.getSelectedPosition()).name
//            this@DialyHoroScopeActivity.image =list.get(binding!!.carouselRecyclerview.getSelectedPosition()).Image*/
//
//        )
////        binding!!.carouselRecyclerview.apply {
////
////            layoutManager = getCarouselLayoutManager()
////            adapter = HoroScopeAdapter(
////                this@DialyHoroScopeActivity, list,
////                object : DialyHoroScopeCallBack {
////                    override fun onClick(name: String, image: Int) {
////                        /*Log.d("TAG", "onClick: ${binding!!.carouselRecyclerview.getSelectedPosition()}")
////                        binding!!.txtName.text = list.get(binding!!.carouselRecyclerview.getSelectedPosition()).name
////                        this@DialyHoroScopeActivity.image =list.get(binding!!.carouselRecyclerview.getSelectedPosition()).Image*/
////                    }
////
////
////                }
////            )
//////            setItemSelectListener(object :
//////                CarouselLayoutManager.OnSelected {
//////                override fun onItemSelected(position: Int) {
////////                    binding!!.txtName.text = list.get(position).name
////////                    this@DialyHoroScopeActivity.image =list.get(position).Image
//////                }
////
//////        })
//////        binding!!.carouselRecyclerview.setItemSelectListener(object :
//////            CarouselLayoutManager.OnSelected {
//////            override fun onItemSelected(position: Int) {
//////                binding!!.txtName.text = list.get(position).name
//////                this@DialyHoroScopeActivity.image =list.get(position).Image
//////            }
//////        })
////        }
////        Handler(Looper.getMainLooper()).postDelayed({
////            binding!!.carouselRecyclerview.setItemSelectListener(object:
////                CarouselLayoutManager.OnSelected {
////                override fun onItemSelected(position: Int) {
////                    Log.d("TAGPos", "onItemSelected: $position")
////                    binding!!.txtName.text = list[position].name
////                    this@DialyHoroScopeActivity.image =list[position].Image
////                }
////
////            })
////        },1000)
////
////        binding!!.carouselRecyclerview.setFlat(true)
////        binding!!.carouselRecyclerview.set3DItem(true)
//        binding!!.constraintLayout29.setOnClickListener {
//            val intent = Intent(this, DetailsHoroScopActivity::class.java)
//            intent.putExtra("key", binding!!.txtName.text)
//            intent.putExtra("image", image)
//            StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation(
//                this@DialyHoroScopeActivity,
//                StreetViewAppSoniMyAppAds.admobInterstitialAd,
//                
//                intent
//            )
//        }
//        binding!!.imageView31.setOnClickListener {
//            onBackPressed()
//        }
////        streetViewBannerAdsSmall()
//    }
//
//    fun getList(): ArrayList<HoroScopeMainModel> {
//        var list = ArrayList<HoroScopeMainModel>()
//        list.add(HoroScopeMainModel("Aries", R.drawable.h1))
//        list.add(HoroScopeMainModel("Pisces", R.drawable.h2))
//        list.add(HoroScopeMainModel("Aquarius", R.drawable.h3))
//        list.add(HoroScopeMainModel("Capricorn", R.drawable.h4))
//        list.add(HoroScopeMainModel("Sagittarius", R.drawable.h5))
//        list.add(HoroScopeMainModel("Scorpio", R.drawable.h6))
//        list.add(HoroScopeMainModel("Libra", R.drawable.h7))
//        list.add(HoroScopeMainModel("Virgo", R.drawable.h8))
//        list.add(HoroScopeMainModel("Leo", R.drawable.h9))
//        list.add(HoroScopeMainModel("Cancer", R.drawable.h10))
//        list.add(HoroScopeMainModel("Gemini", R.drawable.h11))
//
//        return list
//    }
//
//    private fun streetViewBannerAdsSmall() {
//        val billingHelper = StreetViewAppSoniBillingHelper(this)
//        val adContainer: LinearLayout = binding!!.bannerAdPlace.adContainer
//        val adView = AdView(this)
//        adView.setAdSize(StreetViewAppSoniMyAppAds.getAdSize(this))
//        adView.adUnitId = StreetViewAppSoniMyAppAds.banner_admob_inApp
//        if (billingHelper.isNotAdPurchased) {
//            StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediation(
//                adContainer, adView, this
//            )
//        }
//    }
//
//    override fun onBackPressed() {
////        super.onBackPressed()
//        StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation(
//            this,
//            StreetViewAppSoniMyAppAds.admobInterstitialAd,
//            StreetViewAppSoniMyAppAds.maxInterstitialAdStreetViewST
//        )
//    }
//
//    override fun onswap(position: Int) {
//        Log.d("TAG", "onswap: ${position}")
//        binding!!.txtName.text = list!!.get(position).name
//        this@DialyHoroScopeActivity.image = list!!.get(position).Image
//        binding!!.progressBar5.visibility = View.VISIBLE
//        CoroutineScope(Dispatchers.IO).launch {
//            getData(list!!.get(position).name)
//        }
//    }
//
//    fun getData(text: String) {
//
//        val apiInterfaceForHoroscope = ApiClientForHoroscope.getRetrofitInstance().create(
//            ApiInterfaceForHoroscope::class.java
//        )
//        val call = apiInterfaceForHoroscope.getHoroscope(text, "today")
//        call.enqueue(object : Callback<ModelHoroscope?> {
//            override fun onResponse(
//                call: Call<ModelHoroscope?>,
//                response: Response<ModelHoroscope?>
//            ) {
//                if (response.isSuccessful && response.body() != null) {
//                    try {
//
//                    } catch (e: Exception) {
//                    }
//                    val des = response.body()!!.description
//                    val date = response.body()!!.currentDate
//                    val rangeDate = response.body()!!.dateRange
//                    val mood = response.body()!!.mood
//                    val color = response.body()!!.color
//                    val luckynum = response.body()!!.luckyNumber
//                    val luckytime = response.body()!!.luckyTime
//
//                    Log.d("TAG", "onResponse: ${response.body()}")
//                    populateData(date, rangeDate, des, mood, color, luckynum, luckytime)
//                }
//            }
//
//            override fun onFailure(call: Call<ModelHoroscope?>, t: Throwable) {
////                binding!!.progressBar5.visibility = View.INVISIBLE
////                Toast.makeText(this@DialyHoroScopeActivity, "load failed", Toast.LENGTH_SHORT)
////                    .show()
//            }
//        })
//    }
//
//    private fun populateData(
//        date: String?,
//        rangeDate: String?,
//        des: String?,
//        mood: String?,
//        color: String?,
//        luckynum: String?,
//        luckytime: String?
//    ) {
//        binding!!.textView32.text = rangeDate
//        binding!!.progressBar5.visibility = View.INVISIBLE
//    }
//}