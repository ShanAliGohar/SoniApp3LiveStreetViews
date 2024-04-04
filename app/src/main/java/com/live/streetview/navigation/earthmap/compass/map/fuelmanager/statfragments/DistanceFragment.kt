package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.statfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.live.streetview.navigation.earthmap.compass.map.databinding.FragmentDistanceBinding
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.trip.TripDao
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.trip.TripDatabase
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.utils.SharedPrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DistanceFragment : Fragment() {

    private lateinit var binding: FragmentDistanceBinding

    private lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDistanceBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPrefManager = SharedPrefManager.getInstance(requireContext())

        val tripDao = TripDatabase.getDatabase(requireContext()).tripDao()

        getCurrentYearTrips(tripDao)
        getPreviousYearTrips(tripDao)
        getCurrentMonthTrips(tripDao)
        getPreviousMonthTrips(tripDao)
        bestCostPerKm(tripDao)
        worstCostPerKm(tripDao)

    }


    private fun getCurrentYearTrips(tripDao: TripDao) {

        var kmThisYear = 0L

        tripDao.getCurrentYearTripLogs().observe(viewLifecycleOwner) {

            for (item in it) {
                kmThisYear += (item.finalOdoCounter.toLong() - item.startOdoCounter!!.toLong())
            }
            binding.thisyearcostEdittext.text = kmThisYear.toString() + "KM"
        }
    }


    private fun getPreviousYearTrips(tripDao: TripDao) {

        var kmPreviousYear = 0L


        tripDao.getPreviousYearTripLogs().observe(viewLifecycleOwner) {

            for (item in it) {
                kmPreviousYear += (item.finalOdoCounter.toLong() - item.startOdoCounter!!.toLong())
            }
            binding.lastyearpriceEditText.text = kmPreviousYear.toString() + "KM"
        }
    }


    private fun getCurrentMonthTrips(tripDao: TripDao) {

        var kmThisMonth = 0L


        tripDao.getCurrentMonthTripLogs().observe(viewLifecycleOwner) {

            for (item in it) {
                kmThisMonth += (item.finalOdoCounter.toLong() - item.startOdoCounter!!.toLong())
            }
            binding.thismonthcosttextview.text = kmThisMonth.toString() + "KM"
        }
    }


    private fun getPreviousMonthTrips(tripDao: TripDao) {
        var kmPreviousMonth = 0L

        tripDao.getPreviousMonthTripLogs().observe(viewLifecycleOwner) {

            for (item in it) {
                kmPreviousMonth += (item.finalOdoCounter.toLong() - item.startOdoCounter!!.toLong())
            }

            binding.previousmonthpriceTextView.text = kmPreviousMonth.toString() + "KM"
        }

    }


    private fun bestCostPerKm(tripDao: TripDao) {

        tripDao.getAllTripLogs().observe(viewLifecycleOwner) { list ->

            if (list.isNotEmpty()) {

                var bestCostPerKm = 0L

                for (item in list) {
                    if (item.costPerKm != null) {
                        if (item.costPerKm < bestCostPerKm) {
                            bestCostPerKm = item.costPerKm.toLong()
                        }
                    }
                }

                binding.BestCostPerKmTextview.text =    "${sharedPrefManager.getDefaultCurrency()}$bestCostPerKm"

            }

        }


    }


    private fun worstCostPerKm(tripDao: TripDao) {


        tripDao.getAllTripLogs().observe(viewLifecycleOwner) { list ->

            if (list.isNotEmpty()) {

                var worstCostPerKm = 0L

                for (item in list) {
                    if (item.costPerKm != null) {
                        if (item.costPerKm.toLong() > worstCostPerKm) {
                            worstCostPerKm = item.costPerKm.toLong()
                        }
                    }
                }

                binding.WorstCostPerKmTextview.text =
                    "${sharedPrefManager.getDefaultCurrency()}$worstCostPerKm"
            }
        }

    }


}