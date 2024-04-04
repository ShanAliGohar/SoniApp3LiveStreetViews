package com.live.streetview.navigation.earthmap.compass.map.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdView
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds
import com.live.streetview.navigation.earthmap.compass.map.adapters.ChatHistoryAdapter
import com.live.streetview.navigation.earthmap.compass.map.callback.DeleteItem
import com.live.streetview.navigation.earthmap.compass.map.callback.onClickItem
import com.live.streetview.navigation.earthmap.compass.map.database.ChatRecord
import com.live.streetview.navigation.earthmap.compass.map.database.NewChat
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityMainChatViewBinding
import com.live.streetview.navigation.earthmap.compass.map.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainChatViewActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityMainChatViewBinding
//    private val viewModel: MainViewModel by viewModels()
//    var chat: NewChat? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainChatViewBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        getRecent()
//        getChatHistory()
//        binding.chatHistory.layoutManager = LinearLayoutManager(this)
//        binding.newChat.setOnClickListener {
//            CoroutineScope(Dispatchers.IO).launch {
//                viewModel.saveDataToDataBaseForNewChat(NewChat(null, "chat"))
//            }
//            val intent = Intent(this@MainChatViewActivity, ChatActivity::class.java)
//            StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation(
//                this,
//                StreetViewAppSoniMyAppAds.admobInterstitialAd,
//                 intent
//            )
//        }
//        binding.imageView39.setOnClickListener {
//            onBackPressed()
//        }
//        streetViewBannerAdsSmall()
//    }
//
//    override fun onBackPressed() {
//        /*super.onBackPressed()*/
//        StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation(
//            this,
//            StreetViewAppSoniMyAppAds.admobInterstitialAd,
//            StreetViewAppSoniMyAppAds.maxInterstitialAdStreetViewST
//        )
//    }
//
//    fun getRecent() {
//        CoroutineScope(Dispatchers.Main).launch {
//            viewModel.getLastRecord().collect {
//                chat = it
//            }
//        }
//    }
//
//    fun getChatHistory() {
//        CoroutineScope(Dispatchers.Main).launch {
//            viewModel.getChatHistory().collect {
//                if (it.isNotEmpty()) {
//                    binding.chatHistory.visibility = View.VISIBLE
//                    Log.d("TAG", "getChatHistory: ${it}")
//                    binding.chatHistory.adapter = ChatHistoryAdapter(
//                        it as ArrayList<ChatRecord>,
//                        this@MainChatViewActivity, object : onClickItem {
//                            override fun click(position: Int) {
//                                val intent =
//                                    Intent(this@MainChatViewActivity, ChatActivity::class.java)
//                                intent.putExtra("key", it[position].chat)
//                                StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation(
//                                    this@MainChatViewActivity,
//                                    StreetViewAppSoniMyAppAds.admobInterstitialAd,
//                                     intent
//                                )
//                            }
//                        }, object : DeleteItem {
//                            override fun delteItem(id: Int) {
//                                CoroutineScope(Dispatchers.IO).launch {
//                                    viewModel.deleteById(id)
//                                    withContext(Dispatchers.Main) {
//                                        Toast.makeText(
//                                            this@MainChatViewActivity,
//                                            "delete successfully",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                        binding.chatHistory.adapter!!.notifyDataSetChanged()
//                                        getChatHistory()
//                                    }
//
//                                }
//                            }
//                        })
//                } else {
//                    Log.d("TAG", "getChatHistory: empty")
//                    binding.chatHistory.visibility = View.INVISIBLE
//
//                }
//
//            }
//        }
//    }
//
//    private fun streetViewBannerAdsSmall() {
//        val billingHelper = StreetViewAppSoniBillingHelper(this)
//        val adContainer: LinearLayout = binding!!.bannerAdPlace.adContainer
//        val adView = AdView(this)
//        adView.adUnitId = StreetViewAppSoniMyAppAds.banner_admob_inApp
//        if (billingHelper.isNotAdPurchased) {
//            StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediation(
//                adContainer, adView, this
//            )
//        } else {
//            binding!!.bannerID.visibility = View.GONE
//        }
//    }
}