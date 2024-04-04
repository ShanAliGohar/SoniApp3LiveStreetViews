package com.live.streetview.navigation.earthmap.compass.map.fuelmanager

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.live.streetview.navigation.earthmap.compass.map.MainActivity
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityFuelManagerBinding
import com.live.streetview.navigation.earthmap.compass.map.fragments.FinishbottomSheetFragment
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.chart.ChartsActivity
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.fuelprices.FuelDbActivity
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.location.RepoLocation
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.location.interfaces.MyLocationListener
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.vehicle.VehicleDatabase
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.vehicle.VehicleEntity
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.utils.SharedPrefManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale

class FuelManagerActivity : AppCompatActivity() {
    private val galleryPermissionCode = 101 // You can choose any code
    private val storagePermissionCode = 102
    private val locationPermissionCode = 103


    lateinit var repo: RepoLocation

    lateinit var binding: ActivityFuelManagerBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFuelManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Check if permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                galleryPermissionCode
            )
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


        // Check if storage permission is granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            Build.VERSION.SDK_INT < Build.VERSION_CODES.R &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Storage permission is not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                storagePermissionCode
            )
        }


        binding.ivBack.setOnClickListener {

            onBackPressed()
        }



        binding.MileageLog.setOnClickListener {
            val intent = Intent(this, MilageLogActivity::class.java)
            startActivity(intent)
        }
        binding.vehicle.setOnClickListener {
            val intent = Intent(this, VehiclesListActivity::class.java)
            startActivity(intent)
        }
        binding.TripLog.setOnClickListener {
            val intent = Intent(this, TripLogActivity::class.java)
            startActivity(intent)
        }
        /* binding.NearbyPlaces.setOnClickListener {
             val intent = Intent(this, FuelStationMapActivity::class.java)
             startActivity(intent)
         }*/
        binding.FuelPrices.setOnClickListener {
            val intent = Intent(this, FuelDbActivity::class.java)
            startActivity(intent)
        }
        binding.Analysis.setOnClickListener {
            showSpinnerDialog()
          /*  val intent = Intent(this, ChartsActivity::class.java)
            startActivity(intent)*/
        }
        binding.Refulling.setOnClickListener {
            val intent = Intent(this, RefuelingActivity::class.java)
            startActivity(intent)
        }

        binding.statistics.setOnClickListener {
            val intent = Intent(this, StatsActivity::class.java)
            startActivity(intent)
        }

        binding.AddCost.setOnClickListener {
            val intent = Intent(this, CostsLogActivity::class.java)
            startActivity(intent)
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@FuelManagerActivity, MainActivity::class.java))
                finish()
            }
        })

        getLocationUser()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            galleryPermissionCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Gallery permission granted, you can now access the gallery
                } else {
                    // Gallery permission denied, handle it as needed
                }
            }

            storagePermissionCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Storage permission granted, you can now access storage
                } else {
                    // Storage permission denied, handle it as needed
                }
            }
            // Add more cases if you have multiple permissions to request
            locationPermissionCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // location permission granted
                    getLocationUser()
                } else {
                    // Storage permission denied, handle it as needed
                }

            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun getLocationUser() {
        repo = RepoLocation(this, object : MyLocationListener {
            override fun onLocationChange(location: Location) {
                repo.stopLocation()
                currency(location)
            }
        })
    }


    fun currency(location: Location) {

        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)


        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                val country = addresses[0].countryName
                val apiUrl = "https://restcountries.com/v3.1/name/$country"
                val url = URL(apiUrl)

                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"


                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {

                        try {
                            val inputStream = connection.inputStream
                            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                            val response = StringBuilder()

                            var line: String?
                            while (bufferedReader.readLine().also { line = it } != null) {
                                response.append(line)
                            }

                            withContext(Dispatchers.Main) {

                                try {
                                    val jsonArray = JSONArray(response.toString())

                                    for (i in 0 until jsonArray.length()) {
                                        val jsonObject = jsonArray.getJSONObject(i)

                                        // Now you can access the data in the JSONObject
                                        val name =
                                            jsonObject.getJSONObject("name").getString("common")
                                        val capital =
                                            jsonObject.getJSONArray("capital").getString(0)


                                        val currenciesObject =
                                            jsonObject.optJSONObject("currencies")
                                        val currencyCode =
                                            currenciesObject?.keys()?.asSequence()?.toList()
                                                ?.firstOrNull()

                                        binding.defaultCurrency.text = currencyCode

                                        Log.d("current currency", "currency: $currencyCode ${currenciesObject.toString()}")

                                        val sharedPreferences =
                                            SharedPrefManager.getInstance(this@FuelManagerActivity)
                                        if (currencyCode != null) {
                                            sharedPreferences.setDefaultCurrency(currencyCode)
                                        }

                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    Toast.makeText(
                                        this@FuelManagerActivity,
                                        e.localizedMessage.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()

                                } finally {
                                    connection.disconnect()
                                }

                            }
                        } finally {
                            connection.disconnect()
                        }
                    }
                }
            }
        }
    }



    private fun showSpinnerDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_layout, null)
        builder.setView(dialogView)

        val spinner = dialogView.findViewById<Spinner>(R.id.spinner)
        val selectButton = dialogView.findViewById<Button>(R.id.selectButton)

        val dialog = builder.create()


        val vehiclesDao = VehicleDatabase.getDatabase(this).vehicleDao()

        vehiclesDao.getAllVehicleData().observe(this){list ->


            val vehicleNames = listOf("Select All") + list.map { it.vehicleName }

            // Define an array adapter for the spinner (adjust for your data)
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                vehicleNames
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

        }


        // Set a click listener for the "Select" button
        selectButton.setOnClickListener {

            val selectedPosition = spinner.selectedItemPosition

            // Create an intent to the next activity and pass the ID
            val intent = Intent(this, ChartsActivity::class.java)
            intent.putExtra("selectedVehiclePosition", selectedPosition)

            // Start the next activity
            startActivity(intent)

            // Dismiss the dialog
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }

}