package com.live.streetview.navigation.earthmap.compass.map.fuelmanager

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityMilageLogBinding
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.adapter.AdapterMileageLog
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling.MileageLogDao
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling.MileageLogDatabase
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling.MileageLogEntity
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.vehicle.VehicleDao
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.vehicle.VehicleDatabase
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.utils.showDateDialogStartPoint
import java.text.SimpleDateFormat
import java.util.Locale

class MilageLogActivity : AppCompatActivity() {
    lateinit var binding: ActivityMilageLogBinding
    private lateinit var mileageLogDao: MileageLogDao
    private lateinit var vehiclesDao: VehicleDao

    private var startDate: String?= null
    private var endDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMilageLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mileageLogDao = MileageLogDatabase.getDatabase(this).mileageDao()
        vehiclesDao = VehicleDatabase.getDatabase(this).vehicleDao()


        mileageLogDao.getAllMileageLogData().observe(this) { list ->


            setAdapter(list)


            binding.btnFilterDate.setOnClickListener {

                startDate = binding.startDateButton.text.toString()
                endDate = binding.endDateButton.text.toString()


                if (startDate!!.contains("2") && endDate!!.contains("2")){

                    val filteredList = filterItemsByDateRange(startDate!!, endDate!!, list)

                    setAdapter(filteredList)

                }else{
                    Toast.makeText(this, "Select Start & End Date", Toast.LENGTH_SHORT).show()
                }
            }

        }


        binding.startDateButton.setOnClickListener {
            showDateDialogStartPoint(binding.startDateButton)
        }

        binding.endDateButton.setOnClickListener {
            showDateDialogStartPoint(binding.endDateButton)
        }
        binding.imageback.setOnClickListener {
            onBackPressed()
        }

        binding.addMileageBtn.setOnClickListener {
            startActivity(Intent(this, RefuelingActivity::class.java))
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@MilageLogActivity, FuelManagerActivity::class.java))
            }
        })

    }


    private fun filterItemsByDateRange(
        startDateString: String,
        endDateString: String,
        itemList: List<MileageLogEntity>
    ): List<MileageLogEntity> {
        val sdf = SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH)

        try {
            val startDate = sdf.parse(startDateString)
            val endDate = sdf.parse(endDateString)

            if (startDate != null && endDate != null) {
                return itemList.filter { item ->
                    val itemDate = sdf.parse(item.date)
                    itemDate in startDate..endDate
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return emptyList()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun setAdapter(list: List<MileageLogEntity>) {


        if (list.isEmpty()) {
            binding.textEmptyList.visibility = View.VISIBLE
        }
        binding.textEmptyList.visibility = View.INVISIBLE
        binding.recyclerMilageLog.layoutManager = LinearLayoutManager(this)
        val adapter = AdapterMileageLog(list, vehiclesDao, this)
        binding.recyclerMilageLog.adapter = adapter
        adapter.notifyDataSetChanged()

    }
}