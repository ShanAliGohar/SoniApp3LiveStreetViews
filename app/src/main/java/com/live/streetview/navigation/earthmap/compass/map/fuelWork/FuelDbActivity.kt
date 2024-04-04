package com.live.streetview.navigation.earthmap.compass.map.fuelWork


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardItem
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds
import com.live.streetview.navigation.earthmap.compass.map.MainActivity
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.activities.utils.JsonConverter
import com.live.streetview.navigation.earthmap.compass.map.adapters.fuel_db_adapters.CountryListAdapter
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityFuelDbBinding
import com.live.streetview.navigation.earthmap.compass.map.databinding.CountryListDialogBinding
import com.live.streetview.navigation.earthmap.compass.map.databinding.CurrencyListDialogBinding
import com.live.streetview.navigation.earthmap.compass.map.fuelWork.dataClasses.Country
import com.live.streetview.navigation.earthmap.compass.map.fuelWork.interfaces.CountryListener
import org.json.JSONArray
import java.util.*


class FuelDbActivity : AppCompatActivity(), CountryListener, OnUserEarnedRewardListener {
    private val binding: ActivityFuelDbBinding by lazy {
        ActivityFuelDbBinding.inflate(layoutInflater)
    }
    private var countryListAdapter: CountryListAdapter? = null

    private var dialog: Dialog? = null
    private var currencyDialog: Dialog? = null
    private var countryName: String = ""
    private var currencyType: String = ""
    var value = 0
    var countryList: ArrayList<Country> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


//        streetViewBannerAdsSmall()

        //initializing the Google Admob SDK
        countryList = loadCountryListFromAssets()
        listeners()
    }

    private fun loadCountryListFromAssets(): ArrayList<Country> {
        val list = ArrayList<Country>()
        val response = JsonConverter.getJsonFromAssets(this, "countries_list.json")
        val jsonArray = JSONArray(response)
        for (i in 0 until jsonArray.length()) {
            val jsonObject1 = jsonArray.getJSONObject(i)
            val nameCountry = jsonObject1.getString("name")
            Log.d("msg", "getCountryData: $nameCountry")
            val mCountry = Country(nameCountry)
            list.add(mCountry)
        }
        return list
    }

    private fun listeners() {
        with(binding) {
            toolbarBackIcon.setOnClickListener {
                onBackPressed()
            }
            countryConst.setOnClickListener {
                showSaveLocationDialog()
            }
            currencyConst.setOnClickListener {
                selectCurrencyDialog()
            }
            checkPriceBtn.setOnClickListener {
                if (countryName == "" || currencyType == "") {
                    Toast.makeText(
                        this@FuelDbActivity,
                        "please select country and currency",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val intent = Intent(this@FuelDbActivity, TableActivity::class.java)
                    intent.putExtra("countryName", countryName)
                    intent.putExtra("currencyType", currencyType)
//                    rewardedAdShow(this@FuelDbActivity, intent)
//                    startActivity(intent)
                    StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation(
                        this@FuelDbActivity,
                        StreetViewAppSoniMyAppAds.admobInterstitialAd,
                        
                        intent,binding.whiteView
                    )
                }

                //startActivity(intent)
            }
        }
    }

    /*...dialog for country selection on the basis of radio button...*/
    @SuppressLint("NotifyDataSetChanged")
    private fun showSaveLocationDialog() {
        dialog = Dialog(this)
        val countryDialogBinding: CountryListDialogBinding by lazy {
            CountryListDialogBinding.inflate(layoutInflater)
        }
        dialog?.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(countryDialogBinding.root)

            countryDialogBinding.countryRecycler.layoutManager =
                LinearLayoutManager(
                    this@FuelDbActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            countryListAdapter = CountryListAdapter(countryList, this@FuelDbActivity)
            countryDialogBinding.countryRecycler.adapter = countryListAdapter
            Log.d("TAG", "showSaveLocationDialog: $countryList")

            countryDialogBinding.countrySearchEt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val searchText = s.toString().trim().lowercase(Locale.getDefault())
                    val filteredList = countryList.filter { country ->
                        country.name.lowercase(Locale.getDefault()).contains(searchText)
                    }
                    filteredList.let { countryListAdapter?.updateList(it as ArrayList<Country>) }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            dialog?.show()
        }
    }

    private fun selectCurrencyDialog() {
        currencyDialog = Dialog(this)
        val currencyDialogBinding: CurrencyListDialogBinding by lazy {
            CurrencyListDialogBinding.inflate(layoutInflater)
        }
        currencyDialog?.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(currencyDialogBinding.root)
        }

        with(currencyDialogBinding) {
            usdCard.setOnClickListener {
                currencyType = getString(R.string.usd)
                binding.currencyTv.text = currencyType
                currencyDialog?.dismiss()
            }

            localCard.setOnClickListener {
                currencyType = getString(R.string.local)
                binding.currencyTv.text = currencyType
                currencyDialog?.dismiss()
            }
        }
        currencyDialog?.show()
    }


    fun View.gone() {
        this.visibility = View.GONE
    }

    override fun onUserEarnedReward(p0: RewardItem) {
        Log.d("TAG", "rewarded earned")
    }

    override fun onClick(position: Int, selectedText: String) {
        countryName = selectedText
        binding.countryTv.text = countryName
        dialog?.dismiss()
    }


//    private fun streetViewBannerAdsSmall() {
//        val billingHelper = StreetViewAppSoniBillingHelper(this)
//        val adContainer: LinearLayout = binding!!.bannerAdPlace.adContainer
//        val adView = AdView(this)
//        adView.setAdSize(StreetViewAppSoniMyAppAds.getAdSize(this))
//        adView.adUnitId = StreetViewAppSoniMyAppAds.banner_admob_inApp
//
//        if (billingHelper.isNotAdPurchased) {
//            StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediation(
//                adContainer, adView, this
//            )
//        } else {
//            binding!!.bannerID.visibility = View.GONE
//
//        }
//    }

}
