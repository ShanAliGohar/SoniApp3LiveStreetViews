package com.live.streetview.navigation.earthmap.compass.map.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppNativeAds
import com.live.streetview.navigation.earthmap.compass.map.MainActivity
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.adapters.ViewPagerAdapter
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityOnBoardingScreenBinding
import com.live.streetview.navigation.earthmap.compass.map.fragments.OnBoardingFirst
import com.live.streetview.navigation.earthmap.compass.map.fragments.OnBoardingSecond
import com.live.streetview.navigation.earthmap.compass.map.fragments.onBoardingThird

class OnBoardingScreen : AppCompatActivity() {
    var binding: ActivityOnBoardingScreenBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingScreenBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        StreetViewAppSoniMyAppNativeAds.Companion.loadSmartToolsNavAdmobNativeAdPriority(
            this,
            binding!!.adsNative.adViewUnified
        );
        val adapter = ViewPagerAdapter(supportFragmentManager)

        val viewPager = binding!!.viewPager
        viewPager?.adapter = adapter

        val dotIndicator = binding!!.dotsIndicator
        if (viewPager != null) {
            dotIndicator?.viewPager = viewPager
        }

        binding!!.nextBtn.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem < adapter.count - 1) {
                viewPager.currentItem = currentItem + 1
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        binding!!.skipBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}