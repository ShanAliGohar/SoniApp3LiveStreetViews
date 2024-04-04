package com.live.streetview.navigation.earthmap.compass.map.fuelmanager

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityEditVehiclesBinding
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.repos.VehicleRepo
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.vehicle.VehicleDao
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.vehicle.VehicleDatabase
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.vehicle.VehicleEntity
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.viewModels.VehicleViewModel
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.viewModels.VehicleViewModelFactory
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.utils.SharedPrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class EditVehiclesActivity : AppCompatActivity() {
    private val binding: ActivityEditVehiclesBinding by lazy {
        ActivityEditVehiclesBinding.inflate(layoutInflater)
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
    private lateinit var vehicleViewModel: VehicleViewModel


    val fuelTypeSet = arrayOf("Not Set", "Diesel", "Petrol", "CNG", "Electric")


    private lateinit var vehicleEntity: VehicleEntity


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        vehicleDao = VehicleDatabase.getDatabase(this@EditVehiclesActivity).vehicleDao()

        // Inside your Activity or Fragment
        val vehicleRepository = VehicleRepo(vehicleDao)

        val vehicleViewModel1: VehicleViewModel by viewModels {
            VehicleViewModelFactory(vehicleRepository)
        }

        vehicleViewModel = vehicleViewModel1


//        vehicleEntity = intent.getParcelableExtra("vehicle", VehicleEntity::class.java)!!

        val position = intent.getIntExtra("vehicle", 0)
        vehicleViewModel.getAllVehicles().observe(this) {

            vehicleEntity = it[position]
            setVehicle(vehicleEntity)
            spinnerDistanceunit()
            SpinnerFuelUnit()
            Spinnerconsumption()
            SpinnersetType()
            SpinnerFuelUnitBi()
            SpinnerConsumptionBi()
        }



        binding.backImage.setOnClickListener {
            onBackPressed()
        }

        binding.buttonCancel.setOnClickListener {
            onBackPressed()
        }


        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@EditVehiclesActivity, VehiclesListActivity::class.java))
            }
        })

        listeners()


        // initViewModel()
    }
    /*
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


    private fun setVehicle(vehicleEntity: VehicleEntity) {

        binding.apply {

            val decodedImage = decodeBase64Image(vehicleEntity.vehicleImage)

            selectedImage = vehicleEntity.vehicleImage
            imageView2.setImageBitmap(decodedImage)
            nameEdittext.setText(vehicleEntity.vehicleName)
            DescriptionEdittext.setText(vehicleEntity.vehicleDescription)
            MakeEdittext.setText(vehicleEntity.vehicleMake)
            ModelEdittext.setText(vehicleEntity.vehicleModel)
            YearEdittext.setText(vehicleEntity.vehicleYear.toString())
            LicenseEdittext.setText(vehicleEntity.vehicleLicence)
            VinEdittext.setText(vehicleEntity.vehicleVn)
            InsuranceEdittext.setText(vehicleEntity.vehicleInsurance)
            carTankEditText.setText(vehicleEntity.carTankCapacity)
            ActiveSwitch.isChecked = vehicleEntity.isVehicleActive
            binding.BiFuelSwitch.isChecked = vehicleEntity.isBiFuelVehicle


        }

        Log.d("settype", "setVehicle: ${vehicleEntity.vehicleGasType}")
        val fuelType = fuelTypeSet.indexOf(vehicleEntity.vehicleGasType)
        if (fuelType != -1) {
            Log.d("settype", "setVehicle: $fuelType")
            binding.SpinnersetType.setSelection(fuelType)
        }

    }

    private fun listeners() {
        with(binding) {
            addPhotoFromGallery.setOnClickListener {
                val galleryIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, 1)
            }

            buttonSave.setOnClickListener {
                insertDataToRoom()
            }

            buttonCancel.setOnClickListener {
                onBackPressed()
                finish()
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
                val isCarActivated = ActiveSwitch.isChecked
                isBiFuelVehicle = BiFuelSwitch

                val vehicleEntity = VehicleEntity(
                    id = vehicleEntity.id,
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
                    isBiFuelVehicle = isBiFuelVehicle!!.isChecked,
                    gasType = selectedUnitGasTypeBiFuel,
                    gasDistanceType = selectedUnitConsumptionBidistance,
                    carTankCapacity = tankCapacity
                )
                initSwitch(vehicleEntity.id)
                vehicleDao = VehicleDatabase.getDatabase(this@EditVehiclesActivity).vehicleDao()
                withContext(Dispatchers.IO) {
                    vehicleDao.updateVehicle(vehicleEntity)
                    if (vehicleEntity.isVehicleActive) {
                        val sharedPrefManager =
                            SharedPrefManager.getInstance(this@EditVehiclesActivity)
                        sharedPrefManager.setActiveVehicleId(vehicleEntity.id)
                    }
                    Log.d("insertToRoom", "insertToRoom: $vehicleEntity")
                    withContext(Dispatchers.Main) {
                        startActivity(
                            Intent(
                                this@EditVehiclesActivity,
                                VehiclesListActivity::class.java
                            )
                        )
                        finish()
                    }
                }
            }
        }
    }
    
    

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            selectedImage = convertImageToBase64(data.data)
            binding.imageView2.setImageURI(data.data)
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


        if (vehicleEntity.vehicleDistanceUnit == distanceUnits[0]){

            binding.SpinnerDistanceUNit.setSelection(0)
        }else if (vehicleEntity.vehicleDistanceUnit == distanceUnits[1]){
            binding.SpinnerDistanceUNit.setSelection(1)
        }

        // Set an OnItemSelectedListener to respond to item selection
        binding.SpinnerDistanceUNit.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
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
        val fuelUnit = arrayOf("Liters", "Gallons", "Kwh", "Kg")
        val adapter = ArrayAdapter(this, R.layout.custom_spinner_dropdown_item, fuelUnit)
        binding.SpinnerFuelUnit.adapter = adapter




        if (vehicleEntity.vehicleFuelUnit == fuelUnit[0]){
            binding.SpinnerFuelUnit.setSelection(0)
        }else if (vehicleEntity.vehicleFuelUnit == fuelUnit[1]){
            binding.SpinnerFuelUnit.setSelection(1)
        } else if (vehicleEntity.vehicleFuelUnit == fuelUnit[2]){
            binding.SpinnerFuelUnit.setSelection(2)
        }else if (vehicleEntity.vehicleFuelUnit == fuelUnit[3]){
            binding.SpinnerFuelUnit.setSelection(3)
        }




        // Set an OnItemSelectedListener to respond to item selection
        binding.SpinnerFuelUnit.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedUnitFuel = fuelUnit[position]
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
        val fuelConsuptions = arrayOf("km/l", "Km/gal", "Kwh", "Km/kg")
        val adapter = ArrayAdapter(this, R.layout.custom_spinner_dropdown_item, fuelConsuptions)
        binding.SpinnerConsumption.adapter = adapter


        if (vehicleEntity.vehicleConsumption == fuelConsuptions[0]){
            binding.SpinnerConsumption.setSelection(0)
        }else if (vehicleEntity.vehicleConsumption == fuelConsuptions[1]){
            binding.SpinnerConsumption.setSelection(1)
        } else if (vehicleEntity.vehicleConsumption == fuelConsuptions[2]){
            binding.SpinnerConsumption.setSelection(2)
        }else if (vehicleEntity.vehicleConsumption == fuelConsuptions[3]){
            binding.SpinnerConsumption.setSelection(3)
        }

        // Set an OnItemSelectedListener to respond to item selection
        binding.SpinnerConsumption.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedUnitConsumption = fuelConsuptions[position]
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
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedUnitGasType = distanceUnits[position]
                    // You can perform actions based on the selected unit here
//                    Toast.makeText(this@VehiclesActivity, "Selected: $selectedUnit", Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing when nothing is selected
                }
            }



        val fuelType = fuelTypeSet.indexOf(vehicleEntity.vehicleGasType)
        if (fuelType != -1) {
            Log.d("settype", "setVehicle: $fuelType")
            binding.SpinnersetType.setSelection(fuelType)
        }
    }

    private fun SpinnerFuelUnitBi() {
        // Add options to the Spinner
        val distanceUnits = arrayOf("Liters", "Gallons", "Kwh", "Kg")
        val adapter = ArrayAdapter(this, R.layout.custom_spinner_dropdown_item, distanceUnits)
        binding.SpinnerFuelUnitBi.adapter = adapter


        if (vehicleEntity.gasType == distanceUnits[0]){
            binding.SpinnerFuelUnitBi.setSelection(0)
        }else if (vehicleEntity.gasType == distanceUnits[1]){
            binding.SpinnerFuelUnitBi.setSelection(1)
        } else if (vehicleEntity.gasType == distanceUnits[2]){
            binding.SpinnerFuelUnitBi.setSelection(2)
        }else if (vehicleEntity.gasType == distanceUnits[3]){
            binding.SpinnerFuelUnitBi.setSelection(3)
        }


        // Set an OnItemSelectedListener to respond to item selection
        binding.SpinnerFuelUnitBi.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
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
                if (byteArray.size > 1000 * 1024) {
                    return compressAndEncodeImage(byteArray)
                }

                binding.imageView2.setImageURI(uri)

                // Convert the byte array to Base64
                return Base64.encodeToString(byteArray, Base64.DEFAULT)
            }
        }
        Toast.makeText(this, "Failed Try Again", Toast.LENGTH_SHORT).show()
        return ""
    }


    private fun compressAndEncodeImage(byteArray: ByteArray): String {
        // Convert the byte array to a Bitmap
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

        // Compress the bitmap
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream)

        // Encode the compressed data as Base64
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }


    fun compressAndEncodeImage(filePath: String): String {
        val bitmap = BitmapFactory.decodeFile(filePath)

        // Compress the bitmap
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)

        // Encode the compressed data as Base64
        val compressedBase64 = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)

        return compressedBase64
    }

    private fun SpinnerConsumptionBi() {
        // Add options to the Spinner
        val distanceUnits = arrayOf("km/l", "Km/gal", "Kwh", "Km/kg")
        val adapter = ArrayAdapter(this, R.layout.custom_spinner_dropdown_item, distanceUnits)
        binding.SpinnerConsumptionBi.adapter = adapter



        if (vehicleEntity.gasDistanceType == distanceUnits[0]){
            binding.SpinnerConsumptionBi.setSelection(0)
        }else if (vehicleEntity.gasDistanceType == distanceUnits[1]){
            binding.SpinnerConsumptionBi.setSelection(1)
        } else if (vehicleEntity.gasDistanceType == distanceUnits[2]){
            binding.SpinnerConsumptionBi.setSelection(2)
        }else if (vehicleEntity.gasDistanceType == distanceUnits[3]){
            binding.SpinnerConsumptionBi.setSelection(3)
        }



        // Set an OnItemSelectedListener to respond to item selection
        binding.SpinnerConsumptionBi.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
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


    private fun decodeBase64Image(base64Image: String?): Bitmap? {
        base64Image?.let {
            val decodedBytes = Base64.decode(it, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        }
        return null
    }

    override fun onBackPressed() {
        startActivity(Intent(this, VehiclesListActivity::class.java))
        super.onBackPressed()
    }
}