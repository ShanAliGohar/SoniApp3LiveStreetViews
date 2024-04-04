package com.live.streetview.navigation.earthmap.compass.map.fuelmanager

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityVehiclesBinding
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.vehicle.VehicleDao
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.vehicle.VehicleDatabase
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.vehicle.VehicleEntity
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.viewModels.VehicleViewModel
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.utils.SharedPrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VehiclesActivity : AppCompatActivity() {
    private val binding: ActivityVehiclesBinding by lazy {
        ActivityVehiclesBinding.inflate(layoutInflater)
    }
    private var selectedImage: String? = null
    private lateinit var vehicleDao: VehicleDao
    private var selectedUnit: String = ""
    private var selectedUnitFuel: String = ""
    private var selectedUnitConsumption: String = ""
    private var selectedUnitGasType: String = ""
    private var selectedUnitGasTypeBiFuel: String = ""
    private var selectedUnitConsumptionBidistance: String = ""
    private var biFuelVehicle: Boolean = true
    private var isBiFuelVehicle: SwitchCompat? = null
    private  val vehicleViewModel : VehicleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        listeners()
        spinnerDistanceunit()
        SpinnerFuelUnit()
        Spinnerconsumption()
        SpinnersetType()
        SpinnerFuelUnitBi()
        SpinnerConsumptionBi()


        binding.imageBack.setOnClickListener {
            onBackPressed()
        }
        // initViewModel()
    }/*
        private fun initViewModel() {
            val vehicleRepository =
                MileageLogRepo(MileageLogDatabase.getDatabase(this@VehiclesActivity).mileageDao())
            val viewModelFactory = MileageViewModelFactory(vehicleRepository)
            vehicleViewModel =
                ViewModelProvider(
                    this@VehiclesActivity,
                    viewModelFactory
                )[VehicleViewModel::class.java]
        }*/

    private fun listeners() {
        with(binding) {
            addPhotoFromGallery.setOnClickListener {
                val galleryIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, 1)
            }

            buttonSave.setOnClickListener {
                insertDataToRoom()
                onBackPressed()
            }
            buttonCancel.setOnClickListener {
                onBackPressed()
            }
        }
    }

    /*insert user input data to room database....*/
    private fun insertDataToRoom() {
        CoroutineScope(Dispatchers.Main).launch {
            with(binding) {
                val name = nameEdittext.text.toString()
                val description = DescriptionEdittext.text.toString()
                val make = MakeEdittext.text.toString()
                val model = ModelEdittext.text.toString()
                val year = YearEdittext.text.toString().toIntOrNull() ?: 0
                val licence = LicenseEdittext.text.toString()
                val carVn = VinEdittext.text.toString()
                val insurance = InsuranceEdittext.text.toString()
                val tankCapacity = carTankEditText.text.toString()
                val isCarActivated = ActiveSwitch.isActivated
                isBiFuelVehicle = BiFuelSwitch

                val vehicleEntity = VehicleEntity(
                    vehicleImage = selectedImage.toString(),
                    vehicleName = name,
                    vehicleDescription = description,
                    vehicleMake = make,
                    vehicleModel = model,
                    vehicleYear = year,
                    vehicleLicence = licence,
                    vehicleVn = carVn,
                    vehicleInsurance = insurance,
                    isVehicleActive = isCarActivated,
                    vehicleDistanceUnit = selectedUnit,
                    vehicleFuelUnit = selectedUnitFuel,
                    vehicleConsumption = selectedUnitConsumption,
                    vehicleGasType = selectedUnitGasType,
                    isBiFuelVehicle = true,
                    gasType = selectedUnitGasTypeBiFuel,
                    gasDistanceType = selectedUnitConsumptionBidistance,
                    carTankCapacity = tankCapacity
                )
                initSwitch(vehicleEntity.id)
                vehicleDao = VehicleDatabase.getDatabase(this@VehiclesActivity).vehicleDao()
                withContext(Dispatchers.IO) {
                    val id = vehicleDao.insertVehicleData(vehicleEntity)
                    if (vehicleEntity.isVehicleActive) {
                        val sharedPrefManager = SharedPrefManager.getInstance(this@VehiclesActivity)
                        sharedPrefManager.setActiveVehicleId(id)
                    }
                    Log.d("insertToRoom", "insertToRoom: $vehicleEntity")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            selectedImage = convertImageToBase64(data.data)
        }
    }

    private fun initSwitch(id: Long) {
        binding.BiFuelSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Update the ViewModel with the new state
            vehicleViewModel.updateBiFuelState(id, isChecked)
        }
    }

    private fun spinnerDistanceunit() {
        // Add options to the Spinner
        val distanceUnits = arrayOf("Kilometer", "Miles")
        val adapter = ArrayAdapter(this, R.layout.custom_spinner_dropdown_item, distanceUnits)
        binding.SpinnerDistanceUNit.adapter = adapter

        // Set an OnItemSelectedListener to respond to item selection
        binding.SpinnerDistanceUNit.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    selectedUnit =
                        distanceUnits[position]// You can perform actions based on the selected unit here
//                    Toast.makeText(this@VehiclesActivity, "Selected: $selectedUnit", Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing when nothing is selected
                }
            }
    }

    private fun SpinnerFuelUnit() {
        // Add options to the Spinner
        val distanceUnits = arrayOf("Liters", "Gallons", "Kwh", "Kg")
        val adapter = ArrayAdapter(this, R.layout.custom_spinner_dropdown_item, distanceUnits)
        binding.SpinnerFuelUnit.adapter = adapter

        // Set an OnItemSelectedListener to respond to item selection
        binding.SpinnerFuelUnit.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    selectedUnitFuel = distanceUnits[position]
                    // You can perform actions based on the selected unit here
//                    Toast.makeText(this@VehiclesActivity, "Selected: $selectedUnit", Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing when nothing is selected
                }
            }
    }

    private fun Spinnerconsumption() {
        // Add options to the Spinner
        val distanceUnits = arrayOf("km/l", "Km/gal", "Kwh", "Km/kg")
        val adapter = ArrayAdapter(this, R.layout.custom_spinner_dropdown_item, distanceUnits)
        binding.SpinnerConsumption.adapter = adapter

        // Set an OnItemSelectedListener to respond to item selection
        binding.SpinnerConsumption.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    selectedUnitConsumption = distanceUnits[position]
                    // You can perform actions based on the selected unit here
//                    Toast.makeText(this@VehiclesActivity, "Selected: $selectedUnit", Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing when nothing is selected
                }
            }
    }

    private fun SpinnersetType() {
        // Add options to the Spinner
        val distanceUnits = arrayOf("Not Set", "Diesel", "Petrol", "CNG", "Electric")
        val adapter = ArrayAdapter(this, R.layout.custom_spinner_dropdown_item, distanceUnits)
        binding.SpinnersetType.adapter = adapter

        // Set an OnItemSelectedListener to respond to item selection
        binding.SpinnersetType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    selectedUnitGasType = distanceUnits[position]
                    // You can perform actions based on the selected unit here
//                    Toast.makeText(this@VehiclesActivity, "Selected: $selectedUnit", Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing when nothing is selected
                }
            }
    }

    private fun SpinnerFuelUnitBi() {
        // Add options to the Spinner
        val distanceUnits = arrayOf("Liters", "Gallons", "Kwh", "Kg")
        val adapter = ArrayAdapter(this, R.layout.custom_spinner_dropdown_item, distanceUnits)
        binding.SpinnerFuelUnitBi.adapter = adapter

        // Set an OnItemSelectedListener to respond to item selection
        binding.SpinnerFuelUnitBi.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    selectedUnitGasTypeBiFuel = distanceUnits[position]
                    // You can perform actions based on the selected unit here
//                    Toast.makeText(this@VehiclesActivity, "Selected: $selectedUnit", Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing when nothing is selected
                }
            }
    }

    private fun convertImageToBase64(uri: Uri?): String {
        uri?.let {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                // Read the image file into a byte array
                val byteArray = inputStream.readBytes()

                // Check if the file size is within the limit (600 KB)
                if (byteArray.size > 600 * 1024) {
                    // Notify the user that the image size exceeds the limit
                    // For example, show a Toast or display an error message
                    Toast.makeText(this, "Image size should be less than 600 KB", Toast.LENGTH_SHORT).show()
                    return ""
                }

                binding.imageView2.setImageURI(uri)

                // Convert the byte array to Base64
                return Base64.encodeToString(byteArray, Base64.DEFAULT)
            }
        }
        return ""
    }

    private fun SpinnerConsumptionBi() {
        // Add options to the Spinner
        val distanceUnits = arrayOf("km/l", "Km/gal", "Kwh", "Km/kg")
        val adapter = ArrayAdapter(this, R.layout.custom_spinner_dropdown_item, distanceUnits)
        binding.SpinnerConsumptionBi.adapter = adapter

        // Set an OnItemSelectedListener to respond to item selection
        binding.SpinnerConsumptionBi.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    selectedUnitConsumptionBidistance = distanceUnits[position]
                    // You can perform actions based on the selected unit here
//                    Toast.makeText(this@VehiclesActivity, "Selected: $selectedUnit", Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing when nothing is selected
                }
            }
    }
}