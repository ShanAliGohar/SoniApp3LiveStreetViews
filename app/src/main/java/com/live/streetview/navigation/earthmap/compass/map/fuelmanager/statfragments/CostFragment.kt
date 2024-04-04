package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.statfragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.live.streetview.navigation.earthmap.compass.map.databinding.FragmentCostBinding
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.costs.CostDao
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling.MileageLogDao
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling.MileageLogDatabase
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.utils.SharedPrefManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.math.cos

class CostFragment : Fragment() {

    private lateinit var binding: FragmentCostBinding

    private lateinit var currentCurrency: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCostBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentCurrency = SharedPrefManager.getInstance(requireContext()).getDefaultCurrency()


        setCurrentDateTime()

        val mileageLogDao = MileageLogDatabase.getDatabase(requireContext()).mileageDao()
        val costLogDao = MileageLogDatabase.getDatabase(requireContext()).costDao()

        washCost(costLogDao)
        maintenanceCost(costLogDao)


        totalCost(mileageLogDao)
        thisYearCost(mileageLogDao)
        previousYearCost(mileageLogDao)
        currentMonthCost(mileageLogDao)
        previousMonthCost(mileageLogDao)
        minCost(mileageLogDao)
        maxCost(mileageLogDao)
        worstPrice(mileageLogDao)
        bestPrice(mileageLogDao)
    }

    private fun setCurrentDateTime() {
        val calendar = Calendar.getInstance()

        // Format for date: 2023-20-09
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = dateFormat.format(calendar.time)
        binding.tvfillupDate.text = currentDate

        // Format for time: 9:45 PM
        val timeFormat = SimpleDateFormat("h:mm a")
        val currentTime = timeFormat.format(calendar.time)
        binding.tvfillupTime.text = currentTime
    }

    private fun totalCost(mileageLogDao: MileageLogDao) {
        mileageLogDao.getAllMileageLogData().observe(viewLifecycleOwner) { list ->
            var cost = 0L
            list?.forEach { item ->
                cost += item.cost ?: 0L
                binding.tvCostTotal.text = currentCurrency + cost.toString()
            }
        }
    }

    private fun thisYearCost(mileageLogDao: MileageLogDao) {
        mileageLogDao.getCurrentYearMileageLogs().observe(viewLifecycleOwner) { list ->
            var thisYearCost = 0L
            list?.forEach { item ->
                thisYearCost += item.cost ?: 0L
                binding.tvCostThisYear.text = currentCurrency + thisYearCost.toString()
            }
        }
    }

    private fun previousYearCost(mileageLogDao: MileageLogDao) {
        mileageLogDao.getPreviousYearMileageLogs().observe(viewLifecycleOwner) { list ->
            var previousYearCost = 0L
            list?.forEach { item ->
                previousYearCost += item.cost ?: 0L
                binding.tvCostPreviousYear.text = currentCurrency +  previousYearCost.toString()
            }
        }
    }

    private fun currentMonthCost(mileageLogDao: MileageLogDao) {
        mileageLogDao.getCurrentMonthMileageLogs().observe(viewLifecycleOwner) { list ->
            var currentMonthCost = 0L
            list?.forEach { item ->
                item.cost.let {
                    currentMonthCost += it ?: 0L
                    binding.tvThisMonthCost.text = currentCurrency +  currentMonthCost.toString()
                }
            }
        }
    }

    private fun previousMonthCost(mileageLogDao: MileageLogDao) {
        mileageLogDao.getPreviousMonthMileageLogs().observe(viewLifecycleOwner) { list ->
            var previousMonthCost = 0L
            list?.forEach { item ->
                previousMonthCost += item.cost ?: 0L
                binding.tvPreviousMonthCost.text =  currentCurrency + previousMonthCost.toString()
            }
        }
    }

    private fun minCost(mileageLogDao: MileageLogDao) {
        mileageLogDao.getAllMileageLogData().observe(viewLifecycleOwner) { list ->
            var minCost = Long.MAX_VALUE
            list?.forEach { item ->
                item.cost.let {
                    if (minCost > it) {
                        minCost = it
                    }
                }
            }
            if (minCost != Long.MAX_VALUE) {
                binding.tvLowestBill.text =  currentCurrency + "$minCost"
            }
        }
    }

    private fun maxCost(mileageLogDao: MileageLogDao) {
        mileageLogDao.getAllMileageLogData().observe(viewLifecycleOwner) { list ->
            var maxCost = 0L
            list?.forEach { item ->
                item.cost.let {
                    if (maxCost < it) {
                        maxCost = it
                    }
                }
            }
            binding.tvHighestBill.text = currentCurrency +  maxCost.toString()
        }
    }

    private fun bestPrice(mileageLogDao: MileageLogDao) {
        mileageLogDao.getAllMileageLogData().observe(viewLifecycleOwner) { list ->
            var bestPrice = Long.MAX_VALUE
            list?.forEach { item ->
                item.price.let {
                    if (bestPrice > it) {
                        bestPrice = it.toLong()
                    }
                }
            }
            if (bestPrice != Long.MAX_VALUE) {
                binding.tvBestPrice.text = currentCurrency +  "$bestPrice"
            }
        }
    }

    private fun worstPrice(mileageLogDao: MileageLogDao) {
        mileageLogDao.getAllMileageLogData().observe(viewLifecycleOwner) { list ->
            var worstPrice = 0L
            list?.forEach { item ->
                item.price.let {
                    if (worstPrice < it) {
                        worstPrice = it.toLong()
                    }
                }
            }
            binding.tvWorstPrice.text = currentCurrency +  worstPrice.toString()
        }
    }


    private fun washCost(costLogDao: CostDao){

        lifecycleScope.launch {

            var totalWashCost = 0L
             costLogDao.getAllCosts().observe(viewLifecycleOwner){list ->

                   for (item in list){
                       if (item.service == "Wash"){
                           totalWashCost += item.totalCost.toLong()
                       }
                   }

               Log.d("TAG", "washCost: $totalWashCost $currentCurrency")
               binding.tvCostTotalWash.text = "$currentCurrency $totalWashCost"

           }

            var thisYearCost = 0L
           costLogDao.getCurrentYearCosts().observe(viewLifecycleOwner){   thisYearlist ->

               for (item in thisYearlist){
                   if (item.service == "Wash"){
                       thisYearCost += item.totalCost.toLong()
                       Log.d("date", "washCost: ${item.totalCost}")
                   }
               }

               binding.tvCostWashThisYear.text = thisYearCost.toString()
            }

            var previousYearCost = 0L
            costLogDao.getPreviousYearCosts().observe(viewLifecycleOwner){   previousYearlist ->

                for (item in previousYearlist){
                    if (item.service == "Wash"){
                        previousYearCost += item.totalCost.toLong()
                        Log.d("date", "washCost: ${item.totalCost}")
                    }
                }

                binding.tvCostWashPreviousYear.text = previousYearCost.toString()
            }


            var thisMonthCost = 0L
            costLogDao.getCurrentMonthCosts().observe(viewLifecycleOwner){   thisMonthlist ->

                for (item in thisMonthlist){
                    if (item.service == "Wash"){
                        thisMonthCost += item.totalCost.toLong()
                        Log.d("date", "washCost: ${item.totalCost}")
                    }
                }

                binding.tvThisMonthCostWash.text = thisMonthCost.toString()
            }


            var previousMonthCost = 0L
            costLogDao.getPreviousMonthCosts().observe(viewLifecycleOwner){   previousMonthlist ->

                for (item in previousMonthlist){
                    if (item.service == "Wash"){
                        previousMonthCost += item.totalCost.toLong()
                        Log.d("date", "washCost: ${item.totalCost}")
                    }
                }

                binding.tvPreviousMonthCost.text = previousMonthCost.toString()
            }

        }
    }

    private fun maintenanceCost(costLogDao: CostDao){

        lifecycleScope.launch {
            costLogDao.getAllCosts().observe(viewLifecycleOwner){list->

                var totalWashCost = 0L

                    for (item in list){
                        if (item.service == "Maintenance"){
                            totalWashCost += item.totalCost.toLong()
                        }
                    }

                Log.d("TAG", "washCost: $totalWashCost $currentCurrency")
                binding.tvCostTotalMaintenance.text = "$currentCurrency $totalWashCost"
            }


            var thisYearCost = 0L
            costLogDao.getCurrentYearCosts().observe(viewLifecycleOwner){   thisYearlist ->

                for (item in thisYearlist){
                    if (item.service == "Maintenance"){
                        thisYearCost += item.totalCost.toLong()
                        Log.d("date", "washCost: ${item.totalCost}")
                    }
                }

                binding.tvCostMaintenanceThisYear.text = thisYearCost.toString()
            }

            var previousYearCost = 0L
            costLogDao.getPreviousYearCosts().observe(viewLifecycleOwner){   previousYearlist ->

                for (item in previousYearlist){
                    if (item.service == "Maintenance"){
                        previousYearCost += item.totalCost.toLong()
                        Log.d("date", "washCost: ${item.totalCost}")
                    }
                }

                binding.tvCostMaintenancePreviousYear.text = previousYearCost.toString()
            }


            var thisMonthCost = 0L
            costLogDao.getCurrentMonthCosts().observe(viewLifecycleOwner){   thisMonthlist ->

                for (item in thisMonthlist){
                    if (item.service == "Maintenance"){
                        thisMonthCost += item.totalCost.toLong()
                        Log.d("date", "washCost: ${item.totalCost}")
                    }
                }

                binding.tvThisMonthCostMaintenance.text = thisMonthCost.toString()
            }


            var previousMonthCost = 0L
            costLogDao.getPreviousMonthCosts().observe(viewLifecycleOwner){   previousMonthlist ->

                for (item in previousMonthlist){
                    if (item.service == "Maintenance"){
                        previousMonthCost += item.totalCost.toLong()
                        Log.d("date", "washCost: ${item.totalCost}")
                    }
                }

                binding.tvPreviousMonthMaintenanceCost.text = previousMonthCost.toString()
            }

        }

    }

}
