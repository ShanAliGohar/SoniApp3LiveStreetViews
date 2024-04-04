package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.statfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.live.streetview.navigation.earthmap.compass.map.databinding.FragmentFillUpsBinding
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling.MileageLogDatabase
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.trip.TripDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar


class FillUpsFragment : Fragment() {

    private lateinit var binding: FragmentFillUpsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFillUpsBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val fillUpsDao = MileageLogDatabase.getDatabase(requireContext()).mileageDao()

        val tripLogDao = TripDatabase.getDatabase(requireContext()).tripDao()


        fillUpsDao.getAllMileageLogData().observe(viewLifecycleOwner) {

            binding.tvfillupAmountTotal.text = it.size.toString()

        }

        fillUpsDao.getCurrentYearMileageLogs().observe(viewLifecycleOwner) {

            binding.tvFillUpThisYear.text = it.size.toString()

        }

        fillUpsDao.getPreviousYearMileageLogs().observe(viewLifecycleOwner) {

            binding.tvFillUpPreviousYear.text = it.size.toString()
        }

        fillUpsDao.getCurrentMonthMileageLogs().observe(viewLifecycleOwner) {

            binding.tvThisMonthFillUps.text = it.size.toString()
        }

        fillUpsDao.getPreviousMonthMileageLogs().observe(viewLifecycleOwner) {

            binding.tvPreviousMonthFillUp.text = it.size.toString()
        }


        fillUpsDao.getAllMileageLogData().observe(viewLifecycleOwner) {

            var total = 0
            for (item in it) {
                total += item.gas
            }

            binding.gasLitres.text = "${total}L"
        }


        fillUpsDao.getCurrentYearMileageLogs().observe(viewLifecycleOwner) {

            var currentYearFuel = 0
            for (item in it) {
                currentYearFuel += item.gas
            }

            binding.gasLitresThisYear.text = "${currentYearFuel}L"

        }


        fillUpsDao.getPreviousYearMileageLogs().observe(viewLifecycleOwner) {

            var previousYearFuel = 0
            for (item in it) {
                previousYearFuel += item.gas
            }

            binding.gasLitresPreviousYear.text = "${previousYearFuel}L"

        }


        fillUpsDao.getCurrentMonthMileageLogs().observe(viewLifecycleOwner) {

            var currentMonthFuel = 0
            for (item in it) {
                currentMonthFuel += item.gas
            }

            binding.gasLitresCurrentMonth.text = "${currentMonthFuel}L"

        }


        fillUpsDao.getPreviousMonthMileageLogs().observe(viewLifecycleOwner) {

            var previousMonthFuel = 0
            for (item in it) {
                previousMonthFuel += item.gas
            }

            binding.gasLitresPreviousMonth.text = "${previousMonthFuel}L"

        }


        fillUpsDao.getAllMileageLogData().observe(viewLifecycleOwner) {

            if (it.isNotEmpty()) {
                var minFillup = it[0].gas

                for (item in it) {
                    if (minFillup > item.gas) {
                        minFillup = item.gas
                    }
                }

                binding.tvMinFillUpLitres.text = "${minFillup}L"

            }

        }

        fillUpsDao.getAllMileageLogData().observe(viewLifecycleOwner) {

            if (it.isNotEmpty()) {
                var maxFillup = it[0].gas

                for (item in it) {
                    if (maxFillup < item.gas) {
                        maxFillup = item.gas
                    }
                }

                binding.textView15.text = "${maxFillup}L"


            }

        }


/*

        fillUpsDao.getAllMileageLogData().observe(viewLifecycleOwner) {

            CoroutineScope(Dispatchers.IO).launch {

                val list = tripLogDao.getAllTripLogs()

                for (item in list) {


                }
            }


        }
*/

        setCurrentDateTime()

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

}