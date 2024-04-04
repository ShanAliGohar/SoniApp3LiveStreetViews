package com.live.streetview.navigation.earthmap.compass.map.fuelmanager

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityEditTripBinding
import com.live.streetview.navigation.earthmap.compass.map.fragments.FinishbottomSheetFragment
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.trip.TripDao
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.trip.TripDatabase
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.trip.TripEntity
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.utils.SharedPrefManager
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.utils.showDateDialogEndPoint
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.utils.showDateDialogStartPoint
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.utils.showTimeDialogEndPoint
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.utils.showTimeDialogStartPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class EditTripActivity : AppCompatActivity() {

    private val binding: ActivityEditTripBinding by lazy {
        ActivityEditTripBinding.inflate(layoutInflater)
    }

    private lateinit var tripDao: TripDao

    private lateinit var tripOld: TripEntity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor("#2D44B5")


        tripDao = TripDatabase.getDatabase(this).tripDao()


        val position = intent.getIntExtra("trip", 0)



        tripDao.getAllTripLogs().observe(this@EditTripActivity) { list ->
            if (list.isNotEmpty()) {
                tripOld = list[position]
                setTrip(tripOld)
            }

        }



        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@EditTripActivity, TripLogActivity::class.java))
            }
        })


        binding.addTripBtn.setOnClickListener {
            addTripLog()
        }

        binding.imageBack.setOnClickListener {
            onBackPressed()
        }

        listeners()
    }


    private fun listeners() {
        with(binding) {
            startDateBtn.setOnClickListener {
                showDateDialogStartPoint(startDateBtn)
            }
            startTimeBtn.setOnClickListener {
                showTimeDialogStartPoint(startTimeBtn)
            }
            endDateBtn.setOnClickListener {
                showDateDialogEndPoint(endDateBtn)
            }
            endTimeBtn.setOnClickListener {
                showTimeDialogEndPoint(endTimeBtn)
            }
        }
    }


    private fun setTrip(tripEntity: TripEntity) {

        binding.tripTitleType.setText(tripEntity.title)
        binding.tripStartOrigin.setText(tripEntity.startPoint)
        binding.tripStartOdoCounter.setText(tripEntity.startOdoCounter)
        binding.tripEndDestination.setText(tripEntity.endPoint)
        binding.tripEndOdoCounter.setText(tripEntity.finalOdoCounter)
        binding.costPerKm.setText(tripEntity.costPerKm.toString())
        binding.tripCost.setText(tripEntity.tripCost.toString())
        binding.addNoteDescription.setText(tripEntity.description)
        binding.startDateBtn.setText(tripEntity.startDate)
        binding.startTimeBtn.setText(tripEntity.startTime)
        binding.endDateBtn.setText(tripEntity.endDate)
        binding.endTimeBtn.setText(tripEntity.endTime)

        binding.addTripBtn.text = "Update Vehicle"

    }

    private fun addTripLog() {

        val sharedPrefManager = SharedPrefManager.getInstance(this)


        val type = binding.tripTitleType.text.toString()
        val tripStartPoint = binding.tripStartOrigin.text.toString()
        val initialOdometer = binding.tripStartOdoCounter.text.toString()
        val endDestination = binding.tripEndDestination.text.toString()
        val endOdometer = binding.tripEndOdoCounter.text.toString()
        val costPerKm = binding.costPerKm.text.toString()
        val tripCost = binding.tripCost.text.toString()
        val description = binding.addNoteDescription.text.toString()
        val vehicleId = sharedPrefManager.getActiveVehicleId()
        val startDate = binding.startDateBtn.text.toString()
        val startTime = binding.startTimeBtn.text.toString()
        val endDate = binding.endDateBtn.text.toString()
        val endTime = binding.endTimeBtn.text.toString()

        val trip = TripEntity(
            id = tripOld.id,
            title = type,
            startDate = startDate,
            endDate = endDate,
            startPoint = tripStartPoint,
            endPoint = endDestination,
            startOdoCounter = initialOdometer,
            finalOdoCounter = endOdometer,
            costPerKm = costPerKm.toLong(),
            tripCost = tripCost.toLong(),
            description = description,
            vehicleId = vehicleId,
            startTime = startTime,
            endTime = endTime
        )
        CoroutineScope(Dispatchers.IO).launch {
            tripDao.updateTripLog(trip)
        }

        startActivity(Intent(this@EditTripActivity, TripLogActivity::class.java))
        finish()


    }

    override fun onBackPressed() {
        startActivity(Intent(this, TripLogActivity::class.java))
        super.onBackPressed()
    }
}