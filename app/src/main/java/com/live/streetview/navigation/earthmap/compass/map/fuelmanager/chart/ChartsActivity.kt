package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.chart

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityChartsBinding
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.chartsfragments.FillUpCostChartFragment
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.chartsfragments.FuelConsumptionChartFragment
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.chartsfragments.ServicesCostChartFragment
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling.MileageLogDao
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling.MileageLogDatabase


class ChartsActivity : AppCompatActivity() {

    private lateinit var mileageLogDao: MileageLogDao
    lateinit var binding: ActivityChartsBinding

    companion object {
        @JvmStatic
        private var selectedPosition: Int = -1

        fun getSelectedPosition(): Int {
            return selectedPosition
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        selectedPosition = intent.getIntExtra("selectedVehiclePosition", -1)

        mileageLogDao = MileageLogDatabase.getDatabase(this).mileageDao()


        val adapter = MyPagerAdapter(supportFragmentManager)
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit=0

        binding.tabLayout.setupWithViewPager(binding.viewPager)


    }

}


class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getItem(position: Int): Fragment {
        // Return the fragment based on the position
        return when (position) {
            0 -> FuelConsumptionChartFragment()
            1 -> FillUpCostChartFragment()
            2 -> ServicesCostChartFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    override fun getCount(): Int {
        // Return the number of fragments in the ViewPager
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        // Set the tab title based on the position
        return when (position) {
            0 -> "Fuel Consumption"
            1 -> "Fill-Up Cost"
            2 -> "Services Costs"
            else -> null
        }
    }
}