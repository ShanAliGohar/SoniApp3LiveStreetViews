package com.live.streetview.navigation.earthmap.compass.map.fuelmanager

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityTripLogBinding
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.adapter.AdapterListener
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.adapter.AdapterTripLog
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling.MileageLogEntity
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.trip.TripDao
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.trip.TripDatabase
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.trip.TripEntity
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.services.AutoTripServices
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.utils.showDateDialogStartPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class TripLogActivity : AppCompatActivity(), AdapterListener {

    lateinit var tripDao: TripDao

    lateinit var list: List<TripEntity>
    private val locationPermissionCode = 103

    private lateinit var binding: ActivityTripLogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tripDao = TripDatabase.getDatabase(this).tripDao()


        tripDao.getAllTripLogs().observe(this) { list ->

            this.list = list

            setAdapter(list)


            binding.btnFilterDate.setOnClickListener {

                val startDate = binding.startDateButton.text.toString()
                val endDate = binding.endDateButton.text.toString()


                if (startDate.contains("2") && endDate.contains("2")) {

                    val filteredList = filterItemsByDateRange(startDate, endDate, list)

                    setAdapter(filteredList)

                } else {
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
            startActivity(Intent(this@TripLogActivity, FuelManagerActivity::class.java))
        }

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Location permission is not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }

        binding.AddTrip.setOnClickListener {
            showBottomSheet()
        }



        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@TripLogActivity, FuelManagerActivity::class.java))
            }
        })

    }

    private fun filterItemsByDateRange(
        startDateString: String,
        endDateString: String,
        itemList: List<TripEntity>
    ): List<TripEntity> {
        val sdf = SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH)

        try {
            val startDate = sdf.parse(startDateString)
            val endDate = sdf.parse(endDateString)

            if (startDate != null && endDate != null) {
                return itemList.filter { item ->
                    val itemDate = sdf.parse(item.startDate)
                    itemDate in startDate..endDate
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return emptyList()
    }

    private fun setAdapter(list: List<TripEntity>) {

        if (list.isEmpty()) {
            binding.textEmptyList.visibility = View.VISIBLE
        } else {
            binding.textEmptyList.visibility = View.INVISIBLE
            binding.tripLogRecycler.visibility = View.VISIBLE
            binding.tripLogRecycler.layoutManager = LinearLayoutManager(this@TripLogActivity)
            val adapter = AdapterTripLog(this@TripLogActivity, list, this)
            adapter.run {
                binding.tripLogRecycler.adapter = this
                notifyDataSetChanged()
            }
        }
    }


    override fun onEditClicked(position: Int) {
        if (position >= 0 && position < list.size) {
            val intent = Intent(this, EditTripActivity::class.java)
            intent.putExtra("trip", position)
            startActivity(intent)
        } else {
            // Handle invalid position
            Toast.makeText(this, "Invalid position", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDeleteClicked(position: Int) {
        if (position >= 0 && position < list.size) {
            CoroutineScope(Dispatchers.IO).launch {
                tripDao.deleteTripLog(list[position].id)
                withContext(Dispatchers.Main) {
                    if (position == 0) {
                        binding.tripLogRecycler.visibility = View.GONE
                    } else {
                        binding.tripLogRecycler.visibility = View.VISIBLE
                    }
                    setAdapter(list)
                }
            }
        } else {
            // Handle invalid position
            setAdapter(list)
            Toast.makeText(this, "Invalid position", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCarDescriptionClicked(position: Int) {
        showCarDescriptionDialog(this, list[position].description)
    }


    private fun showCarDescriptionDialog(context: Context, tripDescription: String) {
        val alertDialogBuilder = AlertDialog.Builder(context)

        // Set dialog title and message
        alertDialogBuilder.setTitle("Trip Description")
        alertDialogBuilder.setMessage(tripDescription)

        // Set positive button
        alertDialogBuilder.setPositiveButton("OK") { dialog: DialogInterface?, _: Int ->
            // Positive button click action
            dialog?.dismiss()
        }

        // Show the dialog
        alertDialogBuilder.create().show()
    }

    private fun showBottomSheet() {
        val bottomSheetFragment = TripTypeBottomSheetFragment()

        bottomSheetFragment.setOnItemClickListener(object :
            TripTypeBottomSheetFragment.OnItemClickListener {
            override fun onAutoTripClick() {
                try {
                    // Handle Auto Trip click
                    // For example, start Auto Trip activity or perform related actions

                    val serviceIntent = Intent(this@TripLogActivity, AutoTripServices::class.java)
                    startService(serviceIntent)

                    Toast.makeText(this@TripLogActivity, "Auto Trip Started", Toast.LENGTH_SHORT)
                        .show()
                } catch (e: Exception) {
                    // Handle exceptions
                    e.printStackTrace()
                    Toast.makeText(this@TripLogActivity, "Error: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onManualTripClick() {
                // Handle Manual Trip click
                // For example, start Manual Trip activity or perform related actions
                startActivity(Intent(this@TripLogActivity, TripActivity::class.java))
                finish()
            }
        })

        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }

    override fun onBackPressed() {
        startActivity(Intent(this, FuelManagerActivity::class.java))
        super.onBackPressed()
    }
}