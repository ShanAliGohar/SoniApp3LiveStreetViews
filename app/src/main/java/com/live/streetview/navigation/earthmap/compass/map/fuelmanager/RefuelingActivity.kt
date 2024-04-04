package com.live.streetview.navigation.earthmap.compass.map.fuelmanager

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityRefulingBinding
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.repos.MileageLogRepo
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling.MileageLogDao
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling.MileageLogDatabase
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling.MileageLogEntity
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.vehicle.VehicleDao
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.vehicle.VehicleDatabase
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.vehicle.VehicleEntity
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.viewModels.MileageLogViewModel
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.viewModels.MileageViewModelFactory
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.utils.showDateDialogStartPoint
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.utils.showTimeDialogStartPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

class RefuelingActivity : AppCompatActivity() {
    private val binding: ActivityRefulingBinding by lazy {
        ActivityRefulingBinding.inflate(layoutInflater)
    }
    private lateinit var vehicleDao: VehicleDao
    private var selectedCar: VehicleEntity? = null
    private lateinit var mileageLogDao: MileageLogDao
    private lateinit var mileageViewModel: MileageLogViewModel

    private lateinit var estimateFuel: String

    private var cost by Delegates.notNull<Long>()
    private val REQUEST_CODE = 123


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        vehicleDao = VehicleDatabase.getDatabase(this).vehicleDao()

        listeners()
        // initViewModel()
//        binding.tanklevelconstraint.visibility = View.GONE
        spinnerCarselect()

        binding.imageBack.setOnClickListener {
            onBackPressed()
        }

        binding.gasStationTypeTv.setOnClickListener {
            val intent = Intent(this, FuelStationMapActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }
    }


    // Callback method to handle the result from the started activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // The result was successful, handle the data returned from ResultActivity
                val resultValue = data?.getStringExtra(FuelStationMapActivity.RESULT_KEY)
                binding.gasStationTypeTv.text = "Result: $resultValue"
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation
                binding.gasStationTypeTv.text = "Result: Canceled"
            }
        }
    }


    private fun initViewModel() {
        val markerRepository =
            MileageLogRepo(MileageLogDatabase.getDatabase(this@RefuelingActivity).mileageDao())
        val viewModelFactory = MileageViewModelFactory(markerRepository)
        mileageViewModel = ViewModelProvider(
            this@RefuelingActivity, viewModelFactory
        )[MileageLogViewModel::class.java]

        mileageViewModel.getAllMileageData.observe(this) { mileageData ->
            retrieveDataFromDb(mileageData)
        }

    }

    /*retrieve user input data to room database....*/
    private fun retrieveDataFromDb(mileageData: List<MileageLogEntity>) {
        CoroutineScope(Dispatchers.Main).launch {
            with(binding) {
                for (mileageDataEntity in mileageData) {
                    gasTextview.text =
                        Editable.Factory.getInstance().newEditable(mileageDataEntity.gas.toString())
                    gastypetextview.text =
                        Editable.Factory.getInstance().newEditable(mileageDataEntity.gasType)
                    priceTextview.text = Editable.Factory.getInstance()
                        .newEditable(mileageDataEntity.price.toString())
                    costTextview.text = Editable.Factory.getInstance()
                        .newEditable(mileageDataEntity.cost.toString())
                    dateTextview.text =
                        Editable.Factory.getInstance().newEditable(mileageDataEntity.date)
                    timeTextview.text =
                        Editable.Factory.getInstance().newEditable(mileageDataEntity.time)
                    radioButtonFulltank.isChecked = true
                    tankCapBefore.text = Editable.Factory.getInstance()
                        .newEditable(mileageDataEntity.tankLevelBefore)
                    editText.text =
                        Editable.Factory.getInstance().newEditable(mileageDataEntity.tankLevelAfter)
                    gasStationTypeTv.text =
                        Editable.Factory.getInstance().newEditable(mileageDataEntity.gasStationType)
                    radioButtonDiscount.isChecked = true
                    editText2.text = Editable.Factory.getInstance()
                        .newEditable(mileageDataEntity.discountPerLiter.toString())
                    editTextdiscount.text = Editable.Factory.getInstance()
                        .newEditable(mileageDataEntity.totalDiscount.toString())
                    desEdittext.text =
                        Editable.Factory.getInstance().newEditable(mileageDataEntity.addNote)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun listeners() {
        with(binding) {
            spinnerMileageType.setOnClickListener {
                if (radioGroupMileageSelection.visibility == View.VISIBLE) {
                    radioGroupMileageSelection.visibility = View.GONE
                } else {
                    radioGroupMileageSelection.visibility = View.VISIBLE
                }
            }
//            spinnerCarselect.setOnClickListener {
//                spinnerCarselect()
//            }


            radioButton1.setOnClickListener {
                spinnerMileageType.hint = "Odometer"
                radioGroupMileageSelection.visibility = View.GONE
            }
            radioButton2.setOnClickListener {
                spinnerMileageType.hint = "Trip Odometer"
                radioGroupMileageSelection.visibility = View.GONE
            }

            radioButtonFulltank.setOnClickListener {

            }
            radiogasStation.setOnClickListener {

            }


            radioButtonFulltank.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    // RadioButton1 is checked
                    // Do something when RadioButton1 is checked

                    tanklevelconstraint.visibility = View.INVISIBLE

                } else {
                    // RadioButton1 is unchecked
                    // Do something when RadioButton1 is unchecked
                }
            }


            radioButtonsetuptanklevel.setOnClickListener {
                tanklevelconstraint.visibility = View.VISIBLE
            }
            radioButtonDiscount.setOnClickListener {
                val isChecked = radioButtonDiscount.isChecked
                if (isChecked) {
                    discountdialogconstraint.visibility = View.VISIBLE
                } else {
                    discountdialogconstraint.visibility = View.GONE
                    discountradio.clearCheck()
                }

            }

            /*  radioButtonDiscount.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        discountdialogconstraint.visibility = View.VISIBLE
                    } else {
                        discountdialogconstraint.visibility = View.GONE
                    }
                }*/

            addMileageBtn.setOnClickListener {
                insertToRoom()
            }
            buttonSave.setOnClickListener {
                tanklevelconstraint.visibility = View.GONE
                radioButtonsetuptanklevel.text = editText.text
            }
            buttonCancel.setOnClickListener {
                tanklevelconstraint.visibility = View.GONE
            }
            dateTextview.setOnClickListener {
                showDateDialogStartPoint(dateTextview)
            }
            timeTextview.setOnClickListener {
                showTimeDialogStartPoint(timeTextview)
            }
            buttonCanceldiscount.setOnClickListener {
                discountdialogconstraint.visibility = View.INVISIBLE
                discountradio.clearCheck()
            }

            buttonSavediscount.setOnClickListener {
                discountdialogconstraint.visibility = View.INVISIBLE
                binding.discountPercentTV.text = binding.editTextdiscount.text.toString()

            }
        }


        // Add TextWatcher to gasEdittext
        binding.gasTextview.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                calculateCost()
            }
        })

        binding.editTextdiscount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                calculateDiscountedCost()
            }
        })



        // Add TextWatcher to priceEdittext
        binding.priceTextview.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                calculateCost()
            }
        })

    }

    private fun calculateDiscountedCost() {
        val discountPerLiter = binding.editTextdiscount.text.toString().toFloatOrNull() ?: 0.0f

        val gasQuantity = binding.gasTextview.text.toString().toDoubleOrNull() ?: 0.0
        val pricePerLitre = binding.priceTextview.text.toString().toDoubleOrNull() ?: 0.0

        val costBeforeDiscount = (gasQuantity * pricePerLitre).toLong()
        val discountAmount = discountPerLiter.toLong()

        val discountedCost = costBeforeDiscount - discountAmount

        binding.editText2.text = discountedCost.toString()
    }



    private fun calculateCost() {
        val gasQuantity = binding.gasTextview.text.toString().toDoubleOrNull() ?: 0.0
        val pricePerLitre = binding.priceTextview.text.toString().toDoubleOrNull() ?: 0.0

        cost = (gasQuantity * pricePerLitre).toLong()


        binding.costTextview.text = cost.toString()
    }


    /*insert user input data to room database....*/
    private fun insertToRoom() {
        CoroutineScope(Dispatchers.Main).launch {
            with(binding) {
                val gas = gasTextview.text.toString().toIntOrNull() ?: 0
                val odometerType = spinnerMileageType.hint.toString()
                val odometerKm = spinnerMileageType.text.toString()
                val gasType = gastypetextview.text.toString()
                val price = priceTextview.text.toString().toIntOrNull() ?: 0
                //   val cost = costTextview.text.toString().toLongOrNull() ?: 0L
                val date = dateTextview.text.toString()
                val time = timeTextview.text.toString()
                val tankLevel = radioButtonFulltank.isChecked

                // Check if selectedCar is null
                if (selectedCar == null) {
                    Toast.makeText(this@RefuelingActivity, "First Add Car", Toast.LENGTH_SHORT)
                        .show()
                    return@launch
                }

                // Initialize fuelLevel variable
                var fuelLevel = ""

                // Check if radioButtonFulltank is checked
                if (radioButtonFulltank.isChecked) {
                    // Fetch fuelLevel from the selected car
                    CoroutineScope(Dispatchers.IO).launch {
                        fuelLevel = vehicleDao.getVehicleData(selectedCar!!.id)?.carTankCapacity ?: "0    "
                    }
                } else {
                    // If radioButtonFulltank is not checked, use editText.text as fuelLevel
                    fuelLevel = binding.editText.text.toString()
                }

                // Check if any required data is null and show Toast if so
                if (gasType.isEmpty() || odometerKm.isEmpty() || date.isEmpty() || time.isEmpty() || gasStationTypeTv.text.toString()
                        .isEmpty()
                ) {
                    Toast.makeText(
                        this@RefuelingActivity, "Please enter all details", Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                }

                val tankLevelBefore = tankCapBefore.text.toString()
                val tankLevelAfter = editText.text.toString()
                val gasStationType = gasStationTypeTv.text.toString()
                val discountStatus = radioButtonDiscount.isChecked
                val discountPerLiter = editText2.text.toString().toFloatOrNull() ?: 0.0f
                val totalDiscount = editTextdiscount.text.toString().toFloatOrNull() ?: 0.0f
                val addNote = desEdittext.text.toString()

                val mileageLogEntity = MileageLogEntity(
                    gas = gas,
                    odometerType = odometerType,
                    odometerKm = odometerKm,
                    carId = selectedCar!!.id,
                    fuelLevel = fuelLevel,
                    gasType = gasType,
                    price = price,
                    cost = cost,
                    date = date,
                    time = time,
                    tankLevel = tankLevel,
                    tankLevelBefore = tankLevelBefore,
                    tankLevelAfter = tankLevelAfter,
                    gasStationType = gasStationType,
                    discountStatus = discountStatus,
                    discountPerLiter = discountPerLiter,
                    totalDiscount = totalDiscount,
                    addNote = addNote
                )

                mileageLogDao = MileageLogDatabase.getDatabase(this@RefuelingActivity).mileageDao()
                withContext(Dispatchers.IO) {
                    mileageLogDao.insertMileageLogData(mileageLogEntity)
                    Log.d("insertToRoom", "insertToRoom: $mileageLogEntity")
                }
                onBackPressed()
                finish()
            }
        }
    }


    private fun spinnerCarselect() {
        CoroutineScope(Dispatchers.Main).launch {
            val vehicleDao = VehicleDatabase.getDatabase(this@RefuelingActivity).vehicleDao()

            vehicleDao.getAllVehicleData().observe(this@RefuelingActivity) { vehicles ->
                val list = vehicles.map { it.vehicleName }

                // Add options to the Spinner
                val adapter = ArrayAdapter(
                    this@RefuelingActivity, R.layout.custom_spinner_dropdown_item, list
                )
                binding.spinnerCarselect.adapter = adapter

                // Set an OnItemSelectedListener to respond to item selection
                binding.spinnerCarselect.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?, view: View?, position: Int, id: Long
                        ) {
                            selectedCar = vehicles[position]
                            // You can perform actions based on the selected unit here
                            // Toast.makeText(this@RefuelingActivity, "Selected: $selectedUnit", Toast.LENGTH_SHORT).show()
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            // Do nothing when nothing is selected
                        }
                    }
            }
        }
    }

    fun onRadioButtonClicked(view: View) {

        val radio: RadioButton = view as RadioButton
        if (radio.id.toString() == "radioBefore") {
            estimateFuel = binding.editText.text.toString()
        }
    }

}

