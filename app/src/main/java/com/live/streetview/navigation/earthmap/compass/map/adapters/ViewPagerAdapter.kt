package com.live.streetview.navigation.earthmap.compass.map.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.live.streetview.navigation.earthmap.compass.map.fragments.OnBoardingFirst
import com.live.streetview.navigation.earthmap.compass.map.fragments.OnBoardingSecond
import com.live.streetview.navigation.earthmap.compass.map.fragments.onBoardingThird

class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return 2 // Number of pages
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> OnBoardingFirst()
            1 -> onBoardingThird()
            else -> OnBoardingFirst() // Default to first fragment
        }
    }
}
