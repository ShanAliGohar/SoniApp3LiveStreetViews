package com.live.streetview.navigation.earthmap.compass.map.fuelmanager

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityTripBinding
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


class TripActivity : AppCompatActivity() {

    private val binding: ActivityTripBinding by lazy {
        ActivityTripBinding.inflate(layoutInflater)
    }

    private lateinit var tripDao: TripDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor("#2D44B5")


        tripDao = TripDatabase.getDatabase(this).tripDao()


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

    private fun addTripLog() {
        val sharedPrefManager = SharedPrefManager.getInstance(this)

        val type = binding.tripTitleType.text.toString()
        val tripStartPoint = binding.tripStartOrigin.text.toString()
        val initialOdometer = binding.tripStartOdoCounter.text.toString()
        val endDestination = binding.tripEndDestination.text.toString()
        val endOdometer = binding.tripEndOdoCounter.text.toString()
        val costPerKm = binding.costPerKm.text.toString().toLongOrNull() ?: 0
        val tripCost = binding.tripCost.text.toString().toLongOrNull() ?: 0
        val description = binding.addNoteDescription.text.toString()
        val startDate = binding.startDateBtn.text.toString()
        val startTime = binding.startTimeBtn.text.toString()
        val endDate = binding.endDateBtn.text.toString()
        val endTime = binding.endTimeBtn.text.toString()

        // Check if any of the required fields is empty
        if (type.isBlank() || tripStartPoint.isBlank() || initialOdometer.isBlank() ||
            endDestination.isBlank() || endOdometer.isBlank()  || startDate.isBlank() || startTime.isBlank() ||
            endDate.isBlank() || endTime.isBlank()
        ) {
            // Display an error message to the user indicating that all fields are required
            // You can use Toast, Snackbar, or any other UI element to show the message
            // For example, using Toast:
            showToast("All fields are required")
            return
        }

        val vehicleId = sharedPrefManager.getActiveVehicleId()

        val trip = TripEntity(
            title = type,
            startDate = startDate,
            endDate = endDate,
            startPoint = tripStartPoint,
            endPoint = endDestination,
            startOdoCounter = initialOdometer,
            finalOdoCounter = endOdometer,
            costPerKm = costPerKm,
            tripCost = tripCost,
            description = description,
            vehicleId = vehicleId,
            startTime = startTime,
            endTime = endTime
        )

        CoroutineScope(Dispatchers.IO).launch {
            tripDao.insertTripLog(trip)
        }

        startActivity(Intent(this@TripActivity, TripLogActivity::class.java))
        finish()
    }

    // Helper function to show a Toast message
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
       startActivity(Intent(this, TripLogActivity::class.java))
        finish()
        super.onBackPressed()
    }
}