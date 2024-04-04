package com.live.streetview.navigation.earthmap.compass.map.fuelmanager

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityVehiclesListBinding
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.adapter.AdapterListener
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.adapter.VehiclesAdapter
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.vehicle.VehicleDao
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.vehicle.VehicleDatabase
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.vehicle.VehicleEntity
import com.makeramen.roundedimageview.RoundedDrawable.drawableToBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class VehiclesListActivity : AppCompatActivity(), AdapterListener {

    private lateinit var binding: ActivityVehiclesListBinding
    private lateinit var vehicleDao: VehicleDao
    private lateinit var listVehicles: List<VehicleEntity>

    // Request code for both permissions
    private val READ_MEDIA_IMAGES_PERMISSION_CODE = 123


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVehiclesListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        vehicleDao = VehicleDatabase.getDatabase(this).vehicleDao()


        binding.addVehicle.setOnClickListener {
            startActivity(Intent(this, VehiclesActivity::class.java))
        }

        binding.imageBack.setOnClickListener {
            onBackPressed()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            // For Android 13 and below, request READ_EXTERNAL_STORAGE permission
            requestPermission()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // For Android 13 and above, request READ_MEDIA_IMAGES permission

            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is already granted, proceed with your logic
                // e.g., load media images
                setupRecyclerView()
            } else {
                // Permission has not been granted, request it
                requestMediaImagesPermission()
            }
        }


        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@VehiclesListActivity, FuelManagerActivity::class.java))
            }
        })


    }


    private fun requestPermission() {
        // Check if the permission is already granted
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission already granted, proceed with your logic
            setupRecyclerView()
        } else {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_MEDIA_IMAGES_PERMISSION_CODE
            )
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestMediaImagesPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.READ_MEDIA_IMAGES), READ_MEDIA_IMAGES_PERMISSION_CODE
        )
    }


    private fun setupRecyclerView() {
        vehicleDao.getAllVehicleData().observe(this) { list ->

            if (list.isNullOrEmpty()) {
                val vehicleData =
                    VehicleEntity(
                        vehicleImage = drawableToUri(
                            this,
                            resources.getDrawable(R.drawable.vehicle_img)
                        ),
                        vehicleName = "CIVIC",
                        vehicleDescription = "Description",
                        vehicleMake = "Honda",
                        vehicleModel = "Civic VTI",
                        vehicleYear = 2023,
                        vehicleLicence = "Licence",
                        vehicleVn = "Car Vn",
                        vehicleInsurance = "Insurance",
                        isVehicleActive = true,
                        vehicleDistanceUnit = "SelectedUnit",
                        vehicleFuelUnit = "selectedUnitFuel",
                        vehicleConsumption = "km/l",
                        vehicleGasType = "Petrol",
                        isBiFuelVehicle = false,
                        gasType = "CNG",
                        gasDistanceType = "Kg/km",
                        carTankCapacity = "400"
                    )

                lifecycleScope.launch {
                    vehicleDao.insertVehicleData(vehicleData)
                }
            }

            listVehicles = list
            Log.d("tagggggg", "setupRecyclerView: $list")
            binding.vehiclesRecycler.layoutManager = LinearLayoutManager(this)
            val adapter = VehiclesAdapter(this, list)
            binding.vehiclesRecycler.adapter = adapter
            adapter.notifyDataSetChanged()

        }

    }


    override fun onEditClicked(position: Int) {

        //val vehicle = listVehicles[position]
        val intent = Intent(this, EditVehiclesActivity::class.java)
        intent.putExtra("vehicle", position)
        startActivity(intent)
        finish()

    }

    override fun onDeleteClicked(position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            vehicleDao.deleteVehicleData(listVehicles[position].id)
        }
    }

    override fun onCarDescriptionClicked(position: Int) {

        showCarDescriptionDialog(
            this@VehiclesListActivity,
            listVehicles[position].vehicleDescription
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            READ_MEDIA_IMAGES_PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with your logic
                    // e.g., load media images
                    setupRecyclerView()
                } else {
                    // Permission denied, handle accordingly
                    // e.g., show a message or disable functionality
                }
                return
            }
            // Add more cases if you have multiple permissions to request
        }
    }


    fun drawableToUri(context: Context, drawable: Drawable): String? {
        val bitmap = drawableToBitmap(drawable)
        return bitmapToUri(context, bitmap)
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    private fun bitmapToUri(context: Context, bitmap: Bitmap): String? {
        val imagesFolder = File(context.cacheDir, "images")
        if (!imagesFolder.exists()) {
            imagesFolder.mkdirs()
        }

        val file = File(imagesFolder, "image.png")
        try {
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return if (file.exists()) {
            MediaStore.Images.Media.insertImage(
                context.contentResolver,
                file.absolutePath,
                file.name,
                file.name
            )
        } else {
            null
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@VehiclesListActivity, FuelManagerActivity::class.java))
    }


    private fun showCarDescriptionDialog(context: Context, carDescription: String) {
        // Inflate the dialog layout
        val alertDialogBuilder = AlertDialog.Builder(context)

        // Set dialog title and message
        alertDialogBuilder.setTitle("Trip Description")
        alertDialogBuilder.setMessage(carDescription)

        // Set positive button
        alertDialogBuilder.setPositiveButton("OK") { dialog: DialogInterface?, _: Int ->
            // Positive button click action
            dialog?.dismiss()
        }

        // Show the dialog
        alertDialogBuilder.create().show()
    }
}
