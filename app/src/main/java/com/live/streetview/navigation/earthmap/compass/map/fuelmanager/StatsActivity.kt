package com.live.streetview.navigation.earthmap.compass.map.fuelmanager

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.statfragments.CostFragment
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.statfragments.DistanceFragment
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.statfragments.FillUpsFragment

class StatsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val textViewTitle = findViewById<TextView>(R.id.textViewTitle)
        val imageViewBack = findViewById<ImageView>(R.id.imageViewBack)

        textViewTitle.text = "Statistics"

        // Setup ViewPager
        val adapter = StatsPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter

        // Connect ViewPager with TabLayout
        tabLayout.setupWithViewPager(viewPager)

        // Handle back button click
        imageViewBack.setOnClickListener {
            onBackPressed()
        }
    }

    private class StatsPagerAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> FillUpsFragment()
                1 -> CostFragment()
                2 -> DistanceFragment()
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }

        override fun getCount(): Int {
            return 3 // Number of tabs
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "Fill-ups"
                1 -> "Cost"
                2 -> "Distance"
                else -> null
            }
        }
    }
}
